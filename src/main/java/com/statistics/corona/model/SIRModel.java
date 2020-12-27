package com.statistics.corona.model;

/**
 * @deprecated Don't needed for now
 */
@Deprecated(since = "4.0.0-SNAPSHOT", forRemoval = false)
public class SIRModel {

    Double transmissionRate;
    Double recoveryRate;

    public SIRModel() {
    }

    public SIRModel(Double transmissionRate, Double recoveryRate) {
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