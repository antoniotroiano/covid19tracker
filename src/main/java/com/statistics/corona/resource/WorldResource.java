package com.statistics.corona.resource;

import com.statistics.corona.model.dto.CountryDailyDto;
import com.statistics.corona.model.dto.CountryValuesDto;
import com.statistics.corona.model.dto.WorldValuesDto;
import com.statistics.corona.service.CountryDailyService;
import com.statistics.corona.service.CountryService;
import com.statistics.corona.service.DateFormat;
import com.statistics.corona.service.WorldService;
import com.statistics.corona.utils.ResourceUtils;
import com.statistics.corona.utils.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("covid19")
public class WorldResource {

    private static final String TIME_SERIES = "worldUI";
    private final WorldService worldService;
    private final CountryService countryService;
    private final CountryDailyService countryDailyService;
    private final ServiceUtils serviceUtils;
    private final ResourceUtils resourceUtils;
    private final DateFormat dateFormat;

    @Autowired
    public WorldResource(WorldService worldService,
                         CountryService countryService,
                         CountryDailyService countryDailyService,
                         ServiceUtils serviceUtils,
                         ResourceUtils resourceUtils,
                         DateFormat dateFormat) {
        this.worldService = worldService;
        this.countryService = countryService;
        this.countryDailyService = countryDailyService;
        this.serviceUtils = serviceUtils;
        this.resourceUtils = resourceUtils;
        this.dateFormat = dateFormat;
    }

    @GetMapping("/world")
    public String showWorldTimeSeries(Model model) {
        log.info("Invoke controller for time series world");
        model.addAttribute("worldValuesDto", new WorldValuesDto());
        model.addAttribute("countryDailyDto", new CountryDailyDto());
        model.addAttribute("listCountries", countryService.getCountryNames());

        Optional<WorldValuesDto> worldValuesDto = worldService.getWorldValues();
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
        model.addAttribute("dateList", worldValuesDto.get().getWorldTimeSeriesDto().getCases().keySet());
        model.addAttribute("confirmedList", new ArrayList<>(worldValuesDto.get().getWorldTimeSeriesDto().getCases().values()));
        model.addAttribute("recoveredList", new ArrayList<>(worldValuesDto.get().getWorldTimeSeriesDto().getRecovered().values()));
        model.addAttribute("deathsList", new ArrayList<>(worldValuesDto.get().getWorldTimeSeriesDto().getDeaths().values()));

        //Incident Rate: sum new confirmed of a period divided by the size of population * 100
        model.addAttribute("dailyConfirmed", serviceUtils.getDailyTrend(worldValuesDto.get().getWorldTimeSeriesDto().getCases().values()));
        model.addAttribute("dailyRecovered", serviceUtils.getDailyTrend(worldValuesDto.get().getWorldTimeSeriesDto().getRecovered().values()));
        model.addAttribute("dailyDeaths", serviceUtils.getDailyTrend(worldValuesDto.get().getWorldTimeSeriesDto().getDeaths().values()));

        resourceUtils.getValues(model, null, worldValuesDto.get(), null, null);
        List<CountryDailyDto> allValues = countryDailyService.getAllDailyCountryValuesCalculated();
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