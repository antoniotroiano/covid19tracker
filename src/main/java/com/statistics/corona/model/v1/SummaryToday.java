package com.statistics.corona.model.v1;

import java.time.LocalTime;

public class SummaryToday {

    private long dataSummaryTodayId;
    private String country;
    private String provinceState;
    private int newConfirmedToday;
    private int confirmedToday;
    private int newRecoveredToday;
    private int recoveredToday;
    private int newDeathsToday;
    private int deathsToday;
    private int active;
    private String combinedKey;
    private double incidentRate;
    private String lastUpdate;
    private String localDate;
    private LocalTime localTime;

    public long getDataSummaryTodayId() {
        return dataSummaryTodayId;
    }

    public void setDataSummaryTodayId(long dataSummaryTodayId) {
        this.dataSummaryTodayId = dataSummaryTodayId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvinceState() {
        return provinceState;
    }

    public void setProvinceState(String provinceState) {
        this.provinceState = provinceState;
    }

    public int getNewConfirmedToday() {
        return newConfirmedToday;
    }

    public void setNewConfirmedToday(int newConfirmedToday) {
        this.newConfirmedToday = newConfirmedToday;
    }

    public int getConfirmedToday() {
        return confirmedToday;
    }

    public void setConfirmedToday(int confirmedToday) {
        this.confirmedToday = confirmedToday;
    }

    public int getNewRecoveredToday() {
        return newRecoveredToday;
    }

    public void setNewRecoveredToday(int newRecoveredToday) {
        this.newRecoveredToday = newRecoveredToday;
    }

    public int getRecoveredToday() {
        return recoveredToday;
    }

    public void setRecoveredToday(int recoveredToday) {
        this.recoveredToday = recoveredToday;
    }

    public int getNewDeathsToday() {
        return newDeathsToday;
    }

    public void setNewDeathsToday(int newDeathsToday) {
        this.newDeathsToday = newDeathsToday;
    }

    public int getDeathsToday() {
        return deathsToday;
    }

    public void setDeathsToday(int deathsToday) {
        this.deathsToday = deathsToday;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getCombinedKey() {
        return combinedKey;
    }

    public void setCombinedKey(String combinedKey) {
        this.combinedKey = combinedKey;
    }

    public double getIncidentRate() {
        return incidentRate;
    }

    public void setIncidentRate(double incidentRate) {
        this.incidentRate = incidentRate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    @Override
    public String toString() {
        return "SummaryToday{" +
                "country='" + country + '\'' +
                ", provinceState='" + provinceState + '\'' +
                ", combinedKey='" + combinedKey + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", localDate='" + localDate + '\'' +
                '}';
    }
}