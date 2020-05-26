package com.statistics.corona.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("DailyReportUsDto tests")
public class DailyReportUsDtoTest {

    private final DailyReportUsDto dailyReportUsDto = new DailyReportUsDto();

    @BeforeEach
    public void setUp() {
        dailyReportUsDto.setProvince("New York");
        dailyReportUsDto.setCountry("US");
        dailyReportUsDto.setLastUpdate("0000.00.00");
        dailyReportUsDto.setConfirmed(100);
        dailyReportUsDto.setDeaths(100);
        dailyReportUsDto.setRecovered(100);
        dailyReportUsDto.setActive(0.1);
        dailyReportUsDto.setIncidentRate(0.1);
        dailyReportUsDto.setPeopleTested(100);
        dailyReportUsDto.setPeopleHospitalized(100);
        dailyReportUsDto.setMortalityRate(0.1);
        dailyReportUsDto.setTestingRate(0.1);
        dailyReportUsDto.setHospitalizationRate(0.1);
    }

    @Test
    @DisplayName("Test getter and setter of dailyReportUsDto")
    public void getterSetter() {
        assertThat(dailyReportUsDto.getProvince()).isEqualTo("New York");
        assertThat(dailyReportUsDto.getCountry()).isEqualTo("US");
        assertThat(dailyReportUsDto.getLastUpdate()).isEqualTo("0000.00.00");
        assertThat(dailyReportUsDto.getConfirmed()).isEqualTo(100);
        assertThat(dailyReportUsDto.getDeaths()).isEqualTo(100);
        assertThat(dailyReportUsDto.getRecovered()).isEqualTo(100);
        assertThat(dailyReportUsDto.getActive()).isEqualTo(0.1);
        assertThat(dailyReportUsDto.getIncidentRate()).isEqualTo(0.1);
        assertThat(dailyReportUsDto.getPeopleTested()).isEqualTo(100);
        assertThat(dailyReportUsDto.getPeopleHospitalized()).isEqualTo(100);
        assertThat(dailyReportUsDto.getMortalityRate()).isEqualTo(0.1);
        assertThat(dailyReportUsDto.getTestingRate()).isEqualTo(0.1);
        assertThat(dailyReportUsDto.getHospitalizationRate()).isEqualTo(0.1);
    }

    @Test
    @DisplayName("Test toString method of dailyReportDto")
    public void toStringTest() {
        assertThat(dailyReportUsDto.toString())
                .isEqualTo("DailyReportUsDto{province='New York', " +
                        "country='US', " +
                        "lastUpdate='0000.00.00', " +
                        "confirmed=100, " +
                        "deaths=100, " +
                        "recovered=100, " +
                        "active=0.1, " +
                        "incidentRate=0.1, " +
                        "peopleTested=100, " +
                        "peopleHospitalized=100, " +
                        "mortalityRate=0.1, " +
                        "testingRate=0.1, " +
                        "hospitalizationRate=0.1}");
    }
}
