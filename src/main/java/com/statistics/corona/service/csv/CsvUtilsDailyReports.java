package com.statistics.corona.service.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.statistics.corona.model.dto.CountryDailyDto;
import com.statistics.corona.model.dto.UsDailyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class CsvUtilsDailyReports {

    private static final String URL_DAILY_REPORT =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/";
    private static final String URL_DAILY_REPORT_US =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports_us/";

    public URL getURL() {
        URL url = null;
        try {
            LocalDate yesterdayDate = LocalDate.now().minusDays(1);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            url = new URL(URL_DAILY_REPORT + yesterdayDate.format(dtf) + ".csv");
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int statusCode = huc.getResponseCode();
            if (statusCode != 200) {
                LocalDate beforeYesterday = LocalDate.now().minusDays(2);
                url = new URL(URL_DAILY_REPORT + beforeYesterday.format(dtf) + ".csv");
                log.warn("No new csv. Returned last one {}", beforeYesterday);
                return url;
            }
            log.debug("Returned url with daily reports of yesterday {}", yesterdayDate);
            return url;
        } catch (Exception e) {
            log.warn("Occurred an exception during getting csv url {}", e.getMessage());
            return url;
        }
    }

    public List<CountryDailyDto> readDailyReportsCSV() {
        log.debug("Invoke read daily CSV from github");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getURL().openStream()));
             CSVReader reader = new CSVReaderBuilder(in)
                     .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_QUOTES)
                     .withSkipLines(1)
                     .build()) {
            CsvToBean<CountryDailyDto> csvToBean = new CsvToBeanBuilder<CountryDailyDto>(reader)
                    .withType(CountryDailyDto.class)
                    .build();
            List<CountryDailyDto> countryDailyDtoList = csvToBean.parse();
            log.info("Add all daily reports to list");
            return countryDailyDtoList;
        } catch (Exception e) {
            log.warn("Occurred an exception while reading csv from github {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public URL getYesterdayURL() {
        URL url = null;
        try {
            LocalDate theDayBeforeYesterday = LocalDate.now().minusDays(2);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            url = new URL(URL_DAILY_REPORT + theDayBeforeYesterday.format(dtf) + ".csv");
            log.debug("Returned url with daily reports of yesterday {}", theDayBeforeYesterday);
            return url;
        } catch (Exception e) {
            log.warn("Occurred an exception during getting csv url {}", e.getMessage());
            return url;
        }
    }

    public List<CountryDailyDto> readDailyReportsYesterdayCsv() {
        log.debug("Invoke read yesterday daily CSV from github");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getYesterdayURL().openStream()));
             CSVReader reader = new CSVReaderBuilder(in)
                     .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_QUOTES)
                     .withSkipLines(1)
                     .build()) {
            CsvToBean<CountryDailyDto> csvToBean = new CsvToBeanBuilder<CountryDailyDto>(reader)
                    .withType(CountryDailyDto.class)
                    .build();
            List<CountryDailyDto> countryDailyDtoList = csvToBean.parse();
            log.info("Add all yesterday daily reports to list");
            return countryDailyDtoList;
        } catch (Exception e) {
            log.warn("Occurred an exception while reading csv from github {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public URL getURLUs() {
        URL urlUs = null;
        try {
            LocalDate yesterdayDate = LocalDate.now().minusDays(1);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            urlUs = new URL(URL_DAILY_REPORT_US + yesterdayDate.format(dtf) + ".csv");
            HttpURLConnection huc = (HttpURLConnection) urlUs.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int statusCode = huc.getResponseCode();
            if (statusCode != 200) {
                LocalDate beforeYesterday = LocalDate.now().minusDays(2);
                urlUs = new URL(URL_DAILY_REPORT_US + beforeYesterday.format(dtf) + ".csv");
                log.warn("No new csv US. Returned last one {}", beforeYesterday);
                return urlUs;
            }
            log.debug("Returned url with daily reports od yesterday {}", yesterdayDate);
            return urlUs;
        } catch (Exception e) {
            log.warn("Occurred an exception during getting csv US url {}", e.getMessage());
            return urlUs;
        }
    }

    public List<UsDailyDto> readDailyReportUsCSV() {
        log.debug("Invoke read daily CSV US from github.");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getURLUs().openStream()));
             CSVReader reader = new CSVReaderBuilder(in)
                     .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_QUOTES)
                     .withSkipLines(1)
                     .build()) {
            CsvToBean<UsDailyDto> csvToBean = new CsvToBeanBuilder<UsDailyDto>(reader)
                    .withType(UsDailyDto.class)
                    .build();
            List<UsDailyDto> usDailyDtoList = csvToBean.parse();
            log.info("Add all daily reports US to list");
            return usDailyDtoList;
        } catch (Exception e) {
            log.warn("Occurred an exception while reading csv US from github {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}