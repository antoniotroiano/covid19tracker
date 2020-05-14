package com.statistics.corona.service.v1;

import com.statistics.corona.service.v1.json.GetJsonValue;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GetJsonValueTest {

    @Autowired

    @InjectMocks
    private GetJsonValue getJsonValue;

    @BeforeEach
    public void setUp() {

    }

}