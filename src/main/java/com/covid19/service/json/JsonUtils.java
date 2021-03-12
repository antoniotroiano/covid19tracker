package com.covid19.service.json;

import java.util.Objects;

import com.covid19.model.data.CountryValuesTransfer;
import com.covid19.model.dto.CountryValuesDto;
import com.covid19.model.dto.WorldTimeSeriesDto;
import com.covid19.model.dto.WorldValuesDto;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JsonUtils {

    public WorldValuesDto mapWorldJsonToObject() {
        log.debug("Invoke read world values of JSON");
        RestTemplate restTemplate = new RestTemplate();
        WorldTimeSeriesDto worldTimeSeriesDto = Objects.requireNonNull(restTemplate.getForObject(
                "https://corona.lmao.ninja/v3/covid-19/historical/all?lastdays=all", WorldTimeSeriesDto.class));

        RestTemplate restTemplate2 = new RestTemplate();
        WorldValuesDto worldValuesDto = Objects.requireNonNull(restTemplate2
                .getForObject("https://covid19.mathdro.id/api/", WorldValuesDto.class));

        worldValuesDto.setWorldTimeSeriesDto(worldTimeSeriesDto);

        long count = worldTimeSeriesDto.getCases().values().size();
        worldValuesDto.setNewConfirmed(worldValuesDto.getConfirmed().getValue() -
                worldTimeSeriesDto.getCases().values()
                        .stream()
                        .skip(count - 1)
                        .findFirst()
                        .get());
        worldValuesDto.setNewRecovered(worldValuesDto.getRecovered().getValue() -
                worldTimeSeriesDto.getRecovered().values()
                        .stream()
                        .skip(count - 1).findFirst()
                        .get());
        worldValuesDto.setNewDeaths(worldValuesDto.getDeaths().getValue() -
                worldTimeSeriesDto.getDeaths().values()
                        .stream()
                        .skip(count - 1)
                        .findFirst()
                        .get());
        worldValuesDto.setActive(worldValuesDto.getConfirmed().getValue() - worldValuesDto.getRecovered().getValue() -
                worldValuesDto.getDeaths().getValue());
        worldValuesDto.setDeathsRate((double) worldValuesDto.getDeaths().getValue() /
                (double) worldValuesDto.getConfirmed().getValue() * 100);
        worldValuesDto.setRecoveryRate((double) worldValuesDto.getRecovered().getValue() /
                (double) worldValuesDto.getConfirmed().getValue() * 100);
        log.info("Returned world values from JSON");
        return worldValuesDto;
    }

    public CountryValuesDto readCountryValuesOfJson(String country) {
        log.debug("Invoke get country details for {}", country);
        if (country.equals("Korea, South")) {
            country = "South Korea";
        }
        if (country.equals("Taiwan*")) {
            country = "Taiwan";
        }
        if (country.equals("United Kingdom")) {
            country = "UK";
        }
        RestTemplate restTemplate = new RestTemplate();
        CountryValuesDto countryValuesDto = restTemplate
                .getForObject("https://corona.lmao.ninja/v3/covid-19/countries/" + country +
                        "?yesterday=true&twoDaysAgo=false&strict=true&allowNull=true", CountryValuesDto.class);
        if (country.equals("UK")) {
            country = "United Kingdom";
        }
        RestTemplate restTemplate2 = new RestTemplate();
        CountryValuesTransfer countryValuesTransfer = Objects.requireNonNull(restTemplate2
                .getForObject("https://covid19.mathdro.id/api/countries/" + country, CountryValuesTransfer.class));
        if (countryValuesDto != null) {
            countryValuesDto.setCases(countryValuesTransfer.getDataValueConfirmed().getValue());
            countryValuesDto.setRecovered(countryValuesTransfer.getDataValueRecovered().getValue());
            countryValuesDto.setDeaths(countryValuesTransfer.getDataValueDeaths().getValue());
            int active = countryValuesTransfer.getDataValueConfirmed().getValue() -
                    countryValuesTransfer.getDataValueRecovered().getValue() -
                    countryValuesTransfer.getDataValueDeaths().getValue();
            countryValuesDto.setActive(active);
            countryValuesDto.setDeathRate(((double) countryValuesDto.getDeaths() /
                    (double) countryValuesDto.getCases()) * 100);
            countryValuesDto.setRecoveryRate(((double) countryValuesDto.getRecovered() /
                    (double) countryValuesDto.getCases()) * 100);
            countryValuesDto.setCasesPerOneHundred((int) (((double) countryValuesDto.getCases() /
                    (double) countryValuesDto.getPopulation()) * 100000));
            countryValuesDto.setDeathsPerOneHundred((int) (((double) countryValuesDto.getDeaths() /
                    (double) countryValuesDto.getPopulation()) * 100000));
        }
        log.info("Returned details for {}", country);
        return countryValuesDto;
    }
}