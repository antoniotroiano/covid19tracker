package com.valtech.statistics.service;

import com.valtech.statistics.model.DataGermany;
import com.valtech.statistics.repository.DataGermanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class GetJsonValueTest {

    private final DataGermany dataGermanyFirst = new DataGermany();
    private final DataGermany dataGermanyNew = new DataGermany();
    private final DataGermany dataGermanyEqual = new DataGermany();
    private final DataGermanyRepository dataGermanyRepository = mock(DataGermanyRepository.class);

    @Autowired
    private GermanyService germanyService;

    @InjectMocks
    private GetJsonValue getJsonValue;

    @BeforeEach
    public void setUp() {
        dataGermanyFirst.setConfirmed(4);
        dataGermanyFirst.setRecovered(2);
        dataGermanyFirst.setDeaths(1);
        dataGermanyFirst.setLastUpdate("20.04.2020T07:30:31.000Z");
        dataGermanyFirst.setLocalDate("20.04");

        dataGermanyNew.setConfirmed(6);
        dataGermanyNew.setRecovered(3);
        dataGermanyNew.setDeaths(2);
        dataGermanyNew.setLastUpdate("20.04.2020T10:30:31.000Z");
        dataGermanyNew.setLocalDate("20.04");

        dataGermanyRepository.save(dataGermanyFirst);
    }

    @Test
    public void getDataWorldSummary() throws IOException {

        /*int confirmed = jsonToModel.getDataWorldSummary();

        assertThat(confirmed).isPositive();*/
    }
}