package com.valtech.statistics.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DataTest {

    private Data data = new Data();

    @BeforeEach
    public void setUp() {
        data.setTransmissionRate(1.0);
        data.setRecoveryRate(2.0);
    }

    @Test
    public void createdValuesSIR() {
        assertThat(data).isInstanceOf(Data.class);
    }

    @Test
    public void hasTransmissionRate() {
        assertThat(data.getTransmissionRate()).isEqualTo(1.0);
    }

    @Test
    public void hasRecoveryRate() {
        assertThat(data.getRecoveryRate()).isEqualTo(2.0);
    }

    @Test
    public void constructorTest() {
        Data dataTwo = new Data(1.0, 2.0);
        assertThat(dataTwo).isInstanceOf(Data.class);
    }
}