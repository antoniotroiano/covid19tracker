package com.valtech.statistics.service;

import com.valtech.statistics.model.CountryDetailsDto;
import com.valtech.statistics.model.WorldTimeSeriesDto;
import com.valtech.statistics.service.json.ReadJSON;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class ReadJSONTest {

    @InjectMocks
    private ReadJSON readJSON;

    @Test
    public void readJSONWorld() throws IOException {
        List<WorldTimeSeriesDto> worldTimeSeries = readJSON.readWorldValues();

        System.out.println(worldTimeSeries.size());
        System.out.println(worldTimeSeries.get(0));
        System.out.println(worldTimeSeries.get(1));
        System.out.println(worldTimeSeries.get(2));
        System.out.println(worldTimeSeries.stream().findFirst());

        CountryDetailsDto countryDetailsDto = readJSON.readDetailsForCountry("Germany");

        System.out.println(countryDetailsDto);
    }
}