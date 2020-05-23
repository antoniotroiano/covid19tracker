package com.statistics.corona.service.json;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.TimeSeriesWorldDto;
import com.statistics.corona.service.DateFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
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

    private static final String CONFIRMED = "confirmed";
    private static final String RECOVERED = "recovered";
    private static final String DEATHS = "deaths";
    private static final String VALUE = "value";
    private static final String LATEST_DATA = "latest_data";
    private static final String CALCULATED = "calculated";
    private final DateFormat dateFormat = new DateFormat();

    private JSONObject getJSONObject(String url) throws IOException {
        return new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
    }

    private String formatCountry(String country) {
        String[] countryArray = country.split(" ");
        StringBuilder bld = new StringBuilder();
        for (String s : countryArray) {
            bld.append(s).append("%20");
        }
        return bld.toString().substring(0, bld.length() - 3);
    }

    //Todo: Return only the JSONArray to a worldService and build the list in the service. Also better for testing.
    //ToDo: Handel the IO Exception
    public List<TimeSeriesWorldDto> readWorldValues() throws IOException {
        log.debug("Invoke read world time series of JSON");
        List<TimeSeriesWorldDto> allValuesWorld = new ArrayList<>();

        JSONObject jsonObjectWorld = getJSONObject("https://corona-api.com/timeline");
        JSONArray jsonArrayWorld = jsonObjectWorld.getJSONArray("data");
        for (int i = 0; i < jsonArrayWorld.length(); i++) {
            TimeSeriesWorldDto timeSeriesWorldDto = new TimeSeriesWorldDto();
            timeSeriesWorldDto.setLastUpdate(jsonArrayWorld.getJSONObject(i).optString("updated_at", "2000-01-01'T'00:00:00.000'Z'"));
            timeSeriesWorldDto.setDate(dateFormat.formatDate(jsonArrayWorld.getJSONObject(i).optString("date", "2000-01-01")));
            timeSeriesWorldDto.setConfirmed(jsonArrayWorld.getJSONObject(i).optInt(CONFIRMED, 0));
            timeSeriesWorldDto.setRecovered(jsonArrayWorld.getJSONObject(i).optInt(RECOVERED, 0));
            timeSeriesWorldDto.setDeaths(jsonArrayWorld.getJSONObject(i).optInt(DEATHS, 0));
            timeSeriesWorldDto.setActive(jsonArrayWorld.getJSONObject(i).optInt("active", 0));
            timeSeriesWorldDto.setNewConfirmed(jsonArrayWorld.getJSONObject(i).optInt("new_confirmed", 0));
            timeSeriesWorldDto.setNewRecovered(jsonArrayWorld.getJSONObject(i).optInt("new_recovered", 0));
            timeSeriesWorldDto.setNewDeaths(jsonArrayWorld.getJSONObject(i).optInt("new_deaths", 0));
            if (i == 0) {
                timeSeriesWorldDto.setInProgress(jsonArrayWorld.getJSONObject(i).optBoolean("is_in_progress", false));
            }
            allValuesWorld.add(timeSeriesWorldDto);
        }
        log.debug("Returned world time series of JSON");
        return allValuesWorld;
    }

    public CountryDetailsDto readDetailsForCountry(String country) throws IOException {
        log.debug("Invoke read details for {} of JSON", country);
        CountryDetailsDto countryDetailsDto = new CountryDetailsDto();

        String formattedCountry = formatCountry(country);
        String formattedURL = "https://covid19.mathdro.id/api/countries/" + formattedCountry;

        JSONObject jsonObjectMathdro = getJSONObject(formattedURL);
        countryDetailsDto.setConfirmed(jsonObjectMathdro.getJSONObject(CONFIRMED).optInt(VALUE, 0));
        countryDetailsDto.setRecovered(jsonObjectMathdro.getJSONObject(RECOVERED).optInt(VALUE, 0));
        countryDetailsDto.setDeaths(jsonObjectMathdro.getJSONObject(DEATHS).optInt(VALUE, 0));
        countryDetailsDto.setLastUpdate(jsonObjectMathdro.optString("lastUpdate", "2000-01-01'T'00:00:00.000'Z'"));

        if (country.equals("US")) {
            country = "USA";
        }
        if (country.equals("Congo (Brazzaville)") || country.equals("Congo (Kinshasa)")) {
            country = "Congo";
        }
        if (country.equals("Korea, South")) {
            country = "S. Korea";
        }
        if (country.equals("Saint Vincent and the Grenadines")) {
            country = "Saint Vincent Grenadines";
        }
        if (country.equals("Taiwan*")) {
            country = "Taiwan";
        }
        if (country.equals("United Kingdom")) {
            country = "UK";
        }

        JSONObject jsonObject = getJSONObject("https://corona-api.com/countries");
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("name").equals(country)) {
                countryDetailsDto.setCountry(jsonArray.getJSONObject(i).optString("name", "Empty"));
                countryDetailsDto.setCode(jsonArray.getJSONObject(i).optString("code", "Empty"));
                countryDetailsDto.setPopulation(jsonArray.getJSONObject(i).optInt("population", 0));
                countryDetailsDto.setTodayDeaths(jsonArray.getJSONObject(i).getJSONObject("today")
                        .optInt(DEATHS, 0));
                countryDetailsDto.setTodayConfirmed(jsonArray.getJSONObject(i).getJSONObject("today")
                        .optInt(CONFIRMED, 0));
                countryDetailsDto.setCritical(jsonArray.getJSONObject(i).getJSONObject(LATEST_DATA)
                        .optInt("critical", 0));
                countryDetailsDto.setDeathRate(jsonArray.getJSONObject(i).getJSONObject(LATEST_DATA)
                        .getJSONObject(CALCULATED).optDouble("death_rate", 0.0));
                countryDetailsDto.setRecoveryRate(jsonArray.getJSONObject(i).getJSONObject(LATEST_DATA)
                        .getJSONObject(CALCULATED).optDouble("recovery_rate", 0.0));
                countryDetailsDto.setRecoveredVSDeathRatio(jsonArray.getJSONObject(i).getJSONObject(LATEST_DATA)
                        .getJSONObject(CALCULATED).optDouble("recovered_vs_death_ratio", 0.0));
                countryDetailsDto.setCasesPerMillionPopulation(jsonArray.getJSONObject(i).getJSONObject(LATEST_DATA)
                        .getJSONObject(CALCULATED).optInt("cases_per_million_population", 0));
            }
        }
        log.debug("Returned details for {}", country);
        return countryDetailsDto;
    }
}