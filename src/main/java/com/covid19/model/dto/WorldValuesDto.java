package com.covid19.model.dto;

import com.covid19.model.data.JsonValueTransfer;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WorldValuesDto {

    WorldTimeSeriesDto worldTimeSeriesDto;

    @JsonProperty("confirmed")
    private JsonValueTransfer confirmed;

    @JsonProperty("recovered")
    private JsonValueTransfer recovered;

    @JsonProperty("deaths")
    private JsonValueTransfer deaths;

    @JsonProperty("lastUpdate")
    private String lastUpdate;

    private Integer newConfirmed;
    private Integer newRecovered;
    private Integer newDeaths;
    private Integer active;
    private Double deathsRate;
    private Double recoveryRate;
    private Double incidenceRateWeek;
    private Double casesPerOneHundred;
    private Double deathsPerOneHundred;
    private Integer population;

    public WorldTimeSeriesDto getWorldTimeSeriesDto() {
        return worldTimeSeriesDto;
    }

    public void setWorldTimeSeriesDto(WorldTimeSeriesDto worldTimeSeriesDto) {
        this.worldTimeSeriesDto = worldTimeSeriesDto;
    }

    public JsonValueTransfer getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(JsonValueTransfer confirmed) {
        this.confirmed = confirmed;
    }

    public JsonValueTransfer getRecovered() {
        return recovered;
    }

    public void setRecovered(JsonValueTransfer recovered) {
        this.recovered = recovered;
    }

    public JsonValueTransfer getDeaths() {
        return deaths;
    }

    public void setDeaths(JsonValueTransfer deaths) {
        this.deaths = deaths;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getNewConfirmed() {
        return newConfirmed;
    }

    public void setNewConfirmed(Integer newConfirmed) {
        this.newConfirmed = newConfirmed;
    }

    public Integer getNewRecovered() {
        return newRecovered;
    }

    public void setNewRecovered(Integer newRecovered) {
        this.newRecovered = newRecovered;
    }

    public Integer getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(Integer newDeaths) {
        this.newDeaths = newDeaths;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Double getDeathsRate() {
        return deathsRate;
    }

    public void setDeathsRate(Double deathsRate) {
        this.deathsRate = deathsRate;
    }

    public Double getRecoveryRate() {
        return recoveryRate;
    }

    public void setRecoveryRate(Double recoveryRate) {
        this.recoveryRate = recoveryRate;
    }

    public Double getIncidenceRateWeek() {
        return incidenceRateWeek;
    }

    public void setIncidenceRateWeek(Double incidenceRateWeek) {
        this.incidenceRateWeek = incidenceRateWeek;
    }

    public Double getCasesPerOneHundred() {
        return casesPerOneHundred;
    }

    public void setCasesPerOneHundred(Double casesPerOneHundred) {
        this.casesPerOneHundred = casesPerOneHundred;
    }

    public Double getDeathsPerOneHundred() {
        return deathsPerOneHundred;
    }

    public void setDeathsPerOneHundred(Double deathsPerOneHundred) {
        this.deathsPerOneHundred = deathsPerOneHundred;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return "WorldValuesDto{" +
                "worldTimeSeriesDto=" + worldTimeSeriesDto +
                ", confirmed=" + confirmed +
                ", recovered=" + recovered +
                ", deaths=" + deaths +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", newConfirmed=" + newConfirmed +
                ", newRecovered=" + newRecovered +
                ", newDeaths=" + newDeaths +
                ", active=" + active +
                ", deathsRate=" + deathsRate +
                ", recoveryRate=" + recoveryRate +
                ", incidenceRateWeek=" + incidenceRateWeek +
                ", casesPerOneHundred=" + casesPerOneHundred +
                ", deathsPerOneHundred=" + deathsPerOneHundred +
                ", population=" + population +
                '}';
    }
}