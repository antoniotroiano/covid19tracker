package com.statistics.corona.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("TimeSeriesService tests")
public class TimeSeriesServiceTest {

    private final TimeSeriesService timeSeriesService = new TimeSeriesService();

    @Test
    @DisplayName("Get all values of selected country")
    public void getAllValuesCountry() {
        assertThat(timeSeriesService.getValuesSelectedCountry("Germany")).isNotEmpty();
    }

    @Test
    @DisplayName("Create a final result of selected country")
    public void getFinalResult() {
        assertThat(timeSeriesService.mapFinalResultToMap(timeSeriesService.getValuesSelectedCountry("Germany"))).isNotEmpty();
    }

    @Test
    @DisplayName("Get values of one day")
    public void getOneDayValues() {
        List<Integer> values = Arrays.asList(1, 3, 5, 7, 8, 19, 3, 9);
        assertThat(timeSeriesService.getOneDayValues(values)).isNotEmpty();
        assertThat(timeSeriesService.getOneDayValues(values)).contains(2);
    }

    @Test
    @DisplayName("Get last value of the list")
    public void getLastValues() {
        List<Integer> values = Arrays.asList(1, 3, 5, 7, 8, 19, 3, 9);
        assertThat(timeSeriesService.getLastValues(values)).isEqualTo(9);
    }

    @Test
    @DisplayName("Get details for a selected country")
    public void getDetailsForCountry() {
        assertThat(timeSeriesService.getDetailsForCountry("Germany")).isNotNull();
    }

    @Test
    @DisplayName("Get all countries of csv")
    public void getCountry() {
        assertThat(timeSeriesService.getCountry()).isNotEmpty();
        assertThat(timeSeriesService.getCountry()).contains("France");
    }

    @Test
    @DisplayName("Get every second value of the list")
    public void getEverySecondValue() {
        List<Integer> values = Arrays.asList(1, 3, 5, 7, 8, 19, 3, 9);
        assertThat(timeSeriesService.getEverySecondValue(values)).isNotEmpty();
    }

    @Test
    @DisplayName("Get every second date of the list")
    public void getEverySecondDate() {
        List<String> values = Arrays.asList("0000.00.00", "0000.00.00", "0000.00.00", "0000.00.00", "0000.00.00");
        assertThat(timeSeriesService.getEverySecondDate(values)).isNotEmpty();
    }

    @Test
    @DisplayName("Get all values of world")
    public void getAllValuesWorld() {
        assertThat(timeSeriesService.getAllValuesWorld()).isNotEmpty();
    }
}