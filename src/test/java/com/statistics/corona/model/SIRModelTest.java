package com.statistics.corona.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("SIRModel tests")
public class SIRModelTest {

    private final SIRModel sirModel = new SIRModel();

    @BeforeEach
    public void setUp() {
        sirModel.setTransmissionRate(1.0);
        sirModel.setRecoveryRate(2.0);
    }

    @Test
    @DisplayName("Test getter and setter of sirModel")
    public void getterSetter() {
        assertThat(sirModel.getTransmissionRate()).isEqualTo(1.0);
        assertThat(sirModel.getRecoveryRate()).isEqualTo(2.0);
    }

    @Test
    @DisplayName("Test constructor of sirModel")
    public void constructorTest() {
        SIRModel SIRModelTwo = new SIRModel(1.0, 2.0);
        assertThat(SIRModelTwo).isInstanceOf(SIRModel.class);
    }
}