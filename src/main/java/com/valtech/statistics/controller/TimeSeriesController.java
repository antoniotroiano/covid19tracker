package com.valtech.statistics.controller;

import com.valtech.statistics.model.SummaryToday;
import com.valtech.statistics.model.TimeSeriesDto;
import com.valtech.statistics.service.DateFormat;
import com.valtech.statistics.service.TimeSeriesService;
import com.valtech.statistics.service.json.GetJsonValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
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
    private final DateFormat dateFormat;

    @GetMapping("/{country}")
    public String showTimeSeries(@PathVariable("country") String country, Model model) throws IOException {
        log.info("Invoke v2 controller show time series of country {}", country);
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
        }

        Optional<TimeSeriesDto> getOneObject = getAllValuesSelectedCountry.get("confirmedList").stream().map(TimeSeriesDto::new).findFirst();

        if (getOneObject.isPresent()) {
            List<String> datesList = getOneObject.get().getDataMap().entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
            if (getAllValuesSelectedCountry.get("confirmedList").stream().filter(c -> c.getCountry() != null).collect(Collectors.toList()).size() > 1) {
                Map<String, List<Integer>> finalResult = timeSeriesService.mapFinalResultToMap(country);
                model.addAttribute("confirmedList", finalResult.get("confirmedResult"));
                model.addAttribute("recoveredList", finalResult.get("recoveredResult"));
                model.addAttribute("deathsList", finalResult.get("deathsResult"));
                model.addAttribute("dateList", datesList);
                List<Integer> dailyTrendConfirmed = timeSeriesService.getOneDayValues(finalResult.get("confirmedResult"));
                List<Integer> dailyTrendRecovered = timeSeriesService.getOneDayValues(finalResult.get("recoveredResult"));
                List<Integer> dailyTrendDeaths = timeSeriesService.getOneDayValues(finalResult.get("deathsResult"));
                model.addAttribute("dailyTrendConfirmed", dailyTrendConfirmed);
                model.addAttribute("dailyTrendRecovered", dailyTrendRecovered);
                model.addAttribute("dailyTrendDeaths", dailyTrendDeaths);
                log.info("Get data for selected country {}", country);
                return "timeSeries";
            }
            List<Integer> confirmedList = timeSeriesService.mapValuesToList(getAllValuesSelectedCountry.get("confirmedList"));
            List<Integer> recoveredList = timeSeriesService.mapValuesToList(getAllValuesSelectedCountry.get("recoveredList"));
            List<Integer> deathsList = timeSeriesService.mapValuesToList(getAllValuesSelectedCountry.get("deathsList"));
            model.addAttribute("confirmedList", confirmedList);
            model.addAttribute("recoveredList", recoveredList);
            model.addAttribute("deathsList", deathsList);
            model.addAttribute("dateList", datesList);
            List<Integer> dailyConfirmedTrend = timeSeriesService.getOneDayValues(confirmedList);
            List<Integer> dailyRecoveredTrend = timeSeriesService.getOneDayValues(recoveredList);
            List<Integer> dailyDeathsTrend = timeSeriesService.getOneDayValues(deathsList);
            model.addAttribute("dailyTrendConfirmed", dailyConfirmedTrend);
            model.addAttribute("dailyTrendRecovered", dailyRecoveredTrend);
            model.addAttribute("dailyTrendDeaths", dailyDeathsTrend);

            int confirmed = timeSeriesService.getLastValues(confirmedList);
            int recovered = timeSeriesService.getLastValues(recoveredList);
            int deaths = timeSeriesService.getLastValues(deathsList);
            model.addAttribute("confirmedYesterday", confirmed);
            model.addAttribute("recoveredYesterday", recovered);
            model.addAttribute("deathsYesterday", deaths);
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
}