package com.valtech.statistics.service;

import com.valtech.statistics.model.DataWorld;
import com.valtech.statistics.repository.DataWorldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorldService {

    private final DataWorldRepository dataWorldRepository;

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
        log.debug("Get last entry of world.");
        Optional<DataWorld> getLastEntryWorld = dataWorldRepository.findTopByOrderByDataWorldIdDesc();
        if (getLastEntryWorld.isPresent()) {
            log.debug("Found last entry of data world. {}", getLastEntryWorld.get().getLastUpdate());
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
}