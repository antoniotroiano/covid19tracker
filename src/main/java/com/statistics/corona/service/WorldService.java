package com.statistics.corona.service;

import com.statistics.corona.model.DailyReportDto;
import com.statistics.corona.model.TimeSeriesWorldDto;
import com.statistics.corona.model.dto.WorldValuesDto;
import com.statistics.corona.service.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorldService {

    private final JsonUtils jsonUtils;

    @Autowired
    public WorldService(JsonUtils jsonUtils) {
        this.jsonUtils = jsonUtils;
    }

    public Optional<WorldValuesDto> getWorldValues(String query) {
        log.debug("Invoke get all values of world");
        try {
            WorldValuesDto worldValuesDto = jsonUtils.mapWorldJsonToObject(query);
            if (worldValuesDto == null) {
                log.warn("No data available for world");
                return Optional.of(new WorldValuesDto());
            }
            log.info("Returned all values of world");
            return Optional.of(worldValuesDto);
        } catch (Exception e) {
            log.warn("Failed to get data of world: {}", e.getMessage());
            return Optional.of(new WorldValuesDto());
        }
    }

    public Integer getYesterdayActive(Optional<WorldValuesDto> worldValuesDto) {
        log.debug("Invoke get yesterday active");
        if (worldValuesDto.isPresent()) {
            LocalDate yesterday = LocalDate.now().minus(1, ChronoUnit.DAYS);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("M/d/yy", Locale.GERMAN);
            String yesterdayFormat = outputFormatter.format(yesterday);
            int cases = worldValuesDto.get().getWorldTimeSeriesDto().getCases().get(yesterdayFormat);
            int recovered = worldValuesDto.get().getWorldTimeSeriesDto().getRecovered().get(yesterdayFormat);
            int deaths = worldValuesDto.get().getWorldTimeSeriesDto().getDeaths().get(yesterdayFormat);
            log.debug("Returned yesterday active");
            return cases - recovered - deaths;
        }
        log.warn("No value for yesterday active");
        return 0;
    }

/*    public TimeSeriesWorldDto getLatestDataWorldValue(List<TimeSeriesWorldDto> allValuesWorld) {
        log.debug("Invoke get latest data world value");
        return allValuesWorld
                .stream()
                .findFirst()
                .orElse(new TimeSeriesWorldDto());
    }

    public TimeSeriesWorldDto getSecondWorldValue(List<TimeSeriesWorldDto> allValuesWorld) {
        log.debug("Invoke get second value of world data");
        return allValuesWorld
                .stream()
                .skip(1)
                .findFirst()
                .orElse(new TimeSeriesWorldDto());
    }

    public List<Integer> getConfirmedValueWorld(List<TimeSeriesWorldDto> allValuesWorld) {
        log.debug("Get confirmed value of world data");
        List<Integer> listConfirmed = allValuesWorld
                .stream()
                .map(TimeSeriesWorldDto::getConfirmed)
                //.sorted(Integer::compareTo)
                .collect(Collectors.toList());
        Collections.reverse(listConfirmed);
        log.info("Return list confirmed values of world");
        return listConfirmed;
    }

    public List<Integer> getRecoveredValueWorld(List<TimeSeriesWorldDto> allValuesWorld) {
        log.debug("Get recovered value of world data");
        List<Integer> listRecovered = allValuesWorld
                .stream()
                .map(TimeSeriesWorldDto::getRecovered)
                .collect(Collectors.toList());
        Collections.reverse(listRecovered);
        log.info("Return list recovered values of world");
        return listRecovered;
    }

    public List<Integer> getDeathValueWorld(List<TimeSeriesWorldDto> allValuesWorld) {
        log.debug("Get deaths value of world data");
        List<Integer> listDeaths = allValuesWorld
                .stream()
                .map(TimeSeriesWorldDto::getDeaths)
                .collect(Collectors.toList());
        Collections.reverse(listDeaths);
        log.info("Return list deaths values of world");
        return listDeaths;
    }

    public List<Integer> getActiveValueWorld(List<TimeSeriesWorldDto> allValuesWorld) {
        log.debug("Get active values of world data");
        List<Integer> listActive = allValuesWorld.stream()
                .map(TimeSeriesWorldDto::getActive)
                .collect(Collectors.toList());
        Collections.reverse(listActive);
        log.info("Return list active values of world");
        return listActive;
    }

    public List<Integer> getNewConfirmedValueWorld(List<TimeSeriesWorldDto> allValuesWorld) {
        log.debug("Get new confirmed values of world data");
        List<Integer> listNewConfirmed = allValuesWorld.stream()
                .map(TimeSeriesWorldDto::getNew_confirmed)
                .collect(Collectors.toList());
        Collections.reverse(listNewConfirmed);
        log.info("Return list new confirmed values of world");
        return listNewConfirmed;
    }

    public List<Integer> getNewRecoveredValueWorld(List<TimeSeriesWorldDto> allValuesWorld) {
        log.debug("Get new recovered values of world data");
        List<Integer> listNewRecovered = allValuesWorld.stream()
                .map(TimeSeriesWorldDto::getNew_recovered)
                .collect(Collectors.toList());
        Collections.reverse(listNewRecovered);
        log.info("Return list new recovered values of world");
        return listNewRecovered;
    }

    public List<Integer> getNewDeathsValueWorld(List<TimeSeriesWorldDto> allValuesWorld) {
        log.debug("Get new deaths values of world data");
        List<Integer> listNewDeaths = allValuesWorld.stream()
                .map(TimeSeriesWorldDto::getNew_deaths)
                .collect(Collectors.toList());
        Collections.reverse(listNewDeaths);
        log.info("Return list new deaths values of world");
        return listNewDeaths;
    }

    public List<String> getListDates(List<TimeSeriesWorldDto> allValuesWorld) {
        log.debug("Invoke get list with all dates");
        List<String> listDates = allValuesWorld
                .stream()
                .map(TimeSeriesWorldDto::getDate)
                .collect(Collectors.toList());
        Collections.reverse(listDates);
        log.info("Return list with all dates");
        return listDates;
    }*/

    public List<DailyReportDto> getEachCountriesConfirmedDescending(List<DailyReportDto> allCountryDailyReports) {
        log.debug("Get all countries confirmed cases descending");
        return allCountryDailyReports.stream()
                .sorted(Comparator.comparing(DailyReportDto::getConfirmed)
                        .reversed())
                .collect(Collectors.toList());
    }

    public List<DailyReportDto> getEachCountriesActiveDescending(List<DailyReportDto> allCountryDailyReports) {
        log.debug("Get all countries active cases descending");
        return allCountryDailyReports.stream()
                .sorted(Comparator.comparing(DailyReportDto::getActive)
                        .reversed())
                .collect(Collectors.toList());
    }

    public List<DailyReportDto> getEachCountriesDeathsDescending(List<DailyReportDto> allCountryDailyReports) {
        log.debug("Get all countries deaths cases descending");
        return allCountryDailyReports.stream()
                .sorted(Comparator.comparing(DailyReportDto::getDeaths)
                        .reversed())
                .collect(Collectors.toList());
    }
}