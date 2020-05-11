package com.valtech.statistics.service.json;

import com.valtech.statistics.model.WorldTimeSeriesDto;
import com.valtech.statistics.service.DateFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReadJSON {

    private final DateFormat dateFormat = new DateFormat();

    private JSONObject getJSONObject(String url) throws IOException {
        return new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
    }

    private int getValueOfJSONObject(JSONObject jsonObject, String key, String valueKey) throws JSONException {
        JSONObject getIntValue = (JSONObject) jsonObject.get(key);
        return getIntValue.getInt(valueKey);
    }

    private JSONArray getJSONOArray(String url) throws IOException {
        return new JSONArray(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
    }

    public List<WorldTimeSeriesDto> readWorldValues() throws IOException {
        log.debug("Invoke read world time series");
        List<WorldTimeSeriesDto> allValuesWorld = new ArrayList<>();

        JSONObject jsonObjectWorld = getJSONObject("https://corona-api.com/timeline");
        JSONArray jsonArrayWorld = jsonObjectWorld.getJSONArray("data");
        for (int i = 0; i < jsonArrayWorld.length(); i++) {
            WorldTimeSeriesDto worldTimeSeriesDto = new WorldTimeSeriesDto();
            worldTimeSeriesDto.setLastUpdate(jsonArrayWorld.getJSONObject(i).getString("updated_at"));
            String formattedDate = dateFormat.formatDate(jsonArrayWorld.getJSONObject(i).getString("date"));
            worldTimeSeriesDto.setDate(formattedDate);
            worldTimeSeriesDto.setConfirmed(jsonArrayWorld.getJSONObject(i).getInt("confirmed"));
            worldTimeSeriesDto.setRecovered(jsonArrayWorld.getJSONObject(i).getInt("recovered"));
            worldTimeSeriesDto.setDeaths(jsonArrayWorld.getJSONObject(i).getInt("deaths"));
            worldTimeSeriesDto.setActive(jsonArrayWorld.getJSONObject(i).getInt("active"));
            worldTimeSeriesDto.setNewConfirmed(jsonArrayWorld.getJSONObject(i).getInt("new_confirmed"));
            worldTimeSeriesDto.setNewRecovered(jsonArrayWorld.getJSONObject(i).getInt("new_recovered"));
            worldTimeSeriesDto.setNewDeaths(jsonArrayWorld.getJSONObject(i).getInt("new_deaths"));
            if (i == 0) {
                worldTimeSeriesDto.setInProgress(jsonArrayWorld.getJSONObject(i).getBoolean("is_in_progress"));
            }
            allValuesWorld.add(worldTimeSeriesDto);
        }
        log.debug("Returned world time series");
        return allValuesWorld;
    }
}