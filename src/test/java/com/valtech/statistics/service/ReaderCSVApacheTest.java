package com.valtech.statistics.service;

import com.valtech.statistics.model.TimeSeriesDto;
import com.valtech.statistics.service.csv.ReadDailyReportsCSV;
import com.valtech.statistics.service.csv.ReadTimeSeriesCSV;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
public class ReaderCSVApacheTest {

    private final ReadTimeSeriesCSV readTimeSeriesCSV = new ReadTimeSeriesCSV();

    @Test
    public void testCSVReader() {
        List<TimeSeriesDto> allFiles = readTimeSeriesCSV.readConfirmedCsv();
        System.out.println(allFiles.size());
        System.out.println(allFiles.get(120).getCountry());
        System.out.println(allFiles.get(120).getDataMap().size());

        TimeSeriesDto selectedFile = new TimeSeriesDto();
        List<TimeSeriesDto> selectedCountryAustralia = new ArrayList<>();
        for (TimeSeriesDto allFile : allFiles) {
            if (allFile.getCountry().equals("Australia")) {
                selectedCountryAustralia.add(allFile);
                selectedFile = allFile;
            }
        }
        System.out.println(selectedFile.getCountry());
        System.out.println(selectedFile.getDataMap().get("05.05.2020"));
        System.out.println("Number of province " + selectedCountryAustralia.size());

        TimeSeriesDto australia;
        List<List<Integer>> result = new ArrayList<>();
        for (TimeSeriesDto timeSeriesDto : selectedCountryAustralia) {
            australia = timeSeriesDto;
            System.out.println(australia.getProvince());
            System.out.println(australia.getCountry());
            System.out.println(australia.getDataMap());

            List<Integer> confirmed = australia.getDataMap().entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());

            result.add(confirmed);

            Collection newCollection = australia.getDataMap().values();
            System.out.println("Only the value for the key: " + newCollection);
            List<String> dates = australia.getDataMap().entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
            System.out.println("Only the key the dates: " + dates);
        }

        List<Integer> finalResult = new ArrayList<>();

        for (int j = 0; j < result.get(0).size(); j++) {
            int sum = 0;
            for (int k = 0; k < result.size(); k++) {
                sum += result.get(k).get(j);
            }
            finalResult.add(sum);
        }

        System.out.println("Sum of province: " + result.size());
        System.out.println("Sum of values for each province: " + result.get(0).size());
        System.out.println(result.get(0).get(4) + result.get(1).get(4) + result.get(2).get(4) + result.get(3).get(4) + result.get(4).get(4)
                + result.get(5).get(4) + result.get(6).get(4) + result.get(7).get(4));

        System.out.println(finalResult.get(4));
        System.out.println(finalResult.get(106));
    }

    @Test
    public void readDailyReportsCSV() {
        ReadDailyReportsCSV dailyReportsCSV = new ReadDailyReportsCSV();

        System.out.println(dailyReportsCSV.readDailyReportsCSV().size());
        System.out.println(dailyReportsCSV.readDailyReportsCSV().get(3110));
    }

    @Test
    public void testNewService() {
        TimeSeriesService timeSeriesService = new TimeSeriesService();

        Map<String, List<Integer>> testValues = timeSeriesService.mapFinalResultToMap(timeSeriesService.getValuesSelectedCountry("Australia"));

        System.out.println(testValues.get("confirmedResult"));
        System.out.println(testValues.get("recoveredResult"));
        System.out.println(testValues.get("deathsResult"));

        Map<String, List<TimeSeriesDto>> testValuesAll = timeSeriesService.getValuesSelectedCountry("Germany");

        System.out.println(testValuesAll.get("confirmedList"));
        System.out.println(testValuesAll.get("recoveredList"));
        System.out.println(testValuesAll.get("deathsList"));
    }
}