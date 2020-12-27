package com.statistics.corona.service;

import com.statistics.corona.model.dto.CountryDailyDto;
import com.statistics.corona.model.dto.WorldValuesDto;
import com.statistics.corona.service.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    public Optional<WorldValuesDto> getWorldValues() {
        log.debug("Invoke get all values of world");
        try {
            WorldValuesDto worldValuesDto = jsonUtils.mapWorldJsonToObject();
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
            Integer cases = worldValuesDto.get().getWorldTimeSeriesDto().getCases().get(yesterdayFormat);
            Integer recovered = worldValuesDto.get().getWorldTimeSeriesDto().getRecovered().get(yesterdayFormat);
            Integer deaths = worldValuesDto.get().getWorldTimeSeriesDto().getDeaths().get(yesterdayFormat);
            if (cases == null || recovered == null || deaths == null) {
                log.debug("At the moment no new yesterday value available");
                return 0;
            }
            log.debug("Returned yesterday active");
            return cases - recovered - deaths;
        }
        log.warn("No value for yesterday active");
        return 0;
    }

    public List<CountryDailyDto> getEachCountriesConfirmedDescending(List<CountryDailyDto> allCountryDailyReports) {
        log.debug("Get all countries confirmed cases descending");
        return allCountryDailyReports.stream()
                .sorted(Comparator.comparing(CountryDailyDto::getConfirmed)
                        .reversed())
                .collect(Collectors.toList());
    }

    public List<CountryDailyDto> getEachCountriesActiveDescending(List<CountryDailyDto> allCountryDailyReports) {
        log.debug("Get all countries active cases descending");
        return allCountryDailyReports.stream()
                .sorted(Comparator.comparing(CountryDailyDto::getActive)
                        .reversed())
                .collect(Collectors.toList());
    }

    public List<CountryDailyDto> getEachCountriesDeathsDescending(List<CountryDailyDto> allCountryDailyReports) {
        log.debug("Get all countries deaths cases descending");
        return allCountryDailyReports.stream()
                .sorted(Comparator.comparing(CountryDailyDto::getDeaths)
                        .reversed())
                .collect(Collectors.toList());
    }
}