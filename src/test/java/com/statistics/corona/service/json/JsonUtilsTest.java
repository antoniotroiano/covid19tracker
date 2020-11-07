package com.statistics.corona.service.json;

import com.statistics.corona.service.CountryService;
import com.statistics.corona.service.TimeSeriesUtils;
import com.statistics.corona.service.WorldService;
import com.statistics.corona.service.csv.CsvUtilsTimeSeries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@DisplayName("ReadJSON tests")
public class JsonUtilsTest {

    private final JsonUtils jsonUtils = new JsonUtils();
    private final WorldService worldService = new WorldService(jsonUtils);
    private final TimeSeriesUtils timeSeriesUtils = new TimeSeriesUtils();
    private final CsvUtilsTimeSeries csvUtilsTimeSeries = new CsvUtilsTimeSeries();
    private final CountryService countryService = new CountryService(csvUtilsTimeSeries, timeSeriesUtils, jsonUtils);

    @Test
    void test1() {
        //System.out.println(countryService.mapTStoDTO(jsonUtils.readCountryValuesOfJson("Germany")).getCasesValues());
        //System.out.println(countryService.mapTStoDTO(jsonUtils.readCountryValuesOfJson("Germany")).getPopulation());
        //System.out.println(countryService.calculateSevenDayIncidence(countryService.mapTStoDTO(jsonUtils.readCountryValuesOfJson("Germany"))));
    }

    @Test
     void test() {
        System.out.println(jsonUtils.mapWorldJsonToObject("10").getWorldTimeSeriesDto().getCases().values());
        System.out.println(jsonUtils.mapWorldJsonToObject("10").getWorldTimeSeriesDto().getCases().keySet());
        System.out.println(jsonUtils.mapWorldJsonToObject("10").getConfirmed().getValue());
        System.out.println(jsonUtils.mapWorldJsonToObject("10").getLastUpdate());

        System.out.println(jsonUtils.mapWorldJsonToObject("30").getWorldTimeSeriesDto().getCases().get("11/5/20"));
        System.out.println(worldService.getYesterdayActive(Optional.of(jsonUtils.mapWorldJsonToObject("10"))));

        List<Integer> test = timeSeriesUtils.getDailyTrend(jsonUtils.mapWorldJsonToObject("8").getWorldTimeSeriesDto().getCases());

        System.out.println(test);
        System.out.println(test.stream().mapToInt(Integer::intValue).sum());
        System.out.println(((double)test.stream().mapToInt(Integer::intValue).sum() / 7823655300L) * 100000);
    }

/*    @Test
    @DisplayName("Test read values for world of json object/array")
    public void readWorldValues() throws IOException {
        List<TimeSeriesWorldDto> timeSeriesWorldDtoList = jsonUtils.readWorldValuesOfJson();

        assertThat(timeSeriesWorldDtoList).isNotEmpty();
    }*/

/*    @Test
    public void testJacksonJson() throws IOException {
        *//*ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        URL url = new URL("https://corona-api.com/timeline");

        List<TimeSeriesWorldDto> timeSeriesWorldDto = objectMapper.readValue(url, new TypeReference<>() {
        });*//*

        RestTemplate restTemplate = new RestTemplate();
        List<TimeSeriesWorldDto> countries = Objects.requireNonNull(restTemplate.getForObject("https://corona-api.com/timeline", DataObject.class)).getTimeSeriesWorldDtoList();

        System.out.println(countries.get(0));
        System.out.println(countries.get(1));
        System.out.println(countries.get(206));
        System.out.println(countries.get(206).getUpdated_at());
    }

    @Test
    public void testJacksonJson2() {
        RestTemplate restTemplate = new RestTemplate();
        List<DistrictDto> countries = Objects.requireNonNull(restTemplate
                .getForObject("https://www.trackcorona.live/api/cities", DataObjectDistrict.class))
                .getDistrictDtoList();

        System.out.println(countries.get(0));
        System.out.println(countries.get(1));
        System.out.println(countries.get(206));
        System.out.println(countries.get(206).getUpdated());
    }

    @Test
    public void testJacksonJson3() {
        RestTemplate restTemplate = new RestTemplate();
        CountryDetailsDto countries = restTemplate
                .getForObject("https://corona.lmao.ninja/v2/countries/Australia", CountryDetailsDto.class);

        RestTemplate restTemplate2 = new RestTemplate();
        DataObjectCountry countries2 = restTemplate2.getForObject("https://covid19.mathdro.id/api/countries/Australia", DataObjectCountry.class);

        countries.setConfirmed(countries2.getDataValueConfirmed().getValue());
        countries.setRecovered(countries2.getDataValueRecovered().getValue());
        countries.setDeaths(countries2.getDataValueDeaths().getValue());
        System.out.println(countries);
        System.out.println(countries.getDataObjectCountryInfo().getIso2());
*//*        System.out.println(countries.get(1));
        System.out.println(countries.get(206));
        System.out.println(countries.get(206).getUpdated());*//*
    }*/

    /*@Test
    @DisplayName("Test read details for a given country of json object/array")
    public void readDetailsForCountry() throws IOException {
        CountryDetailsDto countryDetailsDtoUS = readJSON.readCountryValues("US");
        CountryDetailsDto countryDetailsDtoCongo = readJSON.readCountryValues("Congo (Brazzaville)");
        CountryDetailsDto countryDetailsDtoKorea = readJSON.readCountryValues("Korea, South");
        CountryDetailsDto countryDetailsDtoSVG = readJSON.readCountryValues("Saint Vincent and the Grenadines");
        CountryDetailsDto countryDetailsDtoTaiwan = readJSON.readCountryValues("Taiwan*");
        CountryDetailsDto countryDetailsDtoUK = readJSON.readCountryValues("United Kingdom");


        assertThat(countryDetailsDtoUS).isNotNull();
        assertThat(countryDetailsDtoUS.getCountry()).isEqualTo("United States");
        assertThat(countryDetailsDtoCongo).isNotNull();
        assertThat(countryDetailsDtoCongo.getCountry()).isEqualTo("Congo");
        assertThat(countryDetailsDtoKorea).isNotNull();
        assertThat(countryDetailsDtoKorea.getCountry()).isEqualTo("South Korea");
        assertThat(countryDetailsDtoSVG).isNotNull();
        assertThat(countryDetailsDtoSVG.getCountry()).isNull(); // ToDo: Fix it
        assertThat(countryDetailsDtoTaiwan).isNotNull();
        assertThat(countryDetailsDtoTaiwan.getCountry()).isEqualTo("Taiwan");
        assertThat(countryDetailsDtoUK).isNotNull();
        assertThat(countryDetailsDtoUK.getCountry()).isEqualTo("United Kingdom");
    }*/
}