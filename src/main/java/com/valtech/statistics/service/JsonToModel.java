package com.valtech.statistics.service;

import com.valtech.statistics.model.DataGermany;
import com.valtech.statistics.model.DataWorld;
import com.valtech.statistics.model.DataWorldSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class JsonToModel {

    private static final String URL_WORLD = "https://covid19.mathdro.id/api";
    private static final String URL_GERMANY = "https://covid19.mathdro.id/api/countries/germany";
    private static final String VALUE = "value";
    private static final String GLOBAL = "Global";
    private final WorldService worldService;
    private final WorldSummaryService worldSummaryService;
    private final GermanyService germanyService;

    private JSONObject getJSONObject(String url) throws IOException {
        return new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
    }

    private int getValueOfJSONObject(JSONObject jsonObject, String key, String valueKey) throws JSONException {
        JSONObject getIntValue = (JSONObject) jsonObject.get(key);
        return getIntValue.getInt(valueKey);
    }

    private void getStringValueOfJSONObject(JSONObject jsonObject, String key) throws JSONException {
        JSONArray getValueOfJSONArray = jsonObject.getJSONArray("Countries");
        for (int i = 0; i < getValueOfJSONArray.length(); i++) {
            JSONObject json = getValueOfJSONArray.getJSONObject(i);
            if (json.getString(key).equals("Germany")) {
                json.getString("Country");
                json.getInt("NewConfirmed");
                json.getInt("TotalConfirmed");
                json.getInt("NewDeaths");
                json.getInt("TotalDeaths");
                json.getInt("NewRecovered");
                json.getInt("TotalRecovered");
            }
        }
    }

    private String getDateNow() {
        String dateNow = "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM");
        LocalDate now = LocalDate.now();
        dateNow = now.format(dtf);
        return dateNow;
    }

    @Scheduled(cron = "0 0 */3 ? * *")
    public void getDataOfWorldAndSaveIt() throws IOException {
        log.info("Invoke create data of world.");

        int confirmedWorld = getValueOfJSONObject(getJSONObject(URL_WORLD), "confirmed", VALUE);
        int recoveredWorld = getValueOfJSONObject(getJSONObject(URL_WORLD), "recovered", VALUE);
        int deathsWorld = getValueOfJSONObject(getJSONObject(URL_WORLD), "deaths", VALUE);
        String lastUpdateWorld = getJSONObject(URL_WORLD).getString("lastUpdate");

        DataWorld dataWorld = new DataWorld();
        dataWorld.setConfirmed(confirmedWorld);
        dataWorld.setRecovered(recoveredWorld);
        dataWorld.setDeaths(deathsWorld);
        dataWorld.setLastUpdate(lastUpdateWorld);
        dataWorld.setLocalDate(getDateNow());

        Optional<DataWorld> dataWorldLast = worldService.getLastEntryWorld();

        if (dataWorldLast.isEmpty()) {
            worldService.saveDataWorld(dataWorld);
            log.info("Saved first data of world {}", dataWorld.getLastUpdate());
        }
        if (dataWorldLast.isPresent()) {
            if (dataWorldLast.get().getConfirmed() != confirmedWorld || dataWorldLast.get().getRecovered() != recoveredWorld ||
                    dataWorldLast.get().getDeaths() != deathsWorld) {
                if (worldService.findDataWorldByLastUpdate(lastUpdateWorld).isEmpty()) {
                    worldService.saveDataWorld(dataWorld);
                    log.info("Saved new data of world {}", dataWorld.getLastUpdate());
                } else {
                    log.info("No new data of world. Returned last one {}", dataWorld.getLastUpdate());
                }
            } else {
                log.info("The data of last entry world are equals the new one.");
            }
        }
    }

    @Scheduled(cron = "0 10 * ? * *")
    public void getDataOfGermanyAndSaveIt() throws IOException {
        log.info("Invoke get data of germany and save it.");

        int confirmedGermany = getValueOfJSONObject(getJSONObject(URL_GERMANY), "confirmed", VALUE);
        int recoveredGermany = getValueOfJSONObject(getJSONObject(URL_GERMANY), "recovered", VALUE);
        int deathsGermany = getValueOfJSONObject(getJSONObject(URL_GERMANY), "deaths", VALUE);
        String lastUpdateGermany = getJSONObject(URL_GERMANY).getString("lastUpdate");

        DataGermany dataGermany = new DataGermany();
        dataGermany.setConfirmed(confirmedGermany);
        dataGermany.setRecovered(recoveredGermany);
        dataGermany.setDeaths(deathsGermany);
        dataGermany.setLastUpdate(lastUpdateGermany);
        dataGermany.setLocalDate(getDateNow());

        Optional<DataGermany> dataGermanyLast = germanyService.getLastEntryGermany();

        if (dataGermanyLast.isEmpty()) {
            germanyService.saveDataGermany(dataGermany);
            log.info("Saved first data of germany {}", dataGermany.getLastUpdate());
        }
        if (dataGermanyLast.isPresent()) {
            if (dataGermanyLast.get().getConfirmed() != confirmedGermany || dataGermanyLast.get().getRecovered() != recoveredGermany ||
                    dataGermanyLast.get().getDeaths() != deathsGermany) {
                if (germanyService.findDataGermanyByLastUpdate(lastUpdateGermany).isEmpty()) {
                    germanyService.saveDataGermany(dataGermany);
                    log.info("Saved new data of germany {}", dataGermany.getLastUpdate());
                } else {
                    log.info("No new data of germany, Returned last one {}", dataGermany.getLastUpdate());
                }
            } else {
                log.info("The data of last entry of germany are equals the new one.");
            }
        }
    }

    @Scheduled(cron = "0 0 */6 ? * *")
    public void getDataWorldSummary() throws IOException {
        log.info("Invoke get data of world summary and save it.");

        final String URL_WORLD_SUMMARY = "https://api.covid19api.com/summary";

        int newConfirmed = getValueOfJSONObject(getJSONObject(URL_WORLD_SUMMARY), GLOBAL, "NewConfirmed");
        int totalConfirmed = getValueOfJSONObject(getJSONObject(URL_WORLD_SUMMARY), GLOBAL, "TotalConfirmed");
        int newDeaths = getValueOfJSONObject(getJSONObject(URL_WORLD_SUMMARY), GLOBAL, "NewDeaths");
        int totalDeaths = getValueOfJSONObject(getJSONObject(URL_WORLD_SUMMARY), GLOBAL, "TotalDeaths");
        int newRecovered = getValueOfJSONObject(getJSONObject(URL_WORLD_SUMMARY), GLOBAL, "NewRecovered");
        int totalRecovered = getValueOfJSONObject(getJSONObject(URL_WORLD_SUMMARY), GLOBAL, "TotalRecovered");

        DataWorldSummary dataWorldSummary = new DataWorldSummary();

        dataWorldSummary.setNewConfirmed(newConfirmed);
        dataWorldSummary.setTotalConfirmed(totalConfirmed);
        dataWorldSummary.setNewDeaths(newDeaths);
        dataWorldSummary.setTotalDeaths(totalDeaths);
        dataWorldSummary.setNewRecovered(newRecovered);
        dataWorldSummary.setTotalRecovered(totalRecovered);
        dataWorldSummary.setLocalDate(LocalDate.now());
        dataWorldSummary.setLocalTime(LocalTime.now().withNano(0));

        Optional<DataWorldSummary> dataWorldSummaryLast = worldSummaryService.getLastEntryWorldSummary();

        if (dataWorldSummaryLast.isEmpty()) {
            worldSummaryService.saveDataWorldSummary(dataWorldSummary);
            log.info("Saved first data of world summary {}", dataWorldSummary.getLocalDate());
        }
        if (dataWorldSummaryLast.isPresent()) {
            if (dataWorldSummaryLast.get().getNewConfirmed() != newConfirmed || dataWorldSummaryLast.get().getTotalConfirmed() !=
                    totalConfirmed || dataWorldSummaryLast.get().getNewDeaths() != newDeaths || dataWorldSummaryLast.get().getTotalDeaths() !=
                    totalDeaths || dataWorldSummaryLast.get().getNewRecovered() != newRecovered || dataWorldSummaryLast.get().getTotalRecovered() != totalRecovered) {
                if (worldSummaryService.findDataWorldSummaryByLocalDate(dataWorldSummary.getLocalDate()).isEmpty()) {
                    worldSummaryService.saveDataWorldSummary(dataWorldSummary);
                    log.info("Saved new data of world summary {}", dataWorldSummary.getLocalDate());
                } else {
                    log.info("No new data of world summary, Returned last one {}", dataWorldSummary.getLocalDate());
                }
            } else {
                log.info("The data of last entry of world summary are equals the new one.");
            }
        }
    }
}