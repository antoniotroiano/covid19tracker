package com.statistics.corona.model;

public class CountryDetailsDto {

    String country;
    String code;
    int population;
    String lastUpdate;
    int todayDeaths;
    int todayConfirmed;
    int todayRecovered;
    int deaths;
    int confirmed;
    int recovered;
    int critical;
    double deathRate;
    double recoveryRate;
    int casesPerMillionPopulation;

    public CountryDetailsDto() {
    }

    public CountryDetailsDto(CountryDetailsDto countryDetailsDto) {
        this.country = countryDetailsDto.getCountry();
        this.code = countryDetailsDto.getCode();
        this.population = countryDetailsDto.getPopulation();
        this.lastUpdate = countryDetailsDto.getLastUpdate();
        this.todayDeaths = countryDetailsDto.getTodayDeaths();
        this.todayConfirmed = countryDetailsDto.getTodayConfirmed();
        this.todayRecovered = countryDetailsDto.getTodayRecovered();
        this.deaths = countryDetailsDto.getDeaths();
        this.confirmed = countryDetailsDto.getConfirmed();
        this.recovered = countryDetailsDto.getRecovered();
        this.critical = countryDetailsDto.getCritical();
        this.deathRate = countryDetailsDto.getDeathRate();
        this.recoveryRate = countryDetailsDto.getRecoveryRate();
        this.casesPerMillionPopulation = countryDetailsDto.getCasesPerMillionPopulation();
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

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getTodayDeaths() {
        return todayDeaths;
    }

    public void setTodayDeaths(int todayDeaths) {
        this.todayDeaths = todayDeaths;
    }

    public int getTodayConfirmed() {
        return todayConfirmed;
    }

    public void setTodayConfirmed(int todayConfirmed) {
        this.todayConfirmed = todayConfirmed;
    }

    public int getTodayRecovered() {
        return todayRecovered;
    }

    public void setTodayRecovered(int todayRecovered) {
        this.todayRecovered = todayRecovered;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
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

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public double getDeathRate() {
        return deathRate;
    }

    public void setDeathRate(double deathRate) {
        this.deathRate = deathRate;
    }

    public double getRecoveryRate() {
        return recoveryRate;
    }

    public void setRecoveryRate(double recoveryRate) {
        this.recoveryRate = recoveryRate;
    }

    public int getCasesPerMillionPopulation() {
        return casesPerMillionPopulation;
    }

    public void setCasesPerMillionPopulation(int casesPerMillionPopulation) {
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
                ", todayRecovered=" + todayRecovered +
                ", deaths=" + deaths +
                ", confirmed=" + confirmed +
                ", recovered=" + recovered +
                ", critical=" + critical +
                ", deathRate=" + deathRate +
                ", recoveryRate=" + recoveryRate +
                ", casesPerMillionPopulation=" + casesPerMillionPopulation +
                '}';
    }
}