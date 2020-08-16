package com.statistics.corona.service;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.TimeSeriesDto;
import com.statistics.corona.model.TimeSeriesWorldDto;
import com.statistics.corona.service.csv.ReadTimeSeriesCSV;
import com.statistics.corona.service.json.ReadJSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("TimeSeriesService tests")
public class TimeSeriesWorldServiceTest {

    private final List<TimeSeriesDto> confirmed = new ArrayList<>();
    private final List<TimeSeriesDto> recovered = new ArrayList<>();
    private final List<TimeSeriesDto> deaths = new ArrayList<>();
    private final Map<String, List<TimeSeriesDto>> allValues = new HashMap<>();
    private final List<Integer> values = Arrays.asList(1, 3, 5, 7, 8, 19, 3, 9);

    @Mock
    private ReadTimeSeriesCSV readTimeSeriesCSV;

    @Mock
    private ReadJSON readJSON;

    @InjectMocks
    private TimeSeriesCountryService timeSeriesCountryService;

    @InjectMocks
    private TimeSeriesWorldService timeSeriesWorldService;

    @InjectMocks
    private DailyReportService dailyReportService;

    @BeforeEach
    public void setUp() {
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
    @DisplayName("Get all values of selected country")
    public void getAllValuesCountry() {
        when(readTimeSeriesCSV.readConfirmedCsv()).thenReturn(confirmed);
        when(readTimeSeriesCSV.readRecoveredCsv()).thenReturn(recovered);
        when(readTimeSeriesCSV.readDeathsCsv()).thenReturn(deaths);

        assertThat(timeSeriesCountryService.getCountryTSValues("Germany")).isNotEmpty();
        assertThat(timeSeriesCountryService.getCountryTSValues("Germany")).isEqualTo(allValues);
    }

    @Test
    @DisplayName("Create a final result of selected country")
    public void getFinalResult() {
        Map<String, List<Integer>> finalResult = new HashMap<>();
        finalResult.put("confirmedResult", Arrays.asList(1, 2, 3));
        finalResult.put("recoveredResult", Arrays.asList(1, 2, 3));
        finalResult.put("deathsResult", Arrays.asList(1, 2, 3));

        when(readTimeSeriesCSV.readConfirmedCsv()).thenReturn(confirmed);
        when(readTimeSeriesCSV.readRecoveredCsv()).thenReturn(recovered);
        when(readTimeSeriesCSV.readDeathsCsv()).thenReturn(deaths);

        assertThat(timeSeriesCountryService.generateFinalTSResult(allValues)).isNotEmpty();
        assertThat(timeSeriesCountryService.generateFinalTSResult(allValues)).isEqualTo(finalResult);
    }

    @Test
    @DisplayName("Test get one day values, excepted no empty list, a result and an empty list")
    public void getOneDayValues() {
        assertThat(timeSeriesCountryService.getOneDayValues(values)).isNotEmpty();
        assertThat(timeSeriesCountryService.getOneDayValues(values)).contains(2);
        assertThat(timeSeriesCountryService.getOneDayValues(Collections.emptyList())).isEmpty();
    }

    @Test
    @DisplayName("Test get last value of the list and an empty list")
    public void getLastValues() {
        assertThat(timeSeriesCountryService.getLastValue(values)).isEqualTo(9);
        assertThat(timeSeriesCountryService.getLastValue(Collections.emptyList())).isEqualTo(0);
    }

    @Test
    @DisplayName("Get details for a selected country")
    public void getDetailsForCountry() throws IOException {
        CountryDetailsDto countryDetailsDto = new CountryDetailsDto();
        countryDetailsDto.setCountry("Germany");
        //countryDetailsDto.setCode("DE");
        countryDetailsDto.setConfirmed(100);
        countryDetailsDto.setRecovered(100);
        countryDetailsDto.setDeaths(100);
        when(readJSON.readCountryValuesOfJson(anyString())).thenReturn(countryDetailsDto);

        assertThat(dailyReportService.getCountryValues("Germany")).isNotNull();
        assertThat(dailyReportService.getCountryValues("Germany")).isEqualTo(Optional.of(countryDetailsDto));
    }

    @Test
    @DisplayName("Test get all countries of csv as list and test the list is empty")
    public void getCountry() {
        when(readTimeSeriesCSV.readCountryName()).thenReturn(Arrays.asList("France", "Spain"));

        assertThat(timeSeriesCountryService.getCountryNames()).isNotEmpty();
        assertThat(timeSeriesCountryService.getCountryNames()).contains("France");

        when(readTimeSeriesCSV.readCountryName()).thenReturn(Collections.emptyList());

        assertThat(timeSeriesCountryService.getCountryNames()).isEmpty();
    }

    @Test
    @DisplayName("Test get every second value of the list and when the list is null or empty")
    public void getEverySecondValue() {
        List<Integer> values = Arrays.asList(1, 3, 5, 7, 8, 19, 3, 9);
        assertThat(timeSeriesCountryService.getEverySecondValue(values)).isNotEmpty();
        assertThat(timeSeriesCountryService.getEverySecondValue(Collections.emptyList())).isEmpty();
    }

    @Test
    @DisplayName("Test get every second date of the list and returned an empty list")
    public void getEverySecondDate() {
        List<String> values = Arrays.asList("0000.00.00", "0000.00.00", "0000.00.00", "0000.00.00", "0000.00.00");
        assertThat(timeSeriesCountryService.getEverySecondDate(values)).isNotEmpty();
        assertThat(timeSeriesCountryService.getEverySecondDate(Collections.emptyList())).isEmpty();
    }

    @Test
    @DisplayName("Test get all values of world and test empty list returned")
    public void getAllValuesWorld() throws IOException {
        TimeSeriesWorldDto timeSeriesWorldDto = new TimeSeriesWorldDto();
        timeSeriesWorldDto.setUpdated_at("0000.00.00");
        timeSeriesWorldDto.setDate("0000.00.00");
        timeSeriesWorldDto.setConfirmed(100);
        timeSeriesWorldDto.setRecovered(100);
        timeSeriesWorldDto.setDeaths(100);
        timeSeriesWorldDto.setActive(100);
        timeSeriesWorldDto.setNewConfirmed(100);
        timeSeriesWorldDto.setNewRecovered(100);
        timeSeriesWorldDto.setNewDeaths(100);
        timeSeriesWorldDto.setIs_in_progress(true);

        List<TimeSeriesWorldDto> timeSeriesWorldDtoList = Stream.of(timeSeriesWorldDto).collect(Collectors.toList());

        when(readJSON.readWorldValuesOfJson()).thenReturn(timeSeriesWorldDtoList);

        assertThat(timeSeriesWorldService.getAllValuesWorld()).isNotEmpty();

        when(readJSON.readWorldValuesOfJson()).thenReturn(Collections.emptyList());

        assertThat(timeSeriesWorldService.getAllValuesWorld()).isEmpty();
    }

    @Test
    @DisplayName("Test get value for a province")
    public void getAllValuesOfProvince() {
        assertThat(timeSeriesCountryService.getProvinceTSValues("Australia", "Queensland")).isNotEmpty();
    }
}