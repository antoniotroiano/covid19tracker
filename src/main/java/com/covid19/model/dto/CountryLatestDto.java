package com.covid19.model.dto;

public class CountryLatestDto {

    String iso_code;
    String continent;
    String location;
    Double total_cases;
    Double new_cases;
    Double new_case_smoothed;
    Double total_deaths;
    Double new_deaths;
    Double new_deaths_smoothed;
    Double total_cases_per_million;
    Double new_cases_per_million;
    Double new_cases_smoothed_per_million;
    Double total_deaths_per_million;
    Double new_deaths_per_million;
    Double new_deaths_smoothed_per_million;
    Double reproduction_rate;
    Double total_tests;
    Double total_tests_per_thousand;
    Double new_tests_per_thousand;
    Double new_tests_smoothed;
    Double new_tests_smoothed_per_thousand;
    Double positive_rate;
    Double tests_per_case;
    Double total_vaccinations;
    Double total_vaccinations_per_hundred;
    Double population;
    Double median_age;
    Double aged_65_older;
    Double aged_70_older;
    Double hospital_beds_per_thousand;
    Double life_expectancy;
    Double human_development_index;

    public CountryLatestDto() {
    }

    public CountryLatestDto(CountryLatestDto countryLatestDto) {
        this.iso_code = countryLatestDto.getIso_code();
        this.continent = countryLatestDto.getContinent();
        this.location = countryLatestDto.getLocation();
        this.total_cases = countryLatestDto.getTotal_cases();
        this.new_cases = countryLatestDto.getNew_cases();
        this.new_case_smoothed = countryLatestDto.getNew_case_smoothed();
        this.total_deaths = countryLatestDto.getTotal_deaths();
        this.new_deaths = countryLatestDto.getNew_deaths();
        this.new_deaths_smoothed = countryLatestDto.getNew_deaths_smoothed();
        this.total_cases_per_million = countryLatestDto.getTotal_cases_per_million();
        this.new_cases_per_million = countryLatestDto.getNew_cases_per_million();
        this.new_cases_smoothed_per_million = countryLatestDto.getNew_cases_smoothed_per_million();
        this.total_deaths_per_million = countryLatestDto.getTotal_deaths_per_million();
        this.new_deaths_per_million = countryLatestDto.getNew_deaths_per_million();
        this.new_deaths_smoothed_per_million = countryLatestDto.getNew_deaths_smoothed_per_million();
        this.reproduction_rate = countryLatestDto.getReproduction_rate();
        this.total_tests = countryLatestDto.getTotal_tests();
        this.total_tests_per_thousand = countryLatestDto.getTotal_tests_per_thousand();
        this.new_tests_per_thousand = countryLatestDto.getNew_tests_per_thousand();
        this.new_tests_smoothed = countryLatestDto.getNew_tests_smoothed();
        this.new_tests_smoothed_per_thousand = countryLatestDto.getNew_tests_smoothed_per_thousand();
        this.positive_rate = countryLatestDto.getPositive_rate();
        this.tests_per_case = countryLatestDto.getTests_per_case();
        this.total_vaccinations = countryLatestDto.getTotal_vaccinations();
        this.total_vaccinations_per_hundred = countryLatestDto.getTotal_vaccinations_per_hundred();
        this.population = countryLatestDto.getPopulation();
        this.median_age = countryLatestDto.getMedian_age();
        this.aged_65_older = countryLatestDto.getAged_65_older();
        this.aged_70_older = countryLatestDto.getAged_70_older();
        this.hospital_beds_per_thousand = countryLatestDto.getHospital_beds_per_thousand();
        this.life_expectancy = countryLatestDto.getLife_expectancy();
        this.human_development_index = countryLatestDto.getHuman_development_index();
    }

    public String getIso_code() {
        return iso_code;
    }

    public void setIso_code(String iso_code) {
        this.iso_code = iso_code;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getTotal_cases() {
        return total_cases;
    }

    public void setTotal_cases(Double total_cases) {
        this.total_cases = total_cases;
    }

    public Double getNew_cases() {
        return new_cases;
    }

    public void setNew_cases(Double new_cases) {
        this.new_cases = new_cases;
    }

    public Double getNew_case_smoothed() {
        return new_case_smoothed;
    }

    public void setNew_case_smoothed(Double new_case_smoothed) {
        this.new_case_smoothed = new_case_smoothed;
    }

    public Double getTotal_deaths() {
        return total_deaths;
    }

    public void setTotal_deaths(Double total_deaths) {
        this.total_deaths = total_deaths;
    }

    public Double getNew_deaths() {
        return new_deaths;
    }

    public void setNew_deaths(Double new_deaths) {
        this.new_deaths = new_deaths;
    }

    public Double getNew_deaths_smoothed() {
        return new_deaths_smoothed;
    }

    public void setNew_deaths_smoothed(Double new_deaths_smoothed) {
        this.new_deaths_smoothed = new_deaths_smoothed;
    }

    public Double getTotal_cases_per_million() {
        return total_cases_per_million;
    }

    public void setTotal_cases_per_million(Double total_cases_per_million) {
        this.total_cases_per_million = total_cases_per_million;
    }

    public Double getNew_cases_per_million() {
        return new_cases_per_million;
    }

    public void setNew_cases_per_million(Double new_cases_per_million) {
        this.new_cases_per_million = new_cases_per_million;
    }

    public Double getNew_cases_smoothed_per_million() {
        return new_cases_smoothed_per_million;
    }

    public void setNew_cases_smoothed_per_million(Double new_cases_smoothed_per_million) {
        this.new_cases_smoothed_per_million = new_cases_smoothed_per_million;
    }

    public Double getTotal_deaths_per_million() {
        return total_deaths_per_million;
    }

    public void setTotal_deaths_per_million(Double total_deaths_per_million) {
        this.total_deaths_per_million = total_deaths_per_million;
    }

    public Double getNew_deaths_per_million() {
        return new_deaths_per_million;
    }

    public void setNew_deaths_per_million(Double new_deaths_per_million) {
        this.new_deaths_per_million = new_deaths_per_million;
    }

    public Double getNew_deaths_smoothed_per_million() {
        return new_deaths_smoothed_per_million;
    }

    public void setNew_deaths_smoothed_per_million(Double new_deaths_smoothed_per_million) {
        this.new_deaths_smoothed_per_million = new_deaths_smoothed_per_million;
    }

    public Double getReproduction_rate() {
        return reproduction_rate;
    }

    public void setReproduction_rate(Double reproduction_rate) {
        this.reproduction_rate = reproduction_rate;
    }

    public Double getTotal_tests() {
        return total_tests;
    }

    public void setTotal_tests(Double total_tests) {
        this.total_tests = total_tests;
    }

    public Double getTotal_tests_per_thousand() {
        return total_tests_per_thousand;
    }

    public void setTotal_tests_per_thousand(Double total_tests_per_thousand) {
        this.total_tests_per_thousand = total_tests_per_thousand;
    }

    public Double getNew_tests_per_thousand() {
        return new_tests_per_thousand;
    }

    public void setNew_tests_per_thousand(Double new_tests_per_thousand) {
        this.new_tests_per_thousand = new_tests_per_thousand;
    }

    public Double getNew_tests_smoothed() {
        return new_tests_smoothed;
    }

    public void setNew_tests_smoothed(Double new_tests_smoothed) {
        this.new_tests_smoothed = new_tests_smoothed;
    }

    public Double getNew_tests_smoothed_per_thousand() {
        return new_tests_smoothed_per_thousand;
    }

    public void setNew_tests_smoothed_per_thousand(Double new_tests_smoothed_per_thousand) {
        this.new_tests_smoothed_per_thousand = new_tests_smoothed_per_thousand;
    }

    public Double getPositive_rate() {
        return positive_rate;
    }

    public void setPositive_rate(Double positive_rate) {
        this.positive_rate = positive_rate;
    }

    public Double getTests_per_case() {
        return tests_per_case;
    }

    public void setTests_per_case(Double tests_per_case) {
        this.tests_per_case = tests_per_case;
    }

    public Double getTotal_vaccinations() {
        return total_vaccinations;
    }

    public void setTotal_vaccinations(Double total_vaccinations) {
        this.total_vaccinations = total_vaccinations;
    }

    public Double getTotal_vaccinations_per_hundred() {
        return total_vaccinations_per_hundred;
    }

    public void setTotal_vaccinations_per_hundred(Double total_vaccinations_per_hundred) {
        this.total_vaccinations_per_hundred = total_vaccinations_per_hundred;
    }

    public Double getPopulation() {
        return population;
    }

    public void setPopulation(Double population) {
        this.population = population;
    }

    public Double getMedian_age() {
        return median_age;
    }

    public void setMedian_age(Double median_age) {
        this.median_age = median_age;
    }

    public Double getAged_65_older() {
        return aged_65_older;
    }

    public void setAged_65_older(Double aged_65_older) {
        this.aged_65_older = aged_65_older;
    }

    public Double getAged_70_older() {
        return aged_70_older;
    }

    public void setAged_70_older(Double aged_70_older) {
        this.aged_70_older = aged_70_older;
    }

    public Double getHospital_beds_per_thousand() {
        return hospital_beds_per_thousand;
    }

    public void setHospital_beds_per_thousand(Double hospital_beds_per_thousand) {
        this.hospital_beds_per_thousand = hospital_beds_per_thousand;
    }

    public Double getLife_expectancy() {
        return life_expectancy;
    }

    public void setLife_expectancy(Double life_expectancy) {
        this.life_expectancy = life_expectancy;
    }

    public Double getHuman_development_index() {
        return human_development_index;
    }

    public void setHuman_development_index(Double human_development_index) {
        this.human_development_index = human_development_index;
    }

    @Override
    public String toString() {
        return "TestNewValue{" +
                "iso_code='" + iso_code + '\'' +
                ", continent='" + continent + '\'' +
                ", location='" + location + '\'' +
                ", total_cases=" + total_cases +
                ", new_cases=" + new_cases +
                ", new_case_smoothed=" + new_case_smoothed +
                ", total_deaths=" + total_deaths +
                ", new_deaths=" + new_deaths +
                ", new_deaths_smoothed=" + new_deaths_smoothed +
                ", total_cases_per_million=" + total_cases_per_million +
                ", new_cases_per_million=" + new_cases_per_million +
                ", new_cases_smoothed_per_million=" + new_cases_smoothed_per_million +
                ", total_deaths_per_million=" + total_deaths_per_million +
                ", new_deaths_per_million=" + new_deaths_per_million +
                ", new_deaths_smoothed_per_million=" + new_deaths_smoothed_per_million +
                ", reproduction_rate=" + reproduction_rate +
                ", total_tests=" + total_tests +
                ", total_tests_per_thousand=" + total_tests_per_thousand +
                ", new_tests_per_thousand=" + new_tests_per_thousand +
                ", new_tests_smoothed=" + new_tests_smoothed +
                ", new_tests_smoothed_per_thousand=" + new_tests_smoothed_per_thousand +
                ", positive_rate=" + positive_rate +
                ", tests_per_case=" + tests_per_case +
                ", total_vaccinations=" + total_vaccinations +
                ", total_vaccinations_per_hundred=" + total_vaccinations_per_hundred +
                ", population=" + population +
                ", median_age=" + median_age +
                ", aged_65_older=" + aged_65_older +
                ", aged_70_older=" + aged_70_older +
                ", hospital_beds_per_thousand=" + hospital_beds_per_thousand +
                ", life_expectancy=" + life_expectancy +
                ", human_development_index=" + human_development_index +
                '}';
    }
}