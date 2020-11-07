package com.statistics.corona.controller;

import com.statistics.corona.model.dto.CountryDailyDto;
import com.statistics.corona.model.DailyReportUsDto;
import com.statistics.corona.model.DistrictDto;
import com.statistics.corona.service.CountryDailyService;
import com.statistics.corona.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@Slf4j
@RequestMapping("covid19/timeSeries")
public class ProvinceDistrictController {

    private static final String TITLE = "title";
    private final CountryService countryService;
    private final CountryDailyService countryDailyService;

    @Autowired
    public ProvinceDistrictController(CountryService countryService, CountryDailyService countryDailyService) {
        this.countryService = countryService;
        this.countryDailyService = countryDailyService;
    }

    @GetMapping("/country/provinces/{country}/{code}")
    public String showProvinceAndDistrict(@PathVariable("country") String country, @PathVariable("code") String code, Model model) {
        log.info("Invoke controller show list of provinces and districts for {}", country);
        model.addAttribute("districtDto", new DistrictDto());
        model.addAttribute("selectedCountry", country);
        getCountryNames(model);

        if (country.equals("US")) {
            getBaseDataDetailsUS(model);
            List<DailyReportUsDto> dailyReportUsDtoList = countryDailyService.getAllDailyReportsUS();
            if (dailyReportUsDtoList.isEmpty()) {
                model.addAttribute("noDetailsProvinceUs", true);
                log.warn("No values for province of US available");
                return "timeSeriesCountryDetailsUs";
            }
            model.addAttribute("allValuesProvinceUS", dailyReportUsDtoList);
            //getDistrictValues(model, code.toLowerCase());
            log.debug("Returned all data of provinces and districts for US");
            return "timeSeriesCountryDetailsUs";
        }

        List<CountryDailyDto> countryDailyDtoList = countryDailyService.getAllDailyReports();
        getBaseDataDetails(model, country);
        List<CountryDailyDto> allValuesProvince = countryDailyService.getDailyReportsOfProvince(countryDailyDtoList, country);
        if (allValuesProvince.isEmpty()) {
            model.addAttribute("noDetailsProvince", true);
            log.warn("No values for provinces and districts for country {} available", country);
            return "timeSeriesCountryDetails";
        }
        List<String> provinceWithTimeSeries = Arrays.asList("Canada", "United Kingdom", "China", "Netherlands",
                "Australia", "Denmark", "France");
        if (provinceWithTimeSeries.contains(country)) {
            model.addAttribute("timeSeriesAvailable", true);
        }
        model.addAttribute("allValuesProvince", allValuesProvince);
        //getDistrictValues(model, code.toLowerCase());
        log.debug("Returned all data of provinces and districts for country {}", country);
        return "timeSeriesCountryDetails";
    }

    private void getCountryNames(Model model) {
        List<String> allCountries = countryService.getCountryNames();
        if (allCountries.isEmpty()) {
            model.addAttribute("listCountries", Collections.emptyList());
        }
        model.addAttribute("listCountries", allCountries);
    }

    private void getBaseDataDetails(Model model, String country) {
        model.addAttribute("dailyReportDto", new CountryDailyDto());
        model.addAttribute(TITLE, "COVID-19 - Details for " + country);
    }

    private void getBaseDataDetailsUS(Model model) {
        model.addAttribute("dailyReportUsDto", new DailyReportUsDto());
        model.addAttribute(TITLE, "COVID-19 - Details for US");
    }

    public void getDistrictValues(Model model, String code) throws ExecutionException, InterruptedException {
        List<DistrictDto> selectedDistrictValues = countryDailyService.getDistrictValuesOfSelectedCountry(code);
        if (selectedDistrictValues.isEmpty()) {
            model.addAttribute("noDistrictValues", true);
        }
        model.addAttribute("districtValues", selectedDistrictValues);
    }
}
