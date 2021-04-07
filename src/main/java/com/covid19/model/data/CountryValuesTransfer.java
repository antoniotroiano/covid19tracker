package com.covid19.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountryValuesTransfer {

    @JsonProperty("confirmed")
    private JsonValueTransfer jsonValueTransferConfirmed;

    @JsonProperty("recovered")
    private JsonValueTransfer jsonValueTransferRecovered;

    @JsonProperty("deaths")
    private JsonValueTransfer jsonValueTransferDeaths;

    public JsonValueTransfer getDataValueConfirmed() {
        return jsonValueTransferConfirmed;
    }

    public void setDataValueConfirmed(final JsonValueTransfer jsonValueTransferConfirmed) {
        this.jsonValueTransferConfirmed = jsonValueTransferConfirmed;
    }

    public JsonValueTransfer getDataValueRecovered() {
        return jsonValueTransferRecovered;
    }

    public void setDataValueRecovered(final JsonValueTransfer jsonValueTransferRecovered) {
        this.jsonValueTransferRecovered = jsonValueTransferRecovered;
    }

    public JsonValueTransfer getDataValueDeaths() {
        return jsonValueTransferDeaths;
    }

    public void setDataValueDeaths(final JsonValueTransfer jsonValueTransferDeaths) {
        this.jsonValueTransferDeaths = jsonValueTransferDeaths;
    }

    @Override
    public String toString() {
        return "CountryValuesTransfer{" +
                "dataValueConfirmed=" + jsonValueTransferConfirmed +
                ", dataValueRecovered=" + jsonValueTransferRecovered +
                ", dataValueDeaths=" + jsonValueTransferDeaths +
                '}';
    }
}