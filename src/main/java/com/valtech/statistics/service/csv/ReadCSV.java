package com.valtech.statistics.service.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class ReadCSV {

    public List<String[]> readCSV() throws IOException, CsvException {
        List<String[]> listAll;
        URL csvURL = new URL("https://github.com/CSSEGISandData/COVID-19/blob/master/csse_covid_19_data/csse_covid_19_daily_reports/05-06-2020.csv");
        BufferedReader in = new BufferedReader(new InputStreamReader(csvURL.openStream()));
        CSVReader reader = new CSVReader(in);
        listAll = reader.readAll();
        reader.close();
        return listAll;
    }

}
