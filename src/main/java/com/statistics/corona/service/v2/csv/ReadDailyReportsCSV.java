package com.statistics.corona.service.v2.csv;

import com.statistics.corona.model.v2.DailyReportDto;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReadDailyReportsCSV {

    public List<DailyReportDto> readDailyReportsCSV() {
        log.debug("Invoke read daily CSV from github.");
        List<DailyReportDto> dailyReportDtoList = new ArrayList<>();

        URL url = null;
        try {
            LocalDate yesterdayDate = LocalDate.now().minusDays(1);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            url = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/" + yesterdayDate.format(dtf) + ".csv");

            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int statusCode = huc.getResponseCode();
            if (statusCode != 200) {
                LocalDate beforeYesterday = LocalDate.now().minusDays(2);
                url = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/" + beforeYesterday.format(dtf) + ".csv");
                log.warn("No new csv. Returned last one {}", beforeYesterday);
            }
            log.debug("Returned url with daily reports od yesterday {}", yesterdayDate);
        } catch (Exception e) {
            log.warn("Occurred an exception during getting csv url {}", e.getMessage());
        }

        assert url != null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
             CSVReader reader = new CSVReaderBuilder(in)
                     .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                     .withSkipLines(1)
                     .build()) {

            String[] record;
            while ((record = reader.readNext()) != null) {
                DailyReportDto dailyReportDto = new DailyReportDto();
                dailyReportDto.setProvince(record[2]);
                dailyReportDto.setCountry(record[3]);
                dailyReportDto.setLastUpdate(record[4]);
                dailyReportDto.setConfirmed(Integer.parseInt(record[7]));
                dailyReportDto.setRecovered(Integer.parseInt(record[8]));
                dailyReportDto.setDeaths(Integer.parseInt(record[9]));
                dailyReportDto.setActive(Integer.parseInt(record[10]));
                dailyReportDto.setCombinedKey(record[11]);
                dailyReportDtoList.add(dailyReportDto);
            }
            log.debug("Add all daily reports to list");
        } catch (Exception e) {
            log.warn("Occurred an exception while reading csv from github {}", e.getMessage());
        }
        return dailyReportDtoList;
    }
}