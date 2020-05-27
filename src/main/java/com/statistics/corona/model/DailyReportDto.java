package com.statistics.corona.model;

import com.opencsv.bean.CsvBindByPosition;

public class DailyReportDto {

    @CsvBindByPosition(position = 2)
    String province;
    @CsvBindByPosition(position = 3)
    String country;
    @CsvBindByPosition(position = 4)
    String lastUpdate;
    @CsvBindByPosition(position = 7)
    Integer confirmed;
    @CsvBindByPosition(position = 8)
    Integer deaths;
    @CsvBindByPosition(position = 9)
    Integer recovered;
    @CsvBindByPosition(position = 10)
    Integer active;
    @CsvBindByPosition(position = 11)
    String combinedKey;

    public DailyReportDto() {
        //Always needed an empty constructor
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

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    public Integer getRecovered() {
        return recovered;
    }

    public void setRecovered(Integer recovered) {
        this.recovered = recovered;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getCombinedKey() {
        return combinedKey;
    }

    public void setCombinedKey(String combinedKey) {
        this.combinedKey = combinedKey;
    }

    @Override
    public String toString() {
        return "DailyReportDto{" +
                "province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", confirmed=" + confirmed +
                ", recovered=" + recovered +
                ", deaths=" + deaths +
                ", active=" + active +
                ", combinedKey='" + combinedKey + '\'' +
                '}';
    }
}