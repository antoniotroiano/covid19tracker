package com.valtech.statistics.controller;

import com.valtech.statistics.model.WorldTimeSeriesDto;
import com.valtech.statistics.service.DateFormat;
import com.valtech.statistics.service.TimeSeriesService;
import com.valtech.statistics.service.json.ReadJSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/v2/covid19")
@RequiredArgsConstructor
public class WorldTimeSeriesController {

    private final ReadJSON readJSON;
    private final TimeSeriesService timeSeriesService;
    private final DateFormat dateFormat;

    @GetMapping
    public String showWorldTimeSeries(Model model) throws IOException {
        log.info("Invoke v2 controller show time series world");
        model.addAttribute("worldTimeSeriesDto", new WorldTimeSeriesDto());

        List<String> allCountries = timeSeriesService.getCountry();
        model.addAttribute("listCountries", allCountries);

        List<WorldTimeSeriesDto> worldTimeSeriesDtoList = readJSON.readWorldValues();
        if (worldTimeSeriesDtoList.isEmpty()) {
            model.addAttribute("noDataForWorldTimeSeries",
                    "No data available for world time series. Please try again later.");
            log.warn("No data available for world time series");
            return "timeSeriesWorld";
        }

        WorldTimeSeriesDto latestDataWorld = worldTimeSeriesDtoList
                .stream()
                .findFirst()
                .orElse(new WorldTimeSeriesDto());
        String date = dateFormat.formatLastUpdateToDate(latestDataWorld.getLastUpdate());
        String time = dateFormat.formatLastUpdateToTime(latestDataWorld.getLastUpdate());
        model.addAttribute("latestDataWorld", latestDataWorld);
        model.addAttribute("lastUpdate", date + " " + time + "h");

        List<Integer> listConfirmed = worldTimeSeriesDtoList
                .stream()
                .map(WorldTimeSeriesDto::getConfirmed)
                .collect(Collectors.toList());
        Collections.reverse(listConfirmed);
        model.addAttribute("confirmed", listConfirmed);
        List<Integer> listRecovered = worldTimeSeriesDtoList
                .stream()
                .map(WorldTimeSeriesDto::getRecovered)
                .collect(Collectors.toList());
        Collections.reverse(listRecovered);
        model.addAttribute("recovered", listRecovered);
        List<Integer> listDeaths = worldTimeSeriesDtoList
                .stream()
                .map(WorldTimeSeriesDto::getDeaths)
                .collect(Collectors.toList());
        Collections.reverse(listDeaths);
        model.addAttribute("deaths", listDeaths);
        model.addAttribute("active", worldTimeSeriesDtoList
                .stream()
                .map(WorldTimeSeriesDto::getActive)
                .collect(Collectors.toList()));
        model.addAttribute("newConfirmed", worldTimeSeriesDtoList
                .stream()
                .map(WorldTimeSeriesDto::getNewConfirmed)
                .collect(Collectors.toList()));
        model.addAttribute("newRecovered", worldTimeSeriesDtoList
                .stream()
                .map(WorldTimeSeriesDto::getNewRecovered)
                .collect(Collectors.toList()));
        model.addAttribute("newDeaths", worldTimeSeriesDtoList
                .stream()
                .map(WorldTimeSeriesDto::getNewDeaths)
                .collect(Collectors.toList()));
        List<String> listDates = worldTimeSeriesDtoList
                .stream()
                .map(WorldTimeSeriesDto::getDate)
                .collect(Collectors.toList());
        Collections.reverse(listDates);
        model.addAttribute("dateTimeSeries", listDates);
        return "timeSeriesWorld";
    }
}
