package com.statistics.corona.service.json;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.TimeSeriesWorldDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("ReadJSON tests")
public class ReadJSONTest {

    private final ReadJSON readJSON = new ReadJSON();

    @Test
    @DisplayName("Test read values for world of json object/array")
    public void readWorldValues() throws IOException {
        List<TimeSeriesWorldDto> timeSeriesWorldDtoList = readJSON.readWorldValues();

        assertThat(timeSeriesWorldDtoList).isNotEmpty();
    }

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