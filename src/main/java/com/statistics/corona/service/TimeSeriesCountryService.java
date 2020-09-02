package com.statistics.corona.service;

import com.statistics.corona.model.TimeSeriesDto;
import com.statistics.corona.service.csv.CsvUtilsTimeSeries;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class TimeSeriesCountryService {

    private static final String CONFIRMED_LIST = "confirmedList";
    private static final String RECOVERED_LIST = "recoveredList";
    private static final String DEATHS_LIST = "deathsList";
    private final CsvUtilsTimeSeries csvUtilsTimeSeries;

    @Autowired
    public TimeSeriesCountryService(CsvUtilsTimeSeries csvUtilsTimeSeries) {
        this.csvUtilsTimeSeries = csvUtilsTimeSeries;
    }

    public List<TimeSeriesDto> getAllConfirmedTSValues() {
        log.debug("Invoke get all confirmed time series values");
        List<TimeSeriesDto> confirmedTSList = csvUtilsTimeSeries.readConfirmedCsv();
        if (confirmedTSList == null || confirmedTSList.isEmpty()) {
            log.warn("No confirmed time series values");
            return Collections.emptyList();
        }
        log.debug("Return list with confirmed time series values");
        return confirmedTSList;
    }

    public List<TimeSeriesDto> getAllRecoveredTSValues() {
        log.debug("Invoke get all recovered time series values");
        List<TimeSeriesDto> recoveredTSList = csvUtilsTimeSeries.readRecoveredCsv();
        if (recoveredTSList == null || recoveredTSList.isEmpty()) {
            log.warn("No recovered time series values");
            return Collections.emptyList();
        }
        log.debug("Return list with recovered time series values");
        return recoveredTSList;
    }

    public List<TimeSeriesDto> getAllDeathsTSValues() {
        log.debug("Invoke get all deaths time series values");
        List<TimeSeriesDto> deathsTSList = csvUtilsTimeSeries.readDeathsCsv();
        if (deathsTSList == null || deathsTSList.isEmpty()) {
            log.warn("No deaths time series values");
            return Collections.emptyList();
        }
        log.debug("Return list with deaths time series values");
        return deathsTSList;
    }

    public Map<String, List<TimeSeriesDto>> getTSValuesForOneCountry(String country) {
        log.debug("Invoke get all values for {}", country);
        Map<String, List<TimeSeriesDto>> allValues = new HashMap<>();
        allValues.put(CONFIRMED_LIST, getAllConfirmedTSValues()
                .stream()
                .filter(c -> c.getCountry().equals(country))
                .collect(Collectors.toList()));
        allValues.put(RECOVERED_LIST, getAllRecoveredTSValues()
                .stream()
                .filter(c -> c.getCountry().equals(country))
                .collect(Collectors.toList()));
        allValues.put(DEATHS_LIST, getAllDeathsTSValues()
                .stream()
                .filter(c -> c.getCountry().equals(country))
                .collect(Collectors.toList()));
        return allValues;
    }

    public Optional<TimeSeriesDto> getLastTimeSeriesValueSelectedCountry(Map<String, List<TimeSeriesDto>> timeSeriesMap) {
        log.debug("Invoke get last time series value of selected country");
        Optional<TimeSeriesDto> getLastTimeSeries = timeSeriesMap.get(CONFIRMED_LIST).stream()
                .map(TimeSeriesDto::new)
                .findFirst();
        if (getLastTimeSeries.isEmpty()) {
            log.warn("No last time series value of selected country");
            return Optional.of(new TimeSeriesDto());
        }
        log.info("Return last time series value of selected country");
        return getLastTimeSeries;
    }

    public Map<String, List<Integer>> generateFinalTSResult(Map<String, List<TimeSeriesDto>> allValuesOfCountry) {
        log.debug("Invoke get final result for one selected country");
        Map<String, List<Integer>> mapFinalResult = new HashMap<>();

        List<Integer> confirmedResult = finalResult(allValuesOfCountry.get(CONFIRMED_LIST));
        List<Integer> recoveredResult = finalResult(allValuesOfCountry.get(RECOVERED_LIST));
        List<Integer> deathsResult = finalResult(allValuesOfCountry.get(DEATHS_LIST));

        mapFinalResult.put("confirmedResult", confirmedResult);
        mapFinalResult.put("recoveredResult", recoveredResult);
        mapFinalResult.put("deathsResult", deathsResult);
        return mapFinalResult;
    }

    private List<List<Integer>> interimResult(List<TimeSeriesDto> dataList) {
        log.debug("Invoke interim result for time series");
        List<List<Integer>> interimResultList = new ArrayList<>();
        if (dataList == null || dataList.isEmpty()) {
            log.warn("The list with data is empty. No calculation for interim result");
            return Collections.emptyList();
        }
        for (TimeSeriesDto timeSeriesDto : dataList) {
            List<Integer> values = new ArrayList<>(timeSeriesDto.getDataMap()
                    .values());
            interimResultList.add(values);
        }
        log.info("Return interim result");
        return interimResultList;
    }

    public List<Integer> finalResult(List<TimeSeriesDto> dataList) {
        log.debug("Invoke final result for time series");
        List<List<Integer>> interimResult = interimResult(dataList);
        List<Integer> finalResult = new ArrayList<>();
        if (interimResult.isEmpty()) {
            log.warn("The interim result list is empty. No calculation for final result");
            return Collections.emptyList();
        }
        for (int j = 0; j < interimResult.get(0).size(); j++) {
            int sum = 0;
            for (List<Integer> integers : interimResult) {
                sum += integers.get(j);
            }
            finalResult.add(sum);
        }
        log.info("Return final result");
        return finalResult;
    }

    public List<Integer> getOneDayValues(List<Integer> values) {
        log.debug("Invoke get one day values");
        if (values == null || values.isEmpty()) {
            log.warn("Values is null for getOneDayValues");
            return Collections.emptyList();
        }
        List<Integer> oneDayValues = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            int sumPerDay = 0;
            if (i == 0) {
                oneDayValues.add(values.get(i));
            } else {
                sumPerDay = values.get(i) - values.get(i - 1);
                if (sumPerDay < 0) {
                    sumPerDay = 0;
                    oneDayValues.add(sumPerDay);
                }
                oneDayValues.add(sumPerDay);
            }
        }
        log.info("Return list with one day values");
        return oneDayValues;
    }

    public int getLastValue(List<Integer> values) {
        log.debug("Invoke get last value");
        int lastValueInt = 0;
        if (values == null || values.isEmpty()) {
            log.warn("Values is null for getLastValues");
            return lastValueInt;
        }
        long count = values.size();
        Optional<Integer> optionalValue = values
                .stream()
                .skip(count - 1)
                .findFirst();
        if (optionalValue.isPresent()) {
            lastValueInt = optionalValue.get();
        }
        log.info("Return last value");
        return lastValueInt;
    }

    public List<Integer> getEverySecondValue(List<Integer> values) {
        log.debug("Invoke get every second value");
        if (values == null || values.isEmpty()) {
            log.warn("Values list is null or empty for getEverySecondValue");
            return Collections.emptyList();
        }
        log.info("Return list with every second value");
        return IntStream.range(0, values.size())
                .filter(n -> n % 2 == 0)
                .mapToObj(values::get)
                .collect(Collectors.toList());
    }

    public List<String> getEverySecondDate(List<String> dates) {
        log.debug("Invoke get every second date");
        if (dates == null || dates.isEmpty()) {
            log.warn("Date list is null or empty for getEverySecondDate");
            return Collections.emptyList();
        }
        log.info("Return list with every second date ");
        return IntStream.range(0, dates.size())
                .filter(n -> n % 2 == 0)
                .mapToObj(dates::get).collect(Collectors.toList());
    }

    public List<String> getCountryNames() {
        log.debug("Invoke get all country names");
        List<String> allCountries = csvUtilsTimeSeries.readCountryName();
        if (allCountries == null || allCountries.isEmpty()) {
            log.warn("No country names available");
            return Collections.emptyList();
        }
        log.info("Returned list with all country names");
        return allCountries;
    }
}