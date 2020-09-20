package com.statistics.corona.model;

public class DistrictDto {

    String location;
    String country_code;
    int confirmed;
    int dead;
    int recovered;
    int velocity_confirmed;
    int velocity_dead;
    int velocity_recovered;
    String updated;

    public DistrictDto() {
    }

    public DistrictDto(DistrictDto districtDto) {
        this.location = districtDto.getLocation();
        this.country_code = districtDto.getCountry_code();
        this.confirmed = districtDto.getConfirmed();
        this.dead = districtDto.getDead();
        this.recovered = districtDto.getRecovered();
        this.velocity_confirmed = districtDto.getVelocity_confirmed();
        this.velocity_dead = districtDto.getVelocity_dead();
        this.velocity_recovered = districtDto.getVelocity_recovered();
        this.updated = districtDto.getUpdated();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
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

    public int getVelocity_confirmed() {
        return velocity_confirmed;
    }

    public void setVelocity_confirmed(int velocity_confirmed) {
        this.velocity_confirmed = velocity_confirmed;
    }

    public int getVelocity_dead() {
        return velocity_dead;
    }

    public void setVelocity_dead(int velocity_dead) {
        this.velocity_dead = velocity_dead;
    }

    public int getVelocity_recovered() {
        return velocity_recovered;
    }

    public void setVelocity_recovered(int velocity_recovered) {
        this.velocity_recovered = velocity_recovered;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "DistrictDto{" +
                "location='" + location + '\'' +
                ", country_code='" + country_code + '\'' +
                ", confirmed=" + confirmed +
                ", dead=" + dead +
                ", recovered=" + recovered +
                ", velocityConfirmed=" + velocity_confirmed +
                ", velocityDead=" + velocity_dead +
                ", velocityRecovered=" + velocity_recovered +
                ", updated='" + updated + '\'' +
                '}';
    }
}