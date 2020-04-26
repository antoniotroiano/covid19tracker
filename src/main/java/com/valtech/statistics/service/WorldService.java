package com.valtech.statistics.service;

import com.valtech.statistics.model.DataWorld;
import com.valtech.statistics.repository.DataWorldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorldService {

    private final DataWorldRepository dataWorldRepository;
    private final GetJsonValue getJsonValue;

    public DataWorld saveDataWorld(DataWorld dataWorld) {
        log.info("Save new data of world {}.", dataWorld);
        return dataWorldRepository.save(dataWorld);
    }

    public List<DataWorld> getAllData() {
        log.info("Get all data of world.");
        final List<DataWorld> allDataWorld = dataWorldRepository.findAll();
        if (allDataWorld.isEmpty()) {
            log.warn("No data world found.");
            return allDataWorld;
        }
        log.info("Got all data world successfully.");
        return allDataWorld;
    }

    public Optional<DataWorld> getLastEntryWorld() {
        log.info("Get last entry of world.");
        Optional<DataWorld> getLastEntryWorld = dataWorldRepository.findTopByOrderByDataWorldIdDesc();
        if (getLastEntryWorld.isPresent()) {
            log.info("Found last entry of data world. {}", getLastEntryWorld.get().getLastUpdate());
            return getLastEntryWorld;
        }
        log.warn("Found no last entry of data world.");
        return getLastEntryWorld;
    }

    public Optional<DataWorld> findDataWorldById(long id) {
        log.info("Find data of world by id {}.", id);
        return dataWorldRepository.findById(id);
    }

    public Optional<DataWorld> findDataWorldByLastUpdate(String lastUpdate) {
        log.info("Find data of world by last update {}.", lastUpdate);
        return dataWorldRepository.findDataWorldByLastUpdate(lastUpdate);
    }

    public void deleteDataWorld(DataWorld dataWorld) {
        log.info("Delete data of world {}.", dataWorld);
        dataWorldRepository.delete(dataWorld);
    }

    @Scheduled(cron = "0 5 */3 ? * *")
    public void saveDataOfJson() throws IOException {
        DataWorld dataWorld = getJsonValue.getDataOfWorldToModel();
        Optional<DataWorld> dataWorldLast = getLastEntryWorld();

        if (dataWorldLast.isEmpty()) {
            saveDataWorld(dataWorld);
            log.info("Saved first data of world {}.", dataWorld.getLastUpdate());
        }
        if (dataWorldLast.isPresent()) {
            if (dataWorldLast.get().getConfirmed() != dataWorld.getConfirmed() ||
                    dataWorldLast.get().getRecovered() != dataWorld.getRecovered() ||
                    dataWorldLast.get().getDeaths() != dataWorld.getDeaths()) {
                if (dataWorldLast.get().getLastUpdate().equals(dataWorld.getLastUpdate())) {
                    log.info("No new data of world. Returned last one {}.", dataWorld.getLastUpdate());
                } else {
                    saveDataWorld(dataWorld);
                    log.info("Saved new data of world {}.", dataWorld.getLastUpdate());
                }
            } else {
                log.info("The data of last entry world are equals the new one {}.", dataWorld.getLastUpdate());
            }
        }
    }
}