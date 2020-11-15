package com.statistics.corona.service;

import com.statistics.corona.model.dto.CountryDailyDto;
import com.statistics.corona.model.dto.DistrictDto;
import com.statistics.corona.model.dto.UsDailyDto;
import com.statistics.corona.service.csv.CsvUtilsDailyReports;
import com.statistics.corona.service.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CountryDailyService {

    private final CsvUtilsDailyReports csvUtilsDailyReports;
    private final JsonUtils jsonUtils;

    @Autowired
    public CountryDailyService(CsvUtilsDailyReports csvUtilsDailyReports,
                               JsonUtils jsonUtils) {
        this.csvUtilsDailyReports = csvUtilsDailyReports;
        this.jsonUtils = jsonUtils;
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

    public List<DistrictDto> getAllDistrictValues() {
        log.debug("Invoke get district values");
        List<DistrictDto> districtDtoList = jsonUtils.readDistrictsValues();
        if (districtDtoList == null || districtDtoList.isEmpty()) {
            log.warn("No values available for districts");
            return Collections.emptyList();
        }
        log.info("Return list with all values for districts");
        return districtDtoList;
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

    //Get district values of a selected country by country code
    public List<DistrictDto> getDistrictValuesOfSelectedCountry(String code) {
        log.debug("Invoke get district values for {}", code);
        return getAllDistrictValues()
                .stream()
                .filter(c -> c.getCountry_code().equals(code))
                .collect(Collectors.toList());
    }

    //Brauche ich das überhaupt? Wird nur für den incident rate bei country verwendet. Kann man doch sicher berechnen
    //Get one value of a selected country calculated from a csv
    public Optional<CountryDailyDto> getDailyReportSelectedCountry(List<CountryDailyDto> countryDailyDtoList, String country) {
        log.debug("Invoke get daily report of {}", country);
        Optional<CountryDailyDto> dailyReportDto = getAllDailyCountryValuesCalculated(countryDailyDtoList)
                .stream()
                .filter(c -> c.getCountry().equals(country))
                .findFirst();
        if (dailyReportDto.isEmpty()) {
            log.warn("No daily report for selected country {}", country);
            return Optional.of(new CountryDailyDto());
        }
        log.info("Return daily report for country {}", country);
        return dailyReportDto;
    }

    //Auch das wird nur im world controller verwendet für die untere Tabelle, was ich aber auch anders bekommen kann über eine API
    //Get all country values calculated
    public List<CountryDailyDto> getAllDailyCountryValuesCalculated(List<CountryDailyDto> countryDailyDtoList) {
        log.debug("Invoke get all daily reports for all countries calculated");
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
}