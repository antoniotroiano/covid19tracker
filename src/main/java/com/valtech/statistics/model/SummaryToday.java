package com.valtech.statistics.model;

import java.time.LocalTime;

public class SummaryToday {

    private long dataSummaryTodayId;
    private String country;
    private int newConfirmedToday;
    private int confirmedToday;
    private int newRecoveredToday;
    private int recoveredToday;
    private int newDeathsToday;
    private int deathsToday;
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
                ", lastUpdate='" + lastUpdate + '\'' +
                ", localDate='" + localDate + '\'' +
                '}';
    }
}