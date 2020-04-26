package com.valtech.statistics.service;

import com.valtech.statistics.model.DataGermany;
import com.valtech.statistics.model.DataGermanySummary;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetJsonValue {

    private static final String URL_WORLD = "https://covid19.mathdro.id/api";
    private static final String URL_GERMANY = "https://covid19.mathdro.id/api/countries/germany";
    private static final String VALUE = "value";
    private static final String GLOBAL = "Global";
    private static final String CONFIRMED = "confirmed";
    private static final String RECOVERED = "recovered";
    private static final String DEATHS = "deaths";
    private static final String LAST_UPDATE = "lastUpdate";
    private final WorldSummaryService worldSummaryService;
    private final GermanyService germanyService;

    private JSONObject getJSONObject(String url) throws IOException {
        return new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
    }

    private int getValueOfJSONObject(JSONObject jsonObject, String key, String valueKey) throws JSONException {
        JSONObject getIntValue = (JSONObject) jsonObject.get(key);
        return getIntValue.getInt(valueKey);
    }

    //TODO: Chart.js xAchse anpassen für normales Date Format
    private String getDateNow() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM");
        LocalDate now = LocalDate.now();
        return now.format(dtf);
    }

    public DataWorld getDataOfWorldToModel() throws IOException {
        log.info("Invoke create data of world.");

        int confirmedWorld = getValueOfJSONObject(getJSONObject(URL_WORLD), CONFIRMED, VALUE);
        int recoveredWorld = getValueOfJSONObject(getJSONObject(URL_WORLD), RECOVERED, VALUE);
        int deathsWorld = getValueOfJSONObject(getJSONObject(URL_WORLD), DEATHS, VALUE);
        String lastUpdateWorld = getJSONObject(URL_WORLD).getString(LAST_UPDATE);

        DataWorld dataWorld = new DataWorld();
        dataWorld.setConfirmed(confirmedWorld);
        dataWorld.setRecovered(recoveredWorld);
        dataWorld.setDeaths(deathsWorld);
        dataWorld.setLastUpdate(lastUpdateWorld);
        dataWorld.setLocalDate(getDateNow());

        return dataWorld;
    }

    public DataGermany getDataOfGermanyToModel() throws IOException {
        log.info("Invoke get data of germany to model.");

        int confirmedGermany = getValueOfJSONObject(getJSONObject(URL_GERMANY), CONFIRMED, VALUE);
        int recoveredGermany = getValueOfJSONObject(getJSONObject(URL_GERMANY), RECOVERED, VALUE);
        int deathsGermany = getValueOfJSONObject(getJSONObject(URL_GERMANY), DEATHS, VALUE);
        String lastUpdateGermany = getJSONObject(URL_GERMANY).getString(LAST_UPDATE);

        DataGermany dataGermany = new DataGermany();
        dataGermany.setConfirmed(confirmedGermany);
        dataGermany.setRecovered(recoveredGermany);
        dataGermany.setDeaths(deathsGermany);
        dataGermany.setLastUpdate(lastUpdateGermany);
        dataGermany.setLocalDate(getDateNow());

        return dataGermany;
    }

    private String getURLWithDate() throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String dailyCOVID19 = "https://covid19.mathdro.id/api/daily/" + yesterday.format(dtf);
        URL url = new URL(dailyCOVID19);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        int code = huc.getResponseCode();
        if (code == 404) {
            LocalDate dayBeforeYesterday = LocalDate.now().minusDays(2);
            dailyCOVID19 = "https://covid19.mathdro.id/api/daily/" + dayBeforeYesterday.format(dtf);
            return dailyCOVID19;
        }
        return dailyCOVID19;
    }

    public DataGermany getDataOfGermanyToModelYesterday() throws IOException {
        log.info("Invoke create data of germany yesterday.");

        String URL_GERMANY_YESTERDAY = getURLWithDate();

        DataGermany dataGermanyYesterday = new DataGermany();
        JSONArray getValueOfArray = new JSONArray(IOUtils.toString(new URL(URL_GERMANY_YESTERDAY), StandardCharsets.UTF_8));
        for (int i = 0; i < getValueOfArray.length(); i++) {
            JSONObject json = getValueOfArray.getJSONObject(i);
            if (json.getString("countryRegion").equals("Germany")) {
                dataGermanyYesterday.setConfirmed(json.getInt(CONFIRMED));
                dataGermanyYesterday.setRecovered(json.getInt(RECOVERED));
                dataGermanyYesterday.setDeaths(json.getInt(DEATHS));
                dataGermanyYesterday.setLocalDate(json.getString(LAST_UPDATE));
            }
        }
        return dataGermanyYesterday;
    }

    public DataGermanySummary createDataOfGermanySummary() throws IOException {
        log.info("Invoke create data of germany summary.");
        DataGermany dataGermanyYesterday = getDataOfGermanyToModelYesterday();
        Optional<DataGermany> lastDataGermany = germanyService.getLastEntryGermany();

        DataGermanySummary dataGermanySummary = new DataGermanySummary();
        if (lastDataGermany.isPresent()) {
            dataGermanySummary.setNewConfirmed(lastDataGermany.get().getConfirmed() - dataGermanyYesterday.getConfirmed());
            dataGermanySummary.setTotalConfirmed(lastDataGermany.get().getConfirmed());
            dataGermanySummary.setNewRecovered(lastDataGermany.get().getRecovered() - dataGermanyYesterday.getRecovered());
            dataGermanySummary.setTotalRecovered(lastDataGermany.get().getRecovered());
            dataGermanySummary.setNewDeaths(lastDataGermany.get().getDeaths() - dataGermanyYesterday.getDeaths());
            dataGermanySummary.setTotalDeaths(lastDataGermany.get().getDeaths());
            dataGermanySummary.setLocalDate(LocalDate.now());
            dataGermanySummary.setLocalTime(LocalTime.now().withNano(0));
        }
        return dataGermanySummary;
    }

    //@Scheduled(cron = "0 15 */4 ? * *")
/*    public void getDataWorldSummaryAndSaveIt() throws IOException {
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
        dataWorldSummary.setActiveCases(totalConfirmed - totalDeaths - totalRecovered);
        dataWorldSummary.setLocalDate(LocalDate.now());
        dataWorldSummary.setLocalTime(LocalTime.now().withNano(0));

        Optional<DataWorldSummary> dataWorldSummaryLast = worldSummaryService.getLastEntryWorldSummary();

        if (dataWorldSummaryLast.isEmpty()) {
            worldSummaryService.saveDataWorldSummary(dataWorldSummary);
            log.info("Saved first data of world summary {}.", dataWorldSummary.getLocalDate());
        }
        if (dataWorldSummaryLast.isPresent()) {
            if (worldSummaryService.findDataWorldSummaryByLocalTime(dataWorldSummary.getLocalTime()).isEmpty()) {
                worldSummaryService.saveDataWorldSummary(dataWorldSummary);
                log.info("Saved new data of world summary {}.", dataWorldSummary.getLocalDate());
            } else {
                log.info("No new data of world summary, Returned last one {}.", dataWorldSummary.getLocalDate());
            }
        }
    }*/
}