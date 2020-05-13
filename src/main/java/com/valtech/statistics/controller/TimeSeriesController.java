package com.valtech.statistics.controller;

import com.valtech.statistics.model.CountryDetailsDto;
import com.valtech.statistics.model.TimeSeriesDto;
import com.valtech.statistics.service.DateFormat;
import com.valtech.statistics.service.TimeSeriesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("v2/covid19/timeSeries")
@RequiredArgsConstructor
public class TimeSeriesController {

    private static final String TIME_SERIES = "timeSeries";
    private static final String CONFIRMED_LIST = "confirmedList";
    private static final String CONFIRMED_RESULT = "confirmedResult";
    private static final String RECOVERED_RESULT = "recoveredResult";
    private static final String DEATHS_RESULT = "deathsResult";
    private final TimeSeriesService timeSeriesService;
    private final DateFormat dateFormat;

    @GetMapping("/{country}")
    public String showTimeSeries(@PathVariable("country") String country, Model model) {
        log.info("Invoke v2 controller show time series of country {}", country);
        model.addAttribute("timeSeriesDto", new TimeSeriesDto());
        model.addAttribute("countryDetailsDto", new CountryDetailsDto());
        model.addAttribute("title", "COVID-19 - Data for " + country);
        model.addAttribute("selectedCountry", country);

        List<String> allCountries = timeSeriesService.getCountry();
        CountryDetailsDto countryDetailsDto = timeSeriesService.getDetailsForCountry(country);
        if (countryDetailsDto.getCountry() == null) {
            model.addAttribute("noDataForThisCountry", "No dataset at the moment for " + country + "!");
            return TIME_SERIES;
        }
        model.addAttribute("listCountries", allCountries);
        model.addAttribute("countryDetails", countryDetailsDto);
        String date = dateFormat.formatLastUpdateToDate(countryDetailsDto.getLastUpdate());
        String time = dateFormat.formatLastUpdateToTime(countryDetailsDto.getLastUpdate());
        model.addAttribute("date", date + " " + time + "h");

        Map<String, List<TimeSeriesDto>> getAllValuesSelectedCountry = timeSeriesService.getValuesSelectedCountry(country);
        if (getAllValuesSelectedCountry.isEmpty()) {
            model.addAttribute("noDataForThisCountry", "No dataset for " + country + ". Please try again later.");
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

            List<String> datesList = getOneObject.get().getDataMap().entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
            if (getAllValuesSelectedCountry.get(CONFIRMED_LIST).stream().filter(c -> c.getCountry() != null).collect(Collectors.toList()).size() > 1) {
                model.addAttribute("moreDetailsAvailable", true);
                getBaseData(model, finalResult, datesList);
                log.info("Get data for selected country {}, with extra details", country);
                return TIME_SERIES;
            }
            getBaseData(model, finalResult, datesList);
            log.info("Get data for selected country {}", country);
            return TIME_SERIES;
        }
        model.addAttribute("somethingWrong", "Something getting wrong please try again later");
        log.warn("Something getting wrong {}", country);
        return TIME_SERIES;
    }

    @GetMapping("/{country}/details")
    public String showDetailsOfSelectedCountry(@PathVariable("country") String country, Model model) {
        //Hier kann ich das ReasDailyRepots benutzen :)
        return "timeSeriesDetails";
    }

    private void getBaseData(Model model, Map<String, List<Integer>> result, List<String> datesList) {
        model.addAttribute("confirmedYesterday", timeSeriesService.getLastValues(result.get(CONFIRMED_RESULT)));
        model.addAttribute("recoveredYesterday", timeSeriesService.getLastValues(result.get(RECOVERED_RESULT)));
        model.addAttribute("deathsYesterday", timeSeriesService.getLastValues(result.get(DEATHS_RESULT)));

        model.addAttribute(CONFIRMED_LIST, result.get(CONFIRMED_RESULT));
        model.addAttribute("recoveredList", result.get(CONFIRMED_RESULT));
        model.addAttribute("deathsList", result.get(DEATHS_RESULT));
        model.addAttribute("dateList", datesList);

        model.addAttribute("dailyTrendConfirmed", timeSeriesService.getOneDayValues(result.get(CONFIRMED_RESULT)));
        model.addAttribute("dailyTrendRecovered", timeSeriesService.getOneDayValues(result.get(CONFIRMED_RESULT)));
        model.addAttribute("dailyTrendDeaths", timeSeriesService.getOneDayValues(result.get(DEATHS_RESULT)));
    }
}