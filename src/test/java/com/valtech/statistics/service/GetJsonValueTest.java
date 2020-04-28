package com.valtech.statistics.service;

import com.valtech.statistics.service.json.GetJsonValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class GetJsonValueTest {

    @Autowired

    @InjectMocks
    private GetJsonValue getJsonValue;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void getDataWorldSummary() throws IOException {

    }
}