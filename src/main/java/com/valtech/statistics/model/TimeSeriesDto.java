package com.valtech.statistics.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfirmedDto {

    String province;
    String country;
    LinkedHashMap<String, Integer> confirmed;

    public ConfirmedDto() {
    }

    public ConfirmedDto(String province, String country, LinkedHashMap<String, Integer> confirmed) {
        this.province = province;
        this.country = country;
        this.confirmed = confirmed;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Map<String, Integer> getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(LinkedHashMap<String, Integer> confirmed) {
        this.confirmed = confirmed;
    }

    @Override
    public String toString() {
        return "TimeSeriesConfirmed{" +
                "province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", confirmed=" + confirmed +
                '}';
    }
}