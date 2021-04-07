package com.covid19.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CountryValuesTransferTest {

    private final CountryValuesTransfer countryValuesTransfer = new CountryValuesTransfer();
    private final JsonValueTransfer jsonValueTransfer1 = new JsonValueTransfer();
    private final JsonValueTransfer jsonValueTransfer2 = new JsonValueTransfer();
    private final JsonValueTransfer jsonValueTransfer3 = new JsonValueTransfer();

    @BeforeEach
    void setUp() {
        jsonValueTransfer1.setValue(1);
        countryValuesTransfer.setDataValueConfirmed(jsonValueTransfer1);

        jsonValueTransfer2.setValue(2);
        countryValuesTransfer.setDataValueRecovered(jsonValueTransfer2);

        jsonValueTransfer3.setValue(3);
        countryValuesTransfer.setDataValueDeaths(jsonValueTransfer3);
    }

    @Test
    @DisplayName("Test getter of dto CountryValuesTransfer")
    void getterTest() {
        assertThat(countryValuesTransfer.getDataValueConfirmed().getValue()).isEqualTo(1);
        assertThat(countryValuesTransfer.getDataValueRecovered().getValue()).isEqualTo(2);
        assertThat(countryValuesTransfer.getDataValueDeaths().getValue()).isEqualTo(3);
    }

    @Test
    @DisplayName("Test the toString of dto CountryValuesTransfer")
    void toStringTest() {
        assertThat(countryValuesTransfer)
                .hasToString("CountryValuesTransfer{dataValueConfirmed=JsonValueTransfer{value=1}, " +
                        "dataValueRecovered=JsonValueTransfer{value=2}, " +
                        "dataValueDeaths=JsonValueTransfer{value=3}}");
    }
}