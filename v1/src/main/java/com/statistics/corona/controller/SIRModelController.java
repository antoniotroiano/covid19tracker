package com.statistics.corona.controller;

import com.statistics.corona.model.SIRModel;
import com.statistics.corona.service.DerivativeService;
import com.statistics.corona.service.json.GetJsonValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("v1/sir-model")
@RequiredArgsConstructor
public class SIRModelController {

    private static final String SIR_MODEL = "sir-model";
    private final GetJsonValue getJsonValue;

    DerivativeService derivativeService = new DerivativeService();
    List<Double> resultSusceptible;
    List<Double> resultInfected;
    List<Double> resultRecovered;

    @GetMapping
    public String showSIRModel(SIRModel sirModel, Model model) throws IOException {
        log.info("Invoke calculate for sir-model.");
        List<String> allCountries = getJsonValue.getCountryOfJSONObject();
        model.addAttribute("listCountries", allCountries);
        SIRModel sirModelInitial = new SIRModel();
        sirModelInitial.setTransmissionRate(1.0);
        sirModelInitial.setRecoveryRate(0.23);

        if (sirModel.getTransmissionRate() != null && sirModel.getRecoveryRate() != null) {
            getResult(sirModel, model);
            return SIR_MODEL;
        }
        getResult(sirModelInitial, model);
        return SIR_MODEL;
    }

    @PostMapping("/newCalculation")
    public String postDataToChar(SIRModel sirModel, BindingResult bindingResult, Model model) throws IOException {
        log.info("Invoke new calculate for sir-model.");
        if (bindingResult.hasErrors()) {
            return SIR_MODEL;
        }
        if (sirModel.getTransmissionRate() == null || sirModel.getRecoveryRate() == null) {
            return showSIRModel(sirModel, model);
        }
        getResult(sirModel, model);
        return showSIRModel(sirModel, model);
    }

    private void getResult(SIRModel sirModel, Model model) {
        resultSusceptible = derivativeService.calculation(0.99, 0.01, 0,
                sirModel.getTransmissionRate(), sirModel.getRecoveryRate(), 30).get("Susceptible");
        resultInfected = derivativeService.calculation(0.99, 0.01, 0,
                sirModel.getTransmissionRate(), sirModel.getRecoveryRate(), 30).get("Infected");
        resultRecovered = derivativeService.calculation(0.99, 0.01, 0,
                sirModel.getTransmissionRate(), sirModel.getRecoveryRate(), 30).get("Recovered");

        model.addAttribute("transmissionRate", sirModel.getTransmissionRate());
        model.addAttribute("recoveryRate", sirModel.getRecoveryRate());
        model.addAttribute("values", new SIRModel());
        model.addAttribute("resultSusceptible", resultSusceptible);
        model.addAttribute("resultInfected", resultInfected);
        model.addAttribute("resultRecovered", resultRecovered);
    }
}