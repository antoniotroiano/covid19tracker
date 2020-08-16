package com.statistics.corona.model;

public class TimeSeriesWorldDto {

    String updated_at;
    String date;
    int confirmed;
    int recovered;
    int deaths;
    int active;
    int newConfirmed;
    int newRecovered;
    int newDeaths;
    boolean is_in_progress;

    public TimeSeriesWorldDto() {
        //Always needed an empty constructor
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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

    public boolean isIs_in_progress() {
        return is_in_progress;
    }

    public void setIs_in_progress(boolean is_in_progress) {
        this.is_in_progress = is_in_progress;
    }

    @Override
    public String toString() {
        return "WorldTimeSeriesDto{" +
                "updated_at='" + updated_at + '\'' +
                ", date='" + date + '\'' +
                ", confirmed=" + confirmed +
                ", recovered=" + recovered +
                ", deaths=" + deaths +
                ", active=" + active +
                ", newConfirmed=" + newConfirmed +
                ", newRecovered=" + newRecovered +
                ", newDeaths=" + newDeaths +
                ", is_in_progress=" + is_in_progress +
                '}';
    }
}