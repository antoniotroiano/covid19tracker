package com.statistics.corona.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("TimeSeriesWorldDto tests")
public class TimeSeriesWorldDtoTest {

    private final TimeSeriesWorldDto timeSeriesWorldDto = new TimeSeriesWorldDto();

    @BeforeEach
    public void setUp() {
        timeSeriesWorldDto.setLastUpdate("0000.00.00");
        timeSeriesWorldDto.setDate("0000.00.00");
        timeSeriesWorldDto.setConfirmed(100);
        timeSeriesWorldDto.setRecovered(100);
        timeSeriesWorldDto.setDeaths(100);
        timeSeriesWorldDto.setActive(100);
        timeSeriesWorldDto.setNewConfirmed(100);
        timeSeriesWorldDto.setNewRecovered(100);
        timeSeriesWorldDto.setNewDeaths(100);
        timeSeriesWorldDto.setInProgress(true);
    }

    @Test
    @DisplayName("Test getter and setter of timeSeriesWorldDto")
    public void getterSetter() {
        assertThat(timeSeriesWorldDto.getLastUpdate()).isEqualTo("0000.00.00");
        assertThat(timeSeriesWorldDto.getDate()).isEqualTo("0000.00.00");
        assertThat(timeSeriesWorldDto.getConfirmed()).isEqualTo(100);
        assertThat(timeSeriesWorldDto.getRecovered()).isEqualTo(100);
        assertThat(timeSeriesWorldDto.getDeaths()).isEqualTo(100);
        assertThat(timeSeriesWorldDto.getActive()).isEqualTo(100);
        assertThat(timeSeriesWorldDto.getNewConfirmed()).isEqualTo(100);
        assertThat(timeSeriesWorldDto.getNewRecovered()).isEqualTo(100);
        assertThat(timeSeriesWorldDto.getNewDeaths()).isEqualTo(100);
        assertThat(timeSeriesWorldDto.isInProgress()).isTrue();
    }

    @Test
    @DisplayName("Test toString method of timeSeriesWorldDto")
    public void toStringTest() {
        assertThat(timeSeriesWorldDto.toString())
                .isEqualTo("WorldTimeSeriesDto{lastUpdate='0000.00.00', " +
                        "date='0000.00.00', " +
                        "confirmed=100, " +
                        "recovered=100, " +
                        "deaths=100, " +
                        "active=100, " +
                        "newConfirmed=100, " +
                        "newRecovered=100, " +
                        "newDeaths=100, " +
                        "inProgress=true}");
    }
}
