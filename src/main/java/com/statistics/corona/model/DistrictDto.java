package com.statistics.corona.model;

public class DistrictDto {

    String location;
    String code;
    int confirmed;
    int dead;
    int recovered;
    int velocityConfirmed;
    int velocityDead;
    int velocityRecovered;
    String lastUpdate;

    public DistrictDto() {

    }

    public DistrictDto(String location, String code, int confirmed, int dead, int recovered, int velocityConfirmed, int velocityDead, int velocityRecovered, String lastUpdate) {
        this.location = location;
        this.code = code;
        this.confirmed = confirmed;
        this.dead = dead;
        this.recovered = recovered;
        this.velocityConfirmed = velocityConfirmed;
        this.velocityDead = velocityDead;
        this.velocityRecovered = velocityRecovered;
        this.lastUpdate = lastUpdate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getDead() {
        return dead;
    }

    public void setDead(int dead) {
        this.dead = dead;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getVelocityConfirmed() {
        return velocityConfirmed;
    }

    public void setVelocityConfirmed(int velocityConfirmed) {
        this.velocityConfirmed = velocityConfirmed;
    }

    public int getVelocityDead() {
        return velocityDead;
    }

    public void setVelocityDead(int velocityDead) {
        this.velocityDead = velocityDead;
    }

    public int getVelocityRecovered() {
        return velocityRecovered;
    }

    public void setVelocityRecovered(int velocityRecovered) {
        this.velocityRecovered = velocityRecovered;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "DistrictDto{" +
                "location='" + location + '\'' +
                ", code='" + code + '\'' +
                ", confirmed=" + confirmed +
                ", dead=" + dead +
                ", recovered=" + recovered +
                ", velocityConfirmed=" + velocityConfirmed +
                ", velocityDead=" + velocityDead +
                ", velocityRecovered=" + velocityRecovered +
                ", lastUpdate='" + lastUpdate + '\'' +
                '}';
    }
}