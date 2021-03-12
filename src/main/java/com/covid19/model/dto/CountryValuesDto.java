package com.covid19.model.dto;

import com.covid19.model.data.CountryInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class CountryValuesDto {

    long updated;
    String country;
    String district;
    String province;

    @JsonProperty("countryInfo")
    CountryInfo countryInfo;

    int cases;
    int todayCases;
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
    int sevenDayIncidence;
    Map<String, Integer> casesValues;
    Map<String, Integer> recoveredValues;
    Map<String, Integer> deathsValues;

    public CountryValuesDto() {
    }

    public CountryValuesDto(CountryValuesDto countryValuesDto) {
        this.updated = countryValuesDto.getUpdated();
        this.country = countryValuesDto.getCountry();
        this.district = countryValuesDto.getDistrict();
        this.province = countryValuesDto.getProvince();
        this.countryInfo = countryValuesDto.getDataObjectCountryInfo();
        this.cases = countryValuesDto.getCases();
        this.todayCases = countryValuesDto.getTodayCases();
        this.deaths = countryValuesDto.getDeaths();
        this.todayDeaths = countryValuesDto.getTodayDeaths();
        this.recovered = countryValuesDto.getRecovered();
        this.todayRecovered = countryValuesDto.getTodayRecovered();
        this.active = countryValuesDto.getActive();
        this.critical = countryValuesDto.getCritical();
        this.casesPerOneMillion = countryValuesDto.getCasesPerOneMillion();
        this.deathsPerOneMillion = countryValuesDto.getDeathsPerOneMillion();
        this.tests = countryValuesDto.getTests();
        this.testsPerOneMillion = countryValuesDto.getTestsPerOneMillion();
        this.population = countryValuesDto.getPopulation();
        this.continent = countryValuesDto.getContinent();
        this.oneCasePerPeople = countryValuesDto.getOneCasePerPeople();
        this.oneDeathPerPeople = countryValuesDto.getOneDeathPerPeople();
        this.oneTestPerPeople = countryValuesDto.getOneTestPerPeople();
        this.activePerOneMillion = countryValuesDto.getActivePerOneMillion();
        this.recoveredPerOneMillion = countryValuesDto.getRecoveredPerOneMillion();
        this.criticalPerOneMillion = countryValuesDto.getCriticalPerOneMillion();
        this.deathRate = countryValuesDto.getDeathRate();
        this.recoveryRate = countryValuesDto.getRecoveryRate();
        this.casesPerOneHundred = countryValuesDto.getCasesPerOneHundred();
        this.deathsPerOneHundred = countryValuesDto.getDeathsPerOneHundred();
        this.sevenDayIncidence = countryValuesDto.getSevenDayIncidence();
        this.casesValues = countryValuesDto.getCasesValues();
        this.recoveredValues = countryValuesDto.getRecoveredValues();
        this.deathsValues = countryValuesDto.getDeathsValues();
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

    public CountryInfo getDataObjectCountryInfo() {
        return countryInfo;
    }

    public void setDataObjectCountryInfo(CountryInfo countryInfo) {
        this.countryInfo = countryInfo;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int confirmed) {
        this.cases = confirmed;
    }

    public int getTodayCases() {
        return todayCases;
    }

    public void setTodayCases(int todayConfirmed) {
        this.todayCases = todayConfirmed;
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

    public int getSevenDayIncidence() {
        return sevenDayIncidence;
    }

    public void setSevenDayIncidence(int sevenDayIncidence) {
        this.sevenDayIncidence = sevenDayIncidence;
    }

    public Map<String, Integer> getCasesValues() {
        return casesValues;
    }

    public void setCasesValues(Map<String, Integer> casesValues) {
        this.casesValues = casesValues;
    }

    public Map<String, Integer> getRecoveredValues() {
        return recoveredValues;
    }

    public void setRecoveredValues(Map<String, Integer> recoveredValues) {
        this.recoveredValues = recoveredValues;
    }

    public Map<String, Integer> getDeathsValues() {
        return deathsValues;
    }

    public void setDeathsValues(Map<String, Integer> deathsValues) {
        this.deathsValues = deathsValues;
    }

    @Override
    public String toString() {
        return "CountryValuesDto{" +
                "updated=" + updated +
                ", country='" + country + '\'' +
                ", district='" + district + '\'' +
                ", province='" + province + '\'' +
                ", dataObjectCountryInfo=" + countryInfo +
                ", cases=" + cases +
                ", todayCases=" + todayCases +
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
                ", sevenDayIncidence=" + sevenDayIncidence +
                ", casesValues=" + casesValues +
                ", recoveredValues=" + recoveredValues +
                ", deathsValues=" + deathsValues +
                '}';
    }
}