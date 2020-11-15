package com.statistics.corona.service.json;

import com.statistics.corona.model.data.CountryValuesTransfer;
import com.statistics.corona.model.data.DataObjectDistrict;
import com.statistics.corona.model.dto.CountryValuesDto;
import com.statistics.corona.model.dto.DistrictDto;
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
                        "?yesterday=false&twoDaysAgo=false&strict=true&allowNull=true", CountryValuesDto.class);
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
            int active = countryValuesTransfer.getDataValueConfirmed().getValue() - countryValuesTransfer.getDataValueRecovered().getValue() -
                    countryValuesTransfer.getDataValueDeaths().getValue();
            countryValuesDto.setActive(active);
            countryValuesDto.setTodayCases(getYesterdayValues(country).getTodayCases());
            countryValuesDto.setTodayRecovered(getYesterdayValues(country).getTodayRecovered());
            countryValuesDto.setTodayDeaths(getYesterdayValues(country).getTodayDeaths());
            double deathRate = ((double) countryValuesDto.getDeaths() / (double) countryValuesDto.getCases()) * 100;
            countryValuesDto.setDeathRate(deathRate);
            double recoveryRate = ((double) countryValuesDto.getRecovered() / (double) countryValuesDto.getCases()) * 100;
            countryValuesDto.setRecoveryRate(recoveryRate);
            double casesPerOneHundred = ((double) countryValuesDto.getCases() / (double) countryValuesDto.getPopulation()) * 100000;
            countryValuesDto.setCasesPerOneHundred((int) casesPerOneHundred);
            double deathsPerOneHundred = ((double) countryValuesDto.getDeaths() / (double) countryValuesDto.getPopulation()) * 100000;
            countryValuesDto.setDeathsPerOneHundred((int) deathsPerOneHundred);
        }
        log.info("Returned details for {}", country);
        return countryValuesDto;
    }

    private CountryValuesDto getYesterdayValues(String country) {
        log.debug("Invoke get country details for yesterday {}", country);
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
        if (countryValuesDto != null) {
            log.info("Returned details for yesterday{}", country);
            return countryValuesDto;
        }
        log.warn("Returned empty details for yesterday{}", country);
        return new CountryValuesDto();
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