package com.valtech.statistics.service;

import com.valtech.statistics.model.CountryDetailsDto;
import com.valtech.statistics.model.TimeSeriesDto;
import com.valtech.statistics.service.csv.ReadTimeSeriesCSV;
import com.valtech.statistics.service.json.ReadJSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimeSeriesService {

    private static final ReadTimeSeriesCSV readTimeSeriesCSV = new ReadTimeSeriesCSV();
    private final ReadJSON readJSON = new ReadJSON();
    private final List<TimeSeriesDto> confirmedAllList = readTimeSeriesCSV.readConfirmedCsv();
    private final List<TimeSeriesDto> recoveredAllList = readTimeSeriesCSV.readRecoveredCsv();
    private final List<TimeSeriesDto> deathsAllList = readTimeSeriesCSV.readDeathsCsv();

    public Map<String, List<TimeSeriesDto>> getValuesSelectedCountry(String country) {
        Map<String, List<TimeSeriesDto>> allValues = new HashMap<>();

        List<TimeSeriesDto> confirmedListSelectedCountry = confirmedAllList
                .stream()
                .filter(c -> c.getCountry().equals(country))
                .collect(Collectors.toList());

        List<TimeSeriesDto> recoveredListSelectedCountry = recoveredAllList
                .stream()
                .filter(c -> c.getCountry().equals(country))
                .collect(Collectors.toList());

        List<TimeSeriesDto> deathsListSelectedCountry = deathsAllList
                .stream()
                .filter(c -> c.getCountry().equals(country))
                .collect(Collectors.toList());

        allValues.put("confirmedList", confirmedListSelectedCountry);
        allValues.put("recoveredList", recoveredListSelectedCountry);
        allValues.put("deathsList", deathsListSelectedCountry);
        return allValues;
    }

    public Map<String, List<Integer>> mapFinalResultToMap(Map<String, List<TimeSeriesDto>> getAllValuesSelectedCountry) {
        Map<String, List<Integer>> mapFinalResult = new HashMap<>();

        mapFinalResult.put("confirmedResult", generateFinalResult(interimResult(getAllValuesSelectedCountry.get("confirmedList"))));
        mapFinalResult.put("recoveredResult", generateFinalResult(interimResult(getAllValuesSelectedCountry.get("recoveredList"))));
        mapFinalResult.put("deathsResult", generateFinalResult(interimResult(getAllValuesSelectedCountry.get("deathsList"))));
        return mapFinalResult;
    }

    public List<Integer> mapResultToList(List<TimeSeriesDto> dataList) {
        List<Integer> values = new ArrayList<>();
        for (TimeSeriesDto timeSeriesDto : dataList) {
            values = timeSeriesDto.getDataMap()
                    .entrySet()
                    .stream()
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }
        return values;
    }

    private List<List<Integer>> interimResult(List<TimeSeriesDto> dataList) {
        log.debug("Invoke interim result");
        List<List<Integer>> interimResultList = new ArrayList<>();
        for (TimeSeriesDto timeSeriesDto : dataList) {
            List<Integer> values = timeSeriesDto.getDataMap()
                    .entrySet()
                    .stream()
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
            interimResultList.add(values);
        }
        log.debug("Return interim result");
        return interimResultList;
    }

    private List<Integer> generateFinalResult(List<List<Integer>> interimResult) {
        log.debug("Invoke final result");
        List<Integer> finalResult = new ArrayList<>();
        for (int j = 0; j < interimResult.get(0).size(); j++) {
            int sum = 0;
            for (int k = 0; k < interimResult.size(); k++) {
                sum += interimResult.get(k).get(j);
            }
            finalResult.add(sum);
        }
        log.debug("Return final result");
        return finalResult;
    }

    public List<Integer> getOneDayValues(List<Integer> values) {
        log.debug("Invoke get one day values of {}", values);
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

    public int getLastValues(List<Integer> lastValue) {
        log.debug("Invoke get last value of list {}", lastValue);
        long count = lastValue.stream().count();
        return lastValue.stream().skip(count - 1).findFirst().get();
    }

    public CountryDetailsDto getDetailsForCountry(String country) {
        log.debug("Invoke get details for country {}", country);
        try {
            log.debug("Returned details for country {}", country);
            return readJSON.readDetailsForCountry(country);
        } catch (Exception e) {
            log.warn("Failed get details for country {}. {}", country, e.getMessage());
            return new CountryDetailsDto();
        }
    }
}