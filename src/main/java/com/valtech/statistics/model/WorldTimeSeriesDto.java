package com.valtech.statistics.model;

public class WorldTimeSeriesDto {

    String lastUpdate;
    String date;
    int confirmed;
    int recovered;
    int deaths;
    int active;
    int newConfirmed;
    int newRecovered;
    int newDeaths;
    boolean inProgress;

    public WorldTimeSeriesDto() {
    }

    public WorldTimeSeriesDto(String lastUpdate, String date, int confirmed, int recovered, int deaths, int active, int newConfirmed, int newRecovered, int newDeaths, boolean inProgress) {
        this.lastUpdate = lastUpdate;
        this.date = date;
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.deaths = deaths;
        this.active = active;
        this.newConfirmed = newConfirmed;
        this.newRecovered = newRecovered;
        this.newDeaths = newDeaths;
        this.inProgress = inProgress;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public int getNewConfirmed() {
        return newConfirmed;
    }

    public void setNewConfirmed(int newConfirmed) {
        this.newConfirmed = newConfirmed;
    }

    public int getNewRecovered() {
        return newRecovered;
    }

    public void setNewRecovered(int newRecovered) {
        this.newRecovered = newRecovered;
    }

    public int getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(int newDeaths) {
        this.newDeaths = newDeaths;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    @Override
    public String toString() {
        return "WorldTimeSeriesDto{" +
                "lastUpdate='" + lastUpdate + '\'' +
                ", date='" + date + '\'' +
                ", confirmed=" + confirmed +
                ", recovered=" + recovered +
                ", deaths=" + deaths +
                ", active=" + active +
                ", newConfirmed=" + newConfirmed +
                ", newRecovered=" + newRecovered +
                ", newDeaths=" + newDeaths +
                ", inProgress=" + inProgress +
                '}';
    }
}