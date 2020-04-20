package com.valtech.statistics.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dataGermany")
public class DataGermany {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long dataGermanyId;
    private int confirmed;
    private int recovered;
    private int deaths;
    private String lastUpdate;
    private String localDate;

    public DataGermany() {
    }

    public DataGermany(int confirmed, int recovered, int deaths, String lastUpdate) {
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.deaths = deaths;
        this.lastUpdate = lastUpdate;
    }

    public long getDataGermanyId() {
        return dataGermanyId;
    }

    public void setDataGermanyId(long dataGermanyId) {
        this.dataGermanyId = dataGermanyId;
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

    @Override
    public String toString() {
        return "DataGermany{" +
                "lastUpdate='" + lastUpdate + '\'' +
                ", localDate='" + localDate + '\'' +
                '}';
    }
}