package com.statistics.corona.service.v2.csv;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.statistics.corona.model.v2.DailyReportDto;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.statistics.corona.model.v2.DailyReportUsDto;
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

    private Object DailyReportUsDto;

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

    public List<DailyReportUsDto> readDailyReportUs() {
        log.debug("Invoke read daily CSV UD from github.");
        List<DailyReportUsDto> dailyReportUsDtoList = new ArrayList<>();

        URL url = null;
        try {
            LocalDate yesterdayDate = LocalDate.now().minusDays(1);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            url = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports_us/" + yesterdayDate.format(dtf) + ".csv");

            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int statusCode = huc.getResponseCode();
            if (statusCode != 200) {
                LocalDate beforeYesterday = LocalDate.now().minusDays(2);
                url = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports_us/" + beforeYesterday.format(dtf) + ".csv");
                log.warn("No new csv US. Returned last one {}", beforeYesterday);
            }
            log.debug("Returned url with daily reports od yesterday {}", yesterdayDate);
        } catch (Exception e) {
            log.warn("Occurred an exception during getting csv US url {}", e.getMessage());
        }

        assert url != null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
             CSVReader reader = new CSVReaderBuilder(in)
                     .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_QUOTES)
                     .withSkipLines(1)
                     .build()) {

            CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                    .withType(DailyReportUsDto.class)
                    .build();

            List<DailyReportUsDto> users = csvToBean.parse();

            for (DailyReportUsDto dailyReportUsDto2 : users) {
                DailyReportUsDto dailyReportUsDto = new DailyReportUsDto();
                dailyReportUsDto.setProvince(dailyReportUsDto2.getProvince());
                dailyReportUsDto.setCountry(dailyReportUsDto2.getCountry());
                dailyReportUsDto.setLastUpdate(dailyReportUsDto2.getLastUpdate());
                dailyReportUsDto.setConfirmed(dailyReportUsDto2.getConfirmed());
                dailyReportUsDto.setRecovered(dailyReportUsDto2.getRecovered());
                dailyReportUsDto.setDeaths(dailyReportUsDto2.getDeaths());
                dailyReportUsDto.setActive(dailyReportUsDto2.getActive());
                dailyReportUsDto.setIncidentRate(dailyReportUsDto2.getIncidentRate());
                dailyReportUsDto.setPeopleTested(dailyReportUsDto2.getPeopleTested());
                dailyReportUsDto.setPeopleHospitalized(dailyReportUsDto2.getPeopleHospitalized());
                dailyReportUsDto.setMortalityRate(dailyReportUsDto2.getMortalityRate());
                dailyReportUsDto.setTestingRate(dailyReportUsDto2.getTestingRate());
                dailyReportUsDto.setHospitalizationRate(dailyReportUsDto2.getHospitalizationRate());
                dailyReportUsDtoList.add(dailyReportUsDto);
             }
            log.debug("Add all daily reports US to list");
        } catch (Exception e) {
            log.warn("Occurred an exception while reading csv US from github {}", e.getMessage());
        }
        return dailyReportUsDtoList;
    }
}