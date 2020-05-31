package com.statistics.corona.service;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.TimeSeriesDto;
import com.statistics.corona.model.TimeSeriesWorldDto;
import com.statistics.corona.service.csv.ReadTimeSeriesCSV;
import com.statistics.corona.service.json.ReadJSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimeSeriesService {

    private final ReadTimeSeriesCSV readTimeSeriesCSV;
    private final ReadJSON readJSON;

    public Map<String, List<TimeSeriesDto>> getValuesSelectedCountry(String country) {
        log.debug("Invoke get all values from time series csv for {}", country);
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
            allValues.put("confirmedList", confirmedListCountry);
        }
        if (recoveredListCountry.isEmpty()) {
            allValues.put("recoveredList", recoveredListCountry);
        }
        if (deathsListCountry.isEmpty()) {
            allValues.put("deathsList", deathsListCountry);
        }
        allValues.put("confirmedList", confirmedListCountry);
        allValues.put("recoveredList", recoveredListCountry);
        allValues.put("deathsList", deathsListCountry);
        return allValues;
    }

    public Map<String, List<Integer>> mapFinalResultToMap(Map<String, List<TimeSeriesDto>> allValuesOfCountry) {
        log.debug("Invoke get final result of all values in a map");
        Map<String, List<Integer>> mapFinalResult = new HashMap<>();

        List<Integer> confirmedResult = generateFinalResult(interimResult(allValuesOfCountry.get("confirmedList")));
        if (confirmedResult.isEmpty()) {
            mapFinalResult.put("confirmedResult", confirmedResult);
        }
        List<Integer> recoveredResult = generateFinalResult(interimResult(allValuesOfCountry.get("recoveredList")));
        if (recoveredResult.isEmpty()) {
            mapFinalResult.put("recoveredResult", recoveredResult);
        }
        List<Integer> deathsResult = generateFinalResult(interimResult(allValuesOfCountry.get("deathsList")));
        if (deathsResult.isEmpty()) {
            mapFinalResult.put("deathsResult", deathsResult);
        }

        mapFinalResult.put("confirmedResult", confirmedResult);
        mapFinalResult.put("recoveredResult", recoveredResult);
        mapFinalResult.put("deathsResult", deathsResult);
        return mapFinalResult;
    }

    public List<Integer> mapResultToList(List<TimeSeriesDto> dataList) {
        log.debug("Invoke map result ti list");
        List<Integer> values = new ArrayList<>();
        if (dataList.isEmpty()) {
            log.warn("The data list with timeSeriesDto is empty, Returned empty integer list");
            return values;
        }
        for (TimeSeriesDto timeSeriesDto : dataList) {
            values = new ArrayList<>(timeSeriesDto.getDataMap()
                    .values());
        }
        log.debug("Returned list with results in a list");
        return values;
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

    //ToDo: Auslagern?
    public Optional<CountryDetailsDto> getDetailsForCountry(String country) {
        log.debug("Invoke get details for country {}", country);
        try {
            log.debug("Returned details for country {}", country);
            CountryDetailsDto countryDetailsDto = readJSON.readDetailsForCountry(country);
            return Optional.of(countryDetailsDto);
        } catch (Exception e) {
            log.warn("Failed get details for country {}: {}", country, e.getMessage());
            return Optional.of(new CountryDetailsDto());
        }
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

    //ToDo: Auslagern?
    public List<TimeSeriesWorldDto> getAllValuesWorld() {
        log.debug("Invoke get all values of world");
        List<TimeSeriesWorldDto> timeSeriesWorldDtoList = new ArrayList<>();
        try {
            timeSeriesWorldDtoList = readJSON.readWorldValues();
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

    public Map<String, List<TimeSeriesDto>> getProvinceValues(String country, String province) {
        log.debug("Invoke get province values");
        Map<String, List<TimeSeriesDto>> allValuesProvince = new HashMap<>();
        Map<String, List<TimeSeriesDto>> getAllValuesCountry = getValuesSelectedCountry(country);
        if (getAllValuesCountry.isEmpty()) {
            log.warn("Map is empty for get all values of selected country {}", country);
            return allValuesProvince;
        }

        List<String> withProvinceNoTimeSeries = Arrays.asList("Canada", "United Kingdom", "China", "Netherlands",
                "Australia", "Denmark", "France");

        if (withProvinceNoTimeSeries.contains(country)) {
            allValuesProvince.put("confirmedList", getAllValuesCountry.get("confirmedList")
                    .stream()
                    .filter(p -> p.getProvince().equals(province))
                    .collect(Collectors.toList()));
            allValuesProvince.put("recoveredList", getAllValuesCountry.get("recoveredList")
                    .stream()
                    .filter(p -> p.getProvince().equals(province))
                    .collect(Collectors.toList()));
            allValuesProvince.put("deathsList", getAllValuesCountry.get("deathsList")
                    .stream()
                    .filter(p -> p.getProvince().equals(province))
                    .collect(Collectors.toList()));
        }
        return allValuesProvince;
    }
}