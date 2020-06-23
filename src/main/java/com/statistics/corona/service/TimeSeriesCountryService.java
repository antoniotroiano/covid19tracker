package com.statistics.corona.service;

import com.statistics.corona.model.TimeSeriesDto;
import com.statistics.corona.service.csv.ReadTimeSeriesCSV;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class TimeSeriesCountryService {

    private static final String CONFIRMED_LIST = "confirmedList";
    private static final String RECOVERED_LIST = "recoveredList";
    private static final String DEATHS_LIST = "deathsList";
    private final ReadTimeSeriesCSV readTimeSeriesCSV;

    public TimeSeriesCountryService(ReadTimeSeriesCSV readTimeSeriesCSV) {
        this.readTimeSeriesCSV = readTimeSeriesCSV;
    }

    public Map<String, List<TimeSeriesDto>> getCountryTSValues(String country) {
        log.debug("Invoke get all values for {}", country);
        Map<String, List<TimeSeriesDto>> allValues = new HashMap<>();

        List<TimeSeriesDto> confirmedListCountry = readTimeSeriesCSV.readConfirmedCsv()
                .stream()
                .filter(c -> c.getCountry().equals(country))
                .collect(Collectors.toList());

        List<TimeSeriesDto> recoveredListCountry = readTimeSeriesCSV.readRecoveredCsv()
                .stream()
                .filter(c -> c.getCountry().equals(country))
                .collect(Collectors.toList());

        List<TimeSeriesDto> deathsListCountry = readTimeSeriesCSV.readDeathsCsv()
                .stream()
                .filter(c -> c.getCountry().equals(country))
                .collect(Collectors.toList());

        if (confirmedListCountry.isEmpty()) {
            allValues.put(CONFIRMED_LIST, confirmedListCountry);
        }
        if (recoveredListCountry.isEmpty()) {
            allValues.put(RECOVERED_LIST, recoveredListCountry);
        }
        if (deathsListCountry.isEmpty()) {
            allValues.put(DEATHS_LIST, deathsListCountry);
        }
        allValues.put(CONFIRMED_LIST, confirmedListCountry);
        allValues.put(RECOVERED_LIST, recoveredListCountry);
        allValues.put(DEATHS_LIST, deathsListCountry);
        return allValues;
    }

    public Map<String, List<Integer>> generateFinalTSResult(Map<String, List<TimeSeriesDto>> allValuesOfCountry) {
        log.debug("Invoke get final result of all values in a map");
        Map<String, List<Integer>> mapFinalResult = new HashMap<>();

        List<Integer> confirmedResult = generateFinalResult(interimResult(allValuesOfCountry.get(CONFIRMED_LIST)));
        if (confirmedResult.isEmpty()) {
            mapFinalResult.put("confirmedResult", confirmedResult);
        }
        List<Integer> recoveredResult = generateFinalResult(interimResult(allValuesOfCountry.get(RECOVERED_LIST)));
        if (recoveredResult.isEmpty()) {
            mapFinalResult.put("recoveredResult", recoveredResult);
        }
        List<Integer> deathsResult = generateFinalResult(interimResult(allValuesOfCountry.get(DEATHS_LIST)));
        if (deathsResult.isEmpty()) {
            mapFinalResult.put("deathsResult", deathsResult);
        }

        mapFinalResult.put("confirmedResult", confirmedResult);
        mapFinalResult.put("recoveredResult", recoveredResult);
        mapFinalResult.put("deathsResult", deathsResult);
        return mapFinalResult;
    }

    private List<List<Integer>> interimResult(List<TimeSeriesDto> dataList) {
        log.debug("Invoke interim result");
        List<List<Integer>> interimResultList = new ArrayList<>();
        if (dataList.isEmpty()) {
            log.warn("The list with data is empty. No calculation for interim result");
            return interimResultList;
        }
        for (TimeSeriesDto timeSeriesDto : dataList) {
            List<Integer> values = new ArrayList<>(timeSeriesDto.getDataMap()
                    .values());
            interimResultList.add(values);
        }
        log.debug("Return interim result");
        return interimResultList;
    }

    private List<Integer> generateFinalResult(List<List<Integer>> interimResult) {
        log.debug("Invoke final result");
        List<Integer> finalResult = new ArrayList<>();
        if (interimResult.isEmpty()) {
            log.debug("The interim result list is empty. No calculation for final result");
            return finalResult;
        }
        for (int j = 0; j < interimResult.get(0).size(); j++) {
            int sum = 0;
            for (List<Integer> integers : interimResult) {
                sum += integers.get(j);
            }
            finalResult.add(sum);
        }
        log.debug("Return final result");
        return finalResult;
    }

    public List<Integer> getOneDayValues(List<Integer> values) {
        log.debug("Invoke get one day values of {}", values);
        if (values.isEmpty()) {
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
        log.debug("Return list with one day values");
        return oneDayValues;
    }

    public int getLastValue(List<Integer> values) {
        log.debug("Invoke get last value of list {}", values);
        int lastValueInt = 0;
        if (values.isEmpty()) {
            log.warn("Values is null for getLastValues");
            return lastValueInt;
        }
        long count = values.size();
        Optional<Integer> optionalValue = values.stream().skip(count - 1).findFirst();
        if (optionalValue.isPresent()) {
            lastValueInt = optionalValue.get();
        }
        return lastValueInt;
    }

    public List<Integer> getEverySecondValue(List<Integer> values) {
        log.debug("Invoke get every second value");
        if (values.isEmpty()) {
            log.warn("Values list is null or empty for getEverySecondValue");
            return Collections.emptyList();
        }
        return IntStream.range(0, values.size())
                .filter(n -> n % 2 == 0)
                .mapToObj(values::get)
                .collect(Collectors.toList());
    }

    public List<String> getEverySecondDate(List<String> dates) {
        log.debug("Invoke get every second date");
        if (dates.isEmpty()) {
            log.warn("Date list is null or empty for getEverySecondDate");
            return Collections.emptyList();
        }
        return IntStream.range(0, dates.size())
                .filter(n -> n % 2 == 0)
                .mapToObj(dates::get).collect(Collectors.toList());
    }

    public List<String> getCountryNames() {
        log.debug("Invoke get all country names");
        List<String> allCountries = readTimeSeriesCSV.readCountryName();
        if (allCountries.isEmpty()) {
            log.warn("No country names available");
            return allCountries;
        }
        log.debug("Returned list with all country names");
        return allCountries;
    }

    public Map<String, List<TimeSeriesDto>> getProvinceTSValues(String country, String province) {
        log.debug("Invoke get province values");
        Map<String, List<TimeSeriesDto>> allValuesProvince = new HashMap<>();
        Map<String, List<TimeSeriesDto>> getAllValuesCountry = getCountryTSValues(country);
        if (getAllValuesCountry.isEmpty()) {
            log.warn("Map is empty for get all values of selected country {}", country);
            return allValuesProvince;
        }

        List<String> withProvinceNoTimeSeries = Arrays.asList("Canada", "United Kingdom", "China", "Netherlands",
                "Australia", "Denmark", "France");

        if (withProvinceNoTimeSeries.contains(country)) {
            getValues(province, allValuesProvince, getAllValuesCountry, CONFIRMED_LIST);
            getValues(province, allValuesProvince, getAllValuesCountry, RECOVERED_LIST);
            getValues(province, allValuesProvince, getAllValuesCountry, DEATHS_LIST);
        }
        return allValuesProvince;
    }

    private void getValues(String province, Map<String, List<TimeSeriesDto>> allValuesProvince, Map<String, List<TimeSeriesDto>> getAllValuesCountry, String key) {
        List<TimeSeriesDto> confirmedList = getAllValuesCountry.get(key)
                .stream()
                .filter(p -> Objects.nonNull(p.getProvince()))
                .filter(p -> p.getProvince().equals(province))
                .collect(Collectors.toList());
        if (!confirmedList.isEmpty()) {
            allValuesProvince.put(key, confirmedList);
        } else {
            allValuesProvince.put(key, new ArrayList<>());
        }
    }
}