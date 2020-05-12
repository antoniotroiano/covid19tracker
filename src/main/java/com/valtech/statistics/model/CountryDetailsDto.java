package com.valtech.statistics.model;

public class CountryDetailsDto {

    String country;
    String code;
    Integer population;
    String lastUpdate;
    Integer todayDeaths;
    Integer todayConfirmed;
    Integer deaths;
    Integer confirmed;
    Integer recovered;
    Integer critical;
    Double deathRate;
    Double recoveryRate;
    Double recoveredVSDeathRatio;
    Integer casesPerMillionPopulation;

    public CountryDetailsDto() {
    }

    public CountryDetailsDto(CountryDetailsDto countryDetailsDto) {
        this.country = countryDetailsDto.getCountry();
        this.code = countryDetailsDto.getCode();
        this.population = countryDetailsDto.getPopulation();
        this.lastUpdate = countryDetailsDto.getLastUpdate();
        this.todayDeaths = countryDetailsDto.getTodayDeaths();
        this.todayConfirmed = countryDetailsDto.getTodayConfirmed();
        this.deaths = countryDetailsDto.getDeaths();
        this.confirmed = countryDetailsDto.getConfirmed();
        this.recovered = countryDetailsDto.getRecovered();
        this.critical = countryDetailsDto.getCritical();
        this.deathRate = countryDetailsDto.getDeathRate();
        this.recoveryRate = countryDetailsDto.getRecoveryRate();
        this.recoveredVSDeathRatio = countryDetailsDto.getRecoveredVSDeathRatio();
        this.casesPerMillionPopulation = countryDetailsDto.getCasesPerMillionPopulation();
    }

    public CountryDetailsDto(String country, String code, int population, String lastUpdate, int todayDeaths, int todayConfirmed, int deaths, int confirmed, int recovered, int critical, Double deathRate, Double recoveryRate, Double recoveredVSDeathRatio, Integer casesPerMillionPopulation) {
        this.country = country;
        this.code = code;
        this.population = population;
        this.lastUpdate = lastUpdate;
        this.todayDeaths = todayDeaths;
        this.todayConfirmed = todayConfirmed;
        this.deaths = deaths;
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.critical = critical;
        this.deathRate = deathRate;
        this.recoveryRate = recoveryRate;
        this.recoveredVSDeathRatio = recoveredVSDeathRatio;
        this.casesPerMillionPopulation = casesPerMillionPopulation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getTodayDeaths() {
        return todayDeaths;
    }

    public void setTodayDeaths(Integer todayDeaths) {
        this.todayDeaths = todayDeaths;
    }

    public Integer getTodayConfirmed() {
        return todayConfirmed;
    }

    public void setTodayConfirmed(Integer todayConfirmed) {
        this.todayConfirmed = todayConfirmed;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
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

    public Integer getCritical() {
        return critical;
    }

    public void setCritical(Integer critical) {
        this.critical = critical;
    }

    public Double getDeathRate() {
        return deathRate;
    }

    public void setDeathRate(Double deathRate) {
        this.deathRate = deathRate;
    }

    public Double getRecoveryRate() {
        return recoveryRate;
    }

    public void setRecoveryRate(Double recoveryRate) {
        this.recoveryRate = recoveryRate;
    }

    public Double getRecoveredVSDeathRatio() {
        return recoveredVSDeathRatio;
    }

    public void setRecoveredVSDeathRatio(Double recoveredVSDeathRatio) {
        this.recoveredVSDeathRatio = recoveredVSDeathRatio;
    }

    public Integer getCasesPerMillionPopulation() {
        return casesPerMillionPopulation;
    }

    public void setCasesPerMillionPopulation(Integer casesPerMillionPopulation) {
        this.casesPerMillionPopulation = casesPerMillionPopulation;
    }

    @Override
    public String toString() {
        return "CountryDetailsDto{" +
                "country='" + country + '\'' +
                ", code='" + code + '\'' +
                ", population=" + population +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", todayDeaths=" + todayDeaths +
                ", todayConfirmed=" + todayConfirmed +
                ", deaths=" + deaths +
                ", confirmed=" + confirmed +
                ", recovered=" + recovered +
                ", critical=" + critical +
                ", deathRate=" + deathRate +
                ", recoveryRate=" + recoveryRate +
                ", recoveredVSDeathRatio=" + recoveredVSDeathRatio +
                ", casesPerMillionPopulation=" + casesPerMillionPopulation +
                '}';
    }
}