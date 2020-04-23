package com.valtech.statistics.controller;

import com.valtech.statistics.model.DataGermany;
import com.valtech.statistics.model.DataGermanySummary;
import com.valtech.statistics.model.DataWorldSummary;
import com.valtech.statistics.service.GermanyService;
import com.valtech.statistics.service.GermanySummaryService;
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
@RequestMapping("covid19/germany")
@RequiredArgsConstructor
public class GermanyController {

    private final GermanyService germanyService;
    private final GermanySummaryService germanySummaryService;

    @GetMapping
    public String showDataGermany(Model model) {
        log.info("Invoke show data of germany.");

        Optional<DataGermany> dataGermany = germanyService.getLastEntryGermany();
        if (dataGermany.isPresent()) {
            model.addAttribute("confirmed", dataGermany.get().getConfirmed());
            model.addAttribute("recovered", dataGermany.get().getRecovered());
            model.addAttribute("deaths", dataGermany.get().getDeaths());
            model.addAttribute("lastUpdate", dataGermany.get().getLastUpdate());
            log.info("Show last entry of germany {}", dataGermany.get().getLastUpdate());
        } else {
            model.addAttribute("noFirstDataGermany", true);
            log.warn("No last entry in database of germany.");
        }

        Optional<DataGermanySummary> dataGermanySummary = germanySummaryService.getLastEntryGermanySummary();
        if (dataGermanySummary.isPresent()) {
            model.addAttribute("newConfirmedG", dataGermanySummary.get().getNewConfirmed());
            model.addAttribute("totalConfirmedG", dataGermanySummary.get().getTotalConfirmed());
            model.addAttribute("newDeathsG", dataGermanySummary.get().getNewDeaths());
            model.addAttribute("totalDeathsG", dataGermanySummary.get().getTotalDeaths());
            model.addAttribute("newRecoveredG", dataGermanySummary.get().getNewRecovered());
            model.addAttribute("totalRecoveredG", dataGermanySummary.get().getTotalRecovered());
            model.addAttribute("activeCasesG", dataGermanySummary.get().getActiveCases());
            model.addAttribute("dateG", DateTimeFormatter.ofPattern("dd.MM.yyy")
                    .format(dataGermanySummary.get().getLocalDate()));
            model.addAttribute("timeG", dataGermanySummary.get().getLocalTime());
            log.info("Show last entry for germany summary {}.", dataGermanySummary.get().getLocalDate());
        } else {
            model.addAttribute("noFirstDataGermany", true);
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
}
