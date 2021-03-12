package com.covid19.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.covid19.model.dto.CountryDailyDto;
import com.covid19.model.dto.CountryTimeSeriesDto;
import com.covid19.model.dto.UsDailyDto;
import com.covid19.service.csv.CsvUtilsDailyReports;
import com.covid19.service.csv.CsvUtilsTimeSeries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProvinceService {

    private static final String CONFIRMED = "confirmed";
    private static final String DEATHS = "deaths";
    private final CsvUtilsTimeSeries csvUtilsTimeSeries;
    private final CsvUtilsDailyReports csvUtilsDailyReports;

    @Autowired
    public ProvinceService(CsvUtilsTimeSeries csvUtilsTimeSeries,
                           CsvUtilsDailyReports csvUtilsDailyReports) {
        this.csvUtilsTimeSeries = csvUtilsTimeSeries;
        this.csvUtilsDailyReports = csvUtilsDailyReports;
    }

    //Get province details for selected province
    public Optional<CountryDailyDto> getProvinceDetails(String province) {
        log.debug("Invoke get details for selected province {}", province);
        Optional<CountryDailyDto> dailyReportDto = csvUtilsDailyReports.readDailyReportsCSV()
                .stream()
                .filter(p -> p.getProvince().equals(province))
                .findAny();
        if (dailyReportDto.isPresent()) {
            log.info("Returned dto for selected province {}", province);
            return dailyReportDto;
        }
        log.warn("No values for dto available of {}", province);
        return Optional.of(new CountryDailyDto());
    }

    //Get us province details for selected province
    public Optional<UsDailyDto> getUsProvinceDetails(String province) {
        log.debug("Invoke get details for us province {}", province);
        Optional<UsDailyDto> dailyReportUsDto = csvUtilsDailyReports.readDailyReportUsCSV()
                .stream()
                .filter(p -> p.getProvince().equals(province))
                .findAny();
        if (dailyReportUsDto.isPresent()) {
            log.info("Returned values of us province {}", province);
            return dailyReportUsDto;
        }
        log.debug("No values available for us province {}", province);
        return Optional.of(new UsDailyDto());
    }

    public Map<String, Optional<CountryTimeSeriesDto>> getProvinceTSValues(String province) {
        log.debug("Invoke get time series for province {}", province);
        Map<String, Optional<CountryTimeSeriesDto>> allProvinceTS = new HashMap<>();
        Optional<CountryTimeSeriesDto> confirmedTS = csvUtilsTimeSeries.readConfirmedCsv()
                .stream()
                .filter(p -> Objects.nonNull(p.getProvince()))
                .filter(p -> p.getProvince().equals(province))
                .findAny();

        Optional<CountryTimeSeriesDto> recoveredTS = csvUtilsTimeSeries.readRecoveredCsv()
                .stream()
                .filter(p -> Objects.nonNull(p.getProvince()))
                .filter(p -> p.getProvince().equals(province))
                .findAny();

        Optional<CountryTimeSeriesDto> deathsTS = csvUtilsTimeSeries.readDeathsCsv()
                .stream()
                .filter(p -> Objects.nonNull(p.getProvince()))
                .filter(p -> p.getProvince().equals(province))
                .findAny();

        if (confirmedTS.isPresent() && recoveredTS.isPresent() && deathsTS.isPresent()) {
            allProvinceTS.put(CONFIRMED, confirmedTS);
            allProvinceTS.put("recovered", recoveredTS);
            allProvinceTS.put(DEATHS, deathsTS);
        } else {
            allProvinceTS.put(CONFIRMED, Optional.empty());
            allProvinceTS.put("recovered", Optional.empty());
            allProvinceTS.put(DEATHS, Optional.empty());
        }
        log.info("Return map with time series for selected province {}", province);
        return allProvinceTS;
    }

    public Map<String, Map<String, Integer>> getUsProvinceTSValues(String province) {
        log.debug("Invoke get all values for us province {}", province);
        Map<String, Map<String, Integer>> allValuesTS = new HashMap<>();
        List<CountryTimeSeriesDto> confirmedTS = csvUtilsTimeSeries.readUsConfirmedCsv()
                .stream()
                .filter(p -> Objects.nonNull(p.getProvince()))
                .filter(c -> c.getProvince().equals(province))
                .collect(Collectors.toList());
        List<CountryTimeSeriesDto> deathsTS = csvUtilsTimeSeries.readUsDeathsCsv()
                .stream()
                .filter(p -> Objects.nonNull(p.getProvince()))
                .filter(c -> c.getProvince().equals(province))
                .collect(Collectors.toList());

        List<String> confirmedKeys = new ArrayList<>(confirmedTS.get(0).getValues().keySet());
        Map<String, Integer> confirmedMap = new LinkedHashMap<>();
        for (int i = 0; i < confirmedKeys.size(); i++) {
            confirmedMap.put(confirmedKeys.get(i), finalResult(confirmedTS).get(i));
        }

        List<String> deathsKeys = new ArrayList<>(confirmedTS.get(0).getValues().keySet());
        Map<String, Integer> deathsMap = new LinkedHashMap<>();
        for (int i = 0; i < deathsKeys.size(); i++) {
            deathsMap.put(deathsKeys.get(i), finalResult(deathsTS).get(i));
        }

        if (!confirmedTS.isEmpty() && !deathsTS.isEmpty()) {
            allValuesTS.put(CONFIRMED, confirmedMap);
            allValuesTS.put(DEATHS, deathsMap);
        } else {
            allValuesTS.put(CONFIRMED, Collections.emptyMap());
            allValuesTS.put(DEATHS, Collections.emptyMap());
        }
        log.info("Return map with time series for selected us province {}", province);
        return allValuesTS;
    }

    private List<List<Integer>> interimResult(List<CountryTimeSeriesDto> dataList) {
        log.debug("Invoke interim result for time series");
        List<List<Integer>> interimResultList = new ArrayList<>();
        if (dataList == null || dataList.isEmpty()) {
            log.warn("The list with data is empty. No calculation for interim result");
            return Collections.emptyList();
        }
        for (CountryTimeSeriesDto countryTimeSeriesDto : dataList) {
            List<Integer> values = new ArrayList<>(countryTimeSeriesDto.getValues().values());
            interimResultList.add(values);
        }
        log.info("Return interim result");
        return interimResultList;
    }

    public List<Integer> finalResult(List<CountryTimeSeriesDto> dataList) {
        log.debug("Invoke final result for time series");
        List<List<Integer>> interimResult = interimResult(dataList);
        List<Integer> finalResult = new ArrayList<>();
        if (interimResult.isEmpty()) {
            log.warn("The interim result list is empty. No calculation for final result");
            return Collections.emptyList();
        }
        for (int j = 0; j < interimResult.get(0).size(); j++) {
            int sum = 0;
            for (List<Integer> integers : interimResult) {
                sum += integers.get(j);
            }
            finalResult.add(sum);
        }
        log.info("Return final result");
        return finalResult;
    }

    public Integer getUsProvincePopulation(String province) {
        log.debug("Invoke get us province population of {}", province);
        int population = csvUtilsTimeSeries.readUsDeathsCsv()
                .stream()
                .filter(p -> Objects.nonNull(p.getProvince()))
                .filter(c -> c.getProvince().equals(province))
                .mapToInt(CountryTimeSeriesDto::getPopulation).sum();
        log.info("Return the population for {}", province);
        return population;
    }

    public Optional<CountryDailyDto> getYesterdayValues(String province) {
        log.debug("Invoke get yesterday values of province {}", province);
        Optional<CountryDailyDto> countryDailyDto = csvUtilsDailyReports.readDailyReportsYesterdayCsv().stream()
                .filter(p -> Objects.nonNull(p.getProvince()))
                .filter(c -> c.getProvince().equals(province))
                .findAny();

        if (countryDailyDto.isPresent()) {
            log.info("Returned yesterday values of province {}", province);
            return countryDailyDto;
        }
        log.warn("No value for yesterday of province {} available", province);
        return Optional.empty();
    }

    public Map<String, Integer> getTodayIncrementForProvince(String province) {
        log.debug("Invoke get today increment for province {}", province);
        Optional<CountryDailyDto> todayCountryDailyDto = getProvinceDetails(province);
        Optional<CountryDailyDto> yesterdayCountryDailyDto = getYesterdayValues(province);
        Map<String, Integer> todayIncrement = new HashMap<>();
        if (todayCountryDailyDto.isPresent() && yesterdayCountryDailyDto.isPresent()) {
            int todayConfirmed = todayCountryDailyDto.get().getConfirmed() - yesterdayCountryDailyDto.get().getConfirmed();
            int todayRecovered = todayCountryDailyDto.get().getRecovered() - yesterdayCountryDailyDto.get().getRecovered();
            int todayDeaths = todayCountryDailyDto.get().getDeaths() - yesterdayCountryDailyDto.get().getDeaths();
            int todayActive = todayCountryDailyDto.get().getActive() - yesterdayCountryDailyDto.get().getActive();
            todayIncrement.put("todayConfirmed", todayConfirmed);
            todayIncrement.put("todayRecovered", todayRecovered);
            todayIncrement.put("todayDeath", todayDeaths);
            todayIncrement.put("todayActive", todayActive);
            log.info("Returned today increment of province {}", province);
            return todayIncrement;
        }
        log.warn("No today increment available for province {}", province);
        return Collections.emptyMap();
    }

    public List<CountryDailyDto> getYesterdayValuesOfCountry(String country) {
        log.debug("Invoke get yesterday values of selected country {}", country);
        List<CountryDailyDto> selectedCountry = csvUtilsDailyReports.readDailyReportsYesterdayCsv().stream()
                .filter(c -> c.getCountry().contains(country))
                .collect(Collectors.toList());
        if (selectedCountry.isEmpty()) {
            log.warn("No values from yesterday is available for {}", country);
            return Collections.emptyList();
        }
        log.debug("Returned all values from yesterday for {}", country);
        return selectedCountry;
    }

    public List<CountryDailyDto> getEnrichedCountryValues(String country) {
        log.debug("Invoke merge yesterday with today values");
        List<CountryDailyDto> countryDailyDtoList = csvUtilsDailyReports.readDailyReportsCSV().stream()
                .filter(c -> c.getCountry().contains(country))
                .collect(Collectors.toList());
        List<CountryDailyDto> countryDailyDtoYesterdayList = getYesterdayValuesOfCountry(country);

        List<CountryDailyDto> enrichedValues = new ArrayList<>();
        for (CountryDailyDto countryDailyDto : countryDailyDtoList) {
            for (CountryDailyDto countryDailyDto1 : countryDailyDtoYesterdayList) {
                if (countryDailyDto.getProvince().equals(countryDailyDto1.getProvince())) {
                    countryDailyDto.setIncrementConfirmed((countryDailyDto.getConfirmed() - countryDailyDto1.getConfirmed()));
                    countryDailyDto.setIncrementRecovered((countryDailyDto.getRecovered() - countryDailyDto1.getRecovered()));
                    countryDailyDto.setIncrementDeaths((countryDailyDto.getDeaths() - countryDailyDto1.getDeaths()));
                    enrichedValues.add(countryDailyDto);
                }
            }
        }
        log.debug("Returned enriched values for selected country {}", country);
        return enrichedValues;
    }
}