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
        log.info("Invoke save new data of world.");
        return dataWorldRepository.save(dataWorld);
    }

    public List<DataWorld> getAllData() {
        log.info("Invoke get all data of world.");
        return dataWorldRepository.findAll();
    }

    public Optional<DataWorld> getLastEntryWorld() {
        log.info("Invoke get last entry world.");
        return dataWorldRepository.findTopByOrderByDataWorldIdDesc();
    }

    public Optional<DataWorld> findDataWorldById(long id) {
        return dataWorldRepository.findById(id);
    }

    public Optional<DataWorld> findDataWorldByLastUpdate(String lastUpdate) {
        log.info("Invoke find data of world by last update {}.", lastUpdate);
        return dataWorldRepository.findDataWorldByLastUpdate(lastUpdate);
    }

    public void deleteDataWorld(DataWorld dataWorld) {
        dataWorldRepository.delete(dataWorld);
    }
}