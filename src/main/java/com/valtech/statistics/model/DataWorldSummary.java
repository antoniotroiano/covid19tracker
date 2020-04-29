package com.valtech.statistics.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity
@Table(name = "dataWorldSummary")
public class DataWorldSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long dataWorldSummaryId;
    private String country;
    private int newConfirmed;
    private int totalConfirmed;
    private int newRecovered;
    private int totalRecovered;
    private int newDeaths;
    private int totalDeaths;
    private String lastUpdate;
    private String localDate;
    private LocalTime localTime;

    public DataWorldSummary() {
    }

    public DataWorldSummary(DataWorldSummary dataWorldSummary) {
        this.dataWorldSummaryId = dataWorldSummary.getDataWorldSummaryId();
        this.country = dataWorldSummary.getCountry();
        this.newConfirmed = dataWorldSummary.getNewConfirmed();
        this.totalConfirmed = dataWorldSummary.getTotalConfirmed();
        this.newRecovered = dataWorldSummary.getNewRecovered();
        this.totalRecovered = dataWorldSummary.getTotalRecovered();
        this.newDeaths = dataWorldSummary.getNewDeaths();
        this.totalDeaths = dataWorldSummary.getTotalDeaths();
        this.lastUpdate = dataWorldSummary.getLastUpdate();
        this.localDate = dataWorldSummary.getLocalDate();
        this.localTime = dataWorldSummary.getLocalTime();
    }

    public DataWorldSummary(SummaryToday summaryToday) {
        this.country = summaryToday.getCountry();
        this.newConfirmed = summaryToday.getNewConfirmedToday();
        this.totalConfirmed = summaryToday.getConfirmedToday();
        this.newRecovered = summaryToday.getNewRecoveredToday();
        this.totalRecovered = summaryToday.getRecoveredToday();
        this.newDeaths = summaryToday.getNewDeathsToday();
        this.totalDeaths = summaryToday.getDeathsToday();
        this.lastUpdate = summaryToday.getLastUpdate();
        this.localDate = summaryToday.getLocalDate();
        this.localTime = summaryToday.getLocalTime();
    }

    public long getDataWorldSummaryId() {
        return dataWorldSummaryId;
    }

    public void setDataWorldSummaryId(long dataWorldSummaryId) {
        this.dataWorldSummaryId = dataWorldSummaryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getNewConfirmed() {
        return newConfirmed;
    }

    public void setNewConfirmed(int newConfirmed) {
        this.newConfirmed = newConfirmed;
    }

    public int getTotalConfirmed() {
        return totalConfirmed;
    }

    public void setTotalConfirmed(int totalConfirmed) {
        this.totalConfirmed = totalConfirmed;
    }

    public int getNewRecovered() {
        return newRecovered;
    }

    public void setNewRecovered(int newRecovered) {
        this.newRecovered = newRecovered;
    }

    public int getTotalRecovered() {
        return totalRecovered;
    }

    public void setTotalRecovered(int totalRecovered) {
        this.totalRecovered = totalRecovered;
    }

    public int getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(int newDeaths) {
        this.newDeaths = newDeaths;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(int totalDeaths) {
        this.totalDeaths = totalDeaths;
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
        return "DataWorldSummary{" +
                "country='" + country + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", localDate='" + localDate + '\'' +
                '}';
    }
}