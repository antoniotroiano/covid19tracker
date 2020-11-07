package com.statistics.corona.model.dto;

import com.opencsv.bean.CsvBindByPosition;

public class CountryDailyDto {

    @CsvBindByPosition(position = 0)
    Integer fips;
    @CsvBindByPosition(position = 1)
    String admin2;
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
    @CsvBindByPosition(position = 12)
    Double incidenceRate;
    @CsvBindByPosition(position = 13)
    Double caseFatalityRatio;

    public CountryDailyDto() {
        //Always needed an empty constructor
    }

    public CountryDailyDto(CountryDailyDto countryDailyDto) {
        this.fips = countryDailyDto.getFips();
        this.admin2 = countryDailyDto.getAdmin2();
        this.province = countryDailyDto.getProvince();
        this.country = countryDailyDto.getCountry();
        this.lastUpdate = countryDailyDto.getLastUpdate();
        this.confirmed = countryDailyDto.getConfirmed();
        this.deaths = countryDailyDto.getDeaths();
        this.recovered = countryDailyDto.getRecovered();
        this.active = countryDailyDto.getActive();
        this.combinedKey = countryDailyDto.getCombinedKey();
        this.incidenceRate = countryDailyDto.getIncidenceRate();
        this.caseFatalityRatio = countryDailyDto.getCaseFatalityRatio();
    }

    public Integer getFips() {
        return fips;
    }

    public void setFips(Integer fips) {
        this.fips = fips;
    }

    public String getAdmin2() {
        return admin2;
    }

    public void setAdmin2(String admin2) {
        this.admin2 = admin2;
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

    public Double getIncidenceRate() {
        return incidenceRate;
    }

    public void setIncidenceRate(Double incidenceRate) {
        this.incidenceRate = incidenceRate;
    }

    public Double getCaseFatalityRatio() {
        return caseFatalityRatio;
    }

    public void setCaseFatalityRatio(Double caseFatalityRatio) {
        this.caseFatalityRatio = caseFatalityRatio;
    }

    @Override
    public String toString() {
        return "DailyReportDto{" +
                "fips=" + fips +
                ", admin2='" + admin2 + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", confirmed=" + confirmed +
                ", deaths=" + deaths +
                ", recovered=" + recovered +
                ", active=" + active +
                ", combinedKey='" + combinedKey + '\'' +
                ", incidenceRate=" + incidenceRate +
                ", caseFatalityRatio=" + caseFatalityRatio +
                '}';
    }
}