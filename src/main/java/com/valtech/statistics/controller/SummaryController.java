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

    private static final String COVID19_DETAILS = "covid19Details";
    private final GetJsonValue getJsonValue;
    private final DateFormat dateFormat;

    @GetMapping("/{country}")
    public String showSummaryOfSelectedCountry(@PathVariable("country") String country, Model model) throws IOException {
        log.info("Invoke show summary of country {}", country);
        List<String> allCountries = getJsonValue.getCountryOfJSONObject();
        model.addAttribute("listCountries", allCountries);
        SummaryToday summaryToday = getJsonValue.getDataForSelectedCountry(country);
        if (summaryToday == null) {
            createBaseModelSummary(country, model);
            model.addAttribute("noDataForThisCountry", "No dataset for " + country + ". Please try again later.");
            log.info("No data for the country {}", country);
        } else {
            model.addAttribute("summaryToday", new SummaryToday());
            createBaseModelSummary(country, model);
            model.addAttribute("dataSummaryToday", summaryToday);
            String date = dateFormat.formatLastUpdateToDate(summaryToday.getLastUpdate());
            String time = dateFormat.formatLastUpdateToTime(summaryToday.getLastUpdate());
            model.addAttribute("date", date + " " + time + "h");
            log.info("Show data for selected country {}", country);
        }

        List<SummaryToday> summaryTodayList = getJsonValue.getDataDayOneTotalSelectedCountry(country);
        if (summaryTodayList.isEmpty() || summaryTodayList.size() <= 1) {
            model.addAttribute("noDataForDayOne", "At the moment no data for day one until today for " + country + " available. Please try again later.");
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
        log.info("Show day one to today data for selected country {}", country);
        return "covid19Summary";
    }

    @GetMapping("/{country}/details")
    public String showDetailsOfSelectedCountry(@PathVariable("country") String country, Model model) throws IOException {
        log.info("Invoke show details of country {}", country);
        List<SummaryToday> summaryTodayList = getJsonValue.getJSONArrayOfOneCountry(country);
        if (summaryTodayList.isEmpty()) {
            createBaseModelDetails(country, model);
            model.addAttribute("noDataFound", "No data found for selected country " + country + ".");
            log.warn("No data found for selected country  {}", country);
            return COVID19_DETAILS;
        }
        if (summaryTodayList.size() <= 1) {
            createBaseModelDetails(country, model);
            model.addAttribute("noDataDetails", "No details available for selected country " + country + ".");
            log.warn("No details available for selected country {}", country);
            return COVID19_DETAILS;
        }
        model.addAttribute("summaryToday", new SummaryToday());
        createBaseModelDetails(country, model);
        model.addAttribute("summaryTodayList", summaryTodayList);
        log.info("Show more details for selected country {}", country);
        return COVID19_DETAILS;
    }

    private void createBaseModelDetails(String country, Model model) {
        model.addAttribute("title", "COVID-19 - Details for " + country);
        model.addAttribute("selectedCountry", country);
        log.debug("Create base model with title and selected country for details.");
    }

    private void createBaseModelSummary(String country, Model model) {
        model.addAttribute("title", "COVID-19 - Summary for " + country);
        model.addAttribute("selectedCountry", country);
        log.debug("Create base model with title and selected country for summary.");
    }
}