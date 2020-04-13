package com.valtech.statistics.model;

public class Data {

    Double transmissionRate;
    Double recoveryRate;

    public Data() {
    }

    public Data(Double transmissionRate, Double recoveryRate) {
        this.transmissionRate = transmissionRate;
        this.recoveryRate = recoveryRate;
    }

    public Double getTransmissionRate() {
        return transmissionRate;
    }

    public void setTransmissionRate(Double transmissionRate) {
        this.transmissionRate = transmissionRate;
    }

    public Double getRecoveryRate() {
        return recoveryRate;
    }

    public void setRecoveryRate(Double recoveryRate) {
        this.recoveryRate = recoveryRate;
    }
}