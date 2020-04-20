package com.valtech.statistics.controller;

import com.valtech.statistics.model.DataGermany;
import com.valtech.statistics.model.DataWorld;
import com.valtech.statistics.service.GermanyService;
import com.valtech.statistics.service.WorldService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("statisticsCorona")
@RequiredArgsConstructor
public class StatisticCoronaController {

    private final WorldService worldService;
    private final GermanyService germanyService;

    @GetMapping("/world")
    public String showDataWorld(Model model) {
        log.info("Invoke controller to show data of world.");
        DataWorld dataWorldNull = new DataWorld(0, 0, 0, "0000-00-00");

        DataWorld dataWorld = worldService.getLastEntryWorld() == null ? dataWorldNull : worldService.getLastEntryWorld();
        worldService.getLastEntryWorld();
        model.addAttribute("confirmed", dataWorld.getConfirmed());
        model.addAttribute("recovered", dataWorld.getRecovered());
        model.addAttribute("deaths", dataWorld.getDeaths());
        model.addAttribute("lastUpdate", dataWorld.getLastUpdate());
        model.addAttribute("confirmedList", worldService.getAllData()
                .stream()
                .map(DataWorld::getConfirmed)
                .collect(Collectors.toList()));
        model.addAttribute("recoveredList", worldService.getAllData()
                .stream()
                .map(DataWorld::getRecovered)
                .collect(Collectors.toList()));
        model.addAttribute("deathsList", worldService.getAllData()
                .stream()
                .map(DataWorld::getDeaths)
                .collect(Collectors.toList()));
        model.addAttribute("dates", worldService.getAllData()
                .stream()
                .map(DataWorld::getLocalDate)
                .collect(Collectors.toList()));
        log.debug("Return data of world.");
        return "statisticCorona";
    }

    @GetMapping("/summary")
    public String showDataWorldSummary(Model model) {

        return "worldSummary";
    }

    @GetMapping("/germany")
    public String showDataGermany(Model model) {
        log.info("Invoke controller to show data of germany.");
        DataGermany dataGermanyNull = new DataGermany(0, 0, 0, "0000-00-00");

        DataGermany dataGermany = germanyService.getLastEntryGermany() == null ? dataGermanyNull : germanyService.getLastEntryGermany();
        germanyService.getLastEntryGermany();
        model.addAttribute("confirmed", dataGermany.getConfirmed());
        model.addAttribute("recovered", dataGermany.getRecovered());
        model.addAttribute("deaths", dataGermany.getDeaths());
        model.addAttribute("lastUpdate", dataGermany.getLastUpdate());
        model.addAttribute("confirmedList", germanyService.getAllDataGermany()
                .stream()
                .map(DataGermany::getConfirmed)
                .collect(Collectors.toList()));
        model.addAttribute("recoveredList", germanyService.getAllDataGermany()
                .stream()
                .map(DataGermany::getRecovered)
                .collect(Collectors.toList()));
        model.addAttribute("deathsList", germanyService.getAllDataGermany()
                .stream()
                .map(DataGermany::getDeaths)
                .collect(Collectors.toList()));
        model.addAttribute("dates", germanyService.getAllDataGermany()
                .stream()
                .map(DataGermany::getLocalDate)
                .collect(Collectors.toList()));
        log.debug("Return data of germany.");
        return "statisticGermany";
    }
}