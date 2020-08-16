package com.statistics.corona.service.json;

import com.statistics.corona.model.CountryDetailsDto;
import com.statistics.corona.model.DataObject;
import com.statistics.corona.model.DataObjectCountry;
import com.statistics.corona.model.DataObjectDistrict;
import com.statistics.corona.model.DistrictDto;
import com.statistics.corona.model.TimeSeriesWorldDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ReadJSON {

    public List<TimeSeriesWorldDto> readWorldValuesOfJson() {
        log.debug("Invoke read world time series of JSON");
        RestTemplate restTemplate = new RestTemplate();
        List<TimeSeriesWorldDto> timeSeriesWorldDtoList = Objects.requireNonNull(restTemplate
                .getForObject("https://corona-api.com/timeline", DataObject.class))
                .getTimeSeriesWorldDtoList();
        log.debug("Returned world time series of JSON");
        return timeSeriesWorldDtoList;
    }

    public CountryDetailsDto readCountryValuesOfJson(String country) {
        log.debug("Invoke get country details for {}", country);
        if (country.equals("Korea, South")) {
            country = "South Korea";
        }
        if (country.equals("Taiwan*")) {
            country = "Taiwan";
        }

        RestTemplate restTemplate = new RestTemplate();
        CountryDetailsDto countryDetailsDto = Objects.requireNonNull(restTemplate
                .getForObject("https://corona.lmao.ninja/v2/countries/" + country, CountryDetailsDto.class));

        RestTemplate restTemplate2 = new RestTemplate();
        DataObjectCountry dataObjectCountry = Objects.requireNonNull(restTemplate2.getForObject("https://covid19.mathdro.id/api/countries/" +
                URLEncoder.encode(country, StandardCharsets.UTF_8).replace("+", "%20"), DataObjectCountry.class));

        countryDetailsDto.setConfirmed(dataObjectCountry.getDataValueConfirmed().getValue());
        countryDetailsDto.setRecovered(dataObjectCountry.getDataValueRecovered().getValue());
        countryDetailsDto.setDeaths(dataObjectCountry.getDataValueDeaths().getValue());

        double deathRate = ((double) countryDetailsDto.getDeaths() / (double) countryDetailsDto.getConfirmed()) * 100;
        countryDetailsDto.setDeathRate(deathRate);
        double recoveryRate = ((double) countryDetailsDto.getRecovered() / (double) countryDetailsDto.getConfirmed()) * 100;
        countryDetailsDto.setRecoveryRate(recoveryRate);
        double casesPerOneHundred = ((double) countryDetailsDto.getConfirmed() / (double) countryDetailsDto.getPopulation()) * 100000;
        countryDetailsDto.setCasesPerOneHundred((int) casesPerOneHundred);
        double deathsPerOneHundred = ((double) countryDetailsDto.getDeaths() / (double) countryDetailsDto.getPopulation()) * 100000;
        countryDetailsDto.setDeathsPerOneHundred((int) deathsPerOneHundred);

        log.debug("Returned details for {}", country);
        return countryDetailsDto;
    }

    public List<DistrictDto> readDistrictsValues() {
        log.debug("Invoke get district values of ");
        RestTemplate restTemplate = new RestTemplate();
        List<DistrictDto> districtDtoList = Objects.requireNonNull(restTemplate
                .getForObject("https://www.trackcorona.live/api/cities", DataObjectDistrict.class))
                .getDistrictDtoList();
        log.debug("Return list with all district values of ");
        return districtDtoList;
    }
}