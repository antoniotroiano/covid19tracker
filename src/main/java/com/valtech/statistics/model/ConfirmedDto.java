package com.valtech.statistics.model;

import com.opencsv.bean.CsvBindByPosition;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfirmedDto {

    @CsvBindByPosition(position = 0)
    String province;

    @CsvBindByPosition(position = 1)
    String country;

    LinkedHashMap<String, String> confirmed;

    public ConfirmedDto() {
    }

    public ConfirmedDto(String province, String country, LinkedHashMap<String, String> confirmed) {
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

    public Map<String, String> getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(LinkedHashMap<String, String> confirmed) {
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