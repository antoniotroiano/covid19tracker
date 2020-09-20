package com.statistics.corona.service;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.DailyReportUsDto;
import com.statistics.corona.model.DistrictDto;
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
public class DailyReportService {

    private final CsvUtilsDailyReports readDailyReportCSV;
    private final JsonUtils jsonUtils;

    @Autowired
    public DailyReportService(CsvUtilsDailyReports readDailyReportCSV, JsonUtils jsonUtils) {
        this.readDailyReportCSV = readDailyReportCSV;
        this.jsonUtils = jsonUtils;
    }

    //3 methods for get all values of districts, us daily report and all country daily reports
    public List<DailyReportDto> getAllDailyReports() {
        log.debug("Invoke get all daily reports for all countries");
        List<DailyReportDto> dailyReportDtoList = readDailyReportCSV.readDailyReportsCSV();
        if (dailyReportDtoList == null || dailyReportDtoList.isEmpty()) {
            log.warn("No values available for daily reports of all countries");
            return Collections.emptyList();
        }
        log.info("Return all daily reports for all countries");
        return dailyReportDtoList;
    }

    public List<DailyReportUsDto> getAllDailyReportsUS() {
        log.debug("Invoke get all daily reports for us");
        List<DailyReportUsDto> dailyReportUsDtoList = readDailyReportCSV.readDailyReportUsCSV();
        if (dailyReportUsDtoList == null || dailyReportUsDtoList.isEmpty()) {
            log.warn("No values available for daily reports of us");
            return Collections.emptyList();
        }
        log.info("Return all daily reports us");
        return dailyReportUsDtoList;
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

    //Get values for a selected country
    public Optional<CountryDetailsDto> getSelectedCountryValues(String country) {
        log.debug("Invoke get details for country {}", country);
        try {
            CountryDetailsDto countryDetailsDto = jsonUtils.readCountryValuesOfJson(country);
            if (countryDetailsDto == null) {
                log.warn("No value available for country {}", country);
                return Optional.of(new CountryDetailsDto());
            }
            log.info("Return value for country {}", country);
            return Optional.of(countryDetailsDto);
        } catch (Exception e) {
            log.warn("Failed get details for country {}: {}", country, e.getMessage());
            return Optional.of(new CountryDetailsDto());
        }
    }

    //Get district values of a selected country by country code
    public List<DistrictDto> getDistrictValuesOfSelectedCountry(String code) {
        log.debug("Invoke get district values for {}", code);
        return getAllDistrictValues()
                .stream()
                .filter(c -> c.getCountry_code().equals(code))
                .collect(Collectors.toList());
    }

    //Get province values of a selected country
    public List<DailyReportDto> getDailyReportsOfProvince(List<DailyReportDto> dailyReportDtoList, String country) {
        log.debug("Invoke get daily reports for province of country {}", country);
        if (dailyReportDtoList == null || dailyReportDtoList.isEmpty()) {
            log.warn("No daily reports available for {}", country);
            return Collections.emptyList();
        }
        log.info("Return all daily reports for province of country {}", country);
        return dailyReportDtoList.stream()
                .filter(c -> c.getCountry().equals(country))
                .collect(Collectors.toList());
    }

    //Get one value of a selected country calculated from a csv
    public Optional<DailyReportDto> getDailyReportSelectedCountry(List<DailyReportDto> dailyReportDtoList, String country) {
        log.debug("Invoke get daily report of {}", country);
        Optional<DailyReportDto> dailyReportDto = getAllDailyCountryValuesCalculated(dailyReportDtoList)
                .stream()
                .filter(c -> c.getCountry().equals(country))
                .findFirst();
        if (dailyReportDto.isEmpty()) {
            log.warn("No daily report for selected country {}", country);
            return Optional.of(new DailyReportDto());
        }
        log.info("Return daily report for country {}", country);
        return dailyReportDto;
    }

    //Get all country values calculated
    public List<DailyReportDto> getAllDailyCountryValuesCalculated(List<DailyReportDto> dailyReportDtoList) {
        log.debug("Invoke get all daily reports for all countries calculated");
        if (dailyReportDtoList == null || dailyReportDtoList.isEmpty()) {
            log.warn("No values available for all counties");
            return Collections.emptyList();
        }
        List<String> countryWithProvince = dailyReportDtoList
                .stream()
                .filter(c -> Strings.isNotEmpty(c.getProvince()))
                .map(DailyReportDto::getCountry)
                .distinct()
                .collect(Collectors.toList());
        List<DailyReportDto> allCountryValues = getCalculatedCountry(countryWithProvince, dailyReportDtoList);
        for (DailyReportDto allDailyReport : dailyReportDtoList) {
            if (!countryWithProvince.contains(allDailyReport.getCountry())) {
                allCountryValues.add(allDailyReport);
            }
        }
        log.info("Returned all values of all countries calculated");
        return allCountryValues;
    }

    private List<DailyReportDto> getCalculatedCountry(List<String> countryWithProvince, List<DailyReportDto> allDailyReports) {
        List<DailyReportDto> allCountryValues = new ArrayList<>();
        for (String country : countryWithProvince) {
            DailyReportDto dailyReportDto = new DailyReportDto();
            dailyReportDto.setCountry(country);
            dailyReportDto.setConfirmed(getSumValues(getAllValuesGivenCountry(allDailyReports, country)).get("sumConfirmed"));
            dailyReportDto.setRecovered(getSumValues(getAllValuesGivenCountry(allDailyReports, country)).get("sumRecovered"));
            dailyReportDto.setDeaths(getSumValues(getAllValuesGivenCountry(allDailyReports, country)).get("sumDeaths"));
            dailyReportDto.setActive(getSumValues(getAllValuesGivenCountry(allDailyReports, country)).get("sumActive"));
            allCountryValues.add(dailyReportDto);
        }
        return allCountryValues;
    }

    private List<DailyReportDto> getAllValuesGivenCountry(List<DailyReportDto> dailyReportDtoList, String country) {
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

    public Optional<DailyReportDto> getProvinceDetails(String province) {
        log.debug("Invoke get details for selected province {}", province);
        Optional<DailyReportDto> dailyReportDto = readDailyReportCSV.readDailyReportsCSV()
                .stream()
                .filter(p -> p.getProvince().equals(province))
                .findAny();
        if (dailyReportDto.isPresent()) {
            log.info("Returned dto for selected province {}", province);
            return dailyReportDto;
        }
        log.warn("No values for dto available of {}", province);
        return Optional.of(new DailyReportDto());
    }

    public Optional<DailyReportUsDto> getUsProvinceDetails(String province) {
        log.debug("Invoke get details for us province {}", province);
        Optional<DailyReportUsDto> dailyReportUsDto = readDailyReportCSV.readDailyReportUsCSV()
                .stream()
                .filter(p -> p.getProvince().equals(province))
                .findAny();
        if (dailyReportUsDto.isPresent()) {
            log.info("Returned values of us province {}", province);
            return dailyReportUsDto;
        }
        log.debug("No values available for us province {}", province);
        return Optional.of(new DailyReportUsDto());
    }
}