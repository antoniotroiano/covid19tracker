package com.valtech.statistics.service.scheuled;

import com.valtech.statistics.model.DataWorldSummary;
import com.valtech.statistics.model.SummaryToday;
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
}