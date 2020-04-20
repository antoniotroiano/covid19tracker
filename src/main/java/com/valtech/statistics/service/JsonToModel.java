package com.valtech.statistics.service;

import com.valtech.statistics.model.DataGermany;
import com.valtech.statistics.model.DataWorld;
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
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class JsonToModel {

    private static final String URL_WORLD = "https://covid19.mathdro.id/api";
    private static final String URL_GERMANY = "https://covid19.mathdro.id/api/countries/germany";
    private static final String URL_WORLD_SUMMARY = "https://api.covid19api.com/summary";
    private final WorldService worldService;
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

    @Scheduled(cron = "0 0 */2 ? * *")
    public void getDataOfWorldAndSaveIt() throws IOException {
        log.info("Invoke create data of world.");

        int confirmedWorld = getValueOfJSONObject(getJSONObject(URL_WORLD), "confirmed", "value");
        int recoveredWorld = getValueOfJSONObject(getJSONObject(URL_WORLD), "recovered", "value");
        int deathsWorld = getValueOfJSONObject(getJSONObject(URL_WORLD), "deaths", "value");
        String lastUpdateWorld = getJSONObject(URL_WORLD).getString("lastUpdate");

        DataWorld dataWorld = new DataWorld();
        dataWorld.setConfirmed(confirmedWorld);
        dataWorld.setRecovered(recoveredWorld);
        dataWorld.setDeaths(deathsWorld);
        dataWorld.setLastUpdate(lastUpdateWorld);
        dataWorld.setLocalDate(getDateNow());

        DataWorld dataWorldLast = worldService.getLastEntryWorld();

        if (dataWorldLast.getConfirmed() != confirmedWorld || dataWorldLast.getRecovered() != recoveredWorld ||
                dataWorldLast.getDeaths() != deathsWorld) {
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

    @Scheduled(cron = "0 10 * ? * *")
    public void getDataOfGermanyAndSaveIt() throws IOException {
        log.info("Invoke get data of germany and save it.");

        int confirmedGermany = getValueOfJSONObject(getJSONObject(URL_GERMANY), "confirmed", "value");
        int recoveredGermany = getValueOfJSONObject(getJSONObject(URL_GERMANY), "recovered", "value");
        int deathsGermany = getValueOfJSONObject(getJSONObject(URL_GERMANY), "deaths", "value");
        String lastUpdateGermany = getJSONObject(URL_GERMANY).getString("lastUpdate");

        DataGermany dataGermany = new DataGermany();
        dataGermany.setConfirmed(confirmedGermany);
        dataGermany.setRecovered(recoveredGermany);
        dataGermany.setDeaths(deathsGermany);
        dataGermany.setLastUpdate(lastUpdateGermany);
        dataGermany.setLocalDate(getDateNow());

        DataGermany dataGermanyLast = germanyService.getLastEntryGermany();

        if (dataGermanyLast.getConfirmed() == confirmedGermany && dataGermanyLast.getRecovered() == recoveredGermany &&
                dataGermanyLast.getDeaths() == deathsGermany) {
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

    public int getDataWorldSummary() throws IOException {
        log.info("Invoke get data of world summary and save it.");

        int newConfirmed = getValueOfJSONObject(getJSONObject(URL_WORLD_SUMMARY), "Global", "NewConfirmed");
        int totalConfirmed = getValueOfJSONObject(getJSONObject(URL_WORLD_SUMMARY), "Global", "TotalConfirmed");
        int newDeaths = getValueOfJSONObject(getJSONObject(URL_WORLD_SUMMARY), "Global", "NewDeaths");
        int totalDeaths = getValueOfJSONObject(getJSONObject(URL_WORLD_SUMMARY), "Global", "TotalDeaths");
        int newRecovered = getValueOfJSONObject(getJSONObject(URL_WORLD_SUMMARY), "Global", "NewRecovered");
        int totalRecovered = getValueOfJSONObject(getJSONObject(URL_WORLD_SUMMARY), "Global", "TotalRecovered");

        getStringValueOfJSONObject(getJSONObject(URL_WORLD_SUMMARY), "Country");

        return newConfirmed;
    }
}