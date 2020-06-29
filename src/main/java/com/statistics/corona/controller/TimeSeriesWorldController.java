package com.statistics.corona.controller;

import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.TimeSeriesWorldDto;
import com.statistics.corona.service.DailyReportService;
import com.statistics.corona.service.DateFormat;
import com.statistics.corona.service.TimeSeriesCountryService;
import com.statistics.corona.service.TimeSeriesWorldService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/")
@RequiredArgsConstructor
public class TimeSeriesWorldController {

    private static final String TIME_SERIES = "timeSeriesWorld";
    private final TimeSeriesWorldService timeSeriesWorldService;
    private final TimeSeriesCountryService timeSeriesCountryService;
    private final DailyReportService dailyReportService;
    private final DateFormat dateFormat;

    @GetMapping
    public String showWorldTimeSeries(Model model) {
        log.info("Invoke v2 controller show time series world");
        model.addAttribute("worldTimeSeriesDto", new TimeSeriesWorldDto());

        List<String> allCountries = timeSeriesCountryService.getCountryNames();
        if (allCountries.isEmpty()) {
            model.addAttribute("listCountries", new ArrayList<>());
        }
        model.addAttribute("listCountries", allCountries);

        List<TimeSeriesWorldDto> timeSeriesWorldDtoList = timeSeriesWorldService.getAllValuesWorld();
        if (timeSeriesWorldDtoList.isEmpty()) {
            model.addAttribute("latestDataWorld", new TimeSeriesWorldDto());
            model.addAttribute("noDataForWorldTimeSeries", true);
            log.warn("No data available for world time series");
            return TIME_SERIES;
        }

        TimeSeriesWorldDto latestDataWorld = timeSeriesWorldDtoList
                .stream()
                .findFirst()
                .orElse(new TimeSeriesWorldDto());
        String date = dateFormat.formatLastUpdateToDate(latestDataWorld.getLastUpdate());
        String time = dateFormat.formatLastUpdateToTime(latestDataWorld.getLastUpdate());
        model.addAttribute("latestDataWorld", latestDataWorld);
        model.addAttribute("lastUpdate", date + " " + time + "h");

        TimeSeriesWorldDto secondValueWorld = timeSeriesWorldDtoList
                .stream()
                .skip(1)
                .findFirst()
                .orElse(new TimeSeriesWorldDto());
        model.addAttribute("secondValueWorld", secondValueWorld);

        List<Integer> listConfirmed = timeSeriesWorldDtoList
                .stream()
                .map(TimeSeriesWorldDto::getConfirmed)
                .collect(Collectors.toList());
        Collections.reverse(listConfirmed);
        model.addAttribute("confirmed", timeSeriesCountryService.getEverySecondValue(listConfirmed));
        List<Integer> listRecovered = timeSeriesWorldDtoList
                .stream()
                .map(TimeSeriesWorldDto::getRecovered)
                .collect(Collectors.toList());
        Collections.reverse(listRecovered);
        model.addAttribute("recovered", timeSeriesCountryService.getEverySecondValue(listRecovered));
        List<Integer> listDeaths = timeSeriesWorldDtoList
                .stream()
                .map(TimeSeriesWorldDto::getDeaths)
                .collect(Collectors.toList());
        Collections.reverse(listDeaths);
        model.addAttribute("deaths", timeSeriesCountryService.getEverySecondValue(listDeaths));

        model.addAttribute("deathsRate", (double) latestDataWorld.getDeaths() / (double) latestDataWorld.getConfirmed() * 100);

        //ToDo: Diagram for daily values active, new confirmed/recovered/deaths?
        model.addAttribute("active", timeSeriesWorldDtoList
                .stream()
                .map(TimeSeriesWorldDto::getActive)
                .collect(Collectors.toList()));
        model.addAttribute("newConfirmed", timeSeriesWorldDtoList
                .stream()
                .map(TimeSeriesWorldDto::getNewConfirmed)
                .collect(Collectors.toList()));
        model.addAttribute("newRecovered", timeSeriesWorldDtoList
                .stream()
                .map(TimeSeriesWorldDto::getNewRecovered)
                .collect(Collectors.toList()));
        model.addAttribute("newDeaths", timeSeriesWorldDtoList
                .stream()
                .map(TimeSeriesWorldDto::getNewDeaths)
                .collect(Collectors.toList()));

        List<String> listDates = timeSeriesWorldDtoList
                .stream()
                .map(TimeSeriesWorldDto::getDate)
                .collect(Collectors.toList());
        Collections.reverse(listDates);
        model.addAttribute("dateTimeSeries", timeSeriesCountryService.getEverySecondDate(listDates));
        log.debug("Returned world values");

        model.addAttribute("dailyReportDto", new DailyReportDto());
        List<DailyReportDto> allValues = dailyReportService.getAllDailyCountryValues();
        if (allValues.isEmpty()) {
            model.addAttribute("noValuesAllCountries", true);
            log.warn("No values available for all countries");
            return TIME_SERIES;
        }
        model.addAttribute("totalTable", allValues
                .stream()
                .sorted(Comparator.comparing(DailyReportDto::getConfirmed)
                        .reversed())
                .collect(Collectors.toList()));
        model.addAttribute("activeTable", allValues
                .stream()
                .sorted(Comparator.comparing(DailyReportDto::getActive)
                        .reversed())
                .collect(Collectors.toList()));
        model.addAttribute("deathsTable", allValues
                .stream()
                .sorted(Comparator.comparing(DailyReportDto::getDeaths)
                        .reversed())
                .collect(Collectors.toList()));
        log.debug("Returned all values of all countries");
        return TIME_SERIES;
    }
}