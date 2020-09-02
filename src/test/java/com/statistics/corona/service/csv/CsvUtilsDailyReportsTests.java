package com.statistics.corona.service.csv;

import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.DailyReportUsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("ReadDailyReportsCSV tests")
public class CsvUtilsDailyReportsTests {

    private final CsvUtilsDailyReports csvUtilsDailyReports = new CsvUtilsDailyReports();

    @Test
    @DisplayName("Read the daily report CSV and returned list with daily reports DTOs")
    public void readDailyReportsCSV() {
        List<DailyReportDto> dailyReportDtoList = csvUtilsDailyReports.readDailyReportsCSV();
        assertThat(dailyReportDtoList).isNotEmpty();

        List<DailyReportUsDto> dailyReportUsDtoList = csvUtilsDailyReports.readDailyReportUsCSV();
        assertThat(dailyReportUsDtoList).isNotEmpty();
    }

    @Test
    @DisplayName("Check URL")
    public void checkURL() throws IOException {
        URL url = csvUtilsDailyReports.getURL();
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        int statusCode = huc.getResponseCode();

        assertThat(statusCode).isEqualTo(404);
    }
}