package com.statistics.corona.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SIRModelTest {

    private final SIRModel SIRModel = new SIRModel();

    @BeforeEach
    public void setUp() {
        SIRModel.setTransmissionRate(1.0);
        SIRModel.setRecoveryRate(2.0);
    }

    @Test
    public void createdValuesSIR() {
        assertThat(SIRModel).isInstanceOf(SIRModel.class);
    }

    @Test
    public void hasTransmissionRate() {
        assertThat(SIRModel.getTransmissionRate()).isEqualTo(1.0);
    }

    @Test
    public void hasRecoveryRate() {
        assertThat(SIRModel.getRecoveryRate()).isEqualTo(2.0);
    }

    @Test
    public void constructorTest() {
        SIRModel SIRModelTwo = new SIRModel(1.0, 2.0);
        assertThat(SIRModelTwo).isInstanceOf(SIRModel.class);
    }
}