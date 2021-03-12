package com.covid19.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.covid19.model.dto.CountryDailyDto;
import com.covid19.model.dto.CountryLatestDto;
import com.covid19.model.dto.UsDailyDto;
import com.covid19.service.csv.CsvUtilsDailyReports;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CountryDailyService {

    private final CsvUtilsDailyReports csvUtilsDailyReports;

    @Autowired
    public CountryDailyService(CsvUtilsDailyReports csvUtilsDailyReports) {
        this.csvUtilsDailyReports = csvUtilsDailyReports;
    }

    //3 methods for get all values of districts, us daily report and all country daily reports
    public List<CountryDailyDto> getAllDailyReports() {
        log.debug("Invoke get all daily reports for all countries");
        List<CountryDailyDto> countryDailyDtoList = csvUtilsDailyReports.readDailyReportsCSV();
        if (countryDailyDtoList == null || countryDailyDtoList.isEmpty()) {
            log.warn("No values available for daily reports of all countries");
            return Collections.emptyList();
        }
        log.info("Return all daily reports for all countries");
        return countryDailyDtoList;
    }

    public List<UsDailyDto> getAllDailyReportsUS() {
        log.debug("Invoke get all daily reports for us");
        List<UsDailyDto> usDailyDtoList = csvUtilsDailyReports.readDailyReportUsCSV();
        if (usDailyDtoList == null || usDailyDtoList.isEmpty()) {
            log.warn("No values available for daily reports of us");
            return Collections.emptyList();
        }
        log.info("Return all daily reports us");
        return usDailyDtoList;
    }

    //Get province values of a selected country
    public List<CountryDailyDto> getAllDailyReportsOfProvince(String country) {
        log.debug("Invoke get daily reports for province of country {}", country);
        List<CountryDailyDto> countryDailyDtoList = getAllDailyReports();
        if (countryDailyDtoList == null || countryDailyDtoList.isEmpty()) {
            log.warn("No daily reports available for {}", country);
            return Collections.emptyList();
        }
        log.info("Return all daily reports for province of country {}", country);
        return countryDailyDtoList.stream()
                .filter(c -> c.getCountry().equals(country))
                .collect(Collectors.toList());
    }

    //Auch das wird nur im world controller verwendet für die untere Tabelle, was ich aber auch anders bekommen kann über eine API
    //Get all country values calculated
    public List<CountryDailyDto> getAllDailyCountryValuesCalculated() {
        log.debug("Invoke get all daily reports for all countries calculated");
        List<CountryDailyDto> countryDailyDtoList = getAllDailyReports();
        if (countryDailyDtoList == null || countryDailyDtoList.isEmpty()) {
            log.warn("No values available for all counties");
            return Collections.emptyList();
        }
        List<String> countryWithProvince = countryDailyDtoList
                .stream()
                .filter(c -> Strings.isNotEmpty(c.getProvince()))
                .map(CountryDailyDto::getCountry)
                .distinct()
                .collect(Collectors.toList());
        List<CountryDailyDto> allCountryValues = getCalculatedCountry(countryWithProvince, countryDailyDtoList);
        for (CountryDailyDto allDailyReport : countryDailyDtoList) {
            if (!countryWithProvince.contains(allDailyReport.getCountry())) {
                allCountryValues.add(allDailyReport);
            }
        }
        log.info("Returned all values of all countries calculated");
        return allCountryValues;
    }

    private List<CountryDailyDto> getCalculatedCountry(List<String> countryWithProvince, List<CountryDailyDto> allDailyReports) {
        List<CountryDailyDto> allCountryValues = new ArrayList<>();
        for (String country : countryWithProvince) {
            CountryDailyDto countryDailyDto = new CountryDailyDto();
            countryDailyDto.setCountry(country);
            countryDailyDto.setConfirmed(getSumValues(getAllValuesGivenCountry(allDailyReports, country)).get("sumConfirmed"));
            countryDailyDto.setRecovered(getSumValues(getAllValuesGivenCountry(allDailyReports, country)).get("sumRecovered"));
            countryDailyDto.setDeaths(getSumValues(getAllValuesGivenCountry(allDailyReports, country)).get("sumDeaths"));
            countryDailyDto.setActive(getSumValues(getAllValuesGivenCountry(allDailyReports, country)).get("sumActive"));
            allCountryValues.add(countryDailyDto);
        }
        return allCountryValues;
    }

    private List<CountryDailyDto> getAllValuesGivenCountry(List<CountryDailyDto> countryDailyDtoList, String country) {
        log.debug("Invoke get all values of given country");
        return countryDailyDtoList.stream()
                .filter(c -> c.getCountry().equals(country))
                .collect(Collectors.toList());
    }

    private Map<String, Integer> getSumValues(List<CountryDailyDto> countryDailyDtoList) {
        log.debug("Invoke get sum of values");
        Map<String, Integer> sumValues = new HashMap<>();
        sumValues.put("sumConfirmed", countryDailyDtoList.stream()
                .filter(c -> c.getConfirmed() != null)
                .mapToInt(CountryDailyDto::getConfirmed)
                .sum());
        sumValues.put("sumRecovered", countryDailyDtoList.stream()
                .filter(c -> c.getRecovered() != null)
                .mapToInt(CountryDailyDto::getRecovered)
                .sum());
        sumValues.put("sumDeaths", countryDailyDtoList.stream()
                .filter(c -> c.getDeaths() != null)
                .mapToInt(CountryDailyDto::getDeaths)
                .sum());
        sumValues.put("sumActive", countryDailyDtoList.stream()
                .filter(c -> c.getActive() != null)
                .mapToInt(CountryDailyDto::getActive)
                .sum());
        return sumValues;
    }

    public Optional<CountryLatestDto> getCountryLatestDto(String country) {
        log.debug("Invoke get latest country values");
        List<CountryLatestDto> countryLatestDtoList = csvUtilsDailyReports.readCountryLatestCSV();
        if (!(countryLatestDtoList == null || countryLatestDtoList.isEmpty())) {
            Optional<CountryLatestDto> countryLatestDto = countryLatestDtoList.stream()
                    .filter(c -> c.getLocation().contains(country))
                    .findFirst();
            if (countryLatestDto.isPresent()) {
                log.debug("Returned latest values of country {}", country);
                return countryLatestDto;
            }
        }
        log.warn("No latest values available for country {}", country);
        return Optional.empty();
    }
}