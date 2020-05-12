package com.valtech.statistics.controller;

import com.valtech.statistics.model.CountryDetailsDto;
import com.valtech.statistics.model.SummaryToday;
import com.valtech.statistics.model.TimeSeriesDto;
import com.valtech.statistics.service.DateFormat;
import com.valtech.statistics.service.TimeSeriesService;
import com.valtech.statistics.service.json.GetJsonValue;
import com.valtech.statistics.service.json.ReadJSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("v2/covid19/timeSeries")
@RequiredArgsConstructor
public class TimeSeriesController {

    private final TimeSeriesService timeSeriesService;
    private final GetJsonValue getJsonValue;
    private final ReadJSON readJSON;
    private final DateFormat dateFormat;

    @GetMapping("/{country}")
    public String showTimeSeries(@PathVariable("country") String country, Model model) throws IOException {
        log.info("Invoke v2 controller show time series of country {}", country);
        model.addAttribute("countryDetailsDto", new CountryDetailsDto());
        model.addAttribute("countryDetails", readJSON.readDetailsForCountry(country));
        List<String> allCountries = getJsonValue.getCountryOfJSONObject();
        model.addAttribute("listCountries", allCountries);
        model.addAttribute("timeSeriesDto", new TimeSeriesDto());
        model.addAttribute("title", "COVID-19 - Data for " + country);
        model.addAttribute("selectedCountry", country);

        SummaryToday summaryToday = getJsonValue.getDataForSelectedCountry(country);
        model.addAttribute("summaryToday", new SummaryToday());
        model.addAttribute("dataSummaryToday", summaryToday);
        String date = dateFormat.formatLastUpdateToDate(summaryToday.getLastUpdate());
        String time = dateFormat.formatLastUpdateToTime(summaryToday.getLastUpdate());
        model.addAttribute("date", date + " " + time + "h");

        Map<String, List<TimeSeriesDto>> getAllValuesSelectedCountry = timeSeriesService.getValuesSelectedCountry(country);
        if (getAllValuesSelectedCountry.isEmpty()) {
            model.addAttribute("noDataForThisCountry", "No dataset for " + country + ". Please try again later.");
            log.info("No data for the country {}", country);
            return "timeSeries";
        }

        Optional<TimeSeriesDto> getOneObject = getAllValuesSelectedCountry.get("confirmedList").stream().map(TimeSeriesDto::new).findFirst();
        if (getOneObject.isPresent()) {
            Map<String, List<Integer>> finalResult = timeSeriesService.mapFinalResultToMap(getAllValuesSelectedCountry);
            if (finalResult.isEmpty()) {
                model.addAttribute("noDataForTimeSeries", "Sorry no data found for time series. Please try again later.");
                log.warn("No data for time series found, for selected country {}", country);
                return "timeSeries";
            }

            List<String> datesList = getOneObject.get().getDataMap().entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
            if (getAllValuesSelectedCountry.get("confirmedList").stream().filter(c -> c.getCountry() != null).collect(Collectors.toList()).size() > 1) {
                model.addAttribute("moreDetailsAvailable", true);
                getBaseData(model, finalResult, datesList);
                log.info("Get data for selected country {}, with extra details", country);
                return "timeSeries";
            }
            getBaseData(model, finalResult, datesList);
            log.info("Get data for selected country {}", country);
            return "timeSeries";
        }
        model.addAttribute("somethingWrong", "Something getting wrong please try again later");
        log.warn("Something getting wrong {}", country);
        return "timeSeries";
    }

    @GetMapping("/{country}/details")
    public String showDetailsOfSelectedCountry(@PathVariable("country") String country, Model model) {
        //Hier kann ich das ReasDailyRepots benutzen :)
        return "timeSeriesDetails";
    }

    private void getBaseData(Model model, Map<String, List<Integer>> result, List<String> datesList) {
        model.addAttribute("confirmedYesterday", timeSeriesService.getLastValues(result.get("confirmedResult")));
        model.addAttribute("recoveredYesterday", timeSeriesService.getLastValues(result.get("recoveredResult")));
        model.addAttribute("deathsYesterday", timeSeriesService.getLastValues(result.get("deathsResult")));

        model.addAttribute("confirmedList", result.get("confirmedResult"));
        model.addAttribute("recoveredList", result.get("recoveredResult"));
        model.addAttribute("deathsList", result.get("deathsResult"));
        model.addAttribute("dateList", datesList);

        model.addAttribute("dailyTrendConfirmed", timeSeriesService.getOneDayValues(result.get("confirmedResult")));
        model.addAttribute("dailyTrendRecovered", timeSeriesService.getOneDayValues(result.get("recoveredResult")));
        model.addAttribute("dailyTrendDeaths", timeSeriesService.getOneDayValues(result.get("deathsResult")));
    }
}