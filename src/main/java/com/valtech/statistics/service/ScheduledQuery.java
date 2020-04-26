package com.valtech.statistics.service;

import com.valtech.statistics.model.DataGermany;
import com.valtech.statistics.model.DataGermanySummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduledQuery {

    private final GermanyService germanyService;
    private final GermanySummaryService germanySummaryService;
    private final GetJsonValue getJsonValue;

    @Scheduled(cron = "0 10 */2 ? * *")
    public void saveGermanyDataOfJson() throws IOException {
        DataGermany dataGermany = getJsonValue.getDataOfGermanyToModel();
        Optional<DataGermany> dataGermanyLast = germanyService.getLastEntryGermany();

        if (dataGermanyLast.isEmpty()) {
            germanyService.saveDataGermany(dataGermany);
            log.info("Saved first data of germany {}.", dataGermany.getLastUpdate());
        }
        if (dataGermanyLast.isPresent()) {
            if (dataGermanyLast.get().getConfirmed() != dataGermany.getConfirmed() ||
                    dataGermanyLast.get().getRecovered() != dataGermany.getRecovered() ||
                    dataGermanyLast.get().getDeaths() != dataGermany.getDeaths()) {
                if (dataGermanyLast.get().getLastUpdate().equals(dataGermany.getLastUpdate())) {
                    log.info("No new data of germany, Returned last one {}.", dataGermany.getLastUpdate());
                } else {
                    germanyService.saveDataGermany(dataGermany);
                    log.info("Saved new data of germany {}.", dataGermany.getLastUpdate());
                }
            } else {
                log.info("The data of last entry of germany are equals the new one {}.", dataGermany.getLastUpdate());
            }
        }
    }

    @Scheduled(cron = "0 20 */5 ? * *")
    public void saveGermanySummaryDataOfJson() throws IOException {
        DataGermanySummary dataGermanySummary = getJsonValue.getDataOfGermanySummary();
        Optional<DataGermanySummary> dataGermanySummaryLast = germanySummaryService.getLastEntryGermanySummary();

        if (dataGermanySummaryLast.isEmpty()) {
            germanySummaryService.saveDataGermanySummary(dataGermanySummary);
            log.info("Saved first data of germany summary {}.", dataGermanySummary.getLocalDate());
        }
        if (dataGermanySummaryLast.isPresent()) {
            if (germanySummaryService.findDataGermanySummaryByLocalTime(dataGermanySummary.getLocalTime()).isEmpty()) {
                germanySummaryService.saveDataGermanySummary(dataGermanySummary);
                log.info("Saved new data of germany summary {}.", dataGermanySummary.getLocalDate());
            } else {
                log.info("No new data of germany summary, Returned last one {}.", dataGermanySummary.getLocalDate());
            }
        }
    }
}
