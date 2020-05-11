package com.valtech.statistics.model;

public class DailyReportDto {

    String province;
    String country;
    String lastUpdate;
    int confirmed;
    int recovered;
    int deaths;
    int active;
    String combinedKey;

    public DailyReportDto() {
    }

    public DailyReportDto(String province, String country, String lastUpdate, int confirmed, int recovered, int deaths, int active, String combinedKey) {
        this.province = province;
        this.country = country;
        this.lastUpdate = lastUpdate;
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.deaths = deaths;
        this.active = active;
        this.combinedKey = combinedKey;
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
