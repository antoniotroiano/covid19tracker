package com.valtech.statistics.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dataWorldDaily")
public class DataWorldDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long dataWorldDailyId;
    private int confirmed;
    private int recovered;
    private int deaths;
    private String localDate;

    public DataWorldDaily() {
    }

    public DataWorldDaily(DataWorldDaily dataWorldDaily) {
        this.dataWorldDailyId = dataWorldDaily.getDataWorldDailyId();
        this.confirmed = dataWorldDaily.getConfirmed();
        this.recovered = dataWorldDaily.getRecovered();
        this.deaths = dataWorldDaily.getDeaths();
        this.localDate = dataWorldDaily.getLocalDate();
    }

    public DataWorldDaily(int confirmed, int recovered, int deaths) {
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.deaths = deaths;
    }

    public long getDataWorldDailyId() {
        return dataWorldDailyId;
    }

    public void setDataWorldDailyId(long dataWorldDailyId) {
        this.dataWorldDailyId = dataWorldDailyId;
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

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    @Override
    public String toString() {
        return "DataWorldDaily{" +
                "localDate='" + localDate + '\'' +
                '}';
    }
}