package com.valtech.statistics.controller;

import com.valtech.statistics.service.DerivativeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SIRModelControllerTest {

    private final Map<String, List<Double>> map = new HashMap<>();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DerivativeService derivativeService;

    @InjectMocks
    private SIRModelController sirModelController;

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
    public void showShowModel() throws Exception {
        when(derivativeService.calculation(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyInt()))
                .thenReturn(map);

        mockMvc.perform(get("/sir-model"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Transmission rate:")))
                .andExpect(content().string(containsString("Recovery rate:")));
    }
}