package com.statistics.corona.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountryDetailsDto {

    long updated;
    String country;
    @JsonProperty("countryInfo")
    DataObjectCountryInfo dataObjectCountryInfo;
    int confirmed;
    int todayConfirmed;
    int deaths;
    int todayDeaths;
    int recovered;
    int todayRecovered;
    int active;
    int critical;
    int casesPerOneMillion;
    int deathsPerOneMillion;
    int tests;
    int testsPerOneMillion;
    int population;
    String continent;
    int oneCasePerPeople;
    int oneDeathPerPeople;
    int oneTestPerPeople;
    double activePerOneMillion;
    double recoveredPerOneMillion;
    double criticalPerOneMillion;
    double deathRate;
    double recoveryRate;
    int casesPerOneHundred;
    int deathsPerOneHundred;

    public CountryDetailsDto() {
    }

    public CountryDetailsDto(CountryDetailsDto countryDetailsDto) {
        this.updated = countryDetailsDto.getUpdated();
        this.country = countryDetailsDto.getCountry();
        this.dataObjectCountryInfo = countryDetailsDto.getDataObjectCountryInfo();
        this.confirmed = countryDetailsDto.getConfirmed();
        this.todayConfirmed = countryDetailsDto.getTodayConfirmed();
        this.deaths = countryDetailsDto.getDeaths();
        this.todayDeaths = countryDetailsDto.getTodayDeaths();
        this.recovered = countryDetailsDto.getRecovered();
        this.todayRecovered = countryDetailsDto.getTodayRecovered();
        this.active = countryDetailsDto.getActive();
        this.critical = countryDetailsDto.getCritical();
        this.casesPerOneMillion = countryDetailsDto.getCasesPerOneMillion();
        this.deathsPerOneMillion = countryDetailsDto.getDeathsPerOneMillion();
        this.tests = countryDetailsDto.getTests();
        this.testsPerOneMillion = countryDetailsDto.getTestsPerOneMillion();
        this.population = countryDetailsDto.getPopulation();
        this.continent = countryDetailsDto.getContinent();
        this.oneCasePerPeople = countryDetailsDto.getOneCasePerPeople();
        this.oneDeathPerPeople = countryDetailsDto.getOneDeathPerPeople();
        this.oneTestPerPeople = countryDetailsDto.getOneTestPerPeople();
        this.activePerOneMillion = countryDetailsDto.getActivePerOneMillion();
        this.recoveredPerOneMillion = countryDetailsDto.getRecoveredPerOneMillion();
        this.criticalPerOneMillion = countryDetailsDto.getCriticalPerOneMillion();
        this.deathRate = countryDetailsDto.getDeathRate();
        this.recoveryRate = countryDetailsDto.getRecoveryRate();
        this.casesPerOneHundred = countryDetailsDto.getCasesPerOneHundred();
        this.deathsPerOneHundred = countryDetailsDto.getDeathsPerOneHundred();
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public DataObjectCountryInfo getDataObjectCountryInfo() {
        return dataObjectCountryInfo;
    }

    public void setDataObjectCountryInfo(DataObjectCountryInfo dataObjectCountryInfo) {
        this.dataObjectCountryInfo = dataObjectCountryInfo;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getTodayConfirmed() {
        return todayConfirmed;
    }

    public void setTodayConfirmed(int todayConfirmed) {
        this.todayConfirmed = todayConfirmed;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getTodayDeaths() {
        return todayDeaths;
    }

    public void setTodayDeaths(int todayDeaths) {
        this.todayDeaths = todayDeaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getTodayRecovered() {
        return todayRecovered;
    }

    public void setTodayRecovered(int todayRecovered) {
        this.todayRecovered = todayRecovered;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public int getCasesPerOneMillion() {
        return casesPerOneMillion;
    }

    public void setCasesPerOneMillion(int casesPerOneMillion) {
        this.casesPerOneMillion = casesPerOneMillion;
    }

    public int getDeathsPerOneMillion() {
        return deathsPerOneMillion;
    }

    public void setDeathsPerOneMillion(int deathsPerOneMillion) {
        this.deathsPerOneMillion = deathsPerOneMillion;
    }

    public int getTests() {
        return tests;
    }

    public void setTests(int tests) {
        this.tests = tests;
    }

    public int getTestsPerOneMillion() {
        return testsPerOneMillion;
    }

    public void setTestsPerOneMillion(int testsPerOneMillion) {
        this.testsPerOneMillion = testsPerOneMillion;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public int getOneCasePerPeople() {
        return oneCasePerPeople;
    }

    public void setOneCasePerPeople(int oneCasePerPeople) {
        this.oneCasePerPeople = oneCasePerPeople;
    }

    public int getOneDeathPerPeople() {
        return oneDeathPerPeople;
    }

    public void setOneDeathPerPeople(int oneDeathPerPeople) {
        this.oneDeathPerPeople = oneDeathPerPeople;
    }

    public int getOneTestPerPeople() {
        return oneTestPerPeople;
    }

    public void setOneTestPerPeople(int oneTestPerPeople) {
        this.oneTestPerPeople = oneTestPerPeople;
    }

    public double getActivePerOneMillion() {
        return activePerOneMillion;
    }

    public void setActivePerOneMillion(double activePerOneMillion) {
        this.activePerOneMillion = activePerOneMillion;
    }

    public double getRecoveredPerOneMillion() {
        return recoveredPerOneMillion;
    }

    public void setRecoveredPerOneMillion(double recoveredPerOneMillion) {
        this.recoveredPerOneMillion = recoveredPerOneMillion;
    }

    public double getCriticalPerOneMillion() {
        return criticalPerOneMillion;
    }

    public void setCriticalPerOneMillion(double criticalPerOneMillion) {
        this.criticalPerOneMillion = criticalPerOneMillion;
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

    public int getCasesPerOneHundred() {
        return casesPerOneHundred;
    }

    public void setCasesPerOneHundred(int casesPerOneHundred) {
        this.casesPerOneHundred = casesPerOneHundred;
    }

    public int getDeathsPerOneHundred() {
        return deathsPerOneHundred;
    }

    public void setDeathsPerOneHundred(int deathsPerOneHundred) {
        this.deathsPerOneHundred = deathsPerOneHundred;
    }

    @Override
    public String toString() {
        return "CountryDetailsDto{" +
                "updated=" + updated +
                ", country='" + country + '\'' +
                ", dataObjectCountryInfo='" + dataObjectCountryInfo + '\'' +
                ", confirmed=" + confirmed +
                ", todayConfirmed=" + todayConfirmed +
                ", deaths=" + deaths +
                ", todayDeaths=" + todayDeaths +
                ", recovered=" + recovered +
                ", todayRecovered=" + todayRecovered +
                ", active=" + active +
                ", critical=" + critical +
                ", casesPerOneMillion=" + casesPerOneMillion +
                ", deathsPerOneMillion=" + deathsPerOneMillion +
                ", tests=" + tests +
                ", testsPerOneMillion=" + testsPerOneMillion +
                ", population=" + population +
                ", continent='" + continent + '\'' +
                ", oneCasePerPeople=" + oneCasePerPeople +
                ", oneDeathPerPeople=" + oneDeathPerPeople +
                ", oneTestPerPeople=" + oneTestPerPeople +
                ", activePerOneMillion=" + activePerOneMillion +
                ", recoveredPerOneMillion=" + recoveredPerOneMillion +
                ", criticalPerOneMillion=" + criticalPerOneMillion +
                ", deathRate=" + deathRate +
                ", recoveryRate=" + recoveryRate +
                ", casesPerOneHundred=" + casesPerOneHundred +
                ", deathsPerOneHundred=" + deathsPerOneHundred +
                '}';
    }
}