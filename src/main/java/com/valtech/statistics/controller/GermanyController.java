package com.valtech.statistics.controller;

import com.valtech.statistics.model.DataGermany;
import com.valtech.statistics.model.DataGermanySummary;
import com.valtech.statistics.service.DateFormat;
import com.valtech.statistics.service.GermanyService;
import com.valtech.statistics.service.GermanySummaryService;
import com.valtech.statistics.service.ScheduledQuery;
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
@RequestMapping("covid19/germany")
@RequiredArgsConstructor
public class GermanyController {

    private final GermanyService germanyService;
    private final GermanySummaryService germanySummaryService;
    private final ScheduledQuery scheduledQuery;
    private final DateFormat dateFormat;

    @GetMapping
    public String showDataGermany(Model model) {
        log.info("Invoke show data of germany.");
        model.addAttribute("dataGermany", new DataGermany());
        model.addAttribute("dataGermanySummary", new DataGermanySummary());

        Optional<DataGermany> dataGermany = germanyService.getLastEntryGermany();
        if (dataGermany.isPresent()) {
            model.addAttribute("dataGermany", new DataGermany(dataGermany.get()));
            String date = dateFormat.formatLastUpdateToDate(dataGermany.get().getLastUpdate());
            String time = dateFormat.formatLastUpdateToTime(dataGermany.get().getLastUpdate());
            model.addAttribute("date", date + " " + time + "h");
            log.info("Show last entry of germany {}", dataGermany.get().getLastUpdate());
        } else {
            model.addAttribute("noFirstDataGermany", true);
            log.warn("No last entry in database of germany.");
        }

        Optional<DataGermanySummary> dataGermanySummary = germanySummaryService.getLastEntryGermanySummary();
        if (dataGermanySummary.isPresent()) {
            model.addAttribute("dataGermanySummary", new DataGermanySummary(dataGermanySummary.get()));
            log.info("Show last entry for germany summary {}.", dataGermanySummary.get().getLocalDate());
        } else {
            model.addAttribute("noFirstDataGermanySummary", true);
            log.warn("No last daily entry in database for germany summary.");
        }

        List<DataGermany> dataGermanyList = germanyService.getAllDataGermany();
        if (dataGermanyList.isEmpty()) {
            model.addAttribute("noDataGermany", true);
            log.warn("Found no data germany.");
            return "covid19Germany";
        }
        model.addAttribute("confirmedListG", dataGermanyList
                .stream()
                .map(DataGermany::getConfirmed)
                .collect(Collectors.toList()));
        model.addAttribute("recoveredListG", dataGermanyList
                .stream()
                .map(DataGermany::getRecovered)
                .collect(Collectors.toList()));
        model.addAttribute("deathsListG", dataGermanyList
                .stream()
                .map(DataGermany::getDeaths)
                .collect(Collectors.toList()));
        model.addAttribute("datesG", dataGermanyList
                .stream()
                .map(DataGermany::getLocalDate)
                .collect(Collectors.toList()));
        log.debug("Return data of germany.");
        return "covid19Germany";
    }

    @GetMapping("/update")
    public String updateDatabase(Model model) throws IOException {
        scheduledQuery.saveGermanyDataOfJson();
        scheduledQuery.saveGermanySummaryDataOfJson();
        return showDataGermany(model);
    }
}