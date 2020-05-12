package com.valtech.statistics.service.json;

import com.valtech.statistics.model.CountryDetailsDto;
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

    public CountryDetailsDto readDetailsForCountry(String country) throws IOException {
        log.debug("Invoke read details for {}", country);
        CountryDetailsDto countryDetailsDto = new CountryDetailsDto();

        JSONObject jsonObject = getJSONObject("https://corona-api.com/countries");
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("name").equals(country)) {
                countryDetailsDto.setCountry(jsonArray.getJSONObject(i).optString("name", ""));
                countryDetailsDto.setCode(jsonArray.getJSONObject(i).optString("code", ""));
                countryDetailsDto.setPopulation(jsonArray.getJSONObject(i).optInt("population", 0));
                countryDetailsDto.setLastUpdate(jsonArray.getJSONObject(i).optString("updated_at", ""));
                countryDetailsDto.setTodayDeaths(jsonArray.getJSONObject(i).getJSONObject("today").optInt("deaths", 0));
                countryDetailsDto.setTodayConfirmed(jsonArray.getJSONObject(i).getJSONObject("today").optInt("confirmed", 0));
                countryDetailsDto.setDeaths(jsonArray.getJSONObject(i).getJSONObject("latest_data").optInt("deaths", 0));
                countryDetailsDto.setConfirmed(jsonArray.getJSONObject(i).getJSONObject("latest_data").optInt("confirmed", 0));
                countryDetailsDto.setRecovered(jsonArray.getJSONObject(i).getJSONObject("latest_data").optInt("recovered", 0));
                countryDetailsDto.setCritical(jsonArray.getJSONObject(i).getJSONObject("latest_data").optInt("critical", 0));
                countryDetailsDto.setDeathRate(jsonArray.getJSONObject(i).getJSONObject("latest_data").getJSONObject("calculated").optDouble("death_rate", 0));
                countryDetailsDto.setRecoveryRate(jsonArray.getJSONObject(i).getJSONObject("latest_data").getJSONObject("calculated").optDouble("recovery_rate", 0));
                countryDetailsDto.setRecoveredVSDeathRatio(jsonArray.getJSONObject(i).getJSONObject("latest_data").getJSONObject("calculated").optDouble("recovered_vs_death_ratio", 0));
                countryDetailsDto.setCasesPerMillionPopulation(jsonArray.getJSONObject(i).getJSONObject("latest_data").getJSONObject("calculated").optInt("cases_per_million_population", 0));
            }
        }
        log.debug("Returned details for {}", country);
        return countryDetailsDto;
    }
}