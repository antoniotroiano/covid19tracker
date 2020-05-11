package com.valtech.statistics.service;

import com.valtech.statistics.model.TimeSeriesDto;
import com.valtech.statistics.service.csv.ReadTimeSeriesCSV;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimeSeriesService {

    private static final ReadTimeSeriesCSV readTimeSeriesCSV = new ReadTimeSeriesCSV();
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

    public Map<String, List<Integer>> mapFinalResultToMap(String country) {
        Map<String, List<Integer>> mapFinalResult = new HashMap<>();

        mapFinalResult.put("confirmedResult", generateFinalResult(interimResult(getValuesSelectedCountry(country).get("confirmedList"))));
        mapFinalResult.put("recoveredResult", generateFinalResult(interimResult(getValuesSelectedCountry(country).get("recoveredList"))));
        mapFinalResult.put("deathsResult", generateFinalResult(interimResult(getValuesSelectedCountry(country).get("deathsList"))));
        return mapFinalResult;
    }

    public List<Integer> mapValuesToList(List<TimeSeriesDto> dataList) {
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
        List<List<Integer>> interimResultList = new ArrayList<>();
        for (TimeSeriesDto timeSeriesDto : dataList) {
            List<Integer> values = timeSeriesDto.getDataMap()
                    .entrySet()
                    .stream()
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
            interimResultList.add(values);
        }
        return interimResultList;
    }

    private List<Integer> generateFinalResult(List<List<Integer>> interimResult) {
        List<Integer> finalResult = new ArrayList<>();
        for (int j = 0; j < interimResult.get(0).size(); j++) {
            int sum = 0;
            for (int k = 0; k < interimResult.size(); k++) {
                sum += interimResult.get(k).get(j);
            }
            finalResult.add(sum);
        }
        return finalResult;
    }

    public List<Integer> getOneDayValues(List<Integer> values) {
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
        return oneDayValues;
    }

    public int getLastValues(List<Integer> lastValue) {
        long count = lastValue.stream().count();
        return lastValue.stream().skip(count - 1).findFirst().get();
    }
}