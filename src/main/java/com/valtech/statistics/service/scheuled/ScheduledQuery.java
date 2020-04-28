package com.valtech.statistics.service.scheuled;

import com.valtech.statistics.model.DataGermanySummary;
import com.valtech.statistics.model.DataWorld;
import com.valtech.statistics.model.DataWorldDaily;
import com.valtech.statistics.model.DataWorldSummary;
import com.valtech.statistics.model.SummaryToday;
import com.valtech.statistics.service.DateFormat;
import com.valtech.statistics.service.GermanySummaryService;
import com.valtech.statistics.service.WorldDailyService;
import com.valtech.statistics.service.WorldService;
import com.valtech.statistics.service.WorldSummaryService;
import com.valtech.statistics.service.json.GetJsonValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduledQuery {

    private final WorldService worldService;
    private final WorldDailyService worldDailyService;
    private final WorldSummaryService worldSummaryService;
    private final GermanySummaryService germanySummaryService;
    private final GetJsonValue getJsonValue;
    private final DateFormat dateFormat;

    /*Get and save new data of world*/
    @Scheduled(cron = "0 5 */3 ? * *")
    public void saveWorldDataOfJson() throws IOException {
        log.info("Invoke get and save new data of world.");
        DataWorld dataWorld = getJsonValue.getDataOfWorldToModel();
        Optional<DataWorld> dataWorldLast = worldService.getLastEntryWorld();

        if (dataWorldLast.isEmpty()) {
            worldService.saveDataWorld(dataWorld);
            log.info("Saved first data of world {}.", dataWorld.getLastUpdate());
        }
        if (dataWorldLast.isPresent()) {
            if (dataWorldLast.get().getConfirmed() != dataWorld.getConfirmed() ||
                    dataWorldLast.get().getRecovered() != dataWorld.getRecovered() ||
                    dataWorldLast.get().getDeaths() != dataWorld.getDeaths()) {
                if (dataWorldLast.get().getLastUpdate().equals(dataWorld.getLastUpdate())) {
                    log.info("No new data of world. Returned last one {}.", dataWorld.getLastUpdate());
                } else {
                    worldService.saveDataWorld(dataWorld);
                    log.info("Saved new data of world {}.", dataWorld.getLastUpdate());
                }
            } else {
                log.info("The data of last entry world are equals the new one {}.", dataWorld.getLastUpdate());
            }
        }
    }

    private void saveNewWorldDaily() {
        log.info("Invoke save new world daily.");
        Optional<DataWorld> dataWorldLast = worldService.getLastEntryWorld();
        DataWorldDaily dataWorldDailyYesterday = new DataWorldDaily();
        Optional<DataWorldDaily> dataWorldDailyLast = worldDailyService.getLastEntryWorldDaily();
        String dateLastWorld;
        LocalDate localDate = LocalDate.now();

        if (dataWorldDailyLast.isEmpty()) {
            dataWorldDailyYesterday.setConfirmed(dataWorldLast.get().getConfirmed());
            dataWorldDailyYesterday.setRecovered(dataWorldLast.get().getRecovered());
            dataWorldDailyYesterday.setDeaths(dataWorldLast.get().getDeaths());
            dataWorldDailyYesterday.setLocalDate(LocalDate.now().toString());
            worldDailyService.saveDataWorldDaily(dataWorldDailyYesterday);
            log.info("Saved first data of world daily {}.", dataWorldDailyYesterday.getLocalDate());
        } else {
            if (dataWorldLast.isPresent()) {
                dateLastWorld = dateFormat.formatLastUpdateToDate(dataWorldLast.get().getLastUpdate());
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyy");
                String local = dtf.format(localDate);
                if (!dateLastWorld.equals(local)) {
                    dataWorldDailyYesterday.setConfirmed(dataWorldLast.get().getConfirmed());
                    dataWorldDailyYesterday.setRecovered(dataWorldLast.get().getRecovered());
                    dataWorldDailyYesterday.setDeaths(dataWorldLast.get().getDeaths());
                    dataWorldDailyYesterday.setLocalDate(LocalDate.now().toString());
                    worldDailyService.saveDataWorldDaily(dataWorldDailyYesterday);
                    log.info("Saved new daily update world {},", dataWorldDailyYesterday.getLocalDate());
                } else {
                    log.info("No new daily update world.");
                }
            } else {
                log.warn("No last data of world.");
            }
        }
    }

    /*Save new data of world summary*/
    @Scheduled(cron = "0 15 */4 ? * *")
    public void saveWorldSummaryDataOfJson() {
        log.info("Invoke get and save world summary.");
        saveNewWorldDaily();

        Optional<DataWorld> dataWorldLast = worldService.getLastEntryWorld();
        if (dataWorldLast.isPresent()) {
            Optional<DataWorldDaily> dataWorldDailyYesterdayLast = worldDailyService.getLastEntryWorldDaily();
            DataWorldSummary dataWorldSummaryNew = new DataWorldSummary();
            Optional<DataWorldSummary> dataWorldSummaryLast = worldSummaryService.getLastEntryWorldSummary();

            if (dataWorldDailyYesterdayLast.isPresent()) {
                dataWorldSummaryNew.setNewConfirmed(dataWorldLast.get().getConfirmed() - dataWorldDailyYesterdayLast.get().getConfirmed());
                dataWorldSummaryNew.setTotalConfirmed(dataWorldLast.get().getConfirmed());
                dataWorldSummaryNew.setNewRecovered(dataWorldLast.get().getRecovered() - dataWorldDailyYesterdayLast.get().getRecovered());
                dataWorldSummaryNew.setTotalRecovered(dataWorldLast.get().getRecovered());
                dataWorldSummaryNew.setNewDeaths(dataWorldLast.get().getDeaths() - dataWorldDailyYesterdayLast.get().getDeaths());
                dataWorldSummaryNew.setTotalDeaths(dataWorldLast.get().getDeaths());
                dataWorldSummaryNew.setLocalDate(LocalDate.now());
                dataWorldSummaryNew.setLocalTime(LocalTime.now().withNano(0));
                log.info("Create new dataset of data world summary");
            }
            if (dataWorldSummaryLast.isEmpty()) {
                worldSummaryService.saveDataWorldSummary(dataWorldSummaryNew);
                log.info("Saved first data world summary {}.", dataWorldSummaryNew.getLocalDate());
            }
            if (dataWorldSummaryLast.isPresent()) {
                if (dataWorldSummaryLast.get().getNewConfirmed() != dataWorldSummaryNew.getNewConfirmed() ||
                        dataWorldSummaryLast.get().getNewRecovered() != dataWorldSummaryNew.getNewRecovered() ||
                        dataWorldSummaryLast.get().getNewDeaths() != dataWorldSummaryNew.getNewDeaths()) {
                    worldSummaryService.saveDataWorldSummary(dataWorldSummaryNew);
                    log.info("Saved new data of world summary {}.", dataWorldSummaryNew.getLocalDate());
                } else {
                    log.warn("No new data of world summary, Returned last one {}.", dataWorldSummaryNew.getLocalDate());
                }
            }

        } else {
            log.warn("No last data of world.");
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
                    germanySummaryService.saveDataGermanySummary(dataGermanySummary);
                    log.info("Saved new data of germany {}.", dataGermanySummary.getLastUpdate());
                }
            } else {
                log.info("The data of last entry of germany are equals the new one, returned last one {}.", dataGermanySummary.getLastUpdate());
            }

        }
    }
}