package com.statistics.corona.model;

public class CountryDetailsDto {

    String country;
    String code;
    int population;
    String lastUpdate;
    int deaths;
    int confirmed;
    int recovered;
    int critical;
    double deathRate;
    double recoveryRate;
    int casesPerOneHundred;
    int deathsPerOneHundred;

    public CountryDetailsDto() {
    }

    public CountryDetailsDto(CountryDetailsDto countryDetailsDto) {
        this.country = countryDetailsDto.getCountry();
        this.code = countryDetailsDto.getCode();
        this.population = countryDetailsDto.getPopulation();
        this.lastUpdate = countryDetailsDto.getLastUpdate();
        this.deaths = countryDetailsDto.getDeaths();
        this.confirmed = countryDetailsDto.getConfirmed();
        this.recovered = countryDetailsDto.getRecovered();
        this.critical = countryDetailsDto.getCritical();
        this.deathRate = countryDetailsDto.getDeathRate();
        this.recoveryRate = countryDetailsDto.getRecoveryRate();
        this.casesPerOneHundred = countryDetailsDto.getCasesPerOneHundred();
        this.deathsPerOneHundred = countryDetailsDto.getDeathsPerOneHundred();
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
                "country='" + country + '\'' +
                ", code='" + code + '\'' +
                ", population=" + population +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", deaths=" + deaths +
                ", confirmed=" + confirmed +
                ", recovered=" + recovered +
                ", critical=" + critical +
                ", deathRate=" + deathRate +
                ", recoveryRate=" + recoveryRate +
                ", casesPerOneHundred=" + casesPerOneHundred +
                ", deathsPerOneHundred=" + deathsPerOneHundred +
                '}';
    }
}