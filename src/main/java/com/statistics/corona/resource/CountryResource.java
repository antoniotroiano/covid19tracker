package com.statistics.corona.resource;

import com.statistics.corona.model.dto.CountryDailyDto;
import com.statistics.corona.model.dto.CountryLatestDto;
import com.statistics.corona.model.dto.CountryValuesDto;
import com.statistics.corona.service.CountryDailyService;
import com.statistics.corona.service.CountryService;
import com.statistics.corona.service.DateFormat;
import com.statistics.corona.utils.ResourceUtils;
import com.statistics.corona.utils.ServiceUtils;
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
public class CountryResource {

    private final CountryService countryService;
    private final CountryDailyService countryDailyService;
    private final ResourceUtils resourceUtils;
    private final DateFormat dateFormat;
    private final ServiceUtils serviceUtils;

    public CountryResource(CountryService countryService,
                           CountryDailyService countryDailyService,
                           ResourceUtils resourceUtils,
                           DateFormat dateFormat,
                           ServiceUtils serviceUtils) {
        this.countryService = countryService;
        this.countryDailyService = countryDailyService;
        this.resourceUtils = resourceUtils;
        this.dateFormat = dateFormat;
        this.serviceUtils = serviceUtils;
    }

    @GetMapping("/country/{country}")
    public String showCountryTimeSeries(@PathVariable("country") String country, Model model) {
        log.info("Invoke controller show time series of country {}", country);
        model.addAttribute("countryValuesDto", new CountryValuesDto());
        model.addAttribute("countryDailyDto", new CountryDailyDto());
        model.addAttribute("countryLatestDto", new CountryLatestDto());
        model.addAttribute("title", "COVID-19 - Data for " + country);
        model.addAttribute("selectedCountry", country);
        getCountryNames(model);

        checkMoreDetailsAvailable(model, country);

        Optional<CountryValuesDto> countryValuesDto = countryService.getSelectedCountryValues(country);
        if (country.equals("US")) {
            country = "United States";
        }
        Optional<CountryLatestDto> countryLatestDto = countryDailyService.getCountryLatestDto(country);
        if (countryValuesDto.isPresent()) {
            countryLatestDto.ifPresent(latestDto -> model.addAttribute("countryLatestDto", new CountryLatestDto(latestDto)));
            model.addAttribute("updated", dateFormat.formatUnixToDate(countryValuesDto.get().getUpdated()));
            model.addAttribute("countryValuesDto", new CountryValuesDto(countryValuesDto.get()));

            getBaseDataCountry(model, countryValuesDto.get());
            resourceUtils.getValues(model, countryValuesDto.get(), null, null, null);
            log.debug("Get data for selected country {}", country);
            return "countryUI";
        }
        getBaseDataCountry(model, new CountryValuesDto()); //ToDo: das kann so nicht bleiben. Durch das leere Objekt fliegt ein 500er
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
        model.addAttribute("dailyTrendConfirmed", serviceUtils.getDailyTrend(countryValuesDto.getCasesValues().values()));
        model.addAttribute("dailyTrendRecovered", serviceUtils.getDailyTrend(countryValuesDto.getRecoveredValues().values()));
        model.addAttribute("dailyTrendDeaths", serviceUtils.getDailyTrend(countryValuesDto.getDeathsValues().values()));
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