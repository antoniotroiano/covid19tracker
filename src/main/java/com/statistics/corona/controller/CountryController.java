package com.statistics.corona.controller;

import com.statistics.corona.model.dto.CountryDailyDto;
import com.statistics.corona.model.dto.CountryValuesDto;
import com.statistics.corona.service.CountryDailyService;
import com.statistics.corona.service.CountryService;
import com.statistics.corona.service.DateFormat;
import com.statistics.corona.service.UtilsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("covid19")
public class CountryController {

    private final CountryService countryService;
    private final CountryDailyService countryDailyService;
    private final DateFormat dateFormat;
    private final UtilsService utilsService;

    public CountryController(CountryService countryService,
                             CountryDailyService countryDailyService,
                             DateFormat dateFormat,
                             UtilsService utilsService) {
        this.countryService = countryService;
        this.countryDailyService = countryDailyService;
        this.dateFormat = dateFormat;
        this.utilsService = utilsService;
    }

    @GetMapping("/country/{country}")
    public String showCountryTimeSeries(@PathVariable("country") String country, Model model) {
        log.info("Invoke controller show time series of country {}", country);
        model.addAttribute("countryValuesDto", new CountryValuesDto());
        model.addAttribute("countryDailyDto", new CountryDailyDto());
        model.addAttribute("title", "COVID-19 - Data for " + country);
        model.addAttribute("selectedCountry", country);
        getCountryNames(model);

        List<CountryDailyDto> countryDailyDtoList = countryDailyService.getAllDailyReports();
        checkMoreDetailsAvailable(model, country);

        Optional<CountryValuesDto> countryValuesDto = countryService.getSelectedCountryValues(country);
        if (countryValuesDto.isPresent()) {
            model.addAttribute("updated", dateFormat.formatUnixToDate(countryValuesDto.get().getUpdated()));
            model.addAttribute("countryValuesDto", new CountryValuesDto(countryValuesDto.get()));

            Optional<CountryDailyDto> countryDailyDto =
                    countryDailyService.getDailyReportSelectedCountry(countryDailyDtoList, country);
            countryDailyDto
                    .ifPresent(reportDto ->
                            model.addAttribute("countryDailyDto", new CountryDailyDto(reportDto)));

            getBaseDataCountry(model, countryValuesDto.get());
            log.debug("Get data for selected country {}", country);
            return "countryUI";
        }
        getBaseDataCountry(model, new CountryValuesDto());
        model.addAttribute("noDataForThisCountry", true);
        log.warn("No data for the country available {}", country);
        return "countryUI";
    }

    private void getBaseDataCountry(Model model, CountryValuesDto countryValuesDto) {
        model.addAttribute("differenceActiveCases", (countryValuesDto.getActive() -
                countryService.calculateYesterdayActive(countryValuesDto)));
        model.addAttribute("confirmedList", countryValuesDto.getCasesValues().values());
        model.addAttribute("recoveredList", countryValuesDto.getRecoveredValues().values());
        model.addAttribute("deathsList", countryValuesDto.getDeathsValues().values());
        model.addAttribute("dateList", countryValuesDto.getCasesValues().keySet());
        model.addAttribute("dailyTrendConfirmed", utilsService.getDailyTrend(countryValuesDto.getCasesValues()));
        model.addAttribute("dailyTrendRecovered", utilsService.getDailyTrend(countryValuesDto.getRecoveredValues()));
        model.addAttribute("dailyTrendDeaths", utilsService.getDailyTrend(countryValuesDto.getDeathsValues()));
    }

    private void getCountryNames(Model model) {
        List<String> allCountries = countryService.getCountryNames();
        if (allCountries.isEmpty()) {
            model.addAttribute("listCountries", Collections.emptyList());
        }
        model.addAttribute("listCountries", allCountries);
    }

    private void checkMoreDetailsAvailable(Model model, String country) {
        List<CountryDailyDto> valuesCountries = countryDailyService.getAllDailyReportsOfProvince(country);
        if (!valuesCountries.isEmpty() && valuesCountries.stream().filter(c -> !c.getCountry().isEmpty()).count() > 1) {
            model.addAttribute("moreDetailsAvailable", true);
        }
    }
}