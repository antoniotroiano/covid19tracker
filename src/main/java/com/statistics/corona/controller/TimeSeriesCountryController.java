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
import org.springframework.scheduling.annotation.Async;
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
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("covid19/timeSeries")
public class TimeSeriesCountryController {

    private static final String TIME_SERIES = "timeSeriesCountry";
    private static final String TIME_SERIES_PROVINCE = "timeSeriesProvince";
    private static final String TIME_SERIES_US_PROVINCE = "timeSeriesProvinceUs";
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
        model.addAttribute(SELECTED_COUNTRY, country);

        getCountryNames(model);

        List<DailyReportDto> valuesCountries = dailyReportService.getDailyDetailsOfProvince(country);
        if (!valuesCountries.isEmpty() && valuesCountries.stream().filter(c -> !c.getCountry().isEmpty()).count() > 1) {
            model.addAttribute("moreDetailsAvailable", true);
        }

        Optional<CountryDetailsDto> countryDetailsDto = dailyReportService.getCountryValues(country);
        if (countryDetailsDto.isPresent()) {
            model.addAttribute("countryDetails", new CountryDetailsDto(countryDetailsDto.get()));
            String date = dateFormat.formatUnixToDate(countryDetailsDto.get().getLastUpdate());
            model.addAttribute("date", date);

            Optional<DailyReportDto> dailyReportDto = dailyReportService.getAllDailyCountryValues()
                    .stream()
                    .filter(c -> c.getCountry().equals(country))
                    .findFirst();
            dailyReportDto.ifPresent(reportDto -> model.addAttribute("dailyReport", new DailyReportDto(reportDto)));

            Map<String, List<TimeSeriesDto>> countryTSValuesMap = timeSeriesCountryService.getCountryTSValues(country);

            if (!countryTSValuesMap.isEmpty()) {
                Optional<TimeSeriesDto> getOneObject = countryTSValuesMap.get(CONFIRMED_LIST).stream()
                        .map(TimeSeriesDto::new)
                        .findFirst();
                if (getOneObject.isPresent()) {
                    List<String> datesList = new ArrayList<>(getOneObject.get().getDataMap().keySet());
                    Map<String, List<Integer>> finalResultMap = timeSeriesCountryService.generateFinalTSResult(countryTSValuesMap);
                    if (!finalResultMap.isEmpty()) {
                        getBaseData(model, countryDetailsDto.get(), finalResultMap, datesList);
                        log.debug("Get data for selected country {}", country);
                        return TIME_SERIES;
                    }
                }
            }
        }
        getBaseData(model, new CountryDetailsDto(), new HashMap<>(), new ArrayList<>());
        model.addAttribute(NO_DATA, true);
        log.warn("No data for the country {}", country);
        return TIME_SERIES;
    }

    @GetMapping("/country/provinces/{country}/{code}")
    public String showProvinceAndDistrict(@PathVariable("country") String country, @PathVariable("code") String code, Model model) {
        log.info("Invoke controller show list of provinces and districts for {}", country);
        model.addAttribute("districtDto", new DistrictDto());
        getCountryNames(model);

        if (country.equals("US")) {
            getBaseDataDetailsUS(model, country);
            List<DailyReportUsDto> allValuesProvinceUs = dailyReportService.getDailyDetailsProvinceUs();
            if (!allValuesProvinceUs.isEmpty()) {
                model.addAttribute("allValuesProvinceUS", allValuesProvinceUs);
                getDistrictValues(model, code);
                log.debug("Returned all data of provinces and districts for US");
                return "timeSeriesCountryDetailsUs";
            }
            model.addAttribute("noDetailsProvince", true);
            log.warn("No values for province of US available");
            return "timeSeriesCountryDetailsUs";
        }

        getBaseDataDetails(model, country);
        List<DailyReportDto> allValuesProvince = dailyReportService.getDailyDetailsOfProvince(country);
        if (!allValuesProvince.isEmpty()) {
            List<String> provinceWithTimeSeries = Arrays.asList("Canada", "United Kingdom", "China", "Netherlands",
                    "Australia", "Denmark", "France");
            if (provinceWithTimeSeries.contains(country)) {
                model.addAttribute("timeSeriesAvailable", true);
            }
            model.addAttribute("allValuesProvince", allValuesProvince);
            getDistrictValues(model, code);
            log.debug("Returned all data of provinces and districts for country {}", country);
            return "timeSeriesCountryDetails";
        }
        model.addAttribute("noDetailsProvince", true);
        log.warn("No values for provinces and districts for country {} available", country);
        return "timeSeriesCountryDetails";
    }

    @GetMapping("/province/{province}")
    public String showProvinceTimeSeries(@PathVariable("province") String province, Model model) {
        log.info("Invoke controller show details for province {}", province);
        model.addAttribute("timeSeriesDto", new TimeSeriesDto());
        model.addAttribute("provinceDetails", new DailyReportDto());
        model.addAttribute(TITLE, "COVID-19 - Details for " + province);
        model.addAttribute("selectedProvince", province);

        getCountryNames(model);

        Optional<DailyReportDto> selectedProvince = dailyReportService.getProvinceDetails(province);
        if (selectedProvince.isPresent()) {
            model.addAttribute("provinceDetails", new DailyReportDto(selectedProvince.get()));
            String date = dateFormat.formatLastUpdateToDateDaily(selectedProvince.get().getLastUpdate());
            String time = dateFormat.formatLastUpdateToTimeDaily(selectedProvince.get().getLastUpdate());
            model.addAttribute("date", date + " " + time + "h");

            Map<String, List<TimeSeriesDto>> provinceValues = timeSeriesCountryService
                    .getProvinceTSValues(selectedProvince.get().getCountry(), province);
            if (!provinceValues.isEmpty()) {
                Optional<TimeSeriesDto> getOneObject = provinceValues.get(CONFIRMED_LIST)
                        .stream()
                        .map(TimeSeriesDto::new)
                        .findFirst();
                if (getOneObject.isPresent()) {
                    List<String> datesList = new ArrayList<>(getOneObject.get().getDataMap().keySet());
                    model.addAttribute("dateList", datesList);
                    getBaseData(model, new CountryDetailsDto(), timeSeriesCountryService.generateFinalTSResult(provinceValues), datesList); //ToDo: empty überprüfung der Map machen
                    log.debug("Return all values for selected province {}", province);
                    return TIME_SERIES_PROVINCE;
                }
            }
        }
        getBaseData(model, new CountryDetailsDto(), new HashMap<>(), new ArrayList<>());
        model.addAttribute(NO_DATA_PROVINCE, true);
        log.warn("No values found for the province {}", province);
        return TIME_SERIES_PROVINCE;
    }

    @GetMapping("/province/us/{usProvince}")
    public String showUsProvinceTimeSeries(@PathVariable("usProvince") String usProvince, Model model) {
        log.info("Invoke controller show details for us province {}", usProvince);
        model.addAttribute("timeSeriesDto", new TimeSeriesDto());
        model.addAttribute("dailyReportUsDto", new DailyReportUsDto());
        model.addAttribute(TITLE, "COVID-19 - Details for " + usProvince);
        model.addAttribute("selectedProvince", usProvince);

        getCountryNames(model);

        Optional<DailyReportUsDto> dailyReportUsDto = dailyReportService.getUsProvinceDetails(usProvince);
        if (dailyReportUsDto.isPresent()) {
            model.addAttribute("dailyReportUsDto", new DailyReportUsDto(dailyReportUsDto.get()));
            String date = dateFormat.formatLastUpdateToDateDaily(dailyReportUsDto.get().getLastUpdate());
            String time = dateFormat.formatLastUpdateToTimeDaily(dailyReportUsDto.get().getLastUpdate());
            model.addAttribute("date", date + " " + time + "h");
        }
        Map<String, List<TimeSeriesDto>> usProvinceTSValuesMap = timeSeriesCountryService.getUsProvinceTSValues(usProvince);

        if (!usProvinceTSValuesMap.isEmpty()) {
            Optional<TimeSeriesDto> getOneObject = usProvinceTSValuesMap.get(CONFIRMED_LIST).stream()
                    .map(TimeSeriesDto::new)
                    .findFirst();

            model.addAttribute("population", usProvinceTSValuesMap.get(DEATHS_LIST)
                    .stream()
                    .mapToInt(TimeSeriesDto::getPopulation)
                    .sum());
            if (getOneObject.isPresent()) {
                List<String> datesList = new ArrayList<>(getOneObject.get().getDataMap().keySet());
                Map<String, List<Integer>> finalResultMap = timeSeriesCountryService.generateUsFinalTSResult(usProvinceTSValuesMap);
                if (!finalResultMap.isEmpty()) {
                    getBaseData(model, new CountryDetailsDto(), finalResultMap, datesList);
                    log.debug("Return all values for selected us province {}", usProvince);
                    return TIME_SERIES_US_PROVINCE;
                }
            }
        }
        getBaseData(model, new CountryDetailsDto(), new HashMap<>(), new ArrayList<>());
        model.addAttribute(NO_DATA_PROVINCE, true);
        log.warn("No object found with the province {}", usProvince);
        return TIME_SERIES_US_PROVINCE;
    }

    private void getBaseData(Model model, CountryDetailsDto countryDetailsDto, Map<String, List<Integer>> result, List<String> datesList) {
        model.addAttribute("selectedCode", countryDetailsDto.getCode());
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

    private void getBaseDataDetailsUS(Model model, String country) {
        model.addAttribute("dailyReportUsDto", new DailyReportUsDto());
        model.addAttribute(TITLE, "COVID-19 - Details for US");
        model.addAttribute(SELECTED_COUNTRY, country);
    }

    private void getBaseDataDetails(Model model, String country) {
        model.addAttribute("dailyReportDto", new DailyReportDto());
        model.addAttribute(TITLE, "COVID-19 - Details for " + country);
        model.addAttribute(SELECTED_COUNTRY, country);
    }

    private void getCountryNames(Model model) {
        List<String> allCountries = timeSeriesCountryService.getCountryNames();
        if (allCountries.isEmpty()) {
            model.addAttribute("listCountries", new ArrayList<>());
        }
        model.addAttribute("listCountries", allCountries);
    }

    @Async
    public void getDistrictValues(Model model, String code) {
        List<DistrictDto> districtDtoList = dailyReportService.getDistrictValues();
        List<DistrictDto> districtDtoListSelectedDistrict = districtDtoList
                .stream()
                .filter(c -> c.getCode().equals(code))
                .collect(Collectors.toList());
        if (!districtDtoListSelectedDistrict.isEmpty()) {
            model.addAttribute("districtValues", districtDtoListSelectedDistrict);
        }
        //Add handling when empty
    }
}