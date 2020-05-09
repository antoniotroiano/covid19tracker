package com.valtech.statistics.service.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvValidationException;
import com.valtech.statistics.model.ConfirmedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
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

    public List<ConfirmedDto> readCSV() throws IOException {
        List<ConfirmedDto> confirmedDtoList = new ArrayList<>();

        URL csvURL = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv");
        BufferedReader in = new BufferedReader(new InputStreamReader(csvURL.openStream()));

        try (CSVReader reader = new CSVReaderBuilder(in)
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                .withSkipLines(1)
                .build()) {

            LocalDate startDate = LocalDate.of(2020, 01, 22);
            LocalDate date;
            String[] record;

            while ((record = reader.readNext()) != null) {
                ConfirmedDto confirmedDto = new ConfirmedDto();
                LinkedHashMap<String, String> mapValues = new LinkedHashMap<>();
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy", Locale.GERMAN);

                date = startDate;
                confirmedDto.setProvince(record[0]);
                confirmedDto.setCountry(record[1]);
                for (int i = 4; i < 111; i++) {
                    String formattedDate = date.format(outputFormatter);
                    mapValues.put(formattedDate, record[i]);
                    date = date.plusDays(1);
                }
                confirmedDto.setConfirmed(mapValues);
                confirmedDtoList.add(confirmedDto);
            }
        } catch (CsvValidationException e) {
            log.warn(e.getMessage());
        }
        return confirmedDtoList;
    }
}