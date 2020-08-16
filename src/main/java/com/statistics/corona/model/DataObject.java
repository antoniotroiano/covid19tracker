package com.statistics.corona.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DataObject {

    @JsonProperty("data")
    List<TimeSeriesWorldDto> timeSeriesWorldDtoList;

    public List<TimeSeriesWorldDto> getTimeSeriesWorldDtoList() {
        return timeSeriesWorldDtoList;
    }

    public void setTimeSeriesWorldDtoList(List<TimeSeriesWorldDto> timeSeriesWorldDtoList) {
        this.timeSeriesWorldDtoList = timeSeriesWorldDtoList;
    }

    @Override
    public String toString() {
        return "DataObject{" +
                "timeSeriesWorldDtoList=" + timeSeriesWorldDtoList +
                '}';
    }
}