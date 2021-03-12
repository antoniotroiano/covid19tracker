//package com.statistics.corona.service.csv;
//
//import com.statistics.corona.model.TimeSeriesDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@DisplayName("Read time series CSV tests")
//public class CsvUtilsTimeSeriesTest {
//
//    private final CsvUtilsTimeSeries csvUtilsTimeSeries = new CsvUtilsTimeSeries();
//
//    @Test
//    @DisplayName("Read all three time series csv")
//    public void readTimeSeriesCSV() {
//        List<TimeSeriesDto> timeSeriesDtoListConfirmed = csvUtilsTimeSeries.readConfirmedCsv();
//        assertThat(timeSeriesDtoListConfirmed).isNotEmpty();
//
//        List<TimeSeriesDto> timeSeriesDtoListRecovery = csvUtilsTimeSeries.readRecoveredCsv();
//        assertThat(timeSeriesDtoListRecovery).isNotEmpty();
//
//        List<TimeSeriesDto> timeSeriesDtoListDeaths = csvUtilsTimeSeries.readDeathsCsv();
//        assertThat(timeSeriesDtoListDeaths).isNotEmpty();
//    }
//}