package com.covid19.model.dto;

import java.util.Map;

public class CountryTimeSeriesDto {

    String district;
    String province;
    String country;
    String combinedKey;
    int population;
    Map<String, Integer> values;

    public CountryTimeSeriesDto() {
    }

    public CountryTimeSeriesDto(CountryTimeSeriesDto countryTimeSeriesDto) {
        this.district = countryTimeSeriesDto.getDistrict();
        this.province = countryTimeSeriesDto.getProvince();
        this.country = countryTimeSeriesDto.getCountry();
        this.combinedKey = countryTimeSeriesDto.getCombinedKey();
        this.population = countryTimeSeriesDto.getPopulation();
        this.values = countryTimeSeriesDto.getValues();
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

    public Map<String, Integer> getValues() {
        return values;
    }

    public void setValues(Map<String, Integer> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "CountryTimeSeriesDto{" +
                "district='" + district + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", combinedKey='" + combinedKey + '\'' +
                ", population=" + population +
                ", cases=" + values +
                '}';
    }
}