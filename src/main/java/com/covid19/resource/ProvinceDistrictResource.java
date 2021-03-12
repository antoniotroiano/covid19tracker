package com.covid19.resource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.covid19.model.dto.CountryDailyDto;
import com.covid19.model.dto.UsDailyDto;
import com.covid19.service.CountryDailyService;
import com.covid19.service.CountryService;
import com.covid19.service.ProvinceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("covid19")
public class ProvinceDistrictResource {

    private final CountryService countryService;
    private final CountryDailyService countryDailyService;
    private final ProvinceService provinceService;

    @Autowired
    public ProvinceDistrictResource(CountryService countryService,
                                    CountryDailyService countryDailyService,
                                    ProvinceService provinceService) {
        this.countryService = countryService;
        this.countryDailyService = countryDailyService;
        this.provinceService = provinceService;
    }

    @GetMapping("/provinces/{country}/{code}")
    public String showProvinceAndDistrict(@PathVariable("country") String country, @PathVariable("code") String code, Model model) {
        log.info("Invoke controller show list of provinces and districts for {}", country);
        model.addAttribute("selectedCountry", country);
        getCountryNames(model);

        if (country.equals("US")) {
            getBaseDataDetailsUS(model);
            List<UsDailyDto> usDailyDtoList = countryDailyService.getAllDailyReportsUS();
            if (usDailyDtoList.isEmpty()) {
                model.addAttribute("noDetailsProvinceUs", true);
                log.warn("No values for province of US available");
                return "provinceDistrictUsUI";
            }
            model.addAttribute("usDailyDto", usDailyDtoList);
            log.debug("Returned all data of provinces and districts for US");
            return "provinceDistrictUsUI";
        }

        getBaseDataDetails(model, country);
        List<CountryDailyDto> countryDailyDtoList = provinceService.getEnrichedCountryValues(country);
        if (countryDailyDtoList.isEmpty()) {
            model.addAttribute("noDetailsProvince", true);
            log.warn("No values for provinces and districts for country {} available", country);
            return "provinceDistrictUI";
        }
        List<String> provinceWithTimeSeries = Arrays.asList("Australia", "China", "Denmark", "France",
                "Netherlands", "United Kingdom");
        if (provinceWithTimeSeries.contains(country)) {
            model.addAttribute("timeSeriesAvailable", true);
        }
        model.addAttribute("countryDailyDto", countryDailyDtoList);
        getYesterdayValues(model, country);
        log.debug("Returned all data of provinces and districts for country {}", country);
        return "provinceDistrictUI";
    }

    private void getCountryNames(Model model) {
        List<String> allCountries = countryService.getCountryNames();
        if (allCountries.isEmpty()) {
            model.addAttribute("listCountries", Collections.emptyList());
        }
        model.addAttribute("listCountries", allCountries);
    }

    private void getBaseDataDetails(Model model, String country) {
        model.addAttribute("countryDailyDto", new CountryDailyDto());
        model.addAttribute("title", "COVID-19 - Details for " + country);
    }

    private void getBaseDataDetailsUS(Model model) {
        model.addAttribute("usDailyDto", new UsDailyDto());
        model.addAttribute("title", "COVID-19 - Details for US");
    }

    private void getYesterdayValues(Model model, String country) {
        List<CountryDailyDto> yesterdayValues = provinceService.getYesterdayValuesOfCountry(country);
        if (yesterdayValues.isEmpty()) {
            model.addAttribute("yesterdayValues", Collections.emptyList());
        }
        model.addAttribute("yesterdayValues", yesterdayValues);
    }
}