package com.statistics.corona.service;

import com.statistics.corona.model.dto.CountryTimeSeriesDto;
import com.statistics.corona.model.dto.CountryValuesDto;
import com.statistics.corona.service.csv.CsvUtilsTimeSeries;
import com.statistics.corona.service.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CountryService {

    private final CsvUtilsTimeSeries csvUtilsTimeSeries;
    private final UtilsService utilsService;
    private final JsonUtils jsonUtils;

    @Autowired
    public CountryService(CsvUtilsTimeSeries csvUtilsTimeSeries,
                          UtilsService utilsService,
                          JsonUtils jsonUtils) {
        this.csvUtilsTimeSeries = csvUtilsTimeSeries;
        this.utilsService = utilsService;
        this.jsonUtils = jsonUtils;
    }

    public List<CountryTimeSeriesDto> getAllConfirmedTSValues() {
        log.debug("Invoke get all confirmed time series values");
        List<CountryTimeSeriesDto> confirmedTSList = csvUtilsTimeSeries.readConfirmedCsv();
        if (confirmedTSList == null || confirmedTSList.isEmpty()) {
            log.warn("No confirmed time series values");
            return Collections.emptyList();
        }
        log.info("Return list with confirmed time series values");
        return confirmedTSList;
    }

    public List<CountryTimeSeriesDto> getAllRecoveredTSValues() {
        log.debug("Invoke get all recovered time series values");
        List<CountryTimeSeriesDto> recoveredTSList = csvUtilsTimeSeries.readRecoveredCsv();
        if (recoveredTSList == null || recoveredTSList.isEmpty()) {
            log.warn("No recovered time series values");
            return Collections.emptyList();
        }
        log.info("Return list with recovered time series values");
        return recoveredTSList;
    }

    public List<CountryTimeSeriesDto> getAllDeathsTSValues() {
        log.debug("Invoke get all deaths time series values");
        List<CountryTimeSeriesDto> deathsTSList = csvUtilsTimeSeries.readDeathsCsv();
        if (deathsTSList == null || deathsTSList.isEmpty()) {
            log.warn("No deaths time series values");
            return Collections.emptyList();
        }
        log.info("Return list with deaths time series values");
        return deathsTSList;
    }

    //Get values for a selected country
    public Optional<CountryValuesDto> getSelectedCountryValues(String country) {
        log.debug("Invoke get details for country {}", country);
        try {
            CountryValuesDto countryValuesDto = jsonUtils.readCountryValuesOfJson(country);
            if (countryValuesDto == null) {
                log.warn("No value available for country {}", country);
                return Optional.of(new CountryValuesDto());
            }
            log.info("Return value for country {}", country);
            return Optional.of(mapTStoDTO(countryValuesDto));
        } catch (Exception e) {
            log.warn("Failed get details for country {}: {}", country, e.getMessage());
            return Optional.of(new CountryValuesDto());
        }
    }

    private CountryValuesDto mapTStoDTO(CountryValuesDto countryValuesDto) {
        log.debug("Invoke map time series to countryValuesDto");
        String country = countryValuesDto.getCountry();
        if (country.equals("USA")) {
            country = "US";
        }
        String finalCountry = country;
        Optional<CountryTimeSeriesDto> confirmed = getAllConfirmedTSValues()
                .stream()
                .filter(c -> c.getCountry().equals(finalCountry))
                .findFirst();

        Optional<CountryTimeSeriesDto> recovered = getAllRecoveredTSValues()
                .stream()
                .filter(c -> c.getCountry().equals(finalCountry))
                .findFirst();

        Optional<CountryTimeSeriesDto> deaths = getAllDeathsTSValues()
                .stream()
                .filter(c -> c.getCountry().equals(finalCountry))
                .findFirst();

        if (confirmed.isPresent() && recovered.isPresent() && deaths.isPresent()) {
            countryValuesDto.setCasesValues(confirmed.get().getValues());
            countryValuesDto.setRecoveredValues(recovered.get().getValues());
            countryValuesDto.setDeathsValues(deaths.get().getValues());
            int sevenDayIncidence = (int) calculateSevenDayIncidence(countryValuesDto);
            countryValuesDto.setSevenDayIncidence(sevenDayIncidence);
        } else {
            countryValuesDto.setCasesValues(Collections.emptyMap());
            countryValuesDto.setRecoveredValues(Collections.emptyMap());
            countryValuesDto.setDeathsValues(Collections.emptyMap());
        }
        log.info("Return countryValuesDto with time series");
        return countryValuesDto;
    }

    //ToDo: calculate also the incidence of yesterday
    private double calculateSevenDayIncidence(CountryValuesDto countryValuesDto) {
        log.debug("Invoke calculate the seven day incidence");
        int size = countryValuesDto.getCasesValues().size();
        int sumSevenDayValues = utilsService.getDailyTrend(countryValuesDto.getCasesValues())
                .stream()
                .skip((long) size - 7)
                .mapToInt(Integer::intValue)
                .sum();
        return ((double) sumSevenDayValues / countryValuesDto.getPopulation()) * 100000;
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

    private int getYesterdayValues(Collection<Integer> values) {
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
        log.info("Return last value");
        return lastValueInt;
    }

    //Eventuell vereinfachen Prüfen ob es dann noch geht mit der null
    public Integer calculateYesterdayActive(CountryValuesDto countryValuesDto) {
        log.debug("Invoke calculate yesterday active");
        Integer yesterdayActive = getYesterdayValues(countryValuesDto.getCasesValues().values()) -
                getYesterdayValues(countryValuesDto.getRecoveredValues().values()) -
                getYesterdayValues(countryValuesDto.getDeathsValues().values());
        if (yesterdayActive == null) {
            return 0;
        } else {
            return yesterdayActive;
        }
    }

    //ToDo: Maybe to the UtilsService
    public List<String> getCountryNames() {
        log.debug("Invoke get all country names");
        List<String> allCountries = csvUtilsTimeSeries.readCountryName();
        if (allCountries == null || allCountries.isEmpty()) {
            log.warn("No country names available");
            return Collections.emptyList();
        }
        log.info("Returned list with all country names");
        return allCountries;
    }
}