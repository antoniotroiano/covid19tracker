package com.statistics.corona.controller;

import com.statistics.corona.model.dto.CountryDailyDto;
import com.statistics.corona.model.dto.CountryValuesDto;
import com.statistics.corona.model.dto.WorldValuesDto;
import com.statistics.corona.service.CountryDailyService;
import com.statistics.corona.service.CountryService;
import com.statistics.corona.service.DateFormat;
import com.statistics.corona.service.UtilsService;
import com.statistics.corona.service.WorldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("covid19")
public class WorldController {

    private static final String TIME_SERIES = "worldUI";
    private final WorldService worldService;
    private final CountryService countryService;
    private final CountryDailyService countryDailyService;
    private final UtilsService utilsService;
    private final DateFormat dateFormat;

    @Autowired
    public WorldController(WorldService worldService,
                           CountryService countryService,
                           CountryDailyService countryDailyService,
                           UtilsService utilsService,
                           DateFormat dateFormat) {
        this.worldService = worldService;
        this.countryService = countryService;
        this.countryDailyService = countryDailyService;
        this.utilsService = utilsService;
        this.dateFormat = dateFormat;
    }

    @GetMapping("/world")
    public String showWorldTimeSeries(@RequestParam(value = "value") String value, Model model) {
        log.info("Invoke controller for time series world");
        model.addAttribute("worldValuesDto", new WorldValuesDto());
        model.addAttribute("countryDailyDto", new CountryDailyDto());
        model.addAttribute("listCountries", countryService.getCountryNames());

        Optional<WorldValuesDto> worldValuesDto = worldService.getWorldValues(value);
        if (worldValuesDto.isEmpty()) {
            model.addAttribute("latestDataWorld", new CountryValuesDto());
            model.addAttribute("noDataForWorldTimeSeries", true);
            log.warn("No data available for world time series");
            return TIME_SERIES;
        }

        model.addAttribute("worldValuesDto", worldValuesDto.get());
        model.addAttribute("lastUpdate",
                dateFormat.formatLastUpdateToDate(worldValuesDto.get().getLastUpdate()) + " " +
                        dateFormat.formatLastUpdateToTime(worldValuesDto.get().getLastUpdate()) + " h");
        model.addAttribute("activeYesterday", worldService.getYesterdayActive(worldValuesDto));
        model.addAttribute("dateTS", worldValuesDto.get().getWorldTimeSeriesDto().getCases().keySet());
        model.addAttribute("confirmed", new ArrayList<>(worldValuesDto.get().getWorldTimeSeriesDto().getCases().values()));
        model.addAttribute("recovered", new ArrayList<>(worldValuesDto.get().getWorldTimeSeriesDto().getRecovered().values()));
        model.addAttribute("deaths", new ArrayList<>(worldValuesDto.get().getWorldTimeSeriesDto().getDeaths().values()));

        //ToDo: Diagram for daily active values
        //Incident Rate: sum new confirmed of a period divided by the size of population * 100
        model.addAttribute("dailyConfirmed", utilsService.getDailyTrend(worldValuesDto.get().getWorldTimeSeriesDto().getCases()));
        model.addAttribute("dailyRecovered", utilsService.getDailyTrend(worldValuesDto.get().getWorldTimeSeriesDto().getRecovered()));
        model.addAttribute("dailyDeaths", utilsService.getDailyTrend(worldValuesDto.get().getWorldTimeSeriesDto().getDeaths()));

        List<CountryDailyDto> dailyReportServiceList = countryDailyService.getAllDailyReports();
        List<CountryDailyDto> allValues = countryDailyService.getAllDailyCountryValuesCalculated(dailyReportServiceList);
        if (allValues.isEmpty()) {
            model.addAttribute("noValuesAllCountries", true);
            log.warn("No values available for all countries");
            return TIME_SERIES;
        }
        model.addAttribute("totalTable", worldService.getEachCountriesConfirmedDescending(allValues));
        model.addAttribute("activeTable", worldService.getEachCountriesActiveDescending(allValues));
        model.addAttribute("deathsTable", worldService.getEachCountriesDeathsDescending(allValues));
        log.debug("Returned all values of all countries and global values");
        return TIME_SERIES;
    }
}