package com.statistics.corona.service.v2;

import com.statistics.corona.model.v2.DailyReportDto;
import com.statistics.corona.model.v2.DailyReportUsDto;
import com.statistics.corona.service.v2.csv.ReadDailyReportsCSV;
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
            log.warn("No daily report available for {}", country);
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

    public List<DailyReportUsDto> getAllDailyProvinceUs() {
        log.debug("Invoke get details for province of US");
        List<DailyReportUsDto> allDailyReportsUs = readDailyReportCSV.readDailyReportUs();
        if (allDailyReportsUs.isEmpty()) {
            log.warn("No daily report available for US");
            return allDailyReportsUs;
        }
        log.debug("Returned all values of province for US");
        return allDailyReportsUs;

    }

    public List<DailyReportDto> getAllCountries() {
        log.debug("Invoke get details for all countries");
        List<DailyReportDto> allDailyReports = readDailyReportCSV.readDailyReportsCSV();
        if (allDailyReports.isEmpty()) {
            log.warn("No values available for all counties.");
            return allDailyReports;
        }
        List<DailyReportDto> allValuesWithoutUs = new ArrayList<>();
        for (DailyReportDto allDailyReport : allDailyReports) {
            if (!allDailyReport.getCountry().equals("US") && !allDailyReport.getCountry().equals("Germany")
                    && !allDailyReport.getCountry().equals("Italy") && !allDailyReport.getCountry().equals("France")
                    && !allDailyReport.getCountry().equals("Denmark") && !allDailyReport.getCountry().equals("Canada")
                    && !allDailyReport.getCountry().equals("Australia") && !allDailyReport.getCountry().equals("China")
                    && !allDailyReport.getCountry().equals("United Kingdom") && !allDailyReport.getCountry().equals("Netherlands")
                    && !allDailyReport.getCountry().equals("Spain")) {
                allValuesWithoutUs.add(allDailyReport);
            }
        }
        log.debug("Returned all values of all countries");
        return allValuesWithoutUs;
    }
}