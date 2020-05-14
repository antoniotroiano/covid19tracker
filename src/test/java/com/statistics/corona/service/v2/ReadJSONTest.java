package com.statistics.corona.service.v2;

import com.statistics.corona.model.v2.CountryDetailsDto;
import com.statistics.corona.model.v2.WorldTimeSeriesDto;
import com.statistics.corona.service.v2.json.ReadJSON;
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
    }

    @Test
    public void readDetails() throws IOException {
        CountryDetailsDto countryDetailsDto = readJSON.readDetailsForCountry("United Kingdom");

        System.out.println(countryDetailsDto);
    }
}