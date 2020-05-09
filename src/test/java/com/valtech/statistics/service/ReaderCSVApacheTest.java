package com.valtech.statistics.service;


import com.valtech.statistics.model.ConfirmedDto;
import com.valtech.statistics.service.csv.ReadCSV;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
public class ReaderCSVApacheTest {

    private final ReadCSV readCSV = new ReadCSV();

    @Test
    public void testCSVReader() throws IOException {
        List<ConfirmedDto> allFiles = readCSV.readCSV();
        System.out.println(allFiles.size());
        System.out.println(allFiles.get(120).getCountry());
        System.out.println(allFiles.get(120).getConfirmed().size());

        ConfirmedDto selectedFile = new ConfirmedDto();
        List<ConfirmedDto> selectedCountryAustralia = new ArrayList<>();
        for (ConfirmedDto allFile : allFiles) {
            if (allFile.getCountry().equals("Australia")) {
                selectedCountryAustralia.add(allFile);
                selectedFile = allFile;
            }
        }
        System.out.println(selectedFile.getCountry());
        System.out.println(selectedFile.getConfirmed().get("05.05.2020"));
        System.out.println(selectedCountryAustralia.size());

        ConfirmedDto australia;
        for (ConfirmedDto confirmedDto : selectedCountryAustralia) {
            australia = confirmedDto;
            System.out.println(australia.getProvince());
            System.out.println(australia.getCountry());
            System.out.println(australia.getConfirmed());

            for (int i = 0; i < australia.getConfirmed().size(); i++) {
                List<String> confirmed = australia.getConfirmed().entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
                String confirmedS = confirmed.get(i);
                System.out.println(newCollection);
                newCollection
                australia.getConfirmed().get(i).
            }
            Collection newCollection = australia.getConfirmed().values();
            System.out.println(newCollection);

            List<String> dates = australia.getConfirmed().entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());

            System.out.println(dates);
            System.out.println(confirmed);
        }
    }
}
