package com.valtech.statistics.controller;

import com.valtech.statistics.model.SummaryToday;
import com.valtech.statistics.service.DateFormat;
import com.valtech.statistics.service.json.GetJsonValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@Slf4j
@RequestMapping("covid19/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final GetJsonValue getJsonValue;
    private final DateFormat dateFormat;

    @GetMapping("/{country}")
    public String showSummaryOfSelectedCountry(@PathVariable("country") String country, Model model) throws IOException {
        log.info("Invoke show summary of country {}", country);
        SummaryToday summaryToday = getJsonValue.getDataOfForSelectedCountry(country);
        if (summaryToday == null) {
            model.addAttribute("noDataForThisCountry", true);
            log.info("No data for the country {}", country);
        }
        model.addAttribute("summaryToday", new SummaryToday());
        model.addAttribute("title", "COVID-19 - Summary for " + country);
        model.addAttribute("selectedCountry", country);
        model.addAttribute("dataSummaryToday", summaryToday);
        String date = dateFormat.formatLastUpdateToDate(summaryToday.getLastUpdate());
        String time = dateFormat.formatLastUpdateToTime(summaryToday.getLastUpdate());
        model.addAttribute("date", date + " " + time + "h");
        return "covid19Summary";
    }
}