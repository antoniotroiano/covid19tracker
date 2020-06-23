package com.statistics.corona.service.json;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.DistrictDto;
import com.statistics.corona.model.TimeSeriesWorldDto;
import com.statistics.corona.service.DateFormat;
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
public class ReadJSON {

    private static final String CONFIRMED = "confirmed";
    private static final String RECOVERED = "recovered";
    private static final String LATEST_DATA = "latest_data";
    private static final String CALCULATED = "calculated";
    private final DateFormat dateFormat = new DateFormat();

    private JSONObject getJSONObject(String url) throws IOException {
        return new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
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
            timeSeriesWorldDto.setDeaths(jsonArrayWorld.getJSONObject(i).optInt("deaths", 0));
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

    public CountryDetailsDto newReadDetailsCountry(String country) throws IOException {
        log.debug("Invoke get country details for {}", country);
        CountryDetailsDto countryDetailsDto = new CountryDetailsDto();

        if (country.equals("Congo (Brazzaville)") || country.equals("Congo (Kinshasa)")) {
            country = "Congo";
        }
        if (country.equals("Taiwan*")) {
            country = "Taiwan";
        }

        JSONObject jsonObject = getJSONObject("https://www.trackcorona.live/api/countries");
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        country = checkCountryName(country);
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("location").contains(country)) {
                countryDetailsDto.setCountry(jsonArray.getJSONObject(i).optString("location", "Empty"));
                countryDetailsDto.setCode(jsonArray.getJSONObject(i).optString("country_code", "Empty"));
                countryDetailsDto.setConfirmed(jsonArray.getJSONObject(i).optInt(CONFIRMED, 0));
                countryDetailsDto.setDeaths(jsonArray.getJSONObject(i).optInt("dead", 0));
                countryDetailsDto.setRecovered(jsonArray.getJSONObject(i).optInt(RECOVERED, 0));
            }
        }

        JSONObject jsonObjectExtension = getJSONObject("https://corona-api.com/countries");
        JSONArray jsonArrayExtension = jsonObjectExtension.getJSONArray("data");
        country = checkCountryNameSecond(country);
        for (int i = 0; i < jsonArrayExtension.length(); i++) {
            if (jsonArrayExtension.getJSONObject(i).getString("name").equals(country)) {
                countryDetailsDto.setPopulation(jsonArrayExtension.getJSONObject(i).optInt("population", 0));
                countryDetailsDto.setLastUpdate(jsonArrayExtension.getJSONObject(i).optString("updated_at", "2000-01-01T00:00:00.000Z"));
                countryDetailsDto.setCritical(jsonArrayExtension.getJSONObject(i).getJSONObject(LATEST_DATA)
                        .optInt("critical", 0));
                countryDetailsDto.setDeathRate(jsonArrayExtension.getJSONObject(i).getJSONObject(LATEST_DATA)
                        .getJSONObject(CALCULATED).optDouble("death_rate", 0.0));
                countryDetailsDto.setRecoveryRate(jsonArrayExtension.getJSONObject(i).getJSONObject(LATEST_DATA)
                        .getJSONObject(CALCULATED).optDouble("recovery_rate", 0.0));
            }
        }
        double casesPerOneHundred = ((double) countryDetailsDto.getConfirmed() / (double) countryDetailsDto.getPopulation()) * 100000;
        countryDetailsDto.setCasesPerOneHundred((int) casesPerOneHundred);
        double deathsPerOneHundred = ((double) countryDetailsDto.getDeaths() / (double) countryDetailsDto.getPopulation()) * 100000;
        countryDetailsDto.setDeathsPerOneHundred((int) deathsPerOneHundred);
        log.debug("Returned details for {}", country);
        return countryDetailsDto;
    }

    private String checkCountryName(String country) {
        if (country.equals("US")) {
            return "United States";
        }
        if (country.equals("Korea, South")) {
            return "South Korea";
        }
        if (country.equals("St. Vincent Grenadines")) {
            return "Saint Vincent Grenadines";
        }
        return country;
    }

    private String checkCountryNameSecond(String country) {
        if (country.equals("United States")) {
            return "USA";
        }
        if (country.equals("South Korea")) {
            return "S. Korea";
        }
        if (country.equals("Saint Vincent and the Grenadines")) { //Brauche ich das noch?
            return "Saint Vincent Grenadines";
        }
        if (country.equals("United Kingdom")) {
            return "UK";
        }
        return country;
    }

    public List<DistrictDto> readDistrictsValues(String code) throws IOException {
        log.debug("Invoke get district values of {}", code);
        List<DistrictDto> districtDtoList = new ArrayList<>();

        JSONObject jsonObject = getJSONObject("https://www.trackcorona.live/api/cities");
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("country_code").equals(code)) {
                DistrictDto districtDto = new DistrictDto();
                districtDto.setLocation(jsonArray.getJSONObject(i).optString("location", "Empty"));
                districtDto.setCode(jsonArray.getJSONObject(i).optString("country_code", "Empty"));
                districtDto.setConfirmed(jsonArray.getJSONObject(i).optInt(CONFIRMED, 0));
                districtDto.setDead(jsonArray.getJSONObject(i).optInt("dead", 0));
                districtDto.setRecovered(jsonArray.getJSONObject(i).optInt(RECOVERED, 0));
                districtDto.setVelocityConfirmed(jsonArray.getJSONObject(i).optInt("velocity_confirmed", 0));
                districtDto.setVelocityDead(jsonArray.getJSONObject(i).optInt("velocity_dead", 0));
                districtDto.setVelocityRecovered(jsonArray.getJSONObject(i).optInt("velocity_recovered", 0));
                districtDto.setLastUpdate(jsonArray.getJSONObject(i).optString("updated", "Empty"));
                districtDtoList.add(districtDto);
            }
        }
        log.debug("Return list with all district values of {}", code);
        return districtDtoList;
    }
}