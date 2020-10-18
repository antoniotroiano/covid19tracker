package com.statistics.corona.controller;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.DailyReportUsDto;
import com.statistics.corona.model.TimeSeriesDto;
import com.statistics.corona.service.DailyReportService;
import com.statistics.corona.service.DateFormat;
import com.statistics.corona.service.TimeSeriesCountryService;
import com.statistics.corona.service.TimeSeriesProvinceService;
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
public class TimeSeriesProvinceController {

    private static final String CONFIRMED_RESULT = "confirmedResult";
    private static final String RECOVERED_RESULT = "recoveredResult";
    private static final String DEATHS_RESULT = "deathsResult";
    private static final String TIME_SERIES_PROVINCE = "timeSeriesProvince";
    private static final String TIME_SERIES_US_PROVINCE = "timeSeriesProvinceUs";
    private static final String NO_DATA_PROVINCE = "noDataForProvince";
    private static final String DEATHS_LIST = "deathsList";
    private final TimeSeriesCountryService timeSeriesCountryService;
    private final TimeSeriesProvinceService timeSeriesProvinceService;
    private final DailyReportService dailyReportService;
    private final DateFormat dateFormat;

    public TimeSeriesProvinceController(TimeSeriesCountryService timeSeriesCountryService, TimeSeriesProvinceService timeSeriesProvinceService, DailyReportService dailyReportService, DateFormat dateFormat) {
        this.timeSeriesCountryService = timeSeriesCountryService;
        this.timeSeriesProvinceService = timeSeriesProvinceService;
        this.dailyReportService = dailyReportService;
        this.dateFormat = dateFormat;
    }

    @GetMapping("/province/{province}")
    public String showProvinceTimeSeries(@PathVariable("province") String province, Model model) {
        log.info("Invoke controller show details for province {}", province);
        model.addAttribute("provinceDetails", new DailyReportDto());
        getCountryNames(model);
        Optional<DailyReportDto> selectedProvince = dailyReportService.getProvinceDetails(province);
        if (selectedProvince.isPresent()) {
            model.addAttribute("provinceDetails", new DailyReportDto(selectedProvince.get()));
            String date = dateFormat.formatLastUpdateToDateDaily(selectedProvince.get().getLastUpdate());
            String time = dateFormat.formatLastUpdateToTimeDaily(selectedProvince.get().getLastUpdate());
            model.addAttribute("date", date + " " + time + "h");
            Map<String, List<TimeSeriesDto>> provinceTSValuesMap = timeSeriesProvinceService
                    .getProvinceTSValues(selectedProvince.get().getCountry(), province);
            if (!provinceTSValuesMap.isEmpty()) {
                Optional<TimeSeriesDto> getlastTSValue = timeSeriesProvinceService.getLatestValueTimeSeries(provinceTSValuesMap);
                if (getlastTSValue.isPresent()) {
                    List<String> datesList = new ArrayList<>(getlastTSValue.get().getDataMap().keySet());
                    Map<String, List<Integer>> finalResultMap = timeSeriesCountryService.generateFinalTSResult(provinceTSValuesMap);
                    if (!finalResultMap.isEmpty()) {
                        getBaseDataProvince(model, new CountryDetailsDto(), finalResultMap, datesList, province);
                        log.debug("Return all values for selected province {}", province);
                        return TIME_SERIES_PROVINCE;
                    }
                }
            }
        }
        getBaseDataProvince(model, new CountryDetailsDto(), Collections.emptyMap(), Collections.emptyList(), province);
        model.addAttribute(NO_DATA_PROVINCE, true);
        log.warn("No values found for the province {}", province);
        return TIME_SERIES_PROVINCE;
    }

    @GetMapping("/province/us/{usProvince}")
    public String showUsProvinceTimeSeries(@PathVariable("usProvince") String usProvince, Model model) {
        log.info("Invoke controller show details for us province {}", usProvince);
        model.addAttribute("dailyReportUsDto", new DailyReportUsDto());
        getCountryNames(model);
        Optional<DailyReportUsDto> dailyReportUsDto = dailyReportService.getUsProvinceDetails(usProvince);
        if (dailyReportUsDto.isPresent()) {
            model.addAttribute("dailyReportUsDto", new DailyReportUsDto(dailyReportUsDto.get()));
            String date = dateFormat.formatLastUpdateToDateDaily(dailyReportUsDto.get().getLastUpdate());
            String time = dateFormat.formatLastUpdateToTimeDaily(dailyReportUsDto.get().getLastUpdate());
            model.addAttribute("date", date + " " + time + "h");
            Map<String, List<TimeSeriesDto>> usProvinceTSValuesMap = timeSeriesProvinceService.getUsProvinceTSValues(usProvince);
            if (!usProvinceTSValuesMap.isEmpty()) {
                Optional<TimeSeriesDto> getlastTSValue = timeSeriesProvinceService.getLatestValueTimeSeries(usProvinceTSValuesMap);
                model.addAttribute("population", usProvinceTSValuesMap.get(DEATHS_LIST)
                        .stream()
                        .mapToInt(TimeSeriesDto::getPopulation)
                        .sum());
                if (getlastTSValue.isPresent()) {
                    List<String> datesList = new ArrayList<>(getlastTSValue.get().getDataMap().keySet());
                    Map<String, List<Integer>> finalResultMap = timeSeriesProvinceService.generateUsFinalTSResult(usProvinceTSValuesMap);
                    if (!finalResultMap.isEmpty()) {
                        getBaseDataProvince(model, new CountryDetailsDto(), finalResultMap, datesList, usProvince);
                        log.debug("Return all values for selected us province {}", usProvince);
                        return TIME_SERIES_US_PROVINCE;
                    }
                }
            }
        }
        getBaseDataProvince(model, new CountryDetailsDto(), Collections.emptyMap(), Collections.emptyList(), usProvince);
        model.addAttribute(NO_DATA_PROVINCE, true);
        log.warn("No object found with the province {}", usProvince);
        return TIME_SERIES_US_PROVINCE;
    }

    private void getBaseDataProvince(Model model, CountryDetailsDto countryDetailsDto, Map<String, List<Integer>> result, List<String> datesList, String province) {
        int activeCasesProvince = countryDetailsDto.getCases() - countryDetailsDto.getRecovered() - countryDetailsDto.getDeaths();
        model.addAttribute("timeSeriesDto", new TimeSeriesDto());
        model.addAttribute("title", "COVID-19 - Details for " + province);
        model.addAttribute("selectedProvince", province);
        model.addAttribute("activeCases", activeCasesProvince);

        int activeCasesYesterday = timeSeriesCountryService.getLastValue(result.get(CONFIRMED_RESULT)) -
                timeSeriesCountryService.getLastValue(result.get(RECOVERED_RESULT)) -
                timeSeriesCountryService.getLastValue(result.get(DEATHS_RESULT));
        model.addAttribute("differenceActiveCases", (activeCasesProvince - activeCasesYesterday));

        model.addAttribute("confirmedList", timeSeriesCountryService.getEverySecondValue(result.get(CONFIRMED_RESULT)));
        model.addAttribute("recoveredList", timeSeriesCountryService.getEverySecondValue(result.get(RECOVERED_RESULT)));
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