package com.statistics.corona.controller;

import com.statistics.corona.service.TimeSeriesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("covid19/symptoms")
@RequiredArgsConstructor
public class SymptomsController {

    private final TimeSeriesService timeSeriesService;

    @GetMapping
    public String showSymptoms(Model model) {
        log.info("Invoke get symptoms of covid19");
        List<String> allCountries = timeSeriesService.getCountryNames();
        if (allCountries.isEmpty()) {
            model.addAttribute("listCountries", new ArrayList<>());
        }
        model.addAttribute("listCountries", allCountries);
        return "symptoms";
    }
}