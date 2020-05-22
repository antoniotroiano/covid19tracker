package com.statistics.corona.service.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.DailyReportUsDto;
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

            CsvToBean<DailyReportDto> csvToBean = new CsvToBeanBuilder<DailyReportDto>(reader)
                    .withType(DailyReportDto.class)
                    .build();

            List<DailyReportDto> dailyReportDtoListCSV = csvToBean.parse();

            for (DailyReportDto dailyReportDtoIterate : dailyReportDtoListCSV) {
                DailyReportDto dailyReportDto = new DailyReportDto();
                dailyReportDto.setProvince(dailyReportDtoIterate.getProvince());
                dailyReportDto.setCountry(dailyReportDtoIterate.getCountry());
                dailyReportDto.setLastUpdate(dailyReportDtoIterate.getLastUpdate());
                dailyReportDto.setConfirmed(dailyReportDtoIterate.getConfirmed());
                dailyReportDto.setRecovered(dailyReportDtoIterate.getRecovered());
                dailyReportDto.setDeaths(dailyReportDtoIterate.getDeaths());
                dailyReportDto.setActive(dailyReportDtoIterate.getActive());
                dailyReportDto.setCombinedKey(dailyReportDtoIterate.getCombinedKey());
                dailyReportDtoList.add(dailyReportDto);
            }
            log.debug("Add all daily reports to list");
        } catch (Exception e) {
            log.warn("Occurred an exception while reading csv from github {}", e.getMessage());
        }
        return dailyReportDtoList;
    }

    public List<DailyReportUsDto> readDailyReportUs() {
        log.debug("Invoke read daily CSV US from github.");
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

            CsvToBean<DailyReportUsDto> csvToBean = new CsvToBeanBuilder<DailyReportUsDto>(reader)
                    .withType(DailyReportUsDto.class)
                    .build();

            List<DailyReportUsDto> dailyReportUsDtoListCSV = csvToBean.parse();

            for (DailyReportUsDto dailyReportUsDtoIterate : dailyReportUsDtoListCSV) {
                DailyReportUsDto dailyReportUsDto = new DailyReportUsDto();
                dailyReportUsDto.setProvince(dailyReportUsDtoIterate.getProvince());
                dailyReportUsDto.setCountry(dailyReportUsDtoIterate.getCountry());
                dailyReportUsDto.setLastUpdate(dailyReportUsDtoIterate.getLastUpdate());
                dailyReportUsDto.setConfirmed(dailyReportUsDtoIterate.getConfirmed());
                dailyReportUsDto.setRecovered(dailyReportUsDtoIterate.getRecovered());
                dailyReportUsDto.setDeaths(dailyReportUsDtoIterate.getDeaths());
                dailyReportUsDto.setActive(dailyReportUsDtoIterate.getActive());
                dailyReportUsDto.setIncidentRate(dailyReportUsDtoIterate.getIncidentRate());
                dailyReportUsDto.setPeopleTested(dailyReportUsDtoIterate.getPeopleTested());
                dailyReportUsDto.setPeopleHospitalized(dailyReportUsDtoIterate.getPeopleHospitalized());
                dailyReportUsDto.setMortalityRate(dailyReportUsDtoIterate.getMortalityRate());
                dailyReportUsDto.setTestingRate(dailyReportUsDtoIterate.getTestingRate());
                dailyReportUsDto.setHospitalizationRate(dailyReportUsDtoIterate.getHospitalizationRate());
                dailyReportUsDtoList.add(dailyReportUsDto);
            }
            log.debug("Add all daily reports US to list");
        } catch (Exception e) {
            log.warn("Occurred an exception while reading csv US from github {}", e.getMessage());
        }
        return dailyReportUsDtoList;
    }
}