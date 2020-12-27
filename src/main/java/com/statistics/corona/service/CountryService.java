package com.statistics.corona.service;

import com.statistics.corona.model.dto.CountryTimeSeriesDto;
import com.statistics.corona.model.dto.CountryValuesDto;
import com.statistics.corona.service.csv.CsvUtilsTimeSeries;
import com.statistics.corona.service.json.JsonUtils;
import com.statistics.corona.utils.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CountryService {

    private final CsvUtilsTimeSeries csvUtilsTimeSeries;
    private final ServiceUtils serviceUtils;
    private final JsonUtils jsonUtils;

    @Autowired
    public CountryService(CsvUtilsTimeSeries csvUtilsTimeSeries,
                          ServiceUtils serviceUtils,
                          JsonUtils jsonUtils) {
        this.csvUtilsTimeSeries = csvUtilsTimeSeries;
        this.serviceUtils = serviceUtils;
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

    private double calculateSevenDayIncidence(CountryValuesDto countryValuesDto) {
        log.debug("Invoke calculate the seven day incidence of country {}", countryValuesDto.getCountry());
        int size = countryValuesDto.getCasesValues().size();
        int sumSevenDayValues = serviceUtils.getDailyTrend(countryValuesDto.getCasesValues().values())
                .stream()
                .skip((long) size - 7)
                .mapToInt(Integer::intValue)
                .sum();
        return ((double) sumSevenDayValues / countryValuesDto.getPopulation()) * 100000;
    }

    //Eventuell vereinfachen Prüfen ob es dann noch geht mit der null
    public Integer calculateYesterdayActive(CountryValuesDto countryValuesDto) {
        log.debug("Invoke calculate yesterday active");
        int yesterdayActive = serviceUtils.getYesterdayValues(countryValuesDto.getCasesValues().values()) -
                serviceUtils.getYesterdayValues(countryValuesDto.getRecoveredValues().values()) -
                serviceUtils.getYesterdayValues(countryValuesDto.getDeathsValues().values());
        if (yesterdayActive == 0) {
            return 0;
        } else {
            return yesterdayActive;
        }
    }

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