package com.valtech.statistics.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dataWorldSummary")
public class DataWorldSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long dataWorldId;
    private int newConfirmed;
    private int confirmed;
    private int newRecovered;
    private int recovered;
    private int newDeaths;
    private int deaths;
    private String localDate;

    public DataWorldSummary() {
    }

    public DataWorldSummary(int confirmed, int recovered, int deaths, String lastUpdate) {
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.deaths = deaths;
    }

    public long getDataWorldId() {
        return dataWorldId;
    }

    public void setDataWorldId(long dataWorldId) {
        this.dataWorldId = dataWorldId;
    }

    public int getNewConfirmed() {
        return newConfirmed;
    }

    public void setNewConfirmed(int newConfirmed) {
        this.newConfirmed = newConfirmed;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getNewRecovered() {
        return newRecovered;
    }

    public void setNewRecovered(int newRecovered) {
        this.newRecovered = newRecovered;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recoveredS) {
        this.recovered = recovered;
    }

    public int getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(int newDeaths) {
        this.newDeaths = newDeaths;
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
        return "DataWorldSummary{" +
                "localDate='" + localDate + '\'' +
                '}';
    }


}
