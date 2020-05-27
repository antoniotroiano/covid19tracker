package com.statistics.corona.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("DateFormat tests")
public class DateFormatTest {

    private final DateFormat dateFormat = new DateFormat();

    @Test
    @DisplayName("Test all format for date and time")
    public void dateFormat() {
        String date = dateFormat.formatLastUpdateToDate("2020-05-18T00:32:18.000Z");
        String time = dateFormat.formatLastUpdateToTime("2020-05-18T00:32:18.000Z");
        String onlyDate = dateFormat.formatDate("2020-05-18");

        assertThat(date).isEqualTo("18.05.2020");
        assertThat(time).isEqualTo("00:32");
        assertThat(onlyDate).isEqualTo("18.05.2020");
    }
}