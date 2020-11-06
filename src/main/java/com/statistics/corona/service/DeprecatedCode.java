package com.statistics.corona.service;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.DistrictDto;
import com.statistics.corona.model.TimeSeriesWorldDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Deprecated
@Slf4j
public class DeprecatedCode {

    private static final String CONFIRMED = "confirmed";
    private static final String RECOVERED = "recovered";
    private static final String DEATHS = "deaths";
    private static final String LATEST_DATA = "latest_data";
    private static final String CALCULATED = "calculated";
    private static final String COUNTRY_CODE = "country_code";
    private static final String LOCATION = "location";
    private static final String VALUE = "value";
    private static final String NO_NAME = "no_name";
    private final DateFormat dateFormat = new DateFormat();

    private JSONObject getJSONObject(String url) throws IOException {
        return new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
    }

    public List<TimeSeriesWorldDto> readWorldValuesOfJson() throws IOException {
        List<TimeSeriesWorldDto> allValuesWorld = new ArrayList<>();
        JSONObject jsonObjectWorld = getJSONObject("https://corona-api.com/timeline");
        JSONArray jsonArrayWorld = jsonObjectWorld.getJSONArray("data");
        for (int i = 0; i < jsonArrayWorld.length(); i++) {
            TimeSeriesWorldDto timeSeriesWorldDto = new TimeSeriesWorldDto();
            timeSeriesWorldDto.setUpdated_at(jsonArrayWorld.getJSONObject(i).optString("updated_at", "2000-01-01'T'00:00:00.000'Z'"));
            //timeSeriesWorldDto.setDate(dateFormat.formatDate(jsonArrayWorld.getJSONObject(i).optString("date", "2000-01-01")));
            timeSeriesWorldDto.setConfirmed(jsonArrayWorld.getJSONObject(i).optInt(CONFIRMED, 0));
            timeSeriesWorldDto.setRecovered(jsonArrayWorld.getJSONObject(i).optInt(RECOVERED, 0));
            timeSeriesWorldDto.setDeaths(jsonArrayWorld.getJSONObject(i).optInt(DEATHS, 0));
            timeSeriesWorldDto.setActive(jsonArrayWorld.getJSONObject(i).optInt("active", 0));
            timeSeriesWorldDto.setNew_confirmed(jsonArrayWorld.getJSONObject(i).optInt("new_confirmed", 0));
            timeSeriesWorldDto.setNew_recovered(jsonArrayWorld.getJSONObject(i).optInt("new_recovered", 0));
            timeSeriesWorldDto.setNew_deaths(jsonArrayWorld.getJSONObject(i).optInt("new_deaths", 0));
            if (i == 0) {
                timeSeriesWorldDto.setIs_in_progress(jsonArrayWorld.getJSONObject(i).optBoolean("is_in_progress", false));
            }
            allValuesWorld.add(timeSeriesWorldDto);
        }
        log.debug("Returned world time series of JSON");
        return allValuesWorld;
    }

    public CountryDetailsDto readCountryValuesOfJson(String country) throws IOException {
        JSONObject jsonObjectFirst = getJSONObject("https://covid19.mathdro.id/api/countries/" +
                URLEncoder.encode(country, StandardCharsets.UTF_8).replace("+", "%20"));
        /*countryDetailsDto.setConfirmed(jsonObjectFirst.getJSONObject(CONFIRMED).optInt(VALUE, 0));
        countryDetailsDto.setRecovered(jsonObjectFirst.getJSONObject(RECOVERED).optInt(VALUE, 0));
        countryDetailsDto.setDeaths(jsonObjectFirst.getJSONObject(DEATHS).optInt(VALUE, 0));

        JSONObject jsonObject = getJSONObject("https://corona.lmao.ninja/v2/countries/" + country);
        countryDetailsDto.setUpdated(jsonObject.optLong("updated", 0));
        countryDetailsDto.setCountry(jsonObject.optString("country", NO_NAME));
        countryDetailsDto.set(jsonObject.getJSONObject("countryInfo").optString("iso2", NO_NAME));
        countryDetailsDto.setTodayConfirmed(jsonObject.optInt("todayCases", 0));
        countryDetailsDto.setTodayDeaths(jsonObject.optInt("todayDeaths", 0));
        countryDetailsDto.setTodayRecovered(jsonObject.optInt("todayRecovered", 0));
        countryDetailsDto.setActive(jsonObject.optInt("active", 0));
        countryDetailsDto.setCritical(jsonObject.optInt("critical", 0));
        countryDetailsDto.setCasesPerOneMillion(jsonObject.optInt("casesPerOneMillion", 0));
        countryDetailsDto.setDeathsPerOneMillion(jsonObject.optInt("deathsPerOneMillion", 0));
        countryDetailsDto.setTests(jsonObject.optInt("tests", 0));
        countryDetailsDto.setTestsPerOneMillion(jsonObject.optInt("testsPerOneMillion", 0));
        countryDetailsDto.setPopulation(jsonObject.optInt("population", 0));
        countryDetailsDto.setContinent(jsonObject.optString("continent", NO_NAME));
        countryDetailsDto.setOneCasePerPeople(jsonObject.optInt("oneCasePerPeople", 0));
        countryDetailsDto.setOneDeathPerPeople(jsonObject.optInt("oneDeathPerPeople", 0));
        countryDetailsDto.setOneTestPerPeople(jsonObject.optInt("oneTestPerPeople", 0));
        countryDetailsDto.setActivePerOneMillion(jsonObject.optDouble("activePerOneMillion", 0.0));
        countryDetailsDto.setRecoveredPerOneMillion(jsonObject.optDouble("recoveredPerOneMillion", 0.0));
        countryDetailsDto.setCriticalPerOneMillion(jsonObject.optDouble("criticalPerOneMillion", 0.0));*/
        return null;
    }

    private String checkCountryName(String country) {
        if (country.equals("US")) {
            return "United States";
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
        if (country.equals("United Kingdom")) {
            return "UK";
        }
        if (country.equals("Congo (Brazzaville)") || country.equals("Congo (Kinshasa)")) {
            return "Congo";
        }
        if (country.equals("Taiwan*")) {
            return "Taiwan";
        }
        return country;
    }

    public List<DistrictDto> readDistrictsValues() throws IOException {
        List<DistrictDto> districtDtoList = new ArrayList<>();

        JSONObject jsonObject = getJSONObject("https://www.trackcorona.live/api/cities");
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            DistrictDto districtDto = new DistrictDto();
            districtDto.setLocation(jsonArray.getJSONObject(i).optString(LOCATION, NO_NAME));
            districtDto.setCountry_code(jsonArray.getJSONObject(i).optString(COUNTRY_CODE, NO_NAME));
            districtDto.setConfirmed(jsonArray.getJSONObject(i).optInt(CONFIRMED, 0));
            districtDto.setDead(jsonArray.getJSONObject(i).optInt("dead", 0));
            districtDto.setRecovered(jsonArray.getJSONObject(i).optInt(RECOVERED, 0));
            districtDto.setVelocity_confirmed(jsonArray.getJSONObject(i).optInt("velocity_confirmed", 0));
            districtDto.setVelocity_dead(jsonArray.getJSONObject(i).optInt("velocity_dead", 0));
            districtDto.setVelocity_recovered(jsonArray.getJSONObject(i).optInt("velocity_recovered", 0));
            districtDto.setUpdated(jsonArray.getJSONObject(i).optString("updated", NO_NAME));
            districtDtoList.add(districtDto);
        }
        log.debug("Return list with all district values of ");
        return districtDtoList;
    }
}