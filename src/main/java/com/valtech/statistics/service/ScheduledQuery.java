package com.valtech.statistics.service;

import com.valtech.statistics.model.DataGermany;
import com.valtech.statistics.model.DataGermanySummary;
import com.valtech.statistics.model.DataWorld;
import com.valtech.statistics.model.DataWorldDaily;
import com.valtech.statistics.model.DataWorldSummary;
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
    private final GermanyService germanyService;
    private final GermanySummaryService germanySummaryService;
    private final GetJsonValue getJsonValue;
    private final DateFormat dateFormat;

    @Scheduled(cron = "0 5 */3 ? * *")
    public void saveWorldDataOfJson() throws IOException {
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
        Optional<DataWorldDaily> dataWorldDaily = worldDailyService.getLastEntryWorldDaily();
        String dateLastWorld;
        LocalDate localDate = LocalDate.now();

        if (dataWorldDaily.isEmpty()) {
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

    @Scheduled(cron = "0 15 */4 ? * *")
    public void saveWorldSummaryDataOfJson() {
        log.info("Invoke save world summary.");
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

                if (dataWorldSummaryLast.isPresent()) {
                    if (dataWorldSummaryLast.get().getNewConfirmed() != dataWorldSummaryNew.getNewConfirmed() &&
                            dataWorldSummaryLast.get().getNewRecovered() != dataWorldSummaryNew.getNewRecovered() &&
                            dataWorldSummaryLast.get().getNewDeaths() != dataWorldSummaryNew.getNewDeaths()) {
                        worldSummaryService.saveDataWorldSummary(dataWorldSummaryNew);
                        log.info("Saved new data of world summary {}.", dataWorldSummaryNew.getLocalDate());
                    } else {
                        log.info("No new data of world summary, Returned last one {}.", dataWorldSummaryNew.getLocalDate());
                    }
                }
            }
        } else {
            log.warn("No last data of world.");
        }
    }

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
        DataGermanySummary dataGermanySummary = getJsonValue.createDataOfGermanySummary();
        Optional<DataGermanySummary> dataGermanySummaryLast = germanySummaryService.getLastEntryGermanySummary();

        if (dataGermanySummaryLast.isEmpty()) {
            germanySummaryService.saveDataGermanySummary(dataGermanySummary);
            log.info("Saved first data of germany summary {}.", dataGermanySummary.getLocalDate());
        }
        if (dataGermanySummaryLast.isPresent()) {
            if (dataGermanySummaryLast.get().getNewConfirmed() != dataGermanySummary.getNewConfirmed() &&
                    dataGermanySummaryLast.get().getNewRecovered() != dataGermanySummary.getNewRecovered() &&
                    dataGermanySummaryLast.get().getNewDeaths() != dataGermanySummary.getNewDeaths()) {
                germanySummaryService.saveDataGermanySummary(dataGermanySummary);
                log.info("Saved new data of germany summary {}.", dataGermanySummary.getLocalDate());
            } else {
                log.info("No new data of germany summary, Returned last one {}.", dataGermanySummary.getLocalDate());
            }
        }
    }
}