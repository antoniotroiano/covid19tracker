package com.statistics.corona.service.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.statistics.corona.model.TimeSeriesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
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

@Service
@Slf4j
public class CsvUtilsTimeSeries {

    private final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy", Locale.GERMAN);
    private final LocalDate startDate = LocalDate.of(2020, 1, 22);

    public List<TimeSeriesDto> readConfirmedCsv() {
        log.debug("Invoke read confirmed time series CSV from github");
        try {
            URL confirmed = new URL(
                    "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv");
            List<TimeSeriesDto> timeSeriesDtoList = readTimeSeriesCSV(confirmed);
            log.info("Returned time series list with all confirmed values");
            return timeSeriesDtoList;
        } catch (Exception e) {
            log.warn("Occurred an exception while reading confirmed CSV from github {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<TimeSeriesDto> readRecoveredCsv() {
        log.debug("Invoke read recovered time series CSV from github");
        try {
            URL recovered = new URL(
                    "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv");
            List<TimeSeriesDto> timeSeriesDtoList = readTimeSeriesCSV(recovered);
            log.info("Returned time series list with all recovered values");
            return timeSeriesDtoList;
        } catch (Exception e) {
            log.warn("Occurred an exception while reading recovered CSV from github {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<TimeSeriesDto> readDeathsCsv() {
        log.debug("Invoke read deaths time series CSV from github");
        try {
            URL deaths = new URL(
                    "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv");
            List<TimeSeriesDto> timeSeriesDtoList = readTimeSeriesCSV(deaths);
            log.info("Returned time series list with all deaths values");
            return timeSeriesDtoList;
        } catch (Exception e) {
            log.warn("Occurred an exception while reading deaths CSV from github {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<TimeSeriesDto> readUsConfirmedCsv() {
        log.debug("Invoke read us confirmed time series CSV from github");
        try {
            URL confirmed = new URL(
                    "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_US.csv");
            List<TimeSeriesDto> timeSeriesDtoList = readTimeSeriesUsConfirmedCSV(confirmed);
            log.info("Returned time series list with all us confirmed values");
            return timeSeriesDtoList;
        } catch (Exception e) {
            log.warn("Occurred an exception while reading us confirmed CSV from github {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<TimeSeriesDto> readUsDeathsCsv() {
        log.debug("Invoke read us deaths time series CSV from github");
        try {
            URL deaths = new URL(
                    "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_US.csv");
            List<TimeSeriesDto> timeSeriesDtoList = readTimeSeriesUsDeathsCSV(deaths);
            log.info("Returned time series list with all us deaths values");
            return timeSeriesDtoList;
        } catch (Exception e) {
            log.warn("Occurred an exception while reading us deaths CSV from github {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<String> readCountryName() {
        log.debug("Invoke read country of CSV from github");
        return readConfirmedCsv()
                .stream()
                .map(TimeSeriesDto::getCountry)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<TimeSeriesDto> readTimeSeriesCSV(URL url) {
        log.debug("Invoke read time series from CSV on github");
        List<TimeSeriesDto> timeSeriesDtoList = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
             CSVReader reader = new CSVReaderBuilder(in)
                     .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                     .withSkipLines(1)
                     .build()) {
            String[] record;
            while ((record = reader.readNext()) != null) {
                TimeSeriesDto timeSeriesDto = new TimeSeriesDto();
                LinkedHashMap<String, Integer> mapValues = new LinkedHashMap<>();
                LocalDate date = startDate;
                timeSeriesDto.setProvince(record[0]);
                timeSeriesDto.setCountry(record[1]);
                for (int i = 4; i < record.length; i++) {
                    String formattedDate = date.format(outputFormatter);
                    mapValues.put(formattedDate, Integer.parseInt(record[i]));
                    date = date.plusDays(1);
                }
                timeSeriesDto.setDataMap(mapValues);
                timeSeriesDtoList.add(timeSeriesDto);
            }
            log.info("Add all time series object to list");
            return timeSeriesDtoList;
        } catch (Exception e) {
            log.warn("Occurred an exception while reading csv on github {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<TimeSeriesDto> readTimeSeriesUsConfirmedCSV(URL url) {
        log.debug("Invoke read time series of us confirmed from CSV on github");
        List<TimeSeriesDto> timeSeriesDtoList = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
             CSVReader reader = new CSVReaderBuilder(in)
                     .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                     .withSkipLines(1)
                     .build()) {
            String[] record;
            while ((record = reader.readNext()) != null) {
                TimeSeriesDto timeSeriesDto = new TimeSeriesDto();
                LinkedHashMap<String, Integer> mapValues = new LinkedHashMap<>();
                LocalDate date = startDate;
                timeSeriesDto.setDistrict(record[5]);
                timeSeriesDto.setProvince(record[6]);
                timeSeriesDto.setCountry(record[7]);
                timeSeriesDto.setCombinedKey(record[10]);
                for (int i = 11; i < record.length; i++) {
                    String formattedDate = date.format(outputFormatter);
                    mapValues.put(formattedDate, Integer.parseInt(record[i]));
                    date = date.plusDays(1);
                }
                timeSeriesDto.setDataMap(mapValues);
                timeSeriesDtoList.add(timeSeriesDto);
            }
            log.info("Add all time series object of us confirmed to list");
            return timeSeriesDtoList;
        } catch (Exception e) {
            log.warn("Occurred an exception while reading CSV for us confirmed on github {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<TimeSeriesDto> readTimeSeriesUsDeathsCSV(URL url) {
        log.debug("Invoke read time series of us deaths from CSV on github");
        List<TimeSeriesDto> timeSeriesDtoList = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
             CSVReader reader = new CSVReaderBuilder(in)
                     .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                     .withSkipLines(1)
                     .build()) {
            String[] record;
            while ((record = reader.readNext()) != null) {
                TimeSeriesDto timeSeriesDto = new TimeSeriesDto();
                LinkedHashMap<String, Integer> mapValues = new LinkedHashMap<>();
                LocalDate date = startDate;
                timeSeriesDto.setDistrict(record[5]);
                timeSeriesDto.setProvince(record[6]);
                timeSeriesDto.setCountry(record[7]);
                timeSeriesDto.setCombinedKey(record[10]);
                timeSeriesDto.setPopulation(Integer.parseInt(record[11]));
                for (int i = 12; i < record.length; i++) {
                    String formattedDate = date.format(outputFormatter);
                    mapValues.put(formattedDate, Integer.parseInt(record[i]));
                    date = date.plusDays(1);
                }
                timeSeriesDto.setDataMap(mapValues);
                timeSeriesDtoList.add(timeSeriesDto);
            }
            log.info("Add all time series object of us deaths to list");
            return timeSeriesDtoList;
        } catch (Exception e) {
            log.warn("Occurred an exception while reading CSV for us deaths on github {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}