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

    private final StatisticService statisticService;
    private final GetDataWorld getDataWorld;

    @GetMapping("/world")
    public String showStatisticCoronaWorld(Model model) throws IOException {
        log.info("Invoke controller to show data of world.");
        return getData(model);
    }

    @GetMapping("/germany")
    public String showStatisticCoronaGermany(Model model) {
        log.info("Invoke controller to show data of germany.");
        return "statisticGermany";
    }

    private String getData(Model model) {
        DataWorld dataWorldNull = new DataWorld(0, 0, 0, "0000-00-00");

        DataWorld dataWorld = statisticService.getLastEntry() == null ? dataWorldNull : statisticService.getLastEntry();
        statisticService.getLastEntry();
        model.addAttribute("confirmed", dataWorld.getConfirmed());
        model.addAttribute("recovered", dataWorld.getRecovered());
        model.addAttribute("deaths", dataWorld.getDeaths());
        model.addAttribute("lastUpdate", dataWorld.getLastUpdate());
        model.addAttribute("confirmedList", statisticService.getAllData()
                .stream()
                .map(DataWorld::getConfirmed)
                .collect(Collectors.toList()));
        model.addAttribute("recoveredList", statisticService.getAllData()
                .stream()
                .map(DataWorld::getRecovered)
                .collect(Collectors.toList()));
        model.addAttribute("deathsList", statisticService.getAllData()
                .stream()
                .map(DataWorld::getDeaths)
                .collect(Collectors.toList()));
        model.addAttribute("dates", statisticService.getAllData()
                .stream()
                .map(DataWorld::getLocalDate)
                .collect(Collectors.toList()));
        log.debug("Return data of world.");
        return "statisticCorona";
    }
}