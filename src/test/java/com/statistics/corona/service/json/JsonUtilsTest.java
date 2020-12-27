package com.statistics.corona.service.json;

import com.statistics.corona.service.ProvinceService;
import com.statistics.corona.service.WorldService;
import com.statistics.corona.service.csv.CsvUtilsDailyReports;
import com.statistics.corona.service.csv.CsvUtilsTimeSeries;
import com.statistics.corona.utils.ServiceUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import static org.apache.logging.log4j.core.impl.ThrowableFormatOptions.FILE_NAME;

@SpringBootTest
@DisplayName("ReadJSON tests")
class JsonUtilsTest {

    private final JsonUtils jsonUtils = new JsonUtils();
    private final WorldService worldService = new WorldService(jsonUtils);
    private final ServiceUtils serviceUtils = new ServiceUtils();
    private final CsvUtilsDailyReports csvUtilsDailyReports = new CsvUtilsDailyReports();
    private final CsvUtilsTimeSeries csvUtilsTimeSeries = new CsvUtilsTimeSeries();

    @Test
    void testYesterdayValues() {
        ProvinceService provinceService = new ProvinceService(csvUtilsTimeSeries, csvUtilsDailyReports);
        System.out.println(provinceService.getYesterdayValuesOfCountry("Germany"));
    }

    @Test
    void testNewLink() {
        CsvUtilsDailyReports csvUtilsDailyReports = new CsvUtilsDailyReports();

        System.out.println(csvUtilsDailyReports.readCountryLatestCSV().stream().filter(c -> c.getLocation().contains("Germany")).reduce((first, second) -> second).get());
    }

    @Test
    void test1() throws IOException, URISyntaxException {

/*        FileInputStream file =
                new FileInputStream(new File("https://www.rki.de/DE/Content/InfAZ/N/Neuartiges_Coronavirus/Daten/Testzahlen-gesamt.xlsx?__blob=publicationFile"));

        Workbook workbook = WorkbookFactory.create(new File("https://www.rki.de/DE/Content/InfAZ/N/Neuartiges_Coronavirus/Daten/Testzahlen-gesamt.xlsx?__blob=publicationFile"));
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");*/

        URL url = new URL("https://www.rki.de/DE/Content/InfAZ/N/Neuartiges_Coronavirus/Daten/Testzahlen-gesamt.xlsx?__blob=publicationFile");
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME);
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileOutputStream.getChannel()
                .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        System.out.println(fileChannel.lock());

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