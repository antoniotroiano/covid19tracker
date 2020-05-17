package com.statistics.corona.service.v1.json;

import com.statistics.corona.model.v1.SummaryToday;
import com.statistics.corona.service.DateFormat;
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
import java.util.Locale;

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

    private JSONArray getJSONOArray(String url) throws IOException {
        return new JSONArray(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
    }

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

    //https://covid19.mathdro.id/api/countries/ All countries names
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

    //https://covid19.mathdro.id/api/daily/05-09-2020
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
            if (statusCode != 200) {
                log.warn("No new json array for yesterday exist.");
                return jsonArray;
            }
            log.debug("Get new json array for yesterday {}.", yesterdayDate);
            return jsonArray;
        } catch (Exception e) {
            log.error("No new json array for yesterday exist. {}", e.getMessage());
            return jsonArray;
        }
    }

    //Get data of today for a selected country
    private void getTodayData(String country, SummaryToday summaryToday, String url) throws IOException {
        summaryToday.setCountry(country);
        summaryToday.setConfirmedToday(getValueOfJSONObject(getJSONObject(url), CONFIRMED, VALUE));
        summaryToday.setRecoveredToday(getValueOfJSONObject(getJSONObject(url), RECOVERED, VALUE));
        summaryToday.setDeathsToday(getValueOfJSONObject(getJSONObject(url), DEATHS, VALUE));
        summaryToday.setLastUpdate(getJSONObject(url).getString("lastUpdate"));
        summaryToday.setLocalDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyy", Locale.GERMAN)));
        summaryToday.setLocalTime(LocalTime.now().withNano(0));
    }

    //Get new data of today for a selected country
    private void getNewData(SummaryToday summaryToday, JSONArray jsonArray) {
        int confirmedNew = 0;
        int recoveredNew = 0;
        int deathsNew = 0;
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject jsonObject = jsonArray.getJSONObject(j);
            confirmedNew += jsonObject.getInt(CONFIRMED);
            recoveredNew += jsonObject.getInt(RECOVERED);
            deathsNew += jsonObject.getInt(DEATHS);
        }
        if (summaryToday.getConfirmedToday() == 0) {
            summaryToday.setNewConfirmedToday(confirmedNew);
        } else {
            summaryToday.setNewConfirmedToday(summaryToday.getConfirmedToday() - confirmedNew);
        }
        if (summaryToday.getRecoveredToday() == 0) {
            summaryToday.setNewRecoveredToday(recoveredNew);
        } else {
            summaryToday.setNewRecoveredToday(summaryToday.getRecoveredToday() - recoveredNew);
        }
        if (summaryToday.getDeathsToday() == 0) {
            summaryToday.setNewDeathsToday(deathsNew);
        } else {
            summaryToday.setNewDeathsToday(summaryToday.getDeathsToday() - deathsNew);
        }
    }

    //https://covid19.mathdro.id/api/
    public SummaryToday getDataOfWorldToModel() throws IOException {
        log.info("Invoke create data of world.");
        SummaryToday summaryToday = new SummaryToday();

        getTodayData("World", summaryToday, "https://covid19.mathdro.id/api");

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

        getTodayData(country, summaryToday, formattedURL);

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
        String formattedURL = "https://api.covid19api.com/dayone/country/" + formattedCountry;

        try {
            JSONArray jsonArray = new JSONArray(IOUtils.toString(new URL(formattedURL), StandardCharsets.UTF_8));
            if (jsonArray.isEmpty()) {
                log.info("No data for selected country available {}", country);
                return summaryTodayList;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String date = jsonObject.getString("Date");
                String province = jsonObject.getString("Province");
                String formattedDate = dateFormat.formatLastUpdateToDateDayOne(date);
                SummaryToday summaryToday = new SummaryToday();
                if (province.equals("")) {
                    summaryToday.setConfirmedToday(jsonObject.getInt("Confirmed"));
                    summaryToday.setDeathsToday(jsonObject.getInt("Deaths"));
                    summaryToday.setRecoveredToday(jsonObject.getInt("Recovered"));
                    summaryToday.setLastUpdate(formattedDate);
                    summaryTodayList.add(summaryToday);
                }
            }
            log.info("Return list with all data since day one od {}", country);
            return summaryTodayList;
        } catch (Exception e) {
            log.warn("No new update for day one to today.");
            return summaryTodayList;
        }
    }

    public List<SummaryToday> getOneDayValues(String country) {
        log.info("Invoke get data of one day for selected country {}", country);
        List<SummaryToday> allValues = getDataDayOneTotalSelectedCountry(country);
        List<SummaryToday> dataOneDayList = new ArrayList<>();
        int confirmedYesterday = 0;
        int recoveredYesterday = 0;
        int deathsYesterday = 0;
        String date;
        for (int i = 0; i < allValues.size(); i++) {
            int confirmedToday = allValues.get(i).getConfirmedToday();
            int recoveredToday = allValues.get(i).getRecoveredToday();
            int deathsToday = allValues.get(i).getDeathsToday();
            date = allValues.get(i).getLastUpdate();
            if (i != 0) {
                confirmedYesterday = allValues.get(i - 1).getConfirmedToday();
                recoveredYesterday = allValues.get(i - 1).getRecoveredToday();
                deathsYesterday = allValues.get(i - 1).getDeathsToday();
            }
            int oneDayConfirmed = confirmedToday - confirmedYesterday;
            int oneDayRecovered = recoveredToday - recoveredYesterday;
            int oneDayDeaths = deathsToday - deathsYesterday;
            SummaryToday summaryToday = new SummaryToday();
            summaryToday.setConfirmedToday(oneDayConfirmed);
            summaryToday.setRecoveredToday(oneDayRecovered);
            summaryToday.setDeathsToday(oneDayDeaths);
            summaryToday.setLastUpdate(date);
            dataOneDayList.add(summaryToday);
        }
        log.info("Returned data for one day of selected country {}", country);
        return dataOneDayList;
    }

    public List<SummaryToday> getJSONArrayOfOneCountry(String country) {
        log.debug("Invoke get data of country {} for all province.", country);
        String formattedCountry = formatCountry(country);
        String countryUrl = "https://covid19.mathdro.id/api/countries/" + formattedCountry + "/confirmed";
        List<SummaryToday> summaryTodayList = new ArrayList<>();

        try {
            JSONArray jsonArray = getJSONOArray(countryUrl);
            for (int i = 0; i < jsonArray.length(); i++) {
                SummaryToday summaryToday = new SummaryToday();
                summaryToday.setCombinedKey(jsonArray.getJSONObject(i).getString("combinedKey"));
                summaryToday.setConfirmedToday(jsonArray.getJSONObject(i).getInt(CONFIRMED));
                summaryToday.setRecoveredToday(jsonArray.getJSONObject(i).getInt(RECOVERED));
                summaryToday.setDeathsToday(jsonArray.getJSONObject(i).getInt(DEATHS));
                summaryToday.setActive(jsonArray.getJSONObject(i).getInt("active"));
                summaryToday.setIncidentRate(jsonArray.getJSONObject(i).getDouble("incidentRate"));
                summaryTodayList.add(summaryToday);
            }
            log.debug("Get new data for selected country {} for all province.", country);
            return summaryTodayList;
        } catch (Exception e) {
            log.error("No new data for selected country {} for all province. {}", country, e.getMessage());
            return summaryTodayList;
        }
    }
}