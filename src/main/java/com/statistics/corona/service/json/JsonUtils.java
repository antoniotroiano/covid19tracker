package com.statistics.corona.service.json;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.DistrictDto;
import com.statistics.corona.model.data.DataObjectCountry;
import com.statistics.corona.model.data.DataObjectDistrict;
import com.statistics.corona.model.dto.WorldTimeSeriesDto;
import com.statistics.corona.model.dto.WorldValuesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class JsonUtils {

    public WorldValuesDto mapWorldJsonToObject(String query) {
        log.debug("Invoke read world values of JSON");
        RestTemplate restTemplate = new RestTemplate();
        WorldTimeSeriesDto worldTimeSeriesDto = Objects.requireNonNull(restTemplate.getForObject(
                "https://corona.lmao.ninja/v3/covid-19/historical/all?lastdays=" + query, WorldTimeSeriesDto.class));

        RestTemplate restTemplate2 = new RestTemplate();
        WorldValuesDto worldValuesDto = Objects.requireNonNull(restTemplate2
                .getForObject("https://covid19.mathdro.id/api/", WorldValuesDto.class));

        worldValuesDto.setWorldTimeSeriesDto(worldTimeSeriesDto);

        long count = worldTimeSeriesDto.getCases().values().size();
        worldValuesDto.setNewConfirmed(worldValuesDto.getConfirmed().getValue() -
                worldTimeSeriesDto.getCases().values().stream().skip(count - 1).findFirst().get());
        worldValuesDto.setNewRecovered(worldValuesDto.getRecovered().getValue() -
                worldTimeSeriesDto.getRecovered().values().stream().skip(count - 1).findFirst().get());
        worldValuesDto.setNewDeaths(worldValuesDto.getDeaths().getValue() -
                worldTimeSeriesDto.getDeaths().values().stream().skip(count - 1).findFirst().get());
        worldValuesDto.setActive(worldValuesDto.getConfirmed().getValue() - worldValuesDto.getRecovered().getValue() -
                worldValuesDto.getDeaths().getValue());
        worldValuesDto.setDeathsRate((double) worldValuesDto.getDeaths().getValue() /
                (double) worldValuesDto.getConfirmed().getValue() * 100);
        worldValuesDto.setRecoveryRate((double) worldValuesDto.getRecovered().getValue() /
                (double) worldValuesDto.getConfirmed().getValue() * 100);

        log.info("Returned world values from JSON");
        return worldValuesDto;
    }

    public CountryDetailsDto readCountryValuesOfJson(String country) {
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
        CountryDetailsDto countryDetailsDto = restTemplate
                .getForObject("https://corona.lmao.ninja/v3/covid-19/countries/" + country +
                        "?yesterday=false&twoDaysAgo=false&strict=true&allowNull=true", CountryDetailsDto.class);
        if (country.equals("UK")) {
            country = "United Kingdom";
        }
        RestTemplate restTemplate2 = new RestTemplate();
        DataObjectCountry dataObjectCountry = Objects.requireNonNull(restTemplate2
                .getForObject("https://covid19.mathdro.id/api/countries/" + country, DataObjectCountry.class));
        if (countryDetailsDto != null) {
            countryDetailsDto.setCases(dataObjectCountry.getDataValueConfirmed().getValue());
            countryDetailsDto.setRecovered(dataObjectCountry.getDataValueRecovered().getValue());
            countryDetailsDto.setDeaths(dataObjectCountry.getDataValueDeaths().getValue());
            double deathRate = ((double) countryDetailsDto.getDeaths() / (double) countryDetailsDto.getCases()) * 100;
            countryDetailsDto.setDeathRate(deathRate);
            double recoveryRate = ((double) countryDetailsDto.getRecovered() / (double) countryDetailsDto.getCases()) * 100;
            countryDetailsDto.setRecoveryRate(recoveryRate);
            double casesPerOneHundred = ((double) countryDetailsDto.getCases() / (double) countryDetailsDto.getPopulation()) * 100000;
            countryDetailsDto.setCasesPerOneHundred((int) casesPerOneHundred);
            double deathsPerOneHundred = ((double) countryDetailsDto.getDeaths() / (double) countryDetailsDto.getPopulation()) * 100000;
            countryDetailsDto.setDeathsPerOneHundred((int) deathsPerOneHundred);
        }
        log.info("Returned details for {}", country);
        return countryDetailsDto;
    }

    public List<DistrictDto> readDistrictsValues() {
        log.debug("Invoke get district values of ");
        RestTemplate restTemplate = new RestTemplate();
        List<DistrictDto> districtDtoList = Objects.requireNonNull(restTemplate
                .getForObject("https://www.trackcorona.live/api/cities", DataObjectDistrict.class))
                .getDistrictDtoList();
        log.info("Return list with all district values of ");
        return districtDtoList;
    }
}