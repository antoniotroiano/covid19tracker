package com.statistics.corona.service.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvValidationException;
import com.statistics.corona.model.dto.CountryDailyDto;
import com.statistics.corona.model.dto.CountryLatestDto;
import com.statistics.corona.model.dto.UsDailyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class CsvUtilsDailyReports {

    private static final String DATE_PATTERN = "MM-dd-yyyy";

    private static final String URL_DAILY_REPORT =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/";
    private static final String URL_DAILY_REPORT_US =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports_us/";

    public static double parseDoubleOrNull(String str) {
        return str != null ? Double.parseDouble(str) : 0;
    }

    public URL getURL() {
        try {
            LocalDate yesterdayDate = LocalDate.now().minusDays(1);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_PATTERN);
            URL url = new URL(URL_DAILY_REPORT + yesterdayDate.format(dtf) + ".csv");
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int statusCode = huc.getResponseCode();
            if (statusCode != 200) {
                LocalDate beforeYesterday = LocalDate.now().minusDays(2);
                url = new URL(URL_DAILY_REPORT + beforeYesterday.format(dtf) + ".csv");
                log.warn("No new CSV. Returned last one {}", beforeYesterday);
                return url;
            }
            log.debug("Returned url with daily reports of yesterday {}", yesterdayDate);
            return url;
        } catch (IOException e) {
            log.warn("Occurred an exception during getting CSV url {}", e.getMessage());
            return null;
        }
    }

    public List<CountryDailyDto> readDailyReportsCSV() {
        log.debug("Invoke read daily values from github");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getURL().openStream()));
             CSVReader reader = new CSVReaderBuilder(in)
                     .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_QUOTES)
                     .withSkipLines(1)
                     .build()) {
            CsvToBean<CountryDailyDto> csvToBean = new CsvToBeanBuilder<CountryDailyDto>(reader)
                    .withType(CountryDailyDto.class)
                    .build();
            List<CountryDailyDto> countryDailyDtoList = csvToBean.parse();
            log.info("Add all daily values to list");
            return countryDailyDtoList;
        } catch (IOException e) {
            log.warn("Occurred an exception while reading values from CSV {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public URL getYesterdayURL() {
        try {
            LocalDate theDayBeforeYesterday = LocalDate.now().minusDays(2);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_PATTERN);
            URL url = new URL(URL_DAILY_REPORT + theDayBeforeYesterday.format(dtf) + ".csv");
            log.debug("Returned url with daily reports of yesterday {}", theDayBeforeYesterday);
            return url;
        } catch (IOException e) {
            log.warn("Occurred an exception during getting CSV url {}", e.getMessage());
            return null;
        }
    }

    public List<CountryDailyDto> readDailyReportsYesterdayCsv() {
        log.debug("Invoke read yesterday daily values from github");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getYesterdayURL().openStream()));
             CSVReader reader = new CSVReaderBuilder(in)
                     .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_QUOTES)
                     .withSkipLines(1)
                     .build()) {
            CsvToBean<CountryDailyDto> csvToBean = new CsvToBeanBuilder<CountryDailyDto>(reader)
                    .withType(CountryDailyDto.class)
                    .build();
            List<CountryDailyDto> countryDailyDtoList = csvToBean.parse();
            log.info("Add all yesterday daily values to list");
            return countryDailyDtoList;
        } catch (IOException e) {
            log.warn("Occurred an exception while reading values from CSV {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public URL getURLUs() {
        try {
            LocalDate yesterdayDate = LocalDate.now().minusDays(1);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_PATTERN);
            URL urlUs = new URL(URL_DAILY_REPORT_US + yesterdayDate.format(dtf) + ".csv");
            HttpURLConnection huc = (HttpURLConnection) urlUs.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int statusCode = huc.getResponseCode();
            if (statusCode != 200) {
                LocalDate beforeYesterday = LocalDate.now().minusDays(2);
                urlUs = new URL(URL_DAILY_REPORT_US + beforeYesterday.format(dtf) + ".csv");
                log.warn("No new CSV US. Returned last one {}", beforeYesterday);
                return urlUs;
            }
            log.debug("Returned url with daily reports od yesterday {}", yesterdayDate);
            return urlUs;
        } catch (IOException e) {
            log.warn("Occurred an exception during getting CSV US url {}", e.getMessage());
            return null;
        }
    }

    public List<UsDailyDto> readDailyReportUsCSV() {
        log.debug("Invoke read US daily values from github.");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getURLUs().openStream()));
             CSVReader reader = new CSVReaderBuilder(in)
                     .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_QUOTES)
                     .withSkipLines(1)
                     .build()) {
            CsvToBean<UsDailyDto> csvToBean = new CsvToBeanBuilder<UsDailyDto>(reader)
                    .withType(UsDailyDto.class)
                    .build();
            List<UsDailyDto> usDailyDtoList = csvToBean.parse();
            log.info("Add all US daily alues to list");
            return usDailyDtoList;
        } catch (IOException e) {
            log.warn("Occurred an exception while reading US values from CSV {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    //ToDo: nicht sicher führt zu abstürzen mit dem parseDoubleOrNull
    public List<CountryLatestDto> readCountryLatestCSV() {
        log.debug("Invoke read latest country values from github");
        List<CountryLatestDto> countryLatestDtoList = new ArrayList<>();
        try {
            URL deaths = new URL("https://raw.githubusercontent.com/owid/covid-19-data/master/public/data/latest/owid-covid-latest.csv");
            BufferedReader in = new BufferedReader(new InputStreamReader(deaths.openStream()));
            CSVReader reader = new CSVReaderBuilder(in)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    .withSkipLines(1)
                    .build();
            String[] record;
            while ((record = reader.readNext()) != null) {
                CountryLatestDto countryLatestDto = new CountryLatestDto();
                countryLatestDto.setIso_code(record[0]);
                countryLatestDto.setContinent(record[1]);
                countryLatestDto.setLocation(record[2]);
                countryLatestDto.setTotal_cases(parseDoubleOrNull(record[4]));
                countryLatestDto.setNew_cases(parseDoubleOrNull(record[5]));
                countryLatestDto.setNew_case_smoothed(parseDoubleOrNull(record[6]));
                countryLatestDto.setTotal_deaths(parseDoubleOrNull(record[7]));
                countryLatestDto.setNew_deaths(parseDoubleOrNull(record[8]));
                countryLatestDto.setNew_deaths_smoothed(parseDoubleOrNull(record[9]));
                countryLatestDto.setTotal_cases_per_million(parseDoubleOrNull(record[10]));
                countryLatestDto.setNew_cases_per_million(parseDoubleOrNull(record[11]));
                countryLatestDto.setNew_cases_smoothed_per_million(parseDoubleOrNull(record[12]));
                countryLatestDto.setTotal_deaths_per_million(parseDoubleOrNull(record[13]));
                countryLatestDto.setNew_deaths_per_million(parseDoubleOrNull(record[14]));
                countryLatestDto.setNew_deaths_smoothed_per_million(parseDoubleOrNull(record[15]));
                countryLatestDto.setReproduction_rate(parseDoubleOrNull(record[16]));
                countryLatestDto.setTotal_tests(parseDoubleOrNull(record[26]));
                countryLatestDto.setTotal_tests_per_thousand(parseDoubleOrNull(record[27]));
                countryLatestDto.setNew_tests_per_thousand(parseDoubleOrNull(record[28]));
                countryLatestDto.setNew_tests_smoothed(parseDoubleOrNull(record[29]));
                countryLatestDto.setNew_tests_smoothed_per_thousand(parseDoubleOrNull(record[30]));
                countryLatestDto.setPositive_rate(parseDoubleOrNull(record[31]));
                countryLatestDto.setTests_per_case(parseDoubleOrNull(record[32]));
                countryLatestDto.setTotal_vaccinations(parseDoubleOrNull(record[34]));
                countryLatestDto.setTotal_vaccinations_per_hundred(parseDoubleOrNull(record[35]));
                countryLatestDto.setPopulation(parseDoubleOrNull(record[37]));
                countryLatestDto.setMedian_age(parseDoubleOrNull(record[39]));
                countryLatestDto.setAged_65_older(parseDoubleOrNull(record[40]));
                countryLatestDto.setAged_70_older(parseDoubleOrNull(record[41]));
                countryLatestDto.setHospital_beds_per_thousand(parseDoubleOrNull(record[49]));
                countryLatestDto.setLife_expectancy(parseDoubleOrNull(record[50]));
                countryLatestDto.setHuman_development_index(parseDoubleOrNull(record[51]));
                countryLatestDtoList.add(countryLatestDto);
            }
            log.debug("Add all latest country values from github to list");
            return countryLatestDtoList;
        } catch (CsvValidationException | IOException e) {
            log.warn("Occurred an exception while reading all country values from github {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}