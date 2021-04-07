package com.covid19.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JsonValueTransferTest {

    private final JsonValueTransfer jsonValueTransfer = new JsonValueTransfer();

    @BeforeEach
    void setUp() {
        jsonValueTransfer.setValue(1);
    }

    @Test
    @DisplayName("Test getter of dto JsonValueTransfer")
    void getterTest() {
        final JsonValueTransfer jsonValueTransfer1 = new JsonValueTransfer(jsonValueTransfer);
        assertThat(jsonValueTransfer1.getValue()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test the toString of dto JsonValueTransfer")
    void toStringTest() {
        assertThat(jsonValueTransfer)
                .hasToString("JsonValueTransfer{value=1}");
    }
}