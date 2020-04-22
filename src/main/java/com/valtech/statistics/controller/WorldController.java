package com.valtech.statistics.controller;

import com.valtech.statistics.model.DataWorld;
import com.valtech.statistics.model.DataWorldSummary;
import com.valtech.statistics.service.WorldService;
import com.valtech.statistics.service.WorldSummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("covid19/world")
@RequiredArgsConstructor
public class WorldController {

    private final WorldService worldService;
    private final WorldSummaryService worldSummaryService;

    @GetMapping
    public String showDataWorld(Model model) {
        log.info("Invoke show data of world.");

        Optional<DataWorld> dataWorld = worldService.getLastEntryWorld();
        if (dataWorld.isPresent()) {
            model.addAttribute("confirmed", dataWorld.get().getConfirmed());
            model.addAttribute("recovered", dataWorld.get().getRecovered());
            model.addAttribute("deaths", dataWorld.get().getDeaths());
            model.addAttribute("lastUpdate", dataWorld.get().getLastUpdate());
            log.info("Show last entry for world {}.", dataWorld.get().getLastUpdate());
        } else {
            model.addAttribute("noFirstDataWorld", true);
            log.warn("No last entry in database for world.");
        }

        Optional<DataWorldSummary> dataWorldSummary = worldSummaryService.getLastEntryWorldSummary();
        if (dataWorldSummary.isPresent()) {
            model.addAttribute("newConfirmed", dataWorldSummary.get().getNewConfirmed());
            model.addAttribute("totalConfirmed", dataWorldSummary.get().getTotalConfirmed());
            model.addAttribute("newDeaths", dataWorldSummary.get().getNewDeaths());
            model.addAttribute("totalDeaths", dataWorldSummary.get().getTotalDeaths());
            model.addAttribute("newRecovered", dataWorldSummary.get().getNewRecovered());
            model.addAttribute("totalRecovered", dataWorldSummary.get().getTotalRecovered());
            model.addAttribute("date", DateTimeFormatter.ofPattern("dd.MM.yyy")
                    .format(dataWorldSummary.get().getLocalDate()));
            model.addAttribute("time", dataWorldSummary.get().getLocalTime());
            log.info("Show last entry for world summary {}.", dataWorldSummary.get().getLocalDate());
        } else {
            model.addAttribute("noFirstDataWorld", true);
            log.warn("No last daily entry in database for world summary.");
        }

        List<DataWorld> dataWorldList = worldService.getAllData();
        if (dataWorldList.isEmpty()) {
            model.addAttribute("noDataWorld", true);
            log.warn("Found no data world.");
            return "covid19World";
        }
        model.addAttribute("confirmedListW", dataWorldList
                .stream()
                .map(DataWorld::getConfirmed)
                .collect(Collectors.toList()));
        model.addAttribute("recoveredListW", dataWorldList
                .stream()
                .map(DataWorld::getRecovered)
                .collect(Collectors.toList()));
        model.addAttribute("deathsListW", dataWorldList
                .stream()
                .map(DataWorld::getDeaths)
                .collect(Collectors.toList()));
        model.addAttribute("datesW", dataWorldList
                .stream()
                .map(DataWorld::getLocalDate)
                .collect(Collectors.toList()));
        log.debug("Return data of world.");
        return "covid19World";
    }


}