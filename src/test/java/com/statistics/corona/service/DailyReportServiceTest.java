package com.statistics.corona.service;

import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.DailyReportUsDto;
import com.statistics.corona.service.csv.CsvUtilsDailyReports;
import com.statistics.corona.service.json.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("TimeSeriesDetailsService tests")
public class DailyReportServiceTest {

    private final DailyReportService dailyReportService = new DailyReportService(new CsvUtilsDailyReports(), new JsonUtils());
    private final CsvUtilsDailyReports readDailyReportCSV = new CsvUtilsDailyReports();

    private final List<DailyReportDto> dailyReportDtoList = new ArrayList<>();
    private final List<DailyReportUsDto> dailyReportUsDtoList = new ArrayList<>();
    private final DailyReportDto dailyReportDto2 = new DailyReportDto();

    private final CsvUtilsDailyReports csvUtilsDailyReports = mock(CsvUtilsDailyReports.class);

   /* @InjectMocks
    private DailyReportService dailyReportService;*/

    @BeforeEach
    public void setUp() {
        DailyReportDto dailyReportDto = new DailyReportDto();
        dailyReportDto.setCountry("US");
        dailyReportDto.setConfirmed(0);
        dailyReportDto.setRecovered(0);
        dailyReportDto.setDeaths(0);
        dailyReportDto.setActive(0);


        dailyReportDto2.setCountry("Italy");
        dailyReportDto2.setProvince("Lombardia");
        dailyReportDto2.setConfirmed(0);
        dailyReportDto2.setRecovered(0);
        dailyReportDto2.setDeaths(0);
        dailyReportDto2.setActive(0);

        DailyReportDto dailyReportDto3 = new DailyReportDto();
        dailyReportDto3.setCountry("Canada");
        dailyReportDto3.setConfirmed(0);
        dailyReportDto3.setRecovered(0);
        dailyReportDto3.setDeaths(0);
        dailyReportDto3.setActive(0);

        DailyReportDto dailyReportDto4 = new DailyReportDto();
        dailyReportDto4.setCountry("Spain");
        dailyReportDto4.setConfirmed(0);
        dailyReportDto4.setRecovered(0);
        dailyReportDto4.setDeaths(0);
        dailyReportDto4.setActive(0);

        DailyReportDto dailyReportDto5 = new DailyReportDto();
        dailyReportDto5.setCountry("United Kingdom");
        dailyReportDto5.setConfirmed(0);
        dailyReportDto5.setRecovered(0);
        dailyReportDto5.setDeaths(0);
        dailyReportDto5.setActive(0);

        DailyReportDto dailyReportDto6 = new DailyReportDto();
        dailyReportDto6.setCountry("China");
        dailyReportDto6.setConfirmed(0);
        dailyReportDto6.setRecovered(0);
        dailyReportDto6.setDeaths(0);
        dailyReportDto6.setActive(0);

        DailyReportDto dailyReportDto7 = new DailyReportDto();
        dailyReportDto7.setCountry("Netherlands");
        dailyReportDto7.setConfirmed(0);
        dailyReportDto7.setRecovered(0);
        dailyReportDto7.setDeaths(0);
        dailyReportDto7.setActive(0);

        DailyReportDto dailyReportDto8 = new DailyReportDto();
        dailyReportDto8.setCountry("Australia");
        dailyReportDto8.setConfirmed(0);
        dailyReportDto8.setRecovered(0);
        dailyReportDto8.setDeaths(0);
        dailyReportDto8.setActive(0);

        DailyReportDto dailyReportDto9 = new DailyReportDto();
        dailyReportDto9.setCountry("Germany");
        dailyReportDto9.setConfirmed(0);
        dailyReportDto9.setRecovered(0);
        dailyReportDto9.setDeaths(0);
        dailyReportDto9.setActive(0);

        DailyReportDto dailyReportDto10 = new DailyReportDto();
        dailyReportDto10.setCountry("Denmark");
        dailyReportDto10.setConfirmed(0);
        dailyReportDto10.setRecovered(0);
        dailyReportDto10.setDeaths(0);
        dailyReportDto10.setActive(0);

        DailyReportDto dailyReportDto11 = new DailyReportDto();
        dailyReportDto11.setCountry("France");
        dailyReportDto11.setConfirmed(0);
        dailyReportDto11.setRecovered(0);
        dailyReportDto11.setDeaths(0);
        dailyReportDto11.setActive(0);

        DailyReportDto dailyReportDto12 = new DailyReportDto();
        dailyReportDto12.setCountry("Brazil");
        dailyReportDto12.setConfirmed(0);
        dailyReportDto12.setRecovered(0);
        dailyReportDto12.setDeaths(0);
        dailyReportDto12.setActive(0);

        DailyReportDto dailyReportDto13 = new DailyReportDto();
        dailyReportDto13.setCountry("Chile");
        dailyReportDto13.setConfirmed(0);
        dailyReportDto13.setRecovered(0);
        dailyReportDto13.setDeaths(0);
        dailyReportDto13.setActive(0);

        DailyReportDto dailyReportDto14 = new DailyReportDto();
        dailyReportDto14.setCountry("Japan");
        dailyReportDto14.setConfirmed(0);
        dailyReportDto14.setRecovered(0);
        dailyReportDto14.setDeaths(0);
        dailyReportDto14.setActive(0);

        DailyReportDto dailyReportDto15 = new DailyReportDto();
        dailyReportDto15.setCountry("Mexico");
        dailyReportDto15.setConfirmed(0);
        dailyReportDto15.setRecovered(0);
        dailyReportDto15.setDeaths(0);
        dailyReportDto15.setActive(0);

        DailyReportUsDto dailyReportUsDto = new DailyReportUsDto();
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

        dailyReportDtoList.add(dailyReportDto);
        dailyReportDtoList.add(dailyReportDto2);
        dailyReportDtoList.add(dailyReportDto3);
        dailyReportDtoList.add(dailyReportDto4);
        dailyReportDtoList.add(dailyReportDto5);
        dailyReportDtoList.add(dailyReportDto6);
        dailyReportDtoList.add(dailyReportDto7);
        dailyReportDtoList.add(dailyReportDto8);
        dailyReportDtoList.add(dailyReportDto9);
        dailyReportDtoList.add(dailyReportDto10);
        dailyReportDtoList.add(dailyReportDto11);
        dailyReportDtoList.add(dailyReportDto12);
        dailyReportDtoList.add(dailyReportDto13);
        dailyReportDtoList.add(dailyReportDto14);
        dailyReportDtoList.add(dailyReportDto15);

        dailyReportUsDtoList.add(dailyReportUsDto);
    }

/*    @Test
    @DisplayName("Test get all values of province of selected country by CSV")
    public void getAllDetailsProvince() {
        DailyReportDto dailyReportDto = new DailyReportDto();
        dailyReportDto.setCountry("Germany");
        List<DailyReportDto> dailyReportDtoList = Stream.of(dailyReportDto).collect(Collectors.toList());
        when(csvUtilsDailyReports.readDailyReportsCSV()).thenReturn(dailyReportDtoList);

        assertThat(dailyReportService.getDailyDetailsOfProvince("Germany")).isNotEmpty();
        assertThat(dailyReportService.getDailyDetailsOfProvince("Germany")).isEqualTo(dailyReportDtoList);
    }*/

/*    @Test
    @DisplayName("Test get all values of province of selected country by CSV with empty list")
    public void getAllDetailsProvince_withEmptyList() {
        when(csvUtilsDailyReports.readDailyReportsCSV()).thenReturn(new ArrayList<>());

        assertThat(dailyReportService.getDailyDetailsOfProvince("Germany")).isEmpty();
    }*/

/*    @Test
    @DisplayName("Test get all values of province of US by CSV")
    public void getAllDetailsProvinceUS() {
        when(csvUtilsDailyReports.readDailyReportUs()).thenReturn(dailyReportUsDtoList);

        assertThat(dailyReportService.getDailyDetailsProvinceUs()).isNotEmpty();
        assertThat(dailyReportService.getDailyDetailsProvinceUs()).isEqualTo(dailyReportUsDtoList);
    }*/

/*    @Test
    @DisplayName("Test get all values of province of US by CSV with empty list")
    public void getAllDetailsProvinceUS_withEmptyList() {
        when(csvUtilsDailyReports.readDailyReportUs()).thenReturn(new ArrayList<>());

        assertThat(dailyReportService.getDailyDetailsProvinceUs()).isEmpty();
    }*/

/*    @Test
    @DisplayName("Test get all values of province of selected country by CSV")
    public void getAllCountries() {
        when(csvUtilsDailyReports.readDailyReportsCSV()).thenReturn(dailyReportDtoList);

        DailyReportDto dailyReportDto = new DailyReportDto();
        dailyReportDto.setConfirmed(0);
        dailyReportDto.setActive(0);
        dailyReportDto.setDeaths(0);
        dailyReportDto.setRecovered(0);
        dailyReportDto.setCountry("Germany");
        assertThat(dailyReportService.getAllDailyCountryValuesCalculated()).isNotEmpty();
        assertThat(dailyReportService.getAllDailyCountryValuesCalculated().get(8).getCountry()).contains(dailyReportDto.getCountry());
    }*/

/*    @Test
    @DisplayName("Test get all values of province of selected country by CSV with empty list")
    public void getAllCountries_withEmptyList() {
        when(csvUtilsDailyReports.readDailyReportsCSV()).thenReturn(new ArrayList<>());

        assertThat(dailyReportService.getAllDailyCountryValuesCalculated()).isEmpty();
    }*/

    /*@Test
    @DisplayName("Test get all province details for selected province is success")
    public void getProvinceDetails() {
        when(readDailyReportsCSV.readDailyReportsCSV()).thenReturn(dailyReportDtoList);
        Optional<DailyReportDto> testObject = readDailyReportService.getProvinceDetails("Lombardia");

        assertThat(testObject).isEqualTo(Optional.of(dailyReportDto2));
    }*/

    @Test
    @DisplayName("Test get all province details for selected province is empty")
    public void getProvinceDetails_isEmpty() {
        assertThat(dailyReportService.getProvinceDetails("Lombardia")).isEmpty();
    }

/*    @Test
    public void getAllCountriesStream() {

        List<DailyReportDto> dailyReportDtos = dailyReportService.getAllDailyCountryValuesCalculated();
        List<DailyReportDto> allDailyReports = readDailyReportCSV.readDailyReportsCSV();
        System.out.println(dailyReportDtos.size());
        System.out.println(allDailyReports.stream().map(DailyReportDto::getCountry).count());

    }*/

    @Test
    public void getAllCountriesStream2() {

        dailyReportService.getProvinceDetails("Queensland");
        System.out.println(dailyReportService.getProvinceDetails("Queensland"));
    }
}