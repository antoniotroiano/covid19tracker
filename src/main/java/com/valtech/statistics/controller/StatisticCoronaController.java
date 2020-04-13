package com.valtech.statistics.controller;

import com.valtech.statistics.model.DataWorld;
import com.valtech.statistics.service.GetDataWorld;
import com.valtech.statistics.service.StatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("statisticsCorona")
@RequiredArgsConstructor
public class StatisticCoronaController {

    private final GetDataWorld getDataWorld;
    private final StatisticService statisticService;

    @GetMapping("/world")
    public String showStatisticCoronaWorld(Model model) throws IOException {
        log.info("Invoke com.valtech.statistics.controller to show data of world.");

        DataWorld dataWorld = getDataWorld.createDataForOneDay();

        if (statisticService.findDataWorldByLastUpdate(dataWorld.getLastUpdate()).isEmpty()) {
            log.info("Find no data with the same date.");
            DataWorld savedDate = statisticService.saveDataWorld(dataWorld);
            log.info("Saved new data do database and showed on the front end.");
            return getData(model, savedDate);
        }
        log.info("Found a dataset with the same date.");
        return getData(model, dataWorld);
    }

    @GetMapping("/germany")
    public String showStatisticCoronaGermany(Model model) {
        log.info("Invoke com.valtech.statistics.controller to show data of germany.");
        return "statisticGermany";
    }

    private String getData(Model model, DataWorld dataWorld) {
        model.addAttribute("confirmed", dataWorld.getConfirmed());
        model.addAttribute("recovered", dataWorld.getRecovered());
        model.addAttribute("deaths", dataWorld.getDeaths());
        model.addAttribute("lastUpdate", dataWorld.getLastUpdate());
        model.addAttribute("confirmedList", statisticService.getAllData().stream()
                .map(c -> c.getConfirmed())
                .collect(Collectors.toList()));
        model.addAttribute("recoveredList", statisticService.getAllData().stream()
                .map(c -> c.getRecovered())
                .collect(Collectors.toList()));
        model.addAttribute("deathsList", statisticService.getAllData().stream()
                .map(c -> c.getDeaths())
                .collect(Collectors.toList()));
        return "statisticCorona";
    }
}