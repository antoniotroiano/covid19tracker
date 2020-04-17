package com.valtech.statistics.service;

import com.valtech.statistics.model.DataGermany;
import com.valtech.statistics.model.DataWorld;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
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
    private final WorldService worldService;
    private final GermanyService germanyService;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM");
    LocalDate now = LocalDate.now();

    private static int getValue(JSONObject json, String key) throws JSONException {
        JSONObject confirmed = (JSONObject) json.get(key);
        return confirmed.getInt("value");
    }

    private JSONObject getJSONObject(String url) throws IOException {
        return new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
    }

    @Scheduled(cron = "0 0 * ? * *")
    public void getDataOfWorldAndSaveIt() throws IOException {
        log.info("Invoke create data of world.");

        int confirmedWorld = getValue(getJSONObject(URL_WORLD), "confirmed");
        int recoveredWorld = getValue(getJSONObject(URL_WORLD), "recovered");
        int deathsWorld = getValue(getJSONObject(URL_WORLD), "deaths");
        String lastUpdateWorld = getJSONObject(URL_WORLD).getString("lastUpdate");

        DataWorld dataWorld = new DataWorld();

        dataWorld.setConfirmed(confirmedWorld);
        dataWorld.setRecovered(recoveredWorld);
        dataWorld.setDeaths(deathsWorld);
        dataWorld.setLastUpdate(lastUpdateWorld);
        dataWorld.setLocalDate(now.format(dtf));

        if (worldService.findDataWorldByLastUpdate(dataWorld.getLastUpdate()).isEmpty()) {
            worldService.saveDataWorld(dataWorld);
            log.info("Saved new data {}", dataWorld.getLastUpdate());
        } else {
            log.info("No new data. Returned last one {}", dataWorld.getLastUpdate());
        }
    }

    @Scheduled(cron = "0 0 * ? * *")
    public void getDataOfGermanyAndSaveIt() throws IOException {
        log.info("Invoke get data of germany and save it.");

        int confirmedGermany = getValue(getJSONObject(URL_GERMANY), "confirmed");
        int recoveredGermany = getValue(getJSONObject(URL_GERMANY), "recovered");
        int deathsGermany = getValue(getJSONObject(URL_GERMANY), "deaths");
        String lastUpdateGermany = getJSONObject(URL_GERMANY).getString("lastUpdate");

        DataGermany dataGermany = new DataGermany();

        dataGermany.setConfirmed(confirmedGermany);
        dataGermany.setRecovered(recoveredGermany);
        dataGermany.setDeaths(deathsGermany);
        dataGermany.setLastUpdate(lastUpdateGermany);
        dataGermany.setLocalDate(now.format(dtf));

        if (germanyService.findDataGermanyByLastUpdate(dataGermany.getLastUpdate()).isEmpty()) {
            germanyService.saveDataGermany(dataGermany);
            log.info("Saved new data of germany {}", dataGermany.getLastUpdate());
        } else {
            log.info("No new data of germany, Returned last one {}", dataGermany.getLastUpdate());
        }
    }
}