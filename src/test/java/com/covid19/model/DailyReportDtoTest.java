/*
package com.statistics.corona.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("DailyReportDto tests")
public class DailyReportDtoTest {

    private final DailyReportDto dailyReportDto = new DailyReportDto();

    @BeforeEach
    public void setUp() {
        dailyReportDto.setProvince("Hessen");
        dailyReportDto.setCountry("Germany");
        dailyReportDto.setLastUpdate("0000.00.00");
        dailyReportDto.setConfirmed(100);
        dailyReportDto.setDeaths(100);
        dailyReportDto.setRecovered(100);
        dailyReportDto.setActive(100);
        dailyReportDto.setCombinedKey("HESS, Germany");
        dailyReportDto.setIncidenceRate(1.0);
        dailyReportDto.setCaseFatalityRatio(10.0);
    }

    @Test
    @DisplayName("Test getter and setter of dailyReportDto")
    public void getterSetter() {
        DailyReportDto dailyReportDtoMapping= new DailyReportDto(dailyReportDto);
        assertThat(dailyReportDtoMapping.getProvince()).isEqualTo("Hessen");
        assertThat(dailyReportDtoMapping.getCountry()).isEqualTo("Germany");
        assertThat(dailyReportDtoMapping.getLastUpdate()).isEqualTo("0000.00.00");
        assertThat(dailyReportDtoMapping.getConfirmed()).isEqualTo(100);
        assertThat(dailyReportDtoMapping.getDeaths()).isEqualTo(100);
        assertThat(dailyReportDtoMapping.getRecovered()).isEqualTo(100);
        assertThat(dailyReportDtoMapping.getActive()).isEqualTo(100);
        assertThat(dailyReportDtoMapping.getCombinedKey()).isEqualTo("HESS, Germany");
        assertThat(dailyReportDtoMapping.getIncidenceRate()).isEqualTo(1.0);
        assertThat(dailyReportDtoMapping.getCaseFatalityRatio()).isEqualTo(10.0);
    }

    @Test
    @DisplayName("Test toString method of dailyReportDto")
    public void toStringTest() {
        assertThat(dailyReportDto.toString())
                .isEqualTo("DailyReportDto{province='Hessen', " +
                        "country='Germany', " +
                        "lastUpdate='0000.00.00', " +
                        "confirmed=100, " +
                        "deaths=100, " +
                        "recovered=100, " +
                        "active=100, " +
                        "combinedKey='HESS, Germany', " +
                        "incidenceRate=1.0, " +
                        "caseFatalityRatio=10.0}");
    }
}*/
