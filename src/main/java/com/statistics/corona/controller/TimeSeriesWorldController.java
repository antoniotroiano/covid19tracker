package com.statistics.corona.controller;

import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.TimeSeriesWorldDto;
import com.statistics.corona.service.DailyReportService;
import com.statistics.corona.service.DateFormat;
import com.statistics.corona.service.TimeSeriesCountryService;
import com.statistics.corona.service.TimeSeriesWorldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/")
public class TimeSeriesWorldController {

    private static final String TIME_SERIES = "timeSeriesWorld";
    private final TimeSeriesWorldService timeSeriesWorldService;
    private final TimeSeriesCountryService timeSeriesCountryService;
    private final DailyReportService dailyReportService;
    private final DateFormat dateFormat;

    @Autowired
    public TimeSeriesWorldController(TimeSeriesWorldService timeSeriesWorldService, TimeSeriesCountryService timeSeriesCountryService, DailyReportService dailyReportService, DateFormat dateFormat) {
        this.timeSeriesWorldService = timeSeriesWorldService;
        this.timeSeriesCountryService = timeSeriesCountryService;
        this.dailyReportService = dailyReportService;
        this.dateFormat = dateFormat;
    }

    @GetMapping
    public String showWorldTimeSeries(Model model) {
        log.info("Invoke controller for time series world");
        model.addAttribute("worldTimeSeriesDto", new TimeSeriesWorldDto());
        model.addAttribute("dailyReportDto", new DailyReportDto());
        model.addAttribute("listCountries", timeSeriesCountryService.getCountryNames());

        List<TimeSeriesWorldDto> timeSeriesWorldDtoList = timeSeriesWorldService.getAllValuesWorld();
        if (timeSeriesWorldDtoList.isEmpty()) {
            model.addAttribute("latestDataWorld", timeSeriesWorldDtoList);
            model.addAttribute("noDataForWorldTimeSeries", true);
            log.warn("No data available for world time series");
            return TIME_SERIES;
        }

        String date = dateFormat.formatLastUpdateToDate(timeSeriesWorldService.getLatestDataWorldValue(timeSeriesWorldDtoList).getUpdated_at());
        String time = dateFormat.formatLastUpdateToTime(timeSeriesWorldService.getLatestDataWorldValue(timeSeriesWorldDtoList).getUpdated_at());
        model.addAttribute("latestDataWorld", timeSeriesWorldService.getLatestDataWorldValue(timeSeriesWorldDtoList));
        model.addAttribute("lastUpdate", date + " " + time + " h");
        model.addAttribute("secondValueWorld", timeSeriesWorldService.getSecondWorldValue(timeSeriesWorldDtoList));
        model.addAttribute("confirmed",
                timeSeriesCountryService.getEverySecondValue(timeSeriesWorldService.getConfirmedValueWorld(timeSeriesWorldDtoList)));
        model.addAttribute("recovered",
                timeSeriesCountryService.getEverySecondValue(timeSeriesWorldService.getRecoveredValueWorld(timeSeriesWorldDtoList)));
        model.addAttribute("deaths",
                timeSeriesCountryService.getEverySecondValue(timeSeriesWorldService.getDeathValueWorld(timeSeriesWorldDtoList)));
        model.addAttribute("deathsRate", (double) timeSeriesWorldService.getLatestDataWorldValue(timeSeriesWorldDtoList).getDeaths() /
                (double) timeSeriesWorldService.getLatestDataWorldValue(timeSeriesWorldDtoList).getConfirmed() * 100);
        model.addAttribute("recoveryRate", (double) timeSeriesWorldService.getLatestDataWorldValue(timeSeriesWorldDtoList).getRecovered() /
                (double) timeSeriesWorldService.getLatestDataWorldValue(timeSeriesWorldDtoList).getConfirmed() * 100);
        //ToDo: Cases per one hundred and death per one hundred. World population needed

        //ToDo: Diagram for daily values active, new confirmed/recovered/deaths?
        model.addAttribute("active", timeSeriesWorldService.getActiveValueWorld(timeSeriesWorldDtoList));
        model.addAttribute("newConfirmed", timeSeriesWorldService.getNewConfirmedValueWorld(timeSeriesWorldDtoList));
        model.addAttribute("newRecovered", timeSeriesWorldService.getNewRecoveredValueWorld(timeSeriesWorldDtoList));
        model.addAttribute("newDeaths", timeSeriesWorldService.getNewDeathsValueWorld(timeSeriesWorldDtoList));

        List<String> listDates = timeSeriesWorldDtoList
                .stream()
                .map(TimeSeriesWorldDto::getDate)
                .collect(Collectors.toList());
        Collections.reverse(listDates);
        model.addAttribute("dateTimeSeries", timeSeriesCountryService.getEverySecondDate(listDates));

        List<DailyReportDto> dailyReportServiceList = dailyReportService.getAllDailyReports();
        List<DailyReportDto> allValues = dailyReportService.getAllDailyCountryValuesCalculated(dailyReportServiceList);
        if (allValues.isEmpty()) {
            model.addAttribute("noValuesAllCountries", true);
            log.warn("No values available for all countries");
            return TIME_SERIES;
        }
        model.addAttribute("totalTable", timeSeriesWorldService.getEachCountriesConfirmedDescending(allValues));
        model.addAttribute("activeTable", timeSeriesWorldService.getEachCountriesActiveDescending(allValues));
        model.addAttribute("deathsTable", timeSeriesWorldService.getEachCountriesDeathsDescending(allValues));
        log.debug("Returned all values of all countries and global values");
        return TIME_SERIES;
    }
}