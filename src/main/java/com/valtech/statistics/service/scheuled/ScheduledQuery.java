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
    @Scheduled(cron = "0 5 */2 ? * *")
    public void saveWorldDataOfJson() throws IOException {
        log.info("Invoke get and save new data of world.");
        SummaryToday summaryToday = getJsonValue.getDataOfWorldToModel();
        DataWorldSummary dataWorldSummary = new DataWorldSummary(summaryToday);
        Optional<DataWorldSummary> getLastEntry = worldSummaryService.getLastEntryWorldSummary();

        if (getLastEntry.isEmpty()) {
            worldSummaryService.saveDataWorldSummary(dataWorldSummary);
            log.info("Saved first data of world {}.", dataWorldSummary.getLastUpdate());
        } else {
            if (getLastEntry.get().getLastUpdate().equals(dataWorldSummary.getLastUpdate())) {
                log.info("No new data of world, returned last one {}.", dataWorldSummary.getLastUpdate());
            } else {
                if (getLastEntry.get().getTotalConfirmed() != dataWorldSummary.getTotalConfirmed() ||
                        getLastEntry.get().getTotalRecovered() != dataWorldSummary.getTotalRecovered() ||
                        getLastEntry.get().getTotalDeaths() != dataWorldSummary.getTotalDeaths()) {
                    lastCheckBeforeSaveWorld(dataWorldSummary);
                } else {
                    log.info("One or more information of last entry of world are equals the new one, returned last one {}.", dataWorldSummary.getLastUpdate());
                }
            }
        }
    }

    private void lastCheckBeforeSaveWorld(DataWorldSummary dataWorldSummary) {
        if (dataWorldSummary.getNewConfirmed() == 0 &&
                dataWorldSummary.getNewRecovered() == 0 &&
                dataWorldSummary.getNewDeaths() == 0) {
            log.info("No new data of world, returned last one {}.", dataWorldSummary.getLastUpdate());
        } else {
            worldSummaryService.saveDataWorldSummary(dataWorldSummary);
            log.info("Saved new data of world {}.", dataWorldSummary.getLastUpdate());
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
        } else {
            if (getLastSummary.get().getLastUpdate().equals(dataGermanySummary.getLastUpdate())) {
                log.info("No new data of germany, returned last one {}.", dataGermanySummary.getLastUpdate());
            } else {
                if (getLastSummary.get().getTotalConfirmed() != dataGermanySummary.getTotalConfirmed() ||
                        getLastSummary.get().getTotalRecovered() != dataGermanySummary.getTotalRecovered() ||
                        getLastSummary.get().getTotalDeaths() != dataGermanySummary.getTotalDeaths()) {
                    lastCheckBeforeSaveGermany(dataGermanySummary);
                } else {
                    log.info("One or more information of last entry of germany are equals the new one, returned last one {}.", dataGermanySummary.getLastUpdate());
                }
            }
        }
    }

    private void lastCheckBeforeSaveGermany(DataGermanySummary dataGermanySummary) {
        if (dataGermanySummary.getNewConfirmed() == 0 &&
                dataGermanySummary.getNewRecovered() == 0 &&
                dataGermanySummary.getNewDeaths() == 0) {
            log.info("No new data of germany, returned last one {}.", dataGermanySummary.getLastUpdate());
        } else {
            germanySummaryService.saveDataGermanySummary(dataGermanySummary);
            log.info("Saved new data of germany {}.", dataGermanySummary.getLastUpdate());
        }
    }
}