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
public class ReadDailyReportsCSVTests {

    private final ReadDailyReportsCSV readDailyReportsCSV = new ReadDailyReportsCSV();

    @Test
    @DisplayName("Read the daily report CSV and returned list with daily reports DTOs")
    public void readDailyReportsCSV() {
        List<DailyReportDto> dailyReportDtoList = readDailyReportsCSV.readDailyReportsCSV();
        List<DailyReportUsDto> dailyReportUsDtoList = readDailyReportsCSV.readDailyReportUs();

        assertThat(dailyReportDtoList).isNotEmpty();
        assertThat(dailyReportUsDtoList).isNotEmpty();
        assertThat(dailyReportUsDtoList.get(50).getProvince()).isEqualTo("Utah");
        assertThat(dailyReportDtoList.get(3280).getCountry()).isEqualTo("Fiji");

        /*ReadDailyReportsCSV readDailyReportsCSV = mock(ReadDailyReportsCSV.class);
        when(readDailyReportsCSV.readDailyReportsCSV()).thenReturn(dailyReportDtoList);
        assertThat(readDailyReportsCSV.readDailyReportsCSV()).isEqualTo(dailyReportDtoList);*/
    }

    @Test
    @DisplayName("Check URL")
    public void checkURL() throws IOException {
        URL url = readDailyReportsCSV.getURL("");
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        int statusCode = huc.getResponseCode();

        assertThat(statusCode).isEqualTo(200);
    }
}