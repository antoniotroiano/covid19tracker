package com.covid19.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.covid19.model.dto.CountryTimeSeriesDto;
import com.covid19.model.dto.CountryValuesDto;
import com.covid19.model.dto.WorldValuesDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResourceUtils {

    private static final String CONFIRMED = "confirmed";
    private static final String DEATHS = "deaths";
    private final ServiceUtils serviceUtils;

    @Autowired
    public ResourceUtils(ServiceUtils serviceUtils) {
        this.serviceUtils = serviceUtils;
    }

    public void getValues(Model model,
                          CountryValuesDto countryValuesDto,
                          WorldValuesDto worldValuesDto,
                          Map<String, Optional<CountryTimeSeriesDto>> provinceValues,
                          Map<String, Map<String, Integer>> provinceUSValues) {
        Collection<Integer> confirmed = new ArrayList<>();
        Collection<Integer> recovered = new ArrayList<>();
        Collection<Integer> deaths = new ArrayList<>();
        Collection<String> dates = new ArrayList<>();

        if (countryValuesDto != null) {
            confirmed = countryValuesDto.getCasesValues().values();
            recovered = countryValuesDto.getRecoveredValues().values();
            deaths = countryValuesDto.getDeathsValues().values();
            dates = countryValuesDto.getCasesValues().keySet();
        }

        if (worldValuesDto != null) {
            confirmed = worldValuesDto.getWorldTimeSeriesDto().getCases().values();
            recovered = worldValuesDto.getWorldTimeSeriesDto().getRecovered().values();
            deaths = worldValuesDto.getWorldTimeSeriesDto().getDeaths().values();
            dates = worldValuesDto.getWorldTimeSeriesDto().getCases().keySet();
        }

        if (provinceValues != null &&
                provinceValues.get(CONFIRMED).isPresent() &&
                provinceValues.get("recovered").isPresent() &&
                provinceValues.get(DEATHS).isPresent()) {
            confirmed = provinceValues.get(CONFIRMED).get().getValues().values();
            recovered = provinceValues.get("recovered").get().getValues().values();
            deaths = provinceValues.get(DEATHS).get().getValues().values();
            dates = provinceValues.get(CONFIRMED).get().getValues().keySet();
        }

        if (provinceUSValues != null) {
            confirmed = provinceUSValues.get(CONFIRMED).values();
            deaths = provinceUSValues.get("deaths").values();
            dates = provinceUSValues.get(CONFIRMED).keySet();
        }

        model.addAttribute("confirmedSixty",
                serviceUtils.getValuesForSelectedValues(60, confirmed));
        model.addAttribute("recoveredSixty",
                serviceUtils.getValuesForSelectedValues(60, recovered));
        model.addAttribute("deathsSixty",
                serviceUtils.getValuesForSelectedValues(60, deaths));
        model.addAttribute("datesSixty",
                serviceUtils.getValuesForSelectedValues(60, dates));

        model.addAttribute("confirmedOneHundredTwenty",
                serviceUtils.getValuesForSelectedValues(120, confirmed));
        model.addAttribute("recoveredOneHundredTwenty",
                serviceUtils.getValuesForSelectedValues(120, recovered));
        model.addAttribute("deathsOneHundredTwenty",
                serviceUtils.getValuesForSelectedValues(120, deaths));
        model.addAttribute("datesOneHundredTwenty",
                serviceUtils.getValuesForSelectedValues(120, dates));

        model.addAttribute("confirmedOneHundredAndEighty",
                serviceUtils.getValuesForSelectedValues(180, confirmed));
        model.addAttribute("recoveredOneHundredAndEighty",
                serviceUtils.getValuesForSelectedValues(180, recovered));
        model.addAttribute("deathsOneHundredAndEighty",
                serviceUtils.getValuesForSelectedValues(180, deaths));
        model.addAttribute("datesOneHundredAndEighty",
                serviceUtils.getValuesForSelectedValues(180, dates));

        model.addAttribute("dailyTrendConfirmedSixty",
                serviceUtils.getDailyTrend(serviceUtils.getValuesForSelectedValues(60, confirmed)));
        model.addAttribute("dailyTrendRecoveredSixty",
                serviceUtils.getDailyTrend(serviceUtils.getValuesForSelectedValues(60, recovered)));
        model.addAttribute("dailyTrendDeathsSixty",
                serviceUtils.getDailyTrend(serviceUtils.getValuesForSelectedValues(60, deaths)));

        model.addAttribute("dailyTrendConfirmedOneHundredTwenty",
                serviceUtils.getDailyTrend(serviceUtils.getValuesForSelectedValues(120, confirmed)));
        model.addAttribute("dailyTrendRecoveredOneHundredTwenty",
                serviceUtils.getDailyTrend(serviceUtils.getValuesForSelectedValues(120, recovered)));
        model.addAttribute("dailyTrendDeathsOneHundredTwenty",
                serviceUtils.getDailyTrend(serviceUtils.getValuesForSelectedValues(120, deaths)));

        model.addAttribute("dailyTrendConfirmedOneHundredAndEighty",
                serviceUtils.getDailyTrend(serviceUtils.getValuesForSelectedValues(180, confirmed)));
        model.addAttribute("dailyTrendRecoveredOneHundredAndEighty",
                serviceUtils.getDailyTrend(serviceUtils.getValuesForSelectedValues(180, recovered)));
        model.addAttribute("dailyTrendDeathsOneHundredAndEighty",
                serviceUtils.getDailyTrend(serviceUtils.getValuesForSelectedValues(180, deaths)));
    }
}