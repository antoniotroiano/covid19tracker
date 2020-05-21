package com.statistics.corona.model;

import java.util.LinkedHashMap;

public class TimeSeriesDto {

    String province;
    String country;
    LinkedHashMap<String, Integer> dataMap;

    public TimeSeriesDto() {
    }

    public TimeSeriesDto(String province, String country, LinkedHashMap<String, Integer> dataMap) {
        this.province = province;
        this.country = country;
        this.dataMap = dataMap;
    }

    public TimeSeriesDto(TimeSeriesDto timeSeriesDto) {
        this.province = timeSeriesDto.getProvince();
        this.country = timeSeriesDto.getCountry();
        this.dataMap = timeSeriesDto.getDataMap();
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

    public LinkedHashMap<String, Integer> getDataMap() {
        return dataMap;
    }

    public void setDataMap(LinkedHashMap<String, Integer> dataMap) {
        this.dataMap = dataMap;
    }

    @Override
    public String toString() {
        return "TimeSeriesConfirmed{" +
                "province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", confirmed=" + dataMap +
                '}';
    }
}