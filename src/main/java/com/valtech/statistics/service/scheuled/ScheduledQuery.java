package com.valtech.statistics.service.scheuled;

import com.valtech.statistics.model.DataGermanySummary;
import com.valtech.statistics.model.DataWorldSummary;
import com.valtech.statistics.model.SummaryToday;
import com.valtech.statistics.service.GermanySummaryService;
import com.valtech.statistics.service.WorldSummaryService;
import com.valtech.statistics.service.json.GetJsonValue;
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

    private final WorldSummaryService worldSummaryService;
    private final GermanySummaryService germanySummaryService;
    private final GetJsonValue getJsonValue;

    /*Get and save new data of world*/
    @Scheduled(cron = "0 5 */3 ? * *")
    public void saveWorldDataOfJson() throws IOException {
        log.info("Invoke get and save new data of world.");
        SummaryToday summaryToday = getJsonValue.getDataOfWorldToModel();
        DataWorldSummary dataWorldSummary = new DataWorldSummary(summaryToday);
        Optional<DataWorldSummary> getLastSummary = worldSummaryService.getLastEntryWorldSummary();

        if (getLastSummary.isEmpty()) {
            worldSummaryService.saveDataWorldSummary(dataWorldSummary);
            log.info("Saved first data of world {}.", dataWorldSummary.getLastUpdate());
        }
        if (getLastSummary.isPresent()) {
            if (getLastSummary.get().getTotalConfirmed() != dataWorldSummary.getTotalConfirmed() ||
                    getLastSummary.get().getTotalRecovered() != dataWorldSummary.getTotalRecovered() ||
                    getLastSummary.get().getTotalDeaths() != dataWorldSummary.getTotalDeaths()) {
                if (getLastSummary.get().getLastUpdate().equals(dataWorldSummary.getLastUpdate())) {
                    log.info("No new data of world, returned last one {}.", dataWorldSummary.getLastUpdate());
                } else {
                    if (dataWorldSummary.getNewConfirmed() == 0 &&
                            dataWorldSummary.getNewRecovered() == 0 &&
                            dataWorldSummary.getNewDeaths() == 0) {
                        log.info("No new data of world, returned last one {}.", dataWorldSummary.getLastUpdate());
                    } else {
                        worldSummaryService.saveDataWorldSummary(dataWorldSummary);
                        log.info("Saved new data of world {}.", dataWorldSummary.getLastUpdate());
                    }
                }
            } else {
                log.info("The data of last entry of world are equals the new one, returned last one {}.", dataWorldSummary.getLastUpdate());
            }
        }
    }

    /*Get and save new data of germany*/
    @Scheduled(cron = "0 10 */2 ? * *")
    public void saveGermanyDataOfJson() throws IOException {
        log.info("Invoke get and save new data of germany.");
        SummaryToday summaryToday = getJsonValue.getDataForSelectedCountry("Germany");
        DataGermanySummary dataGermanySummary = new DataGermanySummary(summaryToday);
        Optional<DataGermanySummary> getLastSummary = germanySummaryService.getLastEntryGermanySummary();

        if (getLastSummary.isEmpty()) {
            germanySummaryService.saveDataGermanySummary(dataGermanySummary);
            log.info("Saved first data of germany {}.", dataGermanySummary.getLastUpdate());
        }
        if (getLastSummary.isPresent()) {
            if (getLastSummary.get().getTotalConfirmed() != dataGermanySummary.getTotalConfirmed() ||
                    getLastSummary.get().getTotalRecovered() != dataGermanySummary.getTotalRecovered() ||
                    getLastSummary.get().getTotalDeaths() != dataGermanySummary.getTotalDeaths()) {
                if (getLastSummary.get().getLastUpdate().equals(dataGermanySummary.getLastUpdate())) {
                    log.info("No new data of germany, returned last one {}.", dataGermanySummary.getLastUpdate());
                } else {
                    if (summaryToday.getNewConfirmedToday() == 0 &&
                            summaryToday.getNewRecoveredToday() == 0 &&
                            summaryToday.getNewDeathsToday() == 0) {
                        log.info("No new data of germany, returned last one {}.", dataGermanySummary.getLastUpdate());
                    } else {
                        germanySummaryService.saveDataGermanySummary(dataGermanySummary);
                        log.info("Saved new data of germany {}.", dataGermanySummary.getLastUpdate());
                    }
                }
            } else {
                log.info("The data of last entry of germany are equals the new one, returned last one {}.", dataGermanySummary.getLastUpdate());
            }
        }
    }
}