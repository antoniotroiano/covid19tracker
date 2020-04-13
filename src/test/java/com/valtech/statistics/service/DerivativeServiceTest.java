package com.valtech.statistics.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DerivativeServiceTest {

    @Test
    public void calculateSIRModel() {

        DerivativeService derivativeService = new DerivativeService();

        assertThat(derivativeService.calculation(0.99, 0.01, 0, 3.2, 0.23, 30))
                .isNotEmpty();

        assertThat(derivativeService.calculation(0.99, 0.01, 0, 3, 0.5, 30))
                .isNotEmpty();

        assertThat(derivativeService.calculation(45400, 2100, 2500, 0.00001, 0.07142857143, 30))
                .isNotEmpty();
    }
}