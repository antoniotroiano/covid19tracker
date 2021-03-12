package com.covid19.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceUtils {

    public List<Integer> getDailyTrend(Collection<Integer> values) {
        log.debug("Invoke get daily trend");
        ArrayList<Integer> valueList = new ArrayList<>(values);
        if (valueList.isEmpty()) {
            log.warn("Values is null for daily trend");
            return Collections.emptyList();
        }
        List<Integer> worldDailyTrend = new ArrayList<>();
        for (int i = 0; i < valueList.size(); i++) {
            int sumPerDay = 0;
            if (i == 0) {
                log.debug("First element is 0. Doesn't add to list.");
            } else {
                sumPerDay = valueList.get(i) - valueList.get(i - 1);
                if (sumPerDay < 0) {
                    sumPerDay = 0;
                    worldDailyTrend.add(sumPerDay);
                }
            }
            worldDailyTrend.add(sumPerDay);
        }
        log.debug("Return list with daily trend");
        return worldDailyTrend;
    }

    public int getYesterdayValues(Collection<Integer> values) {
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
        log.debug("Return last value");
        return lastValueInt;
    }

    public <T> List<T> getValuesForSelectedValues(int value, Collection<T> values) {
        return revertList(outputValues(revertList(convertToList(values)), value));
    }

    private <T> List<T> outputValues(List<T> values, int value) {
        return values.stream().limit(value).collect(Collectors.toList());
    }

    private <T> List<T> convertToList(Collection<T> collection) {
        return List.copyOf(collection);
    }

    private <T> List<T> revertList(List<T> value) {
        List<T> utilList = new ArrayList<>(value);
        Collections.reverse(utilList);
        return utilList;
    }
}