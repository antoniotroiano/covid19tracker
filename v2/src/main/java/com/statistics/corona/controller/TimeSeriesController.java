package com.statistics.corona.controller;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.DailyReportUsDto;
import com.statistics.corona.model.TimeSeriesDto;
import com.statistics.corona.service.DateFormat;
import com.statistics.corona.service.TimeSeriesDetailsService;
import com.statistics.corona.service.TimeSeriesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("v2/covid19/timeSeries")
@RequiredArgsConstructor
public class TimeSeriesController {

    private static final String TIME_SERIES = "timeSeries";
    private static final String CONFIRMED_RESULT = "confirmedResult";
    private static final String RECOVERED_RESULT = "recoveredResult";
    private static final String DEATHS_RESULT = "deathsResult";
    private static final String CONFIRMED_LIST = "confirmedList";
    private static final String SELECTED_COUNTRY = "selectedCountry";
    private static final String TITLE = "title";
    private static final String NO_DATA = "noDataForThisCountry";
    private final TimeSeriesService timeSeriesService;
    private final TimeSeriesDetailsService timeSeriesDetailsService;
    private final DateFormat dateFormat;

    @GetMapping("/{country}")
    public String showTimeSeries(@PathVariable("country") String country, Model model) {
        log.info("Invoke v2 controller show time series of country {}", country);
        model.addAttribute("timeSeriesDto", new TimeSeriesDto());
        model.addAttribute("countryDetailsDto", new CountryDetailsDto());
        model.addAttribute(TITLE, "COVID-19 - Data for " + country);
        model.addAttribute(SELECTED_COUNTRY, country);

        List<String> allCountries = timeSeriesService.getCountry();
        model.addAttribute("listCountries", allCountries);

        CountryDetailsDto countryDetailsDto = timeSeriesService.getDetailsForCountry(country);
        if (countryDetailsDto.getCountry() == null) {
            model.addAttribute(NO_DATA, "No dataset at the moment for " + country + "!");
            return TIME_SERIES;
        }
        model.addAttribute("countryDetails", countryDetailsDto);
        String date = dateFormat.formatLastUpdateToDate(countryDetailsDto.getLastUpdate());
        String time = dateFormat.formatLastUpdateToTime(countryDetailsDto.getLastUpdate());
        model.addAttribute("date", date + " " + time + "h");

        Map<String, List<TimeSeriesDto>> getAllValuesSelectedCountry = timeSeriesService.getValuesSelectedCountry(country);
        if (getAllValuesSelectedCountry.isEmpty()) {
            model.addAttribute(NO_DATA, "No dataset for " + country + ". Please try again later.");
            log.info("No data for the country {}", country);
            return TIME_SERIES;
        }

        Optional<TimeSeriesDto> getOneObject = getAllValuesSelectedCountry.get(CONFIRMED_LIST).stream().map(TimeSeriesDto::new).findFirst();
        if (getOneObject.isPresent()) {
            Map<String, List<Integer>> finalResult = timeSeriesService.mapFinalResultToMap(getAllValuesSelectedCountry);
            if (finalResult.isEmpty()) {
                model.addAttribute("noDataForTimeSeries", "Sorry no data found for time series. Please try again later.");
                log.warn("No data for time series found, for selected country {}", country);
                return TIME_SERIES;
            }

            List<String> datesList = new ArrayList<>(getOneObject.get().getDataMap().keySet());

            List<DailyReportDto> valuesCountries = timeSeriesDetailsService.getAllDetailsProvince(country);
            if (!valuesCountries.isEmpty() && valuesCountries.stream().filter(c -> !c.getCountry().isEmpty()).count() > 1) {
                model.addAttribute("moreDetailsAvailable", true);
                getBaseData(model, finalResult, datesList);
                log.info("Get data for selected country {}, with extra details", country);
                return TIME_SERIES;
            }
            getBaseData(model, finalResult, datesList);
            log.info("Get data for selected country {}", country);
            return TIME_SERIES;
        }
        model.addAttribute(NO_DATA, "Something getting wrong please try again later");
        log.warn("Something getting wrong {}", country);
        return TIME_SERIES;
    }

    @GetMapping("/{country}/details")
    public String showDetailsOfSelectedCountry(@PathVariable("country") String country, Model model) {
        log.info("Invoke get details for province of selected country {}", country);
        if (country.equals("US")) {
            List<DailyReportUsDto> allValuesProvinceUs = timeSeriesDetailsService.getAllDailyProvinceUs();
            if (!allValuesProvinceUs.isEmpty()) {
                createBaseDataDetailsUS(country, model);
                model.addAttribute("allValuesProvinceUS", allValuesProvinceUs);
                log.info("Returned all data of province for US");
                return "timeSeriesDetailsUs";
            }
            createBaseDataDetailsUS(country, model);
            model.addAttribute("noDetailsProvince", "No values for province of US available");
            log.warn("No values for province of US available");
            return "timeSeriesDetailsUs";
        }
        List<DailyReportDto> allValuesProvince = timeSeriesDetailsService.getAllDetailsProvince(country);
        if (!allValuesProvince.isEmpty()) {
            createBaseDataDetails(country, model);
            model.addAttribute("allValuesProvince", allValuesProvince);
            log.info("Returned all data of province for selected country {}", country);
            return "timeSeriesDetails";
        }
        createBaseDataDetails(country, model);
        model.addAttribute("noDetailsProvince", "No values for province of selected country " + country + " available");
        log.warn("No values for province of selected country {} available", country);
        return "timeSeriesDetails";
    }

    private void createBaseDataDetails(String country, Model model) {
        model.addAttribute("dailyReportDto", new DailyReportDto());
        model.addAttribute(TITLE, "COVID-19 - Details for " + country);
        model.addAttribute(SELECTED_COUNTRY, country);
    }

    private void createBaseDataDetailsUS(String country, Model model) {
        model.addAttribute("dailyReportUsDto", new DailyReportUsDto());
        model.addAttribute(TITLE, "COVID-19 - Details for US");
        model.addAttribute(SELECTED_COUNTRY, country);
    }

    private void getBaseData(Model model, Map<String, List<Integer>> result, List<String> datesList) {
        model.addAttribute("confirmedYesterday", timeSeriesService.getLastValues(result.get(CONFIRMED_RESULT)));
        model.addAttribute("recoveredYesterday", timeSeriesService.getLastValues(result.get(RECOVERED_RESULT)));
        model.addAttribute("deathsYesterday", timeSeriesService.getLastValues(result.get(DEATHS_RESULT)));

        model.addAttribute(CONFIRMED_LIST, timeSeriesService.getEverySecondValue(result.get(CONFIRMED_RESULT)));
        model.addAttribute("recoveredList", timeSeriesService.getEverySecondValue(result.get(RECOVERED_RESULT)));
        model.addAttribute("deathsList", timeSeriesService.getEverySecondValue(result.get(DEATHS_RESULT)));
        model.addAttribute("dateList", timeSeriesService.getEverySecondDate(datesList));

        model.addAttribute("dailyTrendConfirmed", timeSeriesService.getOneDayValues(result.get(CONFIRMED_RESULT)));
        model.addAttribute("dailyTrendRecovered", timeSeriesService.getOneDayValues(result.get(RECOVERED_RESULT)));
        model.addAttribute("dailyTrendDeaths", timeSeriesService.getOneDayValues(result.get(DEATHS_RESULT)));
        model.addAttribute("dateListPerDay", datesList);
    }
}