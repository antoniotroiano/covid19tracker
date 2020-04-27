package com.valtech.statistics.controller;

import com.valtech.statistics.model.DataWorld;
import com.valtech.statistics.model.DataWorldDaily;
import com.valtech.statistics.model.DataWorldSummary;
import com.valtech.statistics.service.DateFormat;
import com.valtech.statistics.service.scheuled.ScheduledQuery;
import com.valtech.statistics.service.WorldService;
import com.valtech.statistics.service.WorldSummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
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
    private final ScheduledQuery scheduledQuery;
    private final DateFormat dateFormat;

    @GetMapping
    public String showDataWorld(Model model) {
        log.info("Invoke show data of world.");
        model.addAttribute("dataWorld", new DataWorld());
        model.addAttribute("dataWorldSummary", new DataWorldSummary());
        model.addAttribute("dataWorldDaily", new DataWorldDaily());

        Optional<DataWorld> dataWorld = worldService.getLastEntryWorld();
        if (dataWorld.isPresent()) {
            model.addAttribute("dataWorld", new DataWorld(dataWorld.get()));
            String date = dateFormat.formatLastUpdateToDate(dataWorld.get().getLastUpdate());
            String time = dateFormat.formatLastUpdateToTime(dataWorld.get().getLastUpdate());
            model.addAttribute("date", date + " " + time + "h");
            log.info("Show last entry for world {}.", dataWorld.get().getLastUpdate());
        } else {
            model.addAttribute("noFirstDataWorld", true);
            log.warn("No last entry in database for world.");
        }

        Optional<DataWorldSummary> dataWorldSummary = worldSummaryService.getLastEntryWorldSummary();
        if (dataWorldSummary.isPresent()) {
            model.addAttribute("dataWorldSummary", new DataWorldSummary(dataWorldSummary.get()));
            log.info("Show last entry for world summary {}.", dataWorldSummary.get().getLocalDate());
        } else {
            model.addAttribute("noFirstDataWorldSummary", true);
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

    @GetMapping("/update")
    public String updateDatabase(Model model) throws IOException {
        scheduledQuery.saveWorldDataOfJson();
        scheduledQuery.saveWorldSummaryDataOfJson();
        return showDataWorld(model);
    }
}