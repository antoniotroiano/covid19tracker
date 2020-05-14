package com.valtech.statistics.service;

import com.valtech.statistics.model.DailyReportDto;
import com.valtech.statistics.service.csv.ReadDailyReportsCSV;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimeSeriesDetailsService {

    private static final ReadDailyReportsCSV readDailyReportCSV = new ReadDailyReportsCSV();

    public List<DailyReportDto> getAllDetailsProvince(String country) {
        log.debug("Invoke get details for province of selected country {}", country);
        List<DailyReportDto> allDailyReports = readDailyReportCSV.readDailyReportsCSV();
        if (allDailyReports.isEmpty()) {
            log.warn("No daily report available");
            return allDailyReports;
        }
        List<DailyReportDto> allValuesSelectedCountry = new ArrayList<>();
        for (DailyReportDto allDailyReport : allDailyReports) {
            if (allDailyReport.getCountry().equals(country)) {
                allValuesSelectedCountry.add(allDailyReport);
            }
        }
        log.debug("Returned all values of province for selected country {}", country);
        return allValuesSelectedCountry;
    }
}