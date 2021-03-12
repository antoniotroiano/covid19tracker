package com.covid19.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountryValuesTransfer {

    @JsonProperty("confirmed")
    JsonValueTransfer jsonValueTransferConfirmed;

    @JsonProperty("recovered")
    JsonValueTransfer jsonValueTransferRecovered;

    @JsonProperty("deaths")
    JsonValueTransfer jsonValueTransferDeaths;

    public JsonValueTransfer getDataValueConfirmed() {
        return jsonValueTransferConfirmed;
    }

    public void setDataValueConfirmed(JsonValueTransfer jsonValueTransferConfirmed) {
        this.jsonValueTransferConfirmed = jsonValueTransferConfirmed;
    }

    public JsonValueTransfer getDataValueRecovered() {
        return jsonValueTransferRecovered;
    }

    public void setDataValueRecovered(JsonValueTransfer jsonValueTransferRecovered) {
        this.jsonValueTransferRecovered = jsonValueTransferRecovered;
    }

    public JsonValueTransfer getDataValueDeaths() {
        return jsonValueTransferDeaths;
    }

    public void setDataValueDeaths(JsonValueTransfer jsonValueTransferDeaths) {
        this.jsonValueTransferDeaths = jsonValueTransferDeaths;
    }

    @Override
    public String toString() {
        return "DataObjectCountry{" +
                "dataValueConfirmed=" + jsonValueTransferConfirmed +
                ", dataValueRecovered=" + jsonValueTransferRecovered +
                ", dataValueDeaths=" + jsonValueTransferDeaths +
                '}';
    }
}