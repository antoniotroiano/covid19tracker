package com.statistics.corona.service;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.DailyReportUsDto;
import com.statistics.corona.model.DistrictDto;
import com.statistics.corona.service.csv.ReadDailyReportsCSV;
import com.statistics.corona.service.json.ReadJSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DailyReportService {

    private final ReadDailyReportsCSV readDailyReportCSV;
    private final ReadJSON readJSON;

    public DailyReportService(ReadDailyReportsCSV readDailyReportCSV, ReadJSON readJSON) {
        this.readDailyReportCSV = readDailyReportCSV;
        this.readJSON = readJSON;
    }

    public Optional<CountryDetailsDto> getDetailsForCountry(String country) {
        log.debug("Invoke get details for country {}", country);
        try {
            log.debug("Returned details for country {}", country);
            CountryDetailsDto countryDetailsDto = readJSON.newReadDetailsCountry(country);
            return Optional.of(countryDetailsDto);
        } catch (Exception e) {
            log.warn("Failed get details for country {}: {}", country, e.getMessage());
            return Optional.of(new CountryDetailsDto());
        }
    }

    public List<DailyReportDto> getDailyDetailsOfProvince(String country) {
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

    public List<DailyReportUsDto> getDailyDetailsProvinceUs() {
        log.debug("Invoke get details for province of US");
        List<DailyReportUsDto> allDailyReportsUs = readDailyReportCSV.readDailyReportUs();
        if (allDailyReportsUs.isEmpty()) {
            log.warn("No daily report available for US");
            return allDailyReportsUs;
        }
        log.debug("Returned all values of province for US");
        return allDailyReportsUs;
    }

    public List<DistrictDto> getDistrictValues(String code) {
        log.debug("Invoke get district values of {}", code);
        try {
            List<DistrictDto> districtDtoList = readJSON.readDistrictsValues(code);
            log.debug("Returned list with all district values of {}", code);
            return districtDtoList;
        } catch (IOException e) {
            log.warn("Failed get all district values, with message: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<DailyReportDto> getAllDailyCountryValues() {
        log.debug("Invoke get all details for all countries");
        List<DailyReportDto> allDailyReports = readDailyReportCSV.readDailyReportsCSV();
        if (allDailyReports.isEmpty()) {
            log.warn("No values available for all counties.");
            return allDailyReports;
        }
        List<DailyReportDto> allCountryValues = new ArrayList<>();

        List<String> countryWithProvince = Arrays.asList("US", "Italy", "Canada", "Spain", "United Kingdom", "China",
                "Netherlands", "Australia", "Germany", "Denmark", "France", "Brazil", "Chile", "Japan", "Mexico", "Peru",
                "Russia", "Colombia", "India", "Pakistan", "Ukraine", "Sweden");

        for (String country : countryWithProvince) {
            DailyReportDto dailyReportDto = new DailyReportDto();
            dailyReportDto.setCountry(country);
            dailyReportDto.setConfirmed(getSumValues(getAllValuesGivenCountry(allDailyReports, country)).get("sumConfirmed"));
            dailyReportDto.setRecovered(getSumValues(getAllValuesGivenCountry(allDailyReports, country)).get("sumRecovered"));
            dailyReportDto.setDeaths(getSumValues(getAllValuesGivenCountry(allDailyReports, country)).get("sumDeaths"));
            dailyReportDto.setActive(getSumValues(getAllValuesGivenCountry(allDailyReports, country)).get("sumActive"));
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
                .findFirst();

        if (dailyReportDto.isPresent()) {
            log.debug("Returned dto for selected province {}", province);
            return dailyReportDto;
        }
        log.warn("No values for dto available of {}", province);
        return dailyReportDto;
    }
}