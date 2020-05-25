package com.statistics.corona.controller;

import com.statistics.corona.service.DerivativeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SIRModelController tests")
public class SIRModelControllerTest {

    private final Map<String, List<Double>> map = new HashMap<>();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DerivativeService derivativeService;

    @BeforeEach
    public void setUp() {

        List<Double> susList = new ArrayList<>();
        susList.add(1.0);

        List<Double> infList = new ArrayList<>();
        infList.add(1.0);

        List<Double> reList = new ArrayList<>();
        infList.add(1.0);

        map.put("Susceptible", susList);
        map.put("Infected", infList);
        map.put("Recovered", reList);
    }

    @Test
    @DisplayName("Show sir-model with init values")
    public void showSIRModel() throws Exception {
        when(derivativeService.calculation(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyInt()))
                .thenReturn(map);

        mockMvc.perform(get("/v2/sir"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Show sir-model with new calculation")
    public void showNewCalculation() throws Exception {
        when(derivativeService.calculation(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyInt()))
                .thenReturn(map);

        mockMvc.perform(post("/v2/sir/newCalculation"))
                .andExpect(status().isOk());
    }
}