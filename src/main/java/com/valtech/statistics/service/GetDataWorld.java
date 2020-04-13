package com.valtech.statistics.service;

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

@Service
@Slf4j
@RequiredArgsConstructor
public class GetDataWorld {

    private static final String URL = "https://covid19.mathdro.id/api";
    private final StatisticService statisticService;

    private static int getValue(JSONObject json, String key) throws JSONException {
        JSONObject confirmed = (JSONObject) json.get(key);
        return confirmed.getInt("value");
    }

    @Scheduled(cron = "0 0 * ? * *")
    public void createDataOfWorld() throws IOException {
        log.info("Invoke create data of world.");
        JSONObject json = new JSONObject(IOUtils.toString(new URL(URL), StandardCharsets.UTF_8));

        int confirmed = getValue(json, "confirmed");
        int recovered = getValue(json, "recovered");
        int deaths = getValue(json, "deaths");
        String lastUpdate = json.getString("lastUpdate");

        DataWorld dataWorld = new DataWorld();

        dataWorld.setConfirmed(confirmed);
        dataWorld.setRecovered(recovered);
        dataWorld.setDeaths(deaths);
        dataWorld.setLastUpdate(lastUpdate);

        if (statisticService.findDataWorldByLastUpdate(dataWorld.getLastUpdate()).isEmpty()) {
            log.info("Saved new data {}", dataWorld.getLastUpdate());
            statisticService.saveDataWorld(dataWorld);
        } else {
            log.info("No new data. Returned last one {}", dataWorld.getLastUpdate());
        }
    }
}