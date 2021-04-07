package com.covid19.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CountryInfoTest {

    private final CountryInfo countryInfo = new CountryInfo();

    @BeforeEach
    void setUp() {
        countryInfo.setId(1);
        countryInfo.setIso2("iso2");
        countryInfo.setIso3("iso3");
        countryInfo.setFlag("flag");
    }

    @Test
    @DisplayName("Test getter of dto CountryInfo")
    void getterTest() {
        final CountryInfo countryInfo2 = new CountryInfo(countryInfo);
        assertThat(countryInfo2.getId()).isEqualTo(1);
        assertThat(countryInfo2.getIso2()).isEqualTo("iso2");
        assertThat(countryInfo2.getIso3()).isEqualTo("iso3");
        assertThat(countryInfo2.getFlag()).isEqualTo("flag");
    }

    @Test
    @DisplayName("Test the toString of dto CountryInfo")
    void toStringTest() {
        assertThat(countryInfo)
                .hasToString("CountryInfo{_id=1, " +
                        "iso2='iso2', " +
                        "iso3='iso3', " +
                        "flag='flag'}");
    }
}