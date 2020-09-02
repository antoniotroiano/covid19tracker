package com.statistics.corona.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataObjectCountry {

    @JsonProperty("confirmed")
    DataValue dataValueConfirmed;

    @JsonProperty("recovered")
    DataValue dataValueRecovered;

    @JsonProperty("deaths")
    DataValue dataValueDeaths;

    public DataValue getDataValueConfirmed() {
        return dataValueConfirmed;
    }

    public void setDataValueConfirmed(DataValue dataValueConfirmed) {
        this.dataValueConfirmed = dataValueConfirmed;
    }

    public DataValue getDataValueRecovered() {
        return dataValueRecovered;
    }

    public void setDataValueRecovered(DataValue dataValueRecovered) {
        this.dataValueRecovered = dataValueRecovered;
    }

    public DataValue getDataValueDeaths() {
        return dataValueDeaths;
    }

    public void setDataValueDeaths(DataValue dataValueDeaths) {
        this.dataValueDeaths = dataValueDeaths;
    }

    @Override
    public String toString() {
        return "DataObjectCountry{" +
                "dataValueConfirmed=" + dataValueConfirmed +
                ", dataValueRecovered=" + dataValueRecovered +
                ", dataValueDeaths=" + dataValueDeaths +
                '}';
    }
}