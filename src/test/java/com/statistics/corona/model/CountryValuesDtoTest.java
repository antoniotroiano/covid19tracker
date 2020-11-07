package com.statistics.corona.model;

import com.statistics.corona.model.dto.CountryValuesDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("CountryDetailsDto tests")
public class CountryValuesDtoTest {

    private final CountryValuesDto countryValuesDto = new CountryValuesDto();

    @BeforeEach
    public void setUp() {
        countryValuesDto.setCountry("Germany");
        //countryDetailsDto.setCode("DE");
        countryValuesDto.setPopulation(83000000);
        //countryDetailsDto.setLastUpdate("0000.00.00");
        countryValuesDto.setDeaths(100);
        countryValuesDto.setCases(100);
        countryValuesDto.setRecovered(100);
        countryValuesDto.setCritical(100);
        countryValuesDto.setDeathRate(0.1);
        countryValuesDto.setRecoveryRate(0.1);
        countryValuesDto.setCasesPerOneHundred(100);
    }

    @Test
    @DisplayName("Check countryDetailsDto mapping")
    public void checkCountryDetailsMapping() {
        CountryValuesDto countryValuesDtoMapping = new CountryValuesDto(countryValuesDto);

        assertThat(countryValuesDtoMapping.getCountry()).isEqualTo(countryValuesDto.getCountry());
        //assertThat(countryDetailsDtoMapping.getCode()).isEqualTo(countryDetailsDto.getCode());
        assertThat(countryValuesDtoMapping.getPopulation()).isEqualTo(countryValuesDto.getPopulation());
        assertThat(countryValuesDtoMapping.getUpdated()).isEqualTo(countryValuesDto.getUpdated());
        assertThat(countryValuesDtoMapping.getDeaths()).isEqualTo(countryValuesDto.getDeaths());
        assertThat(countryValuesDtoMapping.getCases()).isEqualTo(countryValuesDto.getCases());
        assertThat(countryValuesDtoMapping.getRecovered()).isEqualTo(countryValuesDto.getRecovered());
        assertThat(countryValuesDtoMapping.getCritical()).isEqualTo(countryValuesDto.getCritical());
        assertThat(countryValuesDtoMapping.getDeathRate()).isEqualTo(countryValuesDto.getDeathRate());
        assertThat(countryValuesDtoMapping.getRecoveryRate()).isEqualTo(countryValuesDto.getRecoveryRate());
        assertThat(countryValuesDtoMapping.getCasesPerOneHundred()).isEqualTo(countryValuesDto.getCasesPerOneHundred());
    }

    /*@Test
    @DisplayName("Test toString method of countryDetailsDto")
    public void toStringTest() {
        assertThat(countryDetailsDto.toString())
                .isEqualTo("CountryDetailsDto{country='Germany', " +
                        "code='DE', " +
                        "population=83000000, " +
                        "lastUpdate='0000.00.00', " +
                        "deaths=100, " +
                        "confirmed=100, " +
                        "recovered=100, " +
                        "critical=100, " +
                        "deathRate=0.1, " +
                        "recoveryRate=0.1, " +
                        "casesPerOneHundred=100, " +
                        "deathsPerOneHundred=0}");
    }*/
}