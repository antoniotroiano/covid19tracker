package com.statistics.corona.controller;

import com.statistics.corona.service.TimeSeriesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SymptomsController tests")
public class SymptomsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeSeriesService timeSeriesService;

    @Test
    @DisplayName("Show symptoms and prevention page successfully")
    public void showSymptomsPage() throws Exception {
        List<String> countries = Arrays.asList("Germany", "France");
        when(timeSeriesService.getCountryNames()).thenReturn(countries);

        mockMvc.perform(get("/covid19/symptoms"))
                .andExpect(status().isOk());
    }
}