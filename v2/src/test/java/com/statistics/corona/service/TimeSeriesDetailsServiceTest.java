package com.statistics.corona.service;

import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.DailyReportUsDto;
import com.statistics.corona.service.csv.ReadDailyReportsCSV;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("TimeSeriesDetailsService tests")
public class TimeSeriesDetailsServiceTest {

    private final List<DailyReportDto> dailyReportDtoList = new ArrayList<>();
    private final List<DailyReportUsDto> dailyReportUsDtoList = new ArrayList<>();

    @Mock
    private ReadDailyReportsCSV readDailyReportsCSV;

    @InjectMocks
    private TimeSeriesDetailsService timeSeriesDetailsService;

    @BeforeEach
    public void setUp() {
        DailyReportDto dailyReportDto = new DailyReportDto();
        dailyReportDto.setProvince("Hessen");
        dailyReportDto.setCountry("Germany");
        dailyReportDto.setLastUpdate("2020-05-24 02:32:43");
        dailyReportDto.setConfirmed(100);
        dailyReportDto.setDeaths(100);
        dailyReportDto.setRecovered(100);
        dailyReportDto.setActive(100);
        dailyReportDto.setCombinedKey("Hessen, Germany");

        DailyReportUsDto dailyReportUsDto = new DailyReportUsDto();
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

        dailyReportDtoList.add(dailyReportDto);
        dailyReportUsDtoList.add(dailyReportUsDto);
    }

    @Test
    @DisplayName("Test get all values of province of selected country by CSV")
    public void getAllDetailsProvince() {

        assertThat(timeSeriesDetailsService.getAllDetailsProvince("Germany")).isNotEmpty();
        assertThat(timeSeriesDetailsService.getAllDailyProvinceUs()).isNotEmpty();
        assertThat(timeSeriesDetailsService.getAllCountries()).isNotEmpty();
    }
}