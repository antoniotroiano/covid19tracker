package com.statistics.corona.model;

import java.util.LinkedHashMap;

public class TimeSeriesDto {

    String district;
    String province;
    String country;
    String combinedKey;
    int population;
    LinkedHashMap<String, Integer> dataMap;

    public TimeSeriesDto() {
    }

    public TimeSeriesDto(TimeSeriesDto timeSeriesDto) {
        this.district = timeSeriesDto.getDistrict();
        this.province = timeSeriesDto.getProvince();
        this.country = timeSeriesDto.getCountry();
        this.combinedKey = timeSeriesDto.getCombinedKey();
        this.population = timeSeriesDto.getPopulation();
        this.dataMap = timeSeriesDto.getDataMap();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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

    public String getCombinedKey() {
        return combinedKey;
    }

    public void setCombinedKey(String combinedKey) {
        this.combinedKey = combinedKey;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public LinkedHashMap<String, Integer> getDataMap() {
        return dataMap;
    }

    public void setDataMap(LinkedHashMap<String, Integer> dataMap) {
        this.dataMap = dataMap;
    }

    @Override
    public String toString() {
        return "TimeSeriesDto{" +
                "district='" + district + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", combinedKey='" + combinedKey + '\'' +
                ", population='" + population + '\'' +
                ", dataMap=" + dataMap +
                '}';
    }
}