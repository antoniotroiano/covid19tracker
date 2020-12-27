//package com.statistics.corona.controller;
//
//import com.statistics.corona.model.DailyReportDto;
//import com.statistics.corona.model.TimeSeriesWorldDto;
//import com.statistics.corona.service.TimeSeriesCountryService;
//import com.statistics.corona.service.DateFormat;
//import com.statistics.corona.service.DailyReportService;
//import com.statistics.corona.service.TimeSeriesWorldService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@DisplayName("TimeSeriesWorldController tests")
//public class TimeSeriesWorldControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private TimeSeriesCountryService timeSeriesCountryService;
//
//    @MockBean
//    private TimeSeriesWorldService timeSeriesWorldService;
//
//    @MockBean
//    private DailyReportService dailyReportService;
//
//    @MockBean
//    private DateFormat dateFormat;
//
//    @BeforeEach
//    public void setUp() {
//        List<String> countries = Arrays.asList("Germany", "France");
//
//        TimeSeriesWorldDto timeSeriesWorldDto = new TimeSeriesWorldDto();
//
//        timeSeriesWorldDto.setConfirmed(100);
//        timeSeriesWorldDto.setNew_confirmed(1);
//        timeSeriesWorldDto.setRecovered(100);
//        timeSeriesWorldDto.setNew_recovered(1);
//        timeSeriesWorldDto.setDeaths(100);
//        timeSeriesWorldDto.setNew_deaths(1);
//        timeSeriesWorldDto.setActive(100);
//        timeSeriesWorldDto.setIs_in_progress(false);
//        timeSeriesWorldDto.setDate("0000.00.00");
//        timeSeriesWorldDto.setUpdated_at("0000.00.00");
//
//        List<TimeSeriesWorldDto> timeSeriesWorldDtoList = new ArrayList<>();
//        timeSeriesWorldDtoList.add(timeSeriesWorldDto);
//
//        List<Integer> valuesOfWorld = Arrays.asList(1, 4, 5, 67, 23, 10);
//        List<String> dates = Arrays.asList("0000.00.00, 0000.00.00, 0000.00.00, 0000.00.00, 0000.00.00,");
//
//        DailyReportDto dailyReportDto = new DailyReportDto();
//        dailyReportDto.setProvince("Hessen");
//        dailyReportDto.setCountry("Germany");
//        dailyReportDto.setLastUpdate("2020-05-24 02:32:43");
//        dailyReportDto.setConfirmed(100);
//        dailyReportDto.setDeaths(100);
//        dailyReportDto.setRecovered(100);
//        dailyReportDto.setActive(100);
//        dailyReportDto.setCombinedKey("Hessen, Germany");
//
//        List<DailyReportDto> dailyReportDtoList = new ArrayList<>();
//        dailyReportDtoList.add(dailyReportDto);
//
//        when(timeSeriesCountryService.getCountryNames()).thenReturn(countries);
//        when(timeSeriesWorldService.getAllValuesWorld()).thenReturn(timeSeriesWorldDtoList);
//        when(dateFormat.formatLastUpdateToDate(anyString())).thenReturn("0000.00.00");
//        when(dateFormat.formatLastUpdateToTime(anyString())).thenReturn("00:00");
//        when(timeSeriesCountryService.getEverySecondValue(anyList())).thenReturn(valuesOfWorld);
//        when(timeSeriesCountryService.getEverySecondDate(anyList())).thenReturn(dates);
//        //when(dailyReportService.getAllDailyCountryValuesCalculated()).thenReturn(dailyReportDtoList);
//    }
//
//    @Test
//    @DisplayName("Show time series page of world")
//    public void showTimeSeriesWorld() throws Exception {
//        mockMvc.perform(get("/"))
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    @DisplayName("Show time series page of world with empty list of country names")
//    public void showTimeSeriesWorld_withEmptyListCountryName() throws Exception {
//        when(timeSeriesCountryService.getCountryNames()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/"))
//                .andExpect(status().isOk());
//    }
//
//    /*@Test
//    @DisplayName("Show time series page of world with empty list of world values")
//    public void showTimeSeriesWorld_withEmptyListWorldValues() throws Exception {
//        when(timeSeriesService.getAllValuesWorld()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/"))
//                .andExpect(status().isOk());
//    }*/
//
///*    @Test
//    @DisplayName("Show time series page of world with empty list of all country values")
//    public void showTimeSeriesWorld_withEmptyListOfAllCountryValues() throws Exception {
//        when(dailyReportService.getAllDailyCountryValuesCalculated()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/"))
//                .andExpect(status().isOk());
//    }*/
//}