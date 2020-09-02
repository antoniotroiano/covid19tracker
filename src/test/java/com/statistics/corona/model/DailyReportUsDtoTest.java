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
        dailyReportUsDto.setRecovered(100.0);
        dailyReportUsDto.setActive(0.1);
        dailyReportUsDto.setIncidentRate(0.1);
        dailyReportUsDto.setPeopleTested(100.0);
        dailyReportUsDto.setPeopleHospitalized(100.0);
        dailyReportUsDto.setMortalityRate(0.1);
        dailyReportUsDto.setTestingRate(0.1);
        dailyReportUsDto.setHospitalizationRate(0.1);
    }

    @Test
    @DisplayName("Test getter and setter of dailyReportUsDto")
    public void getterSetter() {
        DailyReportUsDto dailyReportUsDtoMapping = new DailyReportUsDto(dailyReportUsDto);
        assertThat(dailyReportUsDtoMapping.getProvince()).isEqualTo("New York");
        assertThat(dailyReportUsDtoMapping.getCountry()).isEqualTo("US");
        assertThat(dailyReportUsDtoMapping.getLastUpdate()).isEqualTo("0000.00.00");
        assertThat(dailyReportUsDtoMapping.getConfirmed()).isEqualTo(100);
        assertThat(dailyReportUsDtoMapping.getDeaths()).isEqualTo(100);
        assertThat(dailyReportUsDtoMapping.getRecovered()).isEqualTo(100);
        assertThat(dailyReportUsDtoMapping.getActive()).isEqualTo(0.1);
        assertThat(dailyReportUsDtoMapping.getIncidentRate()).isEqualTo(0.1);
        assertThat(dailyReportUsDtoMapping.getPeopleTested()).isEqualTo(100);
        assertThat(dailyReportUsDtoMapping.getPeopleHospitalized()).isEqualTo(100);
        assertThat(dailyReportUsDtoMapping.getMortalityRate()).isEqualTo(0.1);
        assertThat(dailyReportUsDtoMapping.getTestingRate()).isEqualTo(0.1);
        assertThat(dailyReportUsDtoMapping.getHospitalizationRate()).isEqualTo(0.1);
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
