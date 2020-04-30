package com.valtech.statistics.controller;

import com.valtech.statistics.model.DataWorldSummary;
import com.valtech.statistics.service.DateFormat;
import com.valtech.statistics.service.WorldSummaryService;
import com.valtech.statistics.service.json.GetJsonValue;
import com.valtech.statistics.service.scheuled.ScheduledQuery;
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
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final WorldSummaryService worldSummaryService;
    private final ScheduledQuery scheduledQuery;
    private final DateFormat dateFormat;
    private final GetJsonValue getJsonValue;

    @GetMapping
    public String showHomeController(Model model) throws IOException {
        log.info("Invoke show home controller.");
        List<String> allCountries = getJsonValue.getCountryOfJSONObject();
        model.addAttribute("listCountries", allCountries);
        model.addAttribute("dataWorldSummary", new DataWorldSummary());

        Optional<DataWorldSummary> dataWorldSummary = worldSummaryService.getLastEntryWorldSummary();
        if (dataWorldSummary.isPresent()) {
            model.addAttribute("dataWorldSummary", new DataWorldSummary(dataWorldSummary.get()));
            String date = dateFormat.formatLastUpdateToDate(dataWorldSummary.get().getLastUpdate());
            String time = dateFormat.formatLastUpdateToTime(dataWorldSummary.get().getLastUpdate());
            model.addAttribute("date", date + " " + time + "h");
            log.info("Show last entry for world {}.", dataWorldSummary.get().getLocalDate());
        } else {
            model.addAttribute("noFirstDataWorld", true);
            log.warn("No last daily entry in database for world.");
        }

        List<DataWorldSummary> dataWorldList = worldSummaryService.getAllDataWorldSummary();
        if (dataWorldList.isEmpty()) {
            model.addAttribute("noDataWorld", true);
            log.warn("Found no data world.");
            return "home";
        }
        model.addAttribute("confirmedListW", dataWorldList
                .stream()
                .map(DataWorldSummary::getTotalConfirmed)
                .collect(Collectors.toList()));
        model.addAttribute("recoveredListW", dataWorldList
                .stream()
                .map(DataWorldSummary::getTotalRecovered)
                .collect(Collectors.toList()));
        model.addAttribute("deathsListW", dataWorldList
                .stream()
                .map(DataWorldSummary::getTotalDeaths)
                .collect(Collectors.toList()));
        model.addAttribute("datesW", dataWorldList
                .stream()
                .map(DataWorldSummary::getLocalDate)
                .collect(Collectors.toList()));
        log.debug("Return data of world.");
        return "home";
    }

    @GetMapping("/update")
    public String updateDatabase(Model model) throws IOException {
        log.info("Invoke update data of world.");
        scheduledQuery.saveWorldDataOfJson();
        return showHomeController(model);
    }
}