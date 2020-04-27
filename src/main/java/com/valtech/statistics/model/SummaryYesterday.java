package com.valtech.statistics.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class SummaryYesterday {

    private long dataSummaryYesterdayId;
    private String country;
    private int confirmedYesterday;
    private int recoveredYesterday;
    private int deathsYesterday;
    private LocalDate localDate;
    private LocalTime localTime;

    public long getDataSummaryYesterdayId() {
        return dataSummaryYesterdayId;
    }

    public void setDataSummaryYesterdayId(long dataSummaryYesterdayId) {
        this.dataSummaryYesterdayId = dataSummaryYesterdayId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getConfirmedYesterday() {
        return confirmedYesterday;
    }

    public void setConfirmedYesterday(int confirmedYesterday) {
        this.confirmedYesterday = confirmedYesterday;
    }

    public int getRecoveredYesterday() {
        return recoveredYesterday;
    }

    public void setRecoveredYesterday(int recoveredYesterday) {
        this.recoveredYesterday = recoveredYesterday;
    }

    public int getDeathsYesterday() {
        return deathsYesterday;
    }

    public void setDeathsYesterday(int deathsYesterday) {
        this.deathsYesterday = deathsYesterday;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
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
        return "SummaryYesterday{" +
                "country='" + country + '\'' +
                '}';
    }
}
