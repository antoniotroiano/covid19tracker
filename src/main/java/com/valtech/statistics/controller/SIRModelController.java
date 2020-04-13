package com.valtech.statistics.controller;

import com.valtech.statistics.model.SIRModel;
import com.valtech.statistics.service.DerivativeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("sir-model")
public class SIRModelController {

    private static final String SIR_MODEL = "sir-model";

    DerivativeService derivativeService = new DerivativeService();
    List<Double> resultSusceptible;
    List<Double> resultInfected;
    List<Double> resultRecovered;

    @GetMapping
    public String showSIRModel(SIRModel SIRModel, Model model) {

        SIRModel SIRModelInitial = new SIRModel();
        SIRModelInitial.setTransmissionRate(1.0);
        SIRModelInitial.setRecoveryRate(0.23);

        if (SIRModel.getTransmissionRate() != null && SIRModel.getRecoveryRate() != null) {
            getResult(SIRModel, model);
            return SIR_MODEL;
        }
        getResult(SIRModelInitial, model);
        return SIR_MODEL;
    }

    @PostMapping("/newCalculation")
    public String postDataToChar(SIRModel SIRModel, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return SIR_MODEL;
        }
        if (SIRModel.getTransmissionRate() == null || SIRModel.getRecoveryRate() == null) {
            return showSIRModel(SIRModel, model);
        }
        getResult(SIRModel, model);
        return showSIRModel(SIRModel, model);
    }

    private void getResult(SIRModel SIRModel, Model model) {
        resultSusceptible =
                derivativeService.calculation(0.99, 0.01, 0, SIRModel.getTransmissionRate(),
                        SIRModel.getRecoveryRate(), 30).get("Susceptible");
        resultInfected =
                derivativeService.calculation(0.99, 0.01, 0, SIRModel.getTransmissionRate(),
                        SIRModel.getRecoveryRate(), 30).get("Infected");
        resultRecovered =
                derivativeService.calculation(0.99, 0.01, 0, SIRModel.getTransmissionRate(),
                        SIRModel.getRecoveryRate(), 30).get("Recovered");

        model.addAttribute("transmissionRate", SIRModel.getTransmissionRate());
        model.addAttribute("recoveryRate", SIRModel.getRecoveryRate());
        model.addAttribute("values", new SIRModel());
        model.addAttribute("resultSusceptible", resultSusceptible);
        model.addAttribute("resultInfected", resultInfected);
        model.addAttribute("resultRecovered", resultRecovered);
    }
}