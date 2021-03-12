package com.covid19.service.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.covid19.model.dto.CountryTimeSeriesDto;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvValidationException;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CsvUtilsTimeSeries {

    private final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy", Locale.GERMAN);
    private final LocalDate startDate = LocalDate.of(2020, 1, 22);

    public List<CountryTimeSeriesDto> readConfirmedCsv() {
        log.debug("Invoke read confirmed time series values from github");
        try {
            URL confirmed = new URL(
                    "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv");
            List<CountryTimeSeriesDto> countryTimeSeriesDtoList = readTimeSeriesCSV(confirmed);
            log.info("Returned time series list with all confirmed values");
            return countryTimeSeriesDtoList;
        } catch (IOException e) {
            log.warn("Occurred an exception while reading confirmed time series values from github {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<CountryTimeSeriesDto> readRecoveredCsv() {
        log.debug("Invoke read recovered time series values from github");
        try {
            URL recovered = new URL(
                    "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv");
            List<CountryTimeSeriesDto> countryTimeSeriesDtoList = readTimeSeriesCSV(recovered);
            log.info("Returned time series list with all recovered values");
            return countryTimeSeriesDtoList;
        } catch (IOException e) {
            log.warn("Occurred an exception while reading recovered time series values from github {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<CountryTimeSeriesDto> readDeathsCsv() {
        log.debug("Invoke read deaths time series values from github");
        try {
            URL deaths = new URL(
                    "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv");
            List<CountryTimeSeriesDto> countryTimeSeriesDtoList = readTimeSeriesCSV(deaths);
            log.info("Returned time series list with all deaths values");
            return countryTimeSeriesDtoList;
        } catch (IOException e) {
            log.warn("Occurred an exception while reading deaths time series values from github {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<CountryTimeSeriesDto> readUsConfirmedCsv() {
        log.debug("Invoke read us confirmed time series values from github");
        try {
            URL confirmed = new URL(
                    "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_US.csv");
            List<CountryTimeSeriesDto> countryTimeSeriesDtoList = readTimeSeriesUsConfirmedCSV(confirmed);
            log.info("Returned time series list with all us confirmed values");
            return countryTimeSeriesDtoList;
        } catch (IOException e) {
            log.warn("Occurred an exception while reading us confirmed time series values from github {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<CountryTimeSeriesDto> readUsDeathsCsv() {
        log.debug("Invoke read us deaths time series values from github");
        try {
            URL deaths = new URL(
                    "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_US.csv");
            List<CountryTimeSeriesDto> countryTimeSeriesDtoList = readTimeSeriesUsDeathsCSV(deaths);
            log.info("Returned time series list with all us deaths values");
            return countryTimeSeriesDtoList;
        } catch (IOException e) {
            log.warn("Occurred an exception while reading us deaths time series values from github {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<String> readCountryName() {
        log.debug("Invoke read country names from github");
        return readConfirmedCsv()
                .stream()
                .map(CountryTimeSeriesDto::getCountry)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<CountryTimeSeriesDto> readTimeSeriesCSV(URL url) {
        log.debug("Invoke read time series from CSV");
        List<CountryTimeSeriesDto> countryTimeSeriesDtoList = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
             CSVReader reader = new CSVReaderBuilder(in)
                     .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                     .withSkipLines(1)
                     .build()) {
            String[] record;
            while ((record = reader.readNext()) != null) {
                CountryTimeSeriesDto countryTimeSeriesDto = new CountryTimeSeriesDto();
                LinkedHashMap<String, Integer> mapValues = new LinkedHashMap<>();
                LocalDate date = startDate;
                countryTimeSeriesDto.setProvince(record[0]);
                countryTimeSeriesDto.setCountry(record[1]);
                for (int i = 4; i < record.length; i++) {
                    String formattedDate = date.format(outputFormatter);
                    mapValues.put(formattedDate, Integer.parseInt(record[i]));
                    date = date.plusDays(1);
                }
                countryTimeSeriesDto.setValues(mapValues);
                countryTimeSeriesDtoList.add(countryTimeSeriesDto);
            }
            log.info("Add all time series object to list");
            return countryTimeSeriesDtoList;
        } catch (CsvValidationException | IOException e) {
            log.warn("Occurred an exception while reading all time series from CSV {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<CountryTimeSeriesDto> readTimeSeriesUsConfirmedCSV(URL url) {
        log.debug("Invoke read time series of us confirmed from CSV");
        List<CountryTimeSeriesDto> countryTimeSeriesDtoList = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
             CSVReader reader = new CSVReaderBuilder(in)
                     .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                     .withSkipLines(1)
                     .build()) {
            String[] record;
            while ((record = reader.readNext()) != null) {
                CountryTimeSeriesDto countryTimeSeriesDto = new CountryTimeSeriesDto();
                LinkedHashMap<String, Integer> mapValues = new LinkedHashMap<>();
                LocalDate date = startDate;
                countryTimeSeriesDto.setDistrict(record[5]);
                countryTimeSeriesDto.setProvince(record[6]);
                countryTimeSeriesDto.setCountry(record[7]);
                countryTimeSeriesDto.setCombinedKey(record[10]);
                for (int i = 11; i < record.length; i++) {
                    String formattedDate = date.format(outputFormatter);
                    mapValues.put(formattedDate, Integer.parseInt(record[i]));
                    date = date.plusDays(1);
                }
                countryTimeSeriesDto.setValues(mapValues);
                countryTimeSeriesDtoList.add(countryTimeSeriesDto);
            }
            log.info("Add all time series object of us confirmed to list");
            return countryTimeSeriesDtoList;
        } catch (CsvValidationException | IOException e) {
            log.warn("Occurred an exception while reading all us confirmed from CSV {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<CountryTimeSeriesDto> readTimeSeriesUsDeathsCSV(URL url) {
        log.debug("Invoke read time series of us deaths from CSV");
        List<CountryTimeSeriesDto> countryTimeSeriesDtoList = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
             CSVReader reader = new CSVReaderBuilder(in)
                     .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                     .withSkipLines(1)
                     .build()) {
            String[] record;
            while ((record = reader.readNext()) != null) {
                CountryTimeSeriesDto countryTimeSeriesDto = new CountryTimeSeriesDto();
                LinkedHashMap<String, Integer> mapValues = new LinkedHashMap<>();
                LocalDate date = startDate;
                countryTimeSeriesDto.setDistrict(record[5]);
                countryTimeSeriesDto.setProvince(record[6]);
                countryTimeSeriesDto.setCountry(record[7]);
                countryTimeSeriesDto.setCombinedKey(record[10]);
                countryTimeSeriesDto.setPopulation(Integer.parseInt(record[11]));
                for (int i = 12; i < record.length; i++) {
                    String formattedDate = date.format(outputFormatter);
                    mapValues.put(formattedDate, Integer.parseInt(record[i]));
                    date = date.plusDays(1);
                }
                countryTimeSeriesDto.setValues(mapValues);
                countryTimeSeriesDtoList.add(countryTimeSeriesDto);
            }
            log.info("Add all time series object of us deaths to list");
            return countryTimeSeriesDtoList;
        } catch (CsvValidationException | IOException e) {
            log.warn("Occurred an exception while reading all us deaths values from CSV {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}