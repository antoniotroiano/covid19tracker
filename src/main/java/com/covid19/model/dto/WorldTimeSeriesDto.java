package com.covid19.model.dto;

import java.util.Map;

public class WorldTimeSeriesDto {

    private Map<String, Integer> cases;
    private Map<String, Integer> deaths;
    private Map<String, Integer> recovered;

    public void setCases(String key, Integer value) {
        cases.put(key, value);
    }

    public void setDeaths(String key, Integer value) {
        deaths.put(key, value);
    }

    public void setRecovered(String key, Integer value) {
        recovered.put(key, value);
    }

    public Map<String, Integer> getCases() {
        return cases;
    }

    public Map<String, Integer> getDeaths() {
        return deaths;
    }

    public Map<String, Integer> getRecovered() {
        return recovered;
    }
}