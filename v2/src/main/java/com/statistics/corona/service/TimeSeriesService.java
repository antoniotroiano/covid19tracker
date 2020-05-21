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

    //ToDo: Only usage in tests
    public List<Integer> mapResultToList(List<TimeSeriesDto> dataList) {
        List<Integer> values = new ArrayList<>();
        for (TimeSeriesDto timeSeriesDto : dataList) {
            values = new ArrayList<>(timeSeriesDto.getDataMap()
                    .values());
        }
        return values;
    }

    private List<List<Integer>> interimResult(List<TimeSeriesDto> dataList) {
        log.debug("Invoke interim result");
        List<List<Integer>> interimResultList = new ArrayList<>();
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
        long count = lastValue.size();
        int lastValueInt = 0;
        Optional<Integer> optionalValue = lastValue.stream().skip(count - 1).findFirst();
        if (optionalValue.isPresent()) {
            lastValueInt = optionalValue.get();
        }
        return lastValueInt;
    }

    public CountryDetailsDto getDetailsForCountry(String country) {
        log.debug("Invoke get details for country {}", country);
        try {
            log.debug("Returned details for country {}", country);
            return readJSON.readDetailsForCountry(country);
        } catch (Exception e) {
            log.warn("Failed get details for country {}: {}", country, e.getMessage());
            return new CountryDetailsDto();
        }
    }

    public List<String> getCountry() {
        log.debug("Returned list with all country names.");
        return readTimeSeriesCSV.readCountryName();
    }

    public List<Integer> getEverySecondValue(List<Integer> values) {
        return IntStream.range(0, values.size())
                .filter(n -> n % 2 == 0)
                .mapToObj(values::get)
                .collect(Collectors.toList());
    }

    public List<String> getEverySecondDate(List<String> dates) {
        return IntStream.range(0, dates.size())
                .filter(n -> n % 2 == 0)
                .mapToObj(dates::get).collect(Collectors.toList());
    }

    public List<TimeSeriesWorldDto> getAllValuesWorld() {
        log.debug("Invoke get all values of world");
        List<TimeSeriesWorldDto> timeSeriesWorldDtoList = new ArrayList<>();
        try {
            timeSeriesWorldDtoList = readJSON.readWorldValues();
            if (!timeSeriesWorldDtoList.isEmpty()) {
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