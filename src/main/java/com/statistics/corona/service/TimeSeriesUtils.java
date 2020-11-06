package com.statistics.corona.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TimeSeriesUtils {

    public List<Integer> getDailyTrend(Map<String, Integer> values) {
        log.debug("Invoke get daily trend");
        ArrayList<Integer> valueList = new ArrayList<>(values.values());
        if (valueList.isEmpty()) {
            log.warn("Values is null for daily trend");
            return Collections.emptyList();
        }
        List<Integer> worldDailyTrend = new ArrayList<>();
        for (int i = 0; i < valueList.size(); i++) {
            int sumPerDay = 0;
            if (i == 0) {
                worldDailyTrend.add(0);
            } else {
                sumPerDay = valueList.get(i) - valueList.get(i - 1);
                if (sumPerDay < 0) {
                    sumPerDay = 0;
                    worldDailyTrend.add(sumPerDay);
                }
                worldDailyTrend.add(sumPerDay);
            }
        }
        log.info("Return list with daily trend");
        return worldDailyTrend;
    }
}