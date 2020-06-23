package com.statistics.corona.controller;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.DailyReportUsDto;
import com.statistics.corona.model.DistrictDto;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("covid19/timeSeries")
public class TimeSeriesController {

    private static final String TIME_SERIES = "timeSeries";
    private static final String TIME_SERIES_PROVINCE = "timeSeriesProvince";
    private static final String CONFIRMED_RESULT = "confirmedResult";
    private static final String RECOVERED_RESULT = "recoveredResult";
    private static final String DEATHS_RESULT = "deathsResult";
    private static final String CONFIRMED_LIST = "confirmedList";
    private static final String RECOVERED_LIST = "recoveredList";
    private static final String DEATHS_LIST = "deathsList";
    private static final String SELECTED_COUNTRY = "selectedCountry";
    private static final String TITLE = "title";
    private static final String NO_DATA = "noDataForThisCountry";
    private static final String NO_DATA_PROVINCE = "noDataForProvince";
    private final TimeSeriesCountryService timeSeriesCountryService;
    private final DailyReportService dailyReportService;
    private final DateFormat dateFormat;

    public TimeSeriesController(TimeSeriesCountryService timeSeriesCountryService, DailyReportService dailyReportService, DateFormat dateFormat) {
        this.timeSeriesCountryService = timeSeriesCountryService;
        this.dailyReportService = dailyReportService;
        this.dateFormat = dateFormat;
    }

    @GetMapping("/country/{country}")
    public String showTimeSeries(@PathVariable("country") String country, Model model) {
        log.info("Invoke controller show time series of country {}", country);
        model.addAttribute("timeSeriesDto", new TimeSeriesDto());
        model.addAttribute("countryDetailsDto", new CountryDetailsDto());
        model.addAttribute("dailyReportDto", new DailyReportDto());
        model.addAttribute(TITLE, "COVID-19 - Data for " + country);
        model.addAttribute(SELECTED_COUNTRY, country);

        getCountryNames(model);

        Optional<CountryDetailsDto> countryDetailsDto = dailyReportService.getDetailsForCountry(country);
        if (countryDetailsDto.isEmpty()) {
            model.addAttribute("countryDetails", new CountryDetailsDto());
            model.addAttribute(NO_DATA, true);
        } else {
            model.addAttribute("countryDetails", new CountryDetailsDto(countryDetailsDto.get()));
            String date = dateFormat.formatLastUpdateToDate(countryDetailsDto.get().getLastUpdate());
            String time = dateFormat.formatLastUpdateToTime(countryDetailsDto.get().getLastUpdate());
            model.addAttribute("date", date + " " + time + "h");
        }

        Optional<DailyReportDto> dailyReportDto = dailyReportService.getAllDailyCountryValues()
                .stream()
                .filter(c -> c.getCountry().equals(country))
                .findFirst();
        if (dailyReportDto.isPresent()) {
            model.addAttribute("dailyReport", new DailyReportDto(dailyReportDto.get()));
        } else {
            model.addAttribute("dailyReport", new DailyReportDto());
        }

        Map<String, List<TimeSeriesDto>> getCountryTSValues = timeSeriesCountryService.getCountryTSValues(country);
        if (!getCountryTSValues.isEmpty()) {
            Optional<TimeSeriesDto> getOneObject = getCountryTSValues.get(CONFIRMED_LIST).stream()
                    .map(TimeSeriesDto::new)
                    .findFirst();
            if (getOneObject.isPresent()) {
                List<String> datesList = new ArrayList<>(getOneObject.get().getDataMap().keySet());
                Map<String, List<Integer>> finalResult = timeSeriesCountryService.generateFinalTSResult(getCountryTSValues);
                if (finalResult.isEmpty()) {
                    getBaseData(model, new CountryDetailsDto(), new HashMap<>(), new ArrayList<>());
                    model.addAttribute(NO_DATA, true);
                    log.warn("No data for time series found, for selected country {}", country);
                    return TIME_SERIES;
                }

                List<DailyReportDto> valuesCountries = dailyReportService.getDailyDetailsOfProvince(country);
                if (!valuesCountries.isEmpty() && valuesCountries.stream().filter(c -> !c.getCountry().isEmpty()).count() > 1) {
                    model.addAttribute("moreDetailsAvailable", true);
                    getBaseData(model, countryDetailsDto.get(), finalResult, datesList);
                    log.warn("Get data for selected country {}, with extra details", country);
                    return TIME_SERIES;
                }
                getBaseData(model, countryDetailsDto.get(), finalResult, datesList);
                log.debug("Get data for selected country {}", country);
                return TIME_SERIES;
            }
            getBaseData(model, new CountryDetailsDto(), new HashMap<>(), new ArrayList<>());
            model.addAttribute(NO_DATA, true);
            log.warn("Something getting wrong {}", country);
            return TIME_SERIES;
        }
        getBaseData(model, new CountryDetailsDto(), new HashMap<>(), new ArrayList<>());
        model.addAttribute(NO_DATA, true);
        log.debug("No data for the country {}", country);
        return TIME_SERIES;
    }

    @GetMapping("/country/provinces/{country}")
    public String showDetailsOfSelectedCountry(@PathVariable("country") String country, Model model) {
        log.info("Invoke controller show list of province for {}", country);
        if (country.equals("US")) {
            List<DailyReportUsDto> allValuesProvinceUs = dailyReportService.getDailyDetailsProvinceUs();
            if (!allValuesProvinceUs.isEmpty()) {
                createBaseDataDetailsUS(country, model);
                model.addAttribute("allValuesProvinceUS", allValuesProvinceUs);
                log.debug("Returned all data of province for US");
                return "timeSeriesDetailsUs";
            }
            createBaseDataDetailsUS(country, model);
            model.addAttribute("noDetailsProvince", true);
            log.warn("No values for province of US available");
            return "timeSeriesDetailsUs";
        }
        List<DailyReportDto> allValuesProvince = dailyReportService.getDailyDetailsOfProvince(country);
        if (!allValuesProvince.isEmpty()) {
            List<String> withProvinceNoTimeSeries = Arrays.asList("Canada", "United Kingdom", "China", "Netherlands",
                    "Australia", "Denmark", "France");
            if (withProvinceNoTimeSeries.contains(country)) {
                model.addAttribute("timeSeriesAvailable", true);
            }
            createBaseDataDetails(country, model);
            model.addAttribute("allValuesProvince", allValuesProvince);
            Optional<CountryDetailsDto> countryDetailsDto = dailyReportService.getDetailsForCountry(country);
            if (countryDetailsDto.isPresent()) {
                List<DistrictDto> districtDtoList = dailyReportService.getDistrictValues(countryDetailsDto.get().getCode());
                if (!districtDtoList.isEmpty()) {
                    model.addAttribute("districtDto", new DistrictDto());
                    model.addAttribute("districtValues", districtDtoList);
                }
            }
            log.debug("Returned all data of province for selected country {}", country);
            return "timeSeriesDetails";
        }
        createBaseDataDetails(country, model);
        model.addAttribute("noDetailsProvince", true);
        log.warn("No values for province of selected country {} available", country);
        return "timeSeriesDetails";
    }

    @GetMapping("/province/{province}")
    public String showProvinceTimeSeries(@PathVariable("province") String province, Model model) {
        log.info("Invoke controller show details for province {}", province);
        model.addAttribute("timeSeriesDto", new TimeSeriesDto());
        model.addAttribute(TITLE, province);

        getCountryNames(model);

        Optional<DailyReportDto> selectedProvince = dailyReportService.getProvinceDetails(province);
        if (selectedProvince.isPresent()) {
            model.addAttribute("selectedProvince", province);
            model.addAttribute("provinceDetails", new DailyReportDto(selectedProvince.get()));
            String date = dateFormat.formatLastUpdateToDateDaily(selectedProvince.get().getLastUpdate());
            String time = dateFormat.formatLastUpdateToTimeDaily(selectedProvince.get().getLastUpdate());
            model.addAttribute("date", date + " " + time + "h");

            Map<String, List<TimeSeriesDto>> provinceValues = timeSeriesCountryService
                    .getProvinceTSValues(selectedProvince.get().getCountry(), province);
            if (provinceValues.isEmpty()) {
                model.addAttribute(NO_DATA_PROVINCE, true);
            }

            Map<String, List<TimeSeriesDto>> getMapOfProvinceValues = timeSeriesCountryService.getProvinceTSValues(selectedProvince.get().getCountry(), province);
            Optional<TimeSeriesDto> getOneObject = getMapOfProvinceValues.get(CONFIRMED_LIST)
                    .stream()
                    .map(TimeSeriesDto::new)
                    .findFirst();
            if (getOneObject.isPresent()) {
                List<String> datesList = new ArrayList<>(getOneObject.get().getDataMap().keySet());
                model.addAttribute("dateList", datesList);
                getBaseData(model, new CountryDetailsDto(), timeSeriesCountryService.generateFinalTSResult(provinceValues), datesList);
                log.debug("Return all values for selected province {}", province);
                return TIME_SERIES_PROVINCE;
            }
            model.addAttribute(NO_DATA_PROVINCE, true);
            log.warn("Return all values for selected province {}", province);
            return TIME_SERIES_PROVINCE;
        }
        model.addAttribute("provinceDetails", new DailyReportDto());
        model.addAttribute(NO_DATA_PROVINCE, true);
        log.warn("No object found with the province {}", province);
        return TIME_SERIES_PROVINCE;
    }

    private void getBaseData(Model model, CountryDetailsDto countryDetailsDto, Map<String, List<Integer>> result, List<String> datesList) {
        int activeCasesToday = countryDetailsDto.getConfirmed() - countryDetailsDto.getRecovered() - countryDetailsDto.getDeaths();
        model.addAttribute("activeCasesToday", activeCasesToday);

        int confirmedYesterday = timeSeriesCountryService.getLastValue(result.get(CONFIRMED_RESULT));
        int recoveredYesterday = timeSeriesCountryService.getLastValue(result.get(RECOVERED_RESULT));
        int deathsYesterday = timeSeriesCountryService.getLastValue(result.get(DEATHS_RESULT));
        int activeCasesYesterday = confirmedYesterday - recoveredYesterday - deathsYesterday;
        model.addAttribute("differenceActiveCases", (activeCasesToday - activeCasesYesterday));

        model.addAttribute("confirmedYesterday", confirmedYesterday);
        model.addAttribute("recoveredYesterday", recoveredYesterday);
        model.addAttribute("deathsYesterday", deathsYesterday);

        model.addAttribute(CONFIRMED_LIST, timeSeriesCountryService.getEverySecondValue(result.get(CONFIRMED_RESULT)));
        model.addAttribute(RECOVERED_LIST, timeSeriesCountryService.getEverySecondValue(result.get(RECOVERED_RESULT)));
        model.addAttribute(DEATHS_LIST, timeSeriesCountryService.getEverySecondValue(result.get(DEATHS_RESULT)));
        model.addAttribute("dateList", timeSeriesCountryService.getEverySecondDate(datesList));

        model.addAttribute("dailyTrendConfirmed", timeSeriesCountryService.getOneDayValues(result.get(CONFIRMED_RESULT)));
        model.addAttribute("dailyTrendRecovered", timeSeriesCountryService.getOneDayValues(result.get(RECOVERED_RESULT)));
        model.addAttribute("dailyTrendDeaths", timeSeriesCountryService.getOneDayValues(result.get(DEATHS_RESULT)));
        model.addAttribute("dateListPerDay", datesList);
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

    private void getCountryNames(Model model) {
        List<String> allCountries = timeSeriesCountryService.getCountryNames();
        if (allCountries.isEmpty()) {
            model.addAttribute("listCountries", new ArrayList<>());
        }
        model.addAttribute("listCountries", allCountries);
    }
}