package com.statistics.corona.controller;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.DailyReportUsDto;
import com.statistics.corona.model.TimeSeriesDto;
import com.statistics.corona.service.TimeSeriesCountryService;
import com.statistics.corona.service.DateFormat;
import com.statistics.corona.service.DailyReportService;
import com.statistics.corona.service.csv.ReadTimeSeriesCSV;
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
public class TimeSeriesCountryControllerTest {

    private final TimeSeriesDto timeSeriesDto = new TimeSeriesDto();
    private final DailyReportDto dailyReportDto = new DailyReportDto();
    private final List<DailyReportDto> dailyReportDtoList = new ArrayList<>();

    private final List<TimeSeriesDto> confirmed = new ArrayList<>();
    private final List<TimeSeriesDto> recovered = new ArrayList<>();
    private final List<TimeSeriesDto> deaths = new ArrayList<>();

    private final Map<String, List<TimeSeriesDto>> allValues = new HashMap<>();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeSeriesCountryService timeSeriesCountryService;

    @MockBean
    private DailyReportService dailyReportService;

    @MockBean
    private ReadTimeSeriesCSV readTimeSeriesCSV;

    @MockBean
    private DateFormat dateFormat;

    @BeforeEach
    public void setUp() {
        List<String> countries = Arrays.asList("Germany", "France");

        CountryDetailsDto germany = new CountryDetailsDto();
        germany.setCountry("Germany");
        //germany.setCode("DE");
        germany.setConfirmed(100);
        germany.setRecovered(100);
        germany.setDeaths(100);

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

        when(timeSeriesCountryService.getCountryNames()).thenReturn(countries);
        when(dateFormat.formatLastUpdateToDate(anyString())).thenReturn("0000.00.00");
        when(dateFormat.formatLastUpdateToTime(anyString())).thenReturn("00:00");
        when(dailyReportService.getDetailsForCountry(anyString())).thenReturn(Optional.of(germany));
        when(timeSeriesCountryService.getCountryTSValues(anyString())).thenReturn(listGermany);
        when(timeSeriesCountryService.generateFinalTSResult(anyMap())).thenReturn(finalResult);

        TimeSeriesDto timeSeriesDto1 = new TimeSeriesDto();
        timeSeriesDto1.setCountry("Germany");
        timeSeriesDto1.setProvince("Hessen");
        LinkedHashMap<String, Integer> dataMap1 = new LinkedHashMap<>();
        dataMap1.put("0000.00.00", 1);
        dataMap1.put("0000.00.01", 2);
        dataMap1.put("0000.00.02", 3);
        timeSeriesDto1.setDataMap(dataMap1);

        TimeSeriesDto timeSeriesDto2 = new TimeSeriesDto();
        timeSeriesDto2.setCountry("Germany");
        timeSeriesDto2.setProvince("Hessen");
        LinkedHashMap<String, Integer> dataMap2 = new LinkedHashMap<>();
        dataMap2.put("0000.00.00", 1);
        dataMap2.put("0000.00.01", 2);
        dataMap2.put("0000.00.02", 3);
        timeSeriesDto2.setDataMap(dataMap2);

        TimeSeriesDto timeSeriesDto3 = new TimeSeriesDto();
        timeSeriesDto3.setCountry("Germany");
        timeSeriesDto3.setProvince("Hessen");
        LinkedHashMap<String, Integer> dataMap3 = new LinkedHashMap<>();
        dataMap3.put("0000.00.00", 1);
        dataMap3.put("0000.00.01", 2);
        dataMap3.put("0000.00.02", 3);
        timeSeriesDto3.setDataMap(dataMap3);

        confirmed.add(timeSeriesDto1);
        recovered.add(timeSeriesDto2);
        deaths.add(timeSeriesDto3);

        allValues.put("confirmedList", confirmed);
        allValues.put("recoveredList", recovered);
        allValues.put("deathsList", deaths);
    }

    @Test
    @DisplayName("Empty list with all country names")
    public void showTimeSeries_withEmptyListOfAllCountryNames() throws Exception {
        when(timeSeriesCountryService.getCountryNames()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/covid19/timeSeries/country/{country}", "Germany"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show time series page of a selected country")
    public void showTimeSeries() throws Exception {
        mockMvc.perform(get("/covid19/timeSeries/country/{country}", "Germany"))
                .andExpect(status().isOk());
    }

    /*@Test
    @DisplayName("Show time series page of a selected country with empty countryDetailsDto")
    public void showTimeSeries_emptyCountryDetailsDto() throws Exception {
        when(timeSeriesService.getDetailsForCountry(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/covid19/timeSeries/country/{country}", "Germany"))
                .andExpect(status().isOk());
    }*/

    @Test
    @DisplayName("Show time series page of a selected country with empty list of all values")
    public void showTimeSeries_emptyGetValuesOfSelectedCountry() throws Exception {
        when(timeSeriesCountryService.getCountryTSValues(anyString())).thenReturn(Collections.emptyMap());

        mockMvc.perform(get("/covid19/timeSeries/country/{country}", "Germany"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show time series page of a selected country with empty map of final results")
    public void showTimeSeries_emptyMapOfFinalResults() throws Exception {
        when(timeSeriesCountryService.generateFinalTSResult(anyMap())).thenReturn(Collections.emptyMap());

        mockMvc.perform(get("/covid19/timeSeries/country/{country}", "Germany"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show time series page of a selected country with list of details of province")
    public void showTimeSeries_listOfDetailsProvince() throws Exception {
        when(dailyReportService.getDailyDetailsOfProvince(anyString())).thenReturn(dailyReportDtoList);

        mockMvc.perform(get("/covid19/timeSeries/country/{country}", "Germany"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show time series page of a selected country with empty list of details of province")
    public void showTimeSeries_emptyListOfDetailsProvince() throws Exception {
        when(dailyReportService.getDailyDetailsOfProvince(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/covid19/timeSeries/country/{country}", "Germany"))
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
        when(dailyReportService.getDailyDetailsProvinceUs()).thenReturn(dailyReportUsDtoList);

        mockMvc.perform(get("/covid19/timeSeries/country/provinces/{country}", "US"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show empty list for province of US")
    public void showEmptyListForProvinceOfUs() throws Exception {
        when(dailyReportService.getDailyDetailsProvinceUs()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/covid19/timeSeries/country/provinces/{country}", "US"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show details for province of selected country")
    public void showDetailsForProvinceOfSelectedCountry() throws Exception {
        when(dailyReportService.getDailyDetailsOfProvince(anyString())).thenReturn(dailyReportDtoList);

        mockMvc.perform(get("/covid19/timeSeries/country/provinces/{country}", "Germany"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show empty details list for province of selected country")
    public void showEmptyDetailsListForProvinceOfSelectedCountry() throws Exception {
        when(dailyReportService.getDailyDetailsOfProvince(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/covid19/timeSeries/country/provinces/{country}", "Germany"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show province details for selected province successfully")
    public void showProvinceDetails_successfully() throws Exception {
        when(dailyReportService.getProvinceDetails(anyString())).thenReturn(Optional.of(dailyReportDto));
        when(timeSeriesCountryService.getProvinceTSValues(anyString(), anyString())).thenReturn(allValues);

        mockMvc.perform(get("/covid19/timeSeries/province/{province}", "Hessen"))
                .andExpect(status().isOk());
    }
}