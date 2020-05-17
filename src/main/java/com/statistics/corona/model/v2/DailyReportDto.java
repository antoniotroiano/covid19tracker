package com.statistics.corona.model.v2;

import com.opencsv.bean.CsvBindByPosition;

public class DailyReportDto {

    @CsvBindByPosition(position = 2)
    String province;
    @CsvBindByPosition(position = 3)
    String country;
    @CsvBindByPosition(position = 4)
    String lastUpdate;
    @CsvBindByPosition(position = 7)
    int confirmed;
    @CsvBindByPosition(position = 8)
    int recovered;
    @CsvBindByPosition(position = 9)
    int deaths;
    @CsvBindByPosition(position = 10)
    int active;
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

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
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