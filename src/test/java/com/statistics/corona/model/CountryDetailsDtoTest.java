package com.statistics.corona.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("CountryDetailsDto tests")
public class CountryDetailsDtoTest {

    private final CountryDetailsDto countryDetailsDto = new CountryDetailsDto();

    @BeforeEach
    public void setUp() {
        countryDetailsDto.setCountry("Germany");
        countryDetailsDto.setCode("DE");
        countryDetailsDto.setPopulation(83000000);
        countryDetailsDto.setLastUpdate("0000.00.00");
        countryDetailsDto.setDeaths(100);
        countryDetailsDto.setConfirmed(100);
        countryDetailsDto.setRecovered(100);
        countryDetailsDto.setCritical(100);
        countryDetailsDto.setDeathRate(0.1);
        countryDetailsDto.setRecoveryRate(0.1);
        countryDetailsDto.setCasesPerOneHundred(100);
    }

    @Test
    @DisplayName("Check countryDetailsDto mapping")
    public void checkCountryDetailsMapping() {
        CountryDetailsDto countryDetailsDtoMapping = new CountryDetailsDto(countryDetailsDto);

        assertThat(countryDetailsDtoMapping.getCountry()).isEqualTo(countryDetailsDto.getCountry());
        assertThat(countryDetailsDtoMapping.getCode()).isEqualTo(countryDetailsDto.getCode());
        assertThat(countryDetailsDtoMapping.getPopulation()).isEqualTo(countryDetailsDto.getPopulation());
        assertThat(countryDetailsDtoMapping.getLastUpdate()).isEqualTo(countryDetailsDto.getLastUpdate());
        assertThat(countryDetailsDtoMapping.getDeaths()).isEqualTo(countryDetailsDto.getDeaths());
        assertThat(countryDetailsDtoMapping.getConfirmed()).isEqualTo(countryDetailsDto.getConfirmed());
        assertThat(countryDetailsDtoMapping.getRecovered()).isEqualTo(countryDetailsDto.getRecovered());
        assertThat(countryDetailsDtoMapping.getCritical()).isEqualTo(countryDetailsDto.getCritical());
        assertThat(countryDetailsDtoMapping.getDeathRate()).isEqualTo(countryDetailsDto.getDeathRate());
        assertThat(countryDetailsDtoMapping.getRecoveryRate()).isEqualTo(countryDetailsDto.getRecoveryRate());
        assertThat(countryDetailsDtoMapping.getCasesPerOneHundred()).isEqualTo(countryDetailsDto.getCasesPerOneHundred());
    }

    @Test
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
    }
}