package com.statistics.corona.model;

public class TimeSeriesWorldDto {

    String updated_at;
    String date;
    int confirmed;
    int recovered;
    int deaths;
    int active;
    int new_confirmed;
    int new_recovered;
    int new_deaths;
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

    public int getNew_confirmed() {
        return new_confirmed;
    }

    public void setNew_confirmed(int new_confirmed) {
        this.new_confirmed = new_confirmed;
    }

    public int getNew_recovered() {
        return new_recovered;
    }

    public void setNew_recovered(int new_recovered) {
        this.new_recovered = new_recovered;
    }

    public int getNew_deaths() {
        return new_deaths;
    }

    public void setNew_deaths(int new_deaths) {
        this.new_deaths = new_deaths;
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
                ", new_confirmed=" + new_confirmed +
                ", new_recovered=" + new_recovered +
                ", new_deaths=" + new_deaths +
                ", is_in_progress=" + is_in_progress +
                '}';
    }
}