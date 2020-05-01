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
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("covid19/daily")
@RequiredArgsConstructor
public class SummaryController {

    private final GetJsonValue getJsonValue;
    private final DateFormat dateFormat;

    @GetMapping("/{country}")
    public String showSummaryOfSelectedCountry(@PathVariable("country") String country, Model model) throws IOException {
        log.info("Invoke show summary of country {}", country);
        List<String> allCountries = getJsonValue.getCountryOfJSONObject();
        model.addAttribute("listCountries", allCountries);
        SummaryToday summaryToday = getJsonValue.getDataForSelectedCountry(country);
        if (summaryToday == null) {
            model.addAttribute("noDataForThisCountry", "No dataset for " + country + ". Please try again later.");
            log.info("No data for the country {}", country);
        } else {
            model.addAttribute("summaryToday", new SummaryToday());
            model.addAttribute("title", "COVID-19 - Summary for " + country);
            model.addAttribute("selectedCountry", country);
            model.addAttribute("dataSummaryToday", summaryToday);
            String date = dateFormat.formatLastUpdateToDate(summaryToday.getLastUpdate());
            String time = dateFormat.formatLastUpdateToTime(summaryToday.getLastUpdate());
            model.addAttribute("date", date + " " + time + "h");
        }

        List<SummaryToday> summaryTodayList = getJsonValue.getDataDayOneTotalSelectedCountry(country);
        if (summaryTodayList.isEmpty()) {
            model.addAttribute("noDataForDayOne", "No data for day one until today for " + country + ". Please try again later.");
            log.warn("Found no data of day one to today for {}", country);
            return "covid19Summary";
        }
        model.addAttribute("confirmed", summaryTodayList
                .stream()
                .map(SummaryToday::getConfirmedToday)
                .collect(Collectors.toList()));
        model.addAttribute("deaths", summaryTodayList
                .stream()
                .map(SummaryToday::getDeathsToday)
                .collect(Collectors.toList()));
        model.addAttribute("recovered", summaryTodayList
                .stream()
                .map(SummaryToday::getRecoveredToday)
                .collect(Collectors.toList()));
        model.addAttribute("dates", summaryTodayList
                .stream()
                .map(SummaryToday::getLastUpdate)
                .collect(Collectors.toList()));
        return "covid19Summary";
    }
}