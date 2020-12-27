package com.statistics.corona.resource;

import com.statistics.corona.model.SIRModel;
import com.statistics.corona.service.CountryService;
import com.statistics.corona.service.DerivativeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @deprecated Don't needed for now
 */
@Deprecated(since = "4.0.0-SNAPSHOT", forRemoval = false)
@Controller
@Slf4j
@RequestMapping("sir")
@RequiredArgsConstructor
public class SIRController {

    private static final String SIR_MODEL = "sir";
    private final CountryService countryService;
    private final DerivativeService derivativeService;

    @GetMapping
    public String showSIRModel(SIRModel sirModel, Model model) {
        log.info("Invoke v2 sir-model");
        List<String> allCountries = countryService.getCountryNames();
        model.addAttribute("listCountries", allCountries);

        SIRModel sirModelInit = new SIRModel();
        sirModelInit.setTransmissionRate(1.0);
        sirModelInit.setRecoveryRate(0.23);

        if (sirModel.getTransmissionRate() != null && sirModel.getRecoveryRate() != null) {
            getResult(sirModel, model);
            log.debug("Return result for sir-model");
            return SIR_MODEL;
        }
        log.debug("Return result for sir-model");
        getResult(sirModelInit, model);
        return SIR_MODEL;
    }

    @PostMapping("/newCalculation")
    public String postDataToChar(SIRModel sirModel, BindingResult bindingResult, Model model) {
        log.info("Invoke v2 new calculate for sir-model");
        if (bindingResult.hasErrors()) {
            return SIR_MODEL;
        }
        if (sirModel.getTransmissionRate() == null || sirModel.getRecoveryRate() == null) {
            log.warn("Transmission rate and recovery rate are null");
            return showSIRModel(sirModel, model);
        }
        log.debug("Returned nee calculation of sir-model");
        getResult(sirModel, model);
        return showSIRModel(sirModel, model);
    }

    private void getResult(SIRModel sirModel, Model model) {
        List<Double> resultSusceptible = derivativeService.calculation(0.99, 0.01, 0,
                sirModel.getTransmissionRate(), sirModel.getRecoveryRate(), 30).get("Susceptible");
        List<Double> resultInfected = derivativeService.calculation(0.99, 0.01, 0,
                sirModel.getTransmissionRate(), sirModel.getRecoveryRate(), 30).get("Infected");
        List<Double> resultRecovered = derivativeService.calculation(0.99, 0.01, 0,
                sirModel.getTransmissionRate(), sirModel.getRecoveryRate(), 30).get("Recovered");

        model.addAttribute("transmissionRate", sirModel.getTransmissionRate());
        model.addAttribute("recoveryRate", sirModel.getRecoveryRate());
        model.addAttribute("values", new SIRModel());
        model.addAttribute("resultSusceptible", resultSusceptible);
        model.addAttribute("resultInfected", resultInfected);
        model.addAttribute("resultRecovered", resultRecovered);
    }
}