package com.valtech.statistics.service.json;

import com.valtech.statistics.model.SummaryToday;
import com.valtech.statistics.service.DateFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetJsonValue {

    private static final String VALUE = "value";
    private static final String CONFIRMED = "confirmed";
    private static final String RECOVERED = "recovered";
    private static final String DEATHS = "deaths";
    private final DateFormat dateFormat;

    private JSONObject getJSONObject(String url) throws IOException {
        return new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
    }

    private int getValueOfJSONObject(JSONObject jsonObject, String key, String valueKey) throws JSONException {
        JSONObject getIntValue = (JSONObject) jsonObject.get(key);
        return getIntValue.getInt(valueKey);
    }

/*    private String getDateNow() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM");
        LocalDate now = LocalDate.now();
        return now.format(dtf);
    }*/

    private String formatCountry(String country) {
        String[] countryArray = country.split(" ");
        StringBuilder bld = new StringBuilder();
        for (String s : countryArray) {
            bld.append(s).append("%20");
        }
        return bld.toString().substring(0, bld.length() - 3);
    }

    private String formatCountryDayOne(String country) {
        String[] countryArray = country.split(" ");
        StringBuilder bld = new StringBuilder();
        for (String s : countryArray) {
            bld.append(s).append("-");
        }
        return bld.toString().substring(0, bld.length() - 1);
    }

    public List<String> getCountryOfJSONObject() throws IOException {
        List<String> allCountries = new ArrayList<>();
        JSONObject countries = getJSONObject("https://covid19.mathdro.id/api/countries");
        JSONArray getValueOfArray = countries.getJSONArray("countries");
        for (int i = 1; i < getValueOfArray.length(); i++) {
            JSONObject jsonObject = getValueOfArray.getJSONObject(i);
            allCountries.add(jsonObject.getString("name"));
        }
        return allCountries;
    }

    private JSONArray getJSONArrayOfYesterday() {
        log.debug("Invoke get json array of yesterday.");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        String yesterdayDaily = "https://covid19.mathdro.id/api/daily/" + yesterdayDate.format(dtf);

        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(IOUtils.toString(new URL(yesterdayDaily), StandardCharsets.UTF_8));
            HttpURLConnection huc = (HttpURLConnection) new URL(yesterdayDaily).openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int statusCode = huc.getResponseCode();
            if (statusCode == 404) {
                log.warn("No new json array for yesterday exist.");
            }
        } catch (Exception e) {
            log.error("No new json array for yesterday exist. {}", e.getMessage());
            return jsonArray;
        }
        log.debug("Get new json array for yesterday {}.", yesterdayDate);
        return jsonArray;
    }

    private void getTodayData(String country, SummaryToday summaryToday, String url) throws IOException {
        summaryToday.setCountry(country);
        summaryToday.setConfirmedToday(getValueOfJSONObject(getJSONObject(url), CONFIRMED, VALUE));
        summaryToday.setRecoveredToday(getValueOfJSONObject(getJSONObject(url), RECOVERED, VALUE));
        summaryToday.setDeathsToday(getValueOfJSONObject(getJSONObject(url), DEATHS, VALUE));
        summaryToday.setLastUpdate(getJSONObject(url).getString("lastUpdate"));
        summaryToday.setLocalTime(LocalTime.now().withNano(0));
    }

    private void getNewData(SummaryToday summaryToday, JSONArray jsonArray) {
        int confirmed = 0;
        int recovered = 0;
        int deaths = 0;
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject jsonObject = jsonArray.getJSONObject(j);
            confirmed += jsonObject.getInt(CONFIRMED);
            recovered += jsonObject.getInt(RECOVERED);
            deaths += jsonObject.getInt(DEATHS);
        }
        summaryToday.setNewConfirmedToday(summaryToday.getConfirmedToday() - confirmed);
        summaryToday.setNewRecoveredToday(summaryToday.getRecoveredToday() - recovered);
        summaryToday.setNewDeathsToday(summaryToday.getDeathsToday() - deaths);
    }

    public SummaryToday getDataOfWorldToModel() throws IOException {
        log.info("Invoke create data of world.");
        SummaryToday summaryToday = new SummaryToday();

        //ToDo: die exception fangen oder nach vorne weitergeben auch für die try chatch blöcke versuchen umzusetzen.
        getTodayData("World", summaryToday, "https://covid19.mathdro.id/api");
        summaryToday.setLocalDate(LocalDate.now().toString());

        JSONArray getValueOfArray = getJSONArrayOfYesterday();
        if (getValueOfArray.isEmpty()) {
            log.info("No data for last day of world {}. Return only data of today.", summaryToday.getLocalDate());
            return summaryToday;
        }
        getNewData(summaryToday, getValueOfArray);
        log.info("Set new data for confirmed, recovered, and deaths for world.");
        return summaryToday;
    }

    public SummaryToday getDataForSelectedCountry(String country) throws IOException {
        log.info("Invoke get data for selected country {}", country);
        SummaryToday summaryToday = new SummaryToday();
        String formattedCountry = formatCountry(country);
        String formattedURL = "https://covid19.mathdro.id/api/countries/" + formattedCountry;

        //ToDo: die exception fangen oder nach vorne weitergeben auch für die try chatch blöcke versuchen umzusetzen.
        getTodayData(country, summaryToday, formattedURL);
        summaryToday.setLocalDate(LocalDate.now().toString());

        JSONArray getValueOfArray = getJSONArrayOfYesterday();
        if (getValueOfArray.isEmpty()) {
            log.info("No data for last day of country {}. Return only data of today.", country);
            return summaryToday;
        }
        JSONArray jsonArrayOneCountry = new JSONArray();
        for (int i = 0; i < getValueOfArray.length(); i++) {
            JSONObject jsonObject = getValueOfArray.getJSONObject(i);
            if (jsonObject.getString("countryRegion").equals(country)) {
                jsonArrayOneCountry.put(jsonObject);
            }
        }
        getNewData(summaryToday, jsonArrayOneCountry);
        log.info("Set new data for confirmed, recovered and deaths for country {}.", country);
        return summaryToday;
    }

    public List<SummaryToday> getDataDayOneTotalSelectedCountry(String country) {
        log.info("Invoke get data of day one for selected country {}", country);
        List<SummaryToday> summaryTodayList = new ArrayList<>();
        String formattedCountry = formatCountryDayOne(country);
        String formattedURL = "https://api.covid19api.com/total/dayone/country/" + formattedCountry;

        try {
            JSONArray jsonArray = new JSONArray(IOUtils.toString(new URL(formattedURL), StandardCharsets.UTF_8));
            if (jsonArray.isEmpty()) {
                log.info("No data for selected country available {}", country);
                return summaryTodayList;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int confirmed = jsonObject.getInt("Confirmed");
                int deaths = jsonObject.getInt("Deaths");
                int recovered = jsonObject.getInt("Recovered");
                String date = jsonObject.getString("Date");

                String formattedDate = dateFormat.formatLastUpdateToDateDayOne(date);

                SummaryToday summaryToday = new SummaryToday();
                summaryToday.setConfirmedToday(confirmed);
                summaryToday.setDeathsToday(deaths);
                summaryToday.setRecoveredToday(recovered);
                summaryToday.setLastUpdate(formattedDate);
                summaryTodayList.add(summaryToday);
            }
            log.info("Return list with all data since day one od {}", country);
            return summaryTodayList;
        } catch (Exception e) {
            log.warn("No new update for day one to today.");
            return summaryTodayList;
        }
    }
}