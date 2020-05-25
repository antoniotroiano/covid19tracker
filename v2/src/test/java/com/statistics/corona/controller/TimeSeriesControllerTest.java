package com.statistics.corona.controller;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.DailyReportUsDto;
import com.statistics.corona.model.TimeSeriesDto;
import com.statistics.corona.service.DateFormat;
import com.statistics.corona.service.TimeSeriesDetailsService;
import com.statistics.corona.service.TimeSeriesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TimeSeriesController tests")
public class TimeSeriesControllerTest {

    private final List<DailyReportDto> dailyReportDtoList = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeSeriesService timeSeriesService;

    @MockBean
    private TimeSeriesDetailsService timeSeriesDetailsService;

    @MockBean
    private DateFormat dateFormat;

    @BeforeEach
    public void setUp() {
        List<String> countries = Arrays.asList("Germany", "France");

        CountryDetailsDto germany = new CountryDetailsDto();
        germany.setCountry("Germany");
        germany.setCode("DE");
        germany.setConfirmed(100);
        germany.setRecovered(100);
        germany.setDeaths(100);

        TimeSeriesDto timeSeriesDto = new TimeSeriesDto();
        timeSeriesDto.setProvince("Hessen");
        timeSeriesDto.setCountry("Germany");
        LinkedHashMap<String, Integer> dataMap = new LinkedHashMap<>();
        dataMap.put("0000.00.00", 1);
        timeSeriesDto.setDataMap(dataMap);

        List<TimeSeriesDto> timeSeriesDtoList = new ArrayList<>();
        timeSeriesDtoList.add(timeSeriesDto);

        Map<String, List<TimeSeriesDto>> listGermany = new HashMap<>();
        listGermany.put("confirmedList", timeSeriesDtoList);

        List<Integer> results = Arrays.asList(1, 4, 5, 67, 23, 10);
        Map<String, List<Integer>> finalResult = new HashMap<>();
        finalResult.put("result", results);

        DailyReportDto dailyReportDto = new DailyReportDto();
        dailyReportDto.setProvince("Hessen");
        dailyReportDto.setCountry("Germany");
        dailyReportDto.setLastUpdate("2020-05-24 02:32:43");
        dailyReportDto.setConfirmed(100);
        dailyReportDto.setDeaths(100);
        dailyReportDto.setRecovered(100);
        dailyReportDto.setActive(100);
        dailyReportDto.setCombinedKey("Hessen, Germany");

        DailyReportDto dailyReportDto2 = new DailyReportDto();
        dailyReportDto2.setProvince("Bayern");
        dailyReportDto2.setCountry("Germany");
        dailyReportDto2.setLastUpdate("2020-05-24 02:32:43");
        dailyReportDto2.setConfirmed(100);
        dailyReportDto2.setDeaths(100);
        dailyReportDto2.setRecovered(100);
        dailyReportDto2.setActive(100);
        dailyReportDto2.setCombinedKey("Bayern, Germany");

        dailyReportDtoList.add(dailyReportDto);
        dailyReportDtoList.add(dailyReportDto2);

        when(timeSeriesService.getCountry()).thenReturn(countries);
        when(dateFormat.formatLastUpdateToDate(anyString())).thenReturn("0000.00.00");
        when(dateFormat.formatLastUpdateToTime(anyString())).thenReturn("00:00");
        when(timeSeriesService.getDetailsForCountry(anyString())).thenReturn(Optional.of(germany));
        when(timeSeriesService.getValuesSelectedCountry(anyString())).thenReturn(listGermany);
        when(timeSeriesService.mapFinalResultToMap(anyMap())).thenReturn(finalResult);
    }

    @Test
    @DisplayName("Empty list with all country names")
    public void showTimeSeries_withEmptyListOfAllCountryNames() throws Exception {
        when(timeSeriesService.getCountry()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/v2/covid19/timeSeries/{country}", "Germany"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show time series page of a selected country")
    public void showTimeSeries() throws Exception {
        mockMvc.perform(get("/v2/covid19/timeSeries/{country}", "Germany"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show time series page of a selected country with empty countryDetailsDto")
    public void showTimeSeries_emptyCountryDetailsDto() throws Exception {
        when(timeSeriesService.getDetailsForCountry(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/v2/covid19/timeSeries/{country}", "Germany"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show time series page of a selected country with empty list of all values")
    public void showTimeSeries_emptyGetValuesOfSelectedCountry() throws Exception {
        when(timeSeriesService.getValuesSelectedCountry(anyString())).thenReturn(Collections.emptyMap());

        mockMvc.perform(get("/v2/covid19/timeSeries/{country}", "Germany"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show time series page of a selected country with empty map of final results")
    public void showTimeSeries_emptyMapOfFinalResults() throws Exception {
        when(timeSeriesService.mapFinalResultToMap(anyMap())).thenReturn(Collections.emptyMap());

        mockMvc.perform(get("/v2/covid19/timeSeries/{country}", "Germany"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show time series page of a selected country with list of details of province")
    public void showTimeSeries_listOfDetailsProvince() throws Exception {
        when(timeSeriesDetailsService.getAllDetailsProvince(anyString())).thenReturn(dailyReportDtoList);

        mockMvc.perform(get("/v2/covid19/timeSeries/{country}", "Germany"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show time series page of a selected country with empty list of details of province")
    public void showTimeSeries_emptyListOfDetailsProvince() throws Exception {
        when(timeSeriesDetailsService.getAllDetailsProvince(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/v2/covid19/timeSeries/{country}", "Germany"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show details for province of US")
    public void showDetailsForProvinceOfUs() throws Exception {
        DailyReportUsDto dailyReportUsDto = new DailyReportUsDto();
        dailyReportUsDto.setProvince("New York");
        dailyReportUsDto.setCountry("US");
        dailyReportUsDto.setLastUpdate("2020-05-24 02:32:43");
        dailyReportUsDto.setConfirmed(100);
        dailyReportUsDto.setDeaths(100);
        dailyReportUsDto.setRecovered(100);
        dailyReportUsDto.setActive(10.0);
        List<DailyReportUsDto> dailyReportUsDtoList = Stream.of(dailyReportUsDto).collect(Collectors.toList());
        when(timeSeriesDetailsService.getAllDailyProvinceUs()).thenReturn(dailyReportUsDtoList);

        mockMvc.perform(get("/v2/covid19/timeSeries/{country}/details", "US"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show empty list for province of US")
    public void showEmptyListForProvinceOfUs() throws Exception {
        when(timeSeriesDetailsService.getAllDailyProvinceUs()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/v2/covid19/timeSeries/{country}/details", "US"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show details for province of selected country")
    public void showDetailsForProvinceOfSelectedCountry() throws Exception {
        when(timeSeriesDetailsService.getAllDetailsProvince(anyString())).thenReturn(dailyReportDtoList);

        mockMvc.perform(get("/v2/covid19/timeSeries/{country}/details", "Germany"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show empty details list for province of selected country")
    public void showEmptyDetailsListForProvinceOfSelectedCountry() throws Exception {
        when(timeSeriesDetailsService.getAllDetailsProvince(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/v2/covid19/timeSeries/{country}/details", "Germany"))
                .andExpect(status().isOk());
    }
}