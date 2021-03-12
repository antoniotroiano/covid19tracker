//package com.statistics.corona.model;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@DisplayName("TimeSeriesWorldDto tests")
//public class TimeSeriesWorldDtoTest {
//
//    private final TimeSeriesWorldDto timeSeriesWorldDto = new TimeSeriesWorldDto();
//
//    @BeforeEach
//    public void setUp() {
//        timeSeriesWorldDto.setUpdated_at("0000.00.00");
//        timeSeriesWorldDto.setDate("0000.00.00");
//        timeSeriesWorldDto.setConfirmed(100);
//        timeSeriesWorldDto.setRecovered(100);
//        timeSeriesWorldDto.setDeaths(100);
//        timeSeriesWorldDto.setActive(100);
//        timeSeriesWorldDto.setNew_confirmed(100);
//        timeSeriesWorldDto.setNew_recovered(100);
//        timeSeriesWorldDto.setNew_deaths(100);
//        timeSeriesWorldDto.setIs_in_progress(true);
//    }
//
//    @Test
//    @DisplayName("Test getter and setter of timeSeriesWorldDto")
//    public void getterSetter() {
//        assertThat(timeSeriesWorldDto.getUpdated_at()).isEqualTo("0000.00.00");
//        assertThat(timeSeriesWorldDto.getDate()).isEqualTo("0000.00.00");
//        assertThat(timeSeriesWorldDto.getConfirmed()).isEqualTo(100);
//        assertThat(timeSeriesWorldDto.getRecovered()).isEqualTo(100);
//        assertThat(timeSeriesWorldDto.getDeaths()).isEqualTo(100);
//        assertThat(timeSeriesWorldDto.getActive()).isEqualTo(100);
//        assertThat(timeSeriesWorldDto.getNew_confirmed()).isEqualTo(100);
//        assertThat(timeSeriesWorldDto.getNew_recovered()).isEqualTo(100);
//        assertThat(timeSeriesWorldDto.getNew_deaths()).isEqualTo(100);
//        assertThat(timeSeriesWorldDto.isIs_in_progress()).isTrue();
//    }
//
//    @Test
//    @DisplayName("Test toString method of timeSeriesWorldDto")
//    public void toStringTest() {
//        assertThat(timeSeriesWorldDto.toString())
//                .isEqualTo("WorldTimeSeriesDto{updated_at='0000.00.00', " +
//                        "date='0000.00.00', " +
//                        "confirmed=100, " +
//                        "recovered=100, " +
//                        "deaths=100, " +
//                        "active=100, " +
//                        "newConfirmed=100, " +
//                        "newRecovered=100, " +
//                        "newDeaths=100, " +
//                        "is_in_progress=true}");
//    }
//}
