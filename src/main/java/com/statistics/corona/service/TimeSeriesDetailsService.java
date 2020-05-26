package com.statistics.corona.service;

import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.DailyReportUsDto;
import com.statistics.corona.service.csv.ReadDailyReportsCSV;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimeSeriesDetailsService {

    private final ReadDailyReportsCSV readDailyReportCSV;

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
        List<DailyReportDto> allCountryValues = new ArrayList<>();

        List<String> countryWithProvince = Arrays.asList("US", "Italy", "Canada", "Spain", "United Kingdom", "China",
                "Netherlands", "Australia", "Germany", "Denmark", "France", "Brazil", "Chile", "Japan", "Mexico");

        for (String country : countryWithProvince) {
            DailyReportDto dailyReportDto = new DailyReportDto();
            dailyReportDto.setCountry(country);
            dailyReportDto.setConfirmed(getSumValues(getAllValuesCountry(allDailyReports, country)).get("sumConfirmed"));
            dailyReportDto.setRecovered(getSumValues(getAllValuesCountry(allDailyReports, country)).get("sumRecovered"));
            dailyReportDto.setDeaths(getSumValues(getAllValuesCountry(allDailyReports, country)).get("sumDeaths"));
            dailyReportDto.setActive(getSumValues(getAllValuesCountry(allDailyReports, country)).get("sumActive"));
            allCountryValues.add(dailyReportDto);
        }

        for (DailyReportDto allDailyReport : allDailyReports) {
            if (!countryWithProvince.contains(allDailyReport.getCountry())) {
                allCountryValues.add(allDailyReport);
            }
        }
        log.debug("Returned all values of all countries");
        return allCountryValues;
    }

    private List<DailyReportDto> getAllValuesCountry(List<DailyReportDto> dailyReportDtoList, String country) {
        log.debug("Invoke get all values of given country");
        return dailyReportDtoList.stream()
                .filter(c -> c.getCountry().equals(country))
                .collect(Collectors.toList());
    }

    private Map<String, Integer> getSumValues(List<DailyReportDto> dailyReportDtoList) {
        log.debug("Invoke get sum of values");
        Map<String, Integer> sumValues = new HashMap<>();
        sumValues.put("sumConfirmed", dailyReportDtoList.stream()
                .filter(c -> c.getConfirmed() != null)
                .mapToInt(DailyReportDto::getConfirmed)
                .sum());
        sumValues.put("sumRecovered", dailyReportDtoList.stream()
                .filter(c -> c.getRecovered() != null)
                .mapToInt(DailyReportDto::getRecovered)
                .sum());
        sumValues.put("sumDeaths", dailyReportDtoList.stream()
                .filter(c -> c.getDeaths() != null)
                .mapToInt(DailyReportDto::getDeaths)
                .sum());
        sumValues.put("sumActive", dailyReportDtoList.stream()
                .filter(c -> c.getActive() != null)
                .mapToInt(DailyReportDto::getActive)
                .sum());
        return sumValues;
    }
}