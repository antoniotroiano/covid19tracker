package com.statistics.corona.controller;

import com.statistics.corona.model.dto.CountryDailyDto;
import com.statistics.corona.model.dto.CountryTimeSeriesDto;
import com.statistics.corona.model.dto.UsDailyDto;
import com.statistics.corona.service.CountryService;
import com.statistics.corona.service.DateFormat;
import com.statistics.corona.service.ProvinceService;
import com.statistics.corona.service.UtilsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("covid19")
public class ProvinceController {

    private static final String CONFIRMED = "confirmed";
    private static final String RECOVERED = "recovered";
    private static final String DEATHS = "deaths";
    private final CountryService countryService;
    private final ProvinceService provinceService;
    private final UtilsService utilsService;
    private final DateFormat dateFormat;

    public ProvinceController(CountryService countryService,
                              ProvinceService provinceService,
                              UtilsService utilsService,
                              DateFormat dateFormat) {
        this.countryService = countryService;
        this.provinceService = provinceService;
        this.utilsService = utilsService;
        this.dateFormat = dateFormat;
    }

    @GetMapping("/province/{province}")
    public String showProvinceTimeSeries(@PathVariable("province") String province, Model model) {
        log.info("Invoke controller show time series and details of province {}", province);
        model.addAttribute("countryDailyDto", new CountryDailyDto());
        getCountryNames(model);

        Optional<CountryDailyDto> selectedProvince = provinceService.getProvinceDetails(province);
        if (selectedProvince.isPresent()) {
            model.addAttribute("countryDailyDto", new CountryDailyDto(selectedProvince.get()));
            model.addAttribute("date",
                    dateFormat.formatLastUpdateToDateDaily(selectedProvince.get().getLastUpdate()) + " " +
                            dateFormat.formatLastUpdateToTimeDaily(selectedProvince.get().getLastUpdate()) + "h");

            Map<String, Optional<CountryTimeSeriesDto>> provinceTSValuesMap = provinceService.getProvinceTSValues(province);
            if (!provinceTSValuesMap.isEmpty()) {
                getBaseDataProvince(model, provinceTSValuesMap, province);
                log.debug("Return all values for selected province {}", province);
                return "provinceUI";
            }
        }
        getBaseDataProvince(model, Collections.emptyMap(), province);
        model.addAttribute("noDataForProvince", true);
        log.warn("No values found for the province {}", province);
        return "provinceUI";
    }

    @GetMapping("/province/us/{usProvince}")
    public String showUsProvinceTimeSeries(@PathVariable("usProvince") String usProvince, Model model) {
        log.info("Invoke controller show time series and details for us province {}", usProvince);
        model.addAttribute("usDailyDto", new UsDailyDto());
        getCountryNames(model);

        Optional<UsDailyDto> dailyReportUsDto = provinceService.getUsProvinceDetails(usProvince);
        if (dailyReportUsDto.isPresent()) {
            model.addAttribute("usDailyDto", new UsDailyDto(dailyReportUsDto.get()));
            model.addAttribute("date",
                    dateFormat.formatLastUpdateToDateDaily(dailyReportUsDto.get().getLastUpdate()) + " " +
                            dateFormat.formatLastUpdateToTimeDaily(dailyReportUsDto.get().getLastUpdate()) + "h");

            Map<String, Map<String, Integer>> usProvinceTSValuesMap = provinceService.getUsProvinceTSValues(usProvince);
            if (!usProvinceTSValuesMap.isEmpty()) {
                model.addAttribute("population", provinceService.getUsProvincePopulation(usProvince));
                getBaseDataUsProvince(model, usProvinceTSValuesMap, usProvince);
                log.debug("Return all values for selected us province {}", usProvince);
                return "provinceUsUI";
            }
        }
        getBaseDataUsProvince(model, Collections.emptyMap(), usProvince);
        model.addAttribute("noDataForProvince", true);
        log.warn("No object found with the province {}", usProvince);
        return "provinceUsUI";
    }

    private void getBaseDataProvince(Model model, Map<String, Optional<CountryTimeSeriesDto>> provinceTSValuesMap, String province) {
        model.addAttribute("timeSeriesDto", new CountryTimeSeriesDto());
        model.addAttribute("title", "COVID-19 - Details for " + province);
        model.addAttribute("selectedProvince", province);

        provinceTSValuesMap.get(CONFIRMED)
                .ifPresent(confirmedList -> model.addAttribute("confirmedList", confirmedList.getValues().values()));
        provinceTSValuesMap.get(RECOVERED)
                .ifPresent(recoveredList -> model.addAttribute("recoveredList", recoveredList.getValues().values()));
        provinceTSValuesMap.get(DEATHS)
                .ifPresent(deathsList -> model.addAttribute("deathsList", deathsList.getValues().values()));
        provinceTSValuesMap.get(CONFIRMED)
                .ifPresent(dateList -> model.addAttribute("dateList", dateList.getValues().keySet()));

        provinceTSValuesMap.get(CONFIRMED)
                .ifPresent(dailyTrendConfirmed
                        -> model.addAttribute("dailyTrendConfirmed", utilsService.getDailyTrend(dailyTrendConfirmed.getValues())));
        provinceTSValuesMap.get(RECOVERED)
                .ifPresent(dailyTrendRecovered
                        -> model.addAttribute("dailyTrendRecovered", utilsService.getDailyTrend(dailyTrendRecovered.getValues())));
        provinceTSValuesMap.get(DEATHS)
                .ifPresent(dailyTrendDeaths
                        -> model.addAttribute("dailyTrendDeaths", utilsService.getDailyTrend(dailyTrendDeaths.getValues())));
    }

    private void getBaseDataUsProvince(Model model, Map<String, Map<String, Integer>> provinceTSValuesMap, String province) {
        model.addAttribute("timeSeriesDto", new CountryTimeSeriesDto());
        model.addAttribute("title", "COVID-19 - Details for " + province);
        model.addAttribute("selectedProvince", province);

        model.addAttribute("confirmedList", provinceTSValuesMap.get(CONFIRMED).values());
        model.addAttribute("deathsList", provinceTSValuesMap.get(DEATHS).values());
        model.addAttribute("dateList", provinceTSValuesMap.get(CONFIRMED).keySet());

        model.addAttribute("dailyTrendConfirmed", utilsService.getDailyTrend(provinceTSValuesMap.get(CONFIRMED)));
        model.addAttribute("dailyTrendDeaths", utilsService.getDailyTrend(provinceTSValuesMap.get(DEATHS)));
    }

    private void getCountryNames(Model model) {
        List<String> allCountries = countryService.getCountryNames();
        if (allCountries.isEmpty()) {
            model.addAttribute("listCountries", Collections.emptyList());
        }
        model.addAttribute("listCountries", allCountries);
    }
}