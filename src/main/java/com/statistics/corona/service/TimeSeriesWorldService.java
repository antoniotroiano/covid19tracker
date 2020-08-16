package com.statistics.corona.service;

import com.statistics.corona.model.TimeSeriesWorldDto;
import com.statistics.corona.service.json.ReadJSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TimeSeriesWorldService {

    private final ReadJSON readJSON;

    public TimeSeriesWorldService(ReadJSON readJSON) {
        this.readJSON = readJSON;
    }

    public List<TimeSeriesWorldDto> getAllValuesWorld() {
        log.debug("Invoke get all values of world");
        List<TimeSeriesWorldDto> timeSeriesWorldDtoList = new ArrayList<>();
        try {
            timeSeriesWorldDtoList = readJSON.readWorldValuesOfJson();
            if (timeSeriesWorldDtoList.isEmpty()) {
                log.warn("No data available for world time series");
                return timeSeriesWorldDtoList;
            }
            log.debug("Returned all values of world time series");
            return timeSeriesWorldDtoList;
        } catch (Exception e) {
            log.warn("Failed get data of world time series: {}", e.getMessage());
            return timeSeriesWorldDtoList;
        }
    }
}