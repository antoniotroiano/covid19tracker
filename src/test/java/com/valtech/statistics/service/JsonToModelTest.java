package com.valtech.statistics.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JsonToModelTest {

    @Autowired
    private JsonToModel jsonToModel;

    @Test
    public void getDataWorldSummary() throws IOException {

        /*int confirmed = jsonToModel.getDataWorldSummary();

        assertThat(confirmed).isPositive();*/
    }
}