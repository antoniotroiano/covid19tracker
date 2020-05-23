package com.statistics.corona.service.csv;

import com.statistics.corona.model.TimeSeriesDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReadTimeSeriesCSVTest {

    private final ReadTimeSeriesCSV readTimeSeriesCSV = new ReadTimeSeriesCSV();

    @Test
    @DisplayName("Read all conifirmed of time series CSV")
    public void readConfirmed() {
        List<TimeSeriesDto> timeSeriesDtoListConfirmed = readTimeSeriesCSV.readConfirmedCsv();
        List<TimeSeriesDto> timeSeriesDtoListRecovered = readTimeSeriesCSV.readRecoveredCsv();
        List<TimeSeriesDto> timeSeriesDtoListDeaths = readTimeSeriesCSV.readDeathsCsv();

        assertThat(timeSeriesDtoListConfirmed).isNotEmpty();
        assertThat(timeSeriesDtoListRecovered).isNotEmpty();
        assertThat(timeSeriesDtoListDeaths).isNotEmpty();
    }

    @Test
    @DisplayName("Read all country names")
    public void readCountryName() {
        List<String> countryList = readTimeSeriesCSV.readCountryName();

        assertThat(countryList).isNotEmpty();
        assertThat(countryList).contains("Germany");
    }
}