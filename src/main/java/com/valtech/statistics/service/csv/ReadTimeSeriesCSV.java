package com.valtech.statistics.service.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.valtech.statistics.model.TimeSeriesDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReadCSV {

    public List<TimeSeriesDto> readConfirmedCsv() {
        log.debug("Invoke read Confirmed time series CSV from github.");
        List<TimeSeriesDto> timeSeriesDtoList = new ArrayList<>();
        try {
            URL confirmed = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv");
            timeSeriesDtoList = readCSV(confirmed);
        } catch (Exception e) {
            log.warn("Occurred an exception while reading confirmed csv from github {}", e.getMessage());
        }
        return timeSeriesDtoList;
    }

    public List<TimeSeriesDto> readRecoveredCsv() {
        log.debug("Invoke read recovered time series CSV from github.");
        List<TimeSeriesDto> timeSeriesDtoList = new ArrayList<>();
        try {
            URL recovered = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv");
            timeSeriesDtoList = readCSV(recovered);
        } catch (Exception e) {
            log.warn("Occurred an exception while reading recovered csv from github {}", e.getMessage());
        }
        return timeSeriesDtoList;
    }

    public List<TimeSeriesDto> readDeathsCsv() {
        log.debug("Invoke read deaths time series CSV from github.");
        List<TimeSeriesDto> timeSeriesDtoList = new ArrayList<>();
        try {
            URL deaths = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv");
            timeSeriesDtoList = readCSV(deaths);
        } catch (Exception e) {
            log.warn("Occurred an exception while reading deaths csv from github {}", e.getMessage());
        }
        return timeSeriesDtoList;
    }

    private List<TimeSeriesDto> readCSV(URL url) {
        log.debug("Invoke read CSV from github.");
        List<TimeSeriesDto> timeSeriesDtoList = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
             CSVReader reader = new CSVReaderBuilder(in)
                     .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                     .withSkipLines(1)
                     .build()) {

            LocalDate startDate = LocalDate.of(2020, 01, 22);
            LocalDate date;
            String[] record;

            while ((record = reader.readNext()) != null) {
                TimeSeriesDto timeSeriesDto = new TimeSeriesDto();
                LinkedHashMap<String, Integer> mapValues = new LinkedHashMap<>();
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy", Locale.GERMAN);

                date = startDate;
                timeSeriesDto.setProvince(record[0]);
                timeSeriesDto.setCountry(record[1]);
                for (int i = 4; i < 111; i++) {
                    String formattedDate = date.format(outputFormatter);
                    mapValues.put(formattedDate, Integer.parseInt(record[i]));
                    date = date.plusDays(1);
                }
                timeSeriesDto.setDataMap(mapValues);
                timeSeriesDtoList.add(timeSeriesDto);
            }
        } catch (Exception e) {
            log.warn("Occurred an exception while reading csv from github {}", e.getMessage());
        }
        return timeSeriesDtoList;
    }
}