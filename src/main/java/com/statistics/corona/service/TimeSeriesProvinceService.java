package com.statistics.corona.service;

import com.statistics.corona.model.TimeSeriesDto;
import com.statistics.corona.service.csv.CsvUtilsTimeSeries;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TimeSeriesProvinceService {

    private static final String CONFIRMED_LIST = "confirmedList";
    private static final String RECOVERED_LIST = "recoveredList";
    private static final String DEATHS_LIST = "deathsList";
    private final TimeSeriesCountryService timeSeriesCountryService;
    private final CsvUtilsTimeSeries csvUtilsTimeSeries;

    @Autowired
    public TimeSeriesProvinceService(TimeSeriesCountryService timeSeriesCountryService, CsvUtilsTimeSeries csvUtilsTimeSeries) {
        this.timeSeriesCountryService = timeSeriesCountryService;
        this.csvUtilsTimeSeries = csvUtilsTimeSeries;
    }

    public Map<String, List<TimeSeriesDto>> getProvinceTSValues(String country, String province) {
        log.debug("Invoke get province values");
        Map<String, List<TimeSeriesDto>> allValuesProvince = new HashMap<>();
        Map<String, List<TimeSeriesDto>> getAllValuesCountry = timeSeriesCountryService.getTSValuesForOneCountry(country);
        if (getAllValuesCountry == null || getAllValuesCountry.isEmpty()) {
            log.warn("Map is empty for get all values of selected country {}", country);
            return Collections.emptyMap();
        }
        List<String> withProvinceNoTimeSeries = Arrays.asList("Canada", "United Kingdom", "China", "Netherlands",
                "Australia", "Denmark", "France");
        if (withProvinceNoTimeSeries.contains(country)) {
            getValues(province, allValuesProvince, getAllValuesCountry, CONFIRMED_LIST);
            getValues(province, allValuesProvince, getAllValuesCountry, RECOVERED_LIST);
            getValues(province, allValuesProvince, getAllValuesCountry, DEATHS_LIST);
        }
        log.info("Return map with selected province time series values");
        return allValuesProvince;
    }

    private void getValues(String province, Map<String, List<TimeSeriesDto>> allValuesProvince, Map<String, List<TimeSeriesDto>> getAllValuesCountry, String key) {
        log.debug("Invoke get values");
        List<TimeSeriesDto> timeSeriesDtoList = getAllValuesCountry.get(key)
                .stream()
                .filter(p -> Objects.nonNull(p.getProvince()))
                .filter(p -> p.getProvince().equals(province))
                .collect(Collectors.toList());
        allValuesProvince.put(key, timeSeriesDtoList);
    }

    private List<TimeSeriesDto> getConfirmedUS() {
        log.debug("Invoke get time series confirmed value of us");
        List<TimeSeriesDto> timeSeriesDtoList = csvUtilsTimeSeries.readUsConfirmedCsv();
        if (timeSeriesDtoList == null || timeSeriesDtoList.isEmpty()) {
            log.warn("No time series values for confirmed of us");
            return Collections.emptyList();
        }
        log.info("Return time series values for confirmed of us");
        return timeSeriesDtoList;
    }

    private List<TimeSeriesDto> getDeathsUS() {
        log.debug("Invoke get time series deaths value of us");
        List<TimeSeriesDto> timeSeriesDtoList = csvUtilsTimeSeries.readUsDeathsCsv();
        if (timeSeriesDtoList == null || timeSeriesDtoList.isEmpty()) {
            log.warn("No time series values for deaths of us");
            return Collections.emptyList();
        }
        log.info("Return time series values for deaths of us");
        return timeSeriesDtoList;
    }

    public Map<String, List<TimeSeriesDto>> getUsProvinceTSValues(String province) {
        log.debug("Invoke get all values for us province {}", province);
        Map<String, List<TimeSeriesDto>> allValues = new HashMap<>();
        List<TimeSeriesDto> confirmedListCountry = getConfirmedUS()
                .stream()
                .filter(c -> c.getProvince().equals(province))
                .collect(Collectors.toList());
        List<TimeSeriesDto> deathsListCountry = getDeathsUS()
                .stream()
                .filter(c -> c.getProvince().equals(province))
                .collect(Collectors.toList());
        allValues.put(CONFIRMED_LIST, confirmedListCountry);
        allValues.put(DEATHS_LIST, deathsListCountry);
        log.info("Return all values for us province");
        return allValues;
    }

    public Map<String, List<Integer>> generateUsFinalTSResult(Map<String, List<TimeSeriesDto>> allValuesOfUsProvince) {
        log.debug("Invoke get final result of all values in a map for us");
        Map<String, List<Integer>> mapUsFinalResult = new HashMap<>();
        List<Integer> confirmedResult = timeSeriesCountryService.finalResult(allValuesOfUsProvince.get(CONFIRMED_LIST));
        List<Integer> deathsResult = timeSeriesCountryService.finalResult(allValuesOfUsProvince.get(DEATHS_LIST));
        mapUsFinalResult.put("confirmedResult", confirmedResult);
        mapUsFinalResult.put("deathsResult", deathsResult);
        log.info("Return all final result for us");
        return mapUsFinalResult;
    }

    public Optional<TimeSeriesDto> getLatestValueTimeSeries(Map<String, List<TimeSeriesDto>> provinceTSValues) {
        log.debug("Invoke get latest value of time series for province");
        return provinceTSValues.get(CONFIRMED_LIST)
                .stream()
                .map(TimeSeriesDto::new)
                .findFirst();
    }
}