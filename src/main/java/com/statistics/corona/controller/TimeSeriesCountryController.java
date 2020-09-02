package com.statistics.corona.controller;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.TimeSeriesDto;
import com.statistics.corona.service.DailyReportService;
import com.statistics.corona.service.DateFormat;
import com.statistics.corona.service.TimeSeriesCountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("covid19/timeSeries")
public class TimeSeriesCountryController {

    private static final String TIME_SERIES = "timeSeriesCountry";
    private static final String CONFIRMED_RESULT = "confirmedResult";
    private static final String RECOVERED_RESULT = "recoveredResult";
    private static final String DEATHS_RESULT = "deathsResult";
    private static final String CONFIRMED_LIST = "confirmedList";
    private static final String RECOVERED_LIST = "recoveredList";
    private static final String DEATHS_LIST = "deathsList";
    private static final String TITLE = "title";
    private static final String NO_DATA = "noDataForThisCountry";
    private final TimeSeriesCountryService timeSeriesCountryService;
    private final DailyReportService dailyReportService;
    private final DateFormat dateFormat;

    public TimeSeriesCountryController(TimeSeriesCountryService timeSeriesCountryService, DailyReportService dailyReportService, DateFormat dateFormat) {
        this.timeSeriesCountryService = timeSeriesCountryService;
        this.dailyReportService = dailyReportService;
        this.dateFormat = dateFormat;
    }

    @GetMapping("/country/{country}")
    public String showCountryTimeSeries(@PathVariable("country") String country, Model model) {
        log.info("Invoke controller show time series of country {}", country);
        model.addAttribute("countryDetails", new CountryDetailsDto());
        model.addAttribute("dailyReport", new DailyReportDto());
        model.addAttribute(TITLE, "COVID-19 - Data for " + country);
        model.addAttribute("selectedCountry", country);
        getCountryNames(model);
        List<DailyReportDto> dailyReportDtoList = dailyReportService.getAllDailyReports();
        List<DailyReportDto> valuesCountries = dailyReportService.getDailyReportsOfProvince(dailyReportDtoList, country);
        if (!valuesCountries.isEmpty() && valuesCountries.stream().filter(c -> !c.getCountry().isEmpty()).count() > 1) {
            model.addAttribute("moreDetailsAvailable", true);
        }
        Optional<CountryDetailsDto> countryDetailsDto = dailyReportService.getSelectedCountryValues(country);
        if (countryDetailsDto.isPresent()) {
            model.addAttribute("countryDetails", new CountryDetailsDto(countryDetailsDto.get()));
            String date = dateFormat.formatUnixToDate(countryDetailsDto.get().getUpdated());
            model.addAttribute("date", date);
            Optional<DailyReportDto> dailyReportDto = dailyReportService.getDailyReportSelectedCountry(dailyReportDtoList, country);
            dailyReportDto.ifPresent(reportDto -> model.addAttribute("dailyReport", new DailyReportDto(reportDto)));
            Map<String, List<TimeSeriesDto>> countryTSValuesMap = timeSeriesCountryService.getTSValuesForOneCountry(country);
            model.addAttribute("countryCode", countryDetailsDto.get().getDataObjectCountryInfo().getIso2()); //ToDo: nicht ausfall sicher
            if (!countryTSValuesMap.isEmpty()) {
                Optional<TimeSeriesDto> getLastTSValue = timeSeriesCountryService.getLastTimeSeriesValueSelectedCountry(countryTSValuesMap);
                if (getLastTSValue.isPresent()) {
                    List<String> datesList = new ArrayList<>(getLastTSValue.get().getDataMap().keySet());
                    Map<String, List<Integer>> finalResultMap = timeSeriesCountryService.generateFinalTSResult(countryTSValuesMap);
                    if (!finalResultMap.isEmpty()) {
                        getBaseData(model, countryDetailsDto.get(), finalResultMap, datesList);
                        log.debug("Get data for selected country {}", country);
                        return TIME_SERIES;
                    }
                }
            }
        }
        getBaseData(model, new CountryDetailsDto(), Collections.emptyMap(), Collections.emptyList());
        model.addAttribute(NO_DATA, true);
        log.warn("No data for the country {}", country);
        return TIME_SERIES;
    }

    private void getBaseData(Model model, CountryDetailsDto countryDetailsDto, Map<String, List<Integer>> result, List<String> datesList) {
        int activeCases = countryDetailsDto.getConfirmed() - countryDetailsDto.getRecovered() - countryDetailsDto.getDeaths();
        model.addAttribute("activeCases", activeCases);

        int confirmedYesterday = timeSeriesCountryService.getLastValue(result.get(CONFIRMED_RESULT));
        int recoveredYesterday = timeSeriesCountryService.getLastValue(result.get(RECOVERED_RESULT));
        int deathsYesterday = timeSeriesCountryService.getLastValue(result.get(DEATHS_RESULT));
        int activeCasesYesterday = confirmedYesterday - recoveredYesterday - deathsYesterday;
        model.addAttribute("differenceActiveCases", (activeCases - activeCasesYesterday));

        model.addAttribute(CONFIRMED_LIST, timeSeriesCountryService.getEverySecondValue(result.get(CONFIRMED_RESULT)));
        model.addAttribute(RECOVERED_LIST, timeSeriesCountryService.getEverySecondValue(result.get(RECOVERED_RESULT)));
        model.addAttribute(DEATHS_LIST, timeSeriesCountryService.getEverySecondValue(result.get(DEATHS_RESULT)));
        model.addAttribute("dateList", timeSeriesCountryService.getEverySecondDate(datesList));

        model.addAttribute("dailyTrendConfirmed", timeSeriesCountryService.getOneDayValues(result.get(CONFIRMED_RESULT)));
        model.addAttribute("dailyTrendRecovered", timeSeriesCountryService.getOneDayValues(result.get(RECOVERED_RESULT)));
        model.addAttribute("dailyTrendDeaths", timeSeriesCountryService.getOneDayValues(result.get(DEATHS_RESULT)));
        model.addAttribute("dateListPerDay", datesList);
    }

    private void getCountryNames(Model model) {
        List<String> allCountries = timeSeriesCountryService.getCountryNames();
        if (allCountries.isEmpty()) {
            model.addAttribute("listCountries", Collections.emptyList());
        }
        model.addAttribute("listCountries", allCountries);
    }
}