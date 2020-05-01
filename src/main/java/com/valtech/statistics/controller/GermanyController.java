package com.valtech.statistics.controller;

import com.valtech.statistics.model.DataGermanySummary;
import com.valtech.statistics.service.DateFormat;
import com.valtech.statistics.service.GermanySummaryService;
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
@RequestMapping("covid19/germany")
@RequiredArgsConstructor
public class GermanyController {

    private final GermanySummaryService germanySummaryService;
    private final ScheduledQuery scheduledQuery;
    private final DateFormat dateFormat;
    private final GetJsonValue getJsonValue;

    @GetMapping
    public String showDataGermany(Model model) throws IOException {
        log.info("Invoke show data of germany.");
        List<String> allCountries = getJsonValue.getCountryOfJSONObject();
        model.addAttribute("listCountries", allCountries);
        model.addAttribute("dataGermanySummary", new DataGermanySummary());

        Optional<DataGermanySummary> dataGermanySummary = germanySummaryService.getLastEntryGermanySummary();
        if (dataGermanySummary.isPresent()) {
            model.addAttribute("dataGermanySummary", new DataGermanySummary(dataGermanySummary.get()));
            String date = dateFormat.formatLastUpdateToDate(dataGermanySummary.get().getLastUpdate());
            String time = dateFormat.formatLastUpdateToTime(dataGermanySummary.get().getLastUpdate());
            model.addAttribute("date", date + " " + time + "h");
            log.info("Show last entry for germany {}.", dataGermanySummary.get().getLocalDate());
        } else {
            model.addAttribute("noFirstDataGermany", "No first dataset of germany. Please try again in an hour.");
            log.warn("No last daily entry in database for germany.");
        }

        List<DataGermanySummary> dataGermanyList = germanySummaryService.getAllDataGermanySummary();
        if (dataGermanyList.isEmpty()) {
            model.addAttribute("noDataGermany", "No data for germany. Please try again later.");
            log.warn("Found no data germany.");
            return "covid19Germany";
        }
        model.addAttribute("confirmedListG", dataGermanyList
                .stream()
                .map(DataGermanySummary::getTotalConfirmed)
                .collect(Collectors.toList()));
        model.addAttribute("recoveredListG", dataGermanyList
                .stream()
                .map(DataGermanySummary::getTotalRecovered)
                .collect(Collectors.toList()));
        model.addAttribute("deathsListG", dataGermanyList
                .stream()
                .map(DataGermanySummary::getTotalDeaths)
                .collect(Collectors.toList()));
        model.addAttribute("datesG", dataGermanyList
                .stream()
                .map(DataGermanySummary::getLocalDate)
                .collect(Collectors.toList()));
        log.debug("Return data of germany.");
        return "covid19Germany";
    }

    @GetMapping("/update")
    public String updateDatabase(Model model) throws IOException {
        log.info("Invoke update data manual for germany.");
        scheduledQuery.saveGermanyDataOfJson();
        return showDataGermany(model);
    }
}