package com.valtech.statistics.service;

import com.valtech.statistics.model.DataWorld;
import com.valtech.statistics.repository.CoronaWorldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticService {

    private final CoronaWorldRepository coronaWorldRepository;

    public DataWorld saveDataWorld(DataWorld dataWorld) {
        log.info("Invoke save new data of world.");
        return coronaWorldRepository.save(dataWorld);
    }

    public List<DataWorld> getAllData() {
        log.info("Invoke get all data of world.");
        return coronaWorldRepository.findAll();
    }

    public DataWorld getLastEntry() {
        log.info("Invoke get last entry.");
        return coronaWorldRepository.findTopByOrderByDataWorldIdDesc();
    }

    public Optional<DataWorld> findDataWorldById(long id) {
        return coronaWorldRepository.findById(id);
    }

    public Optional<DataWorld> findDataWorldByLastUpdate(String lastUpdate) {
        log.info("Invoke find data of world by last update {}.", lastUpdate);
        return coronaWorldRepository.findDataWorldByLastUpdate(lastUpdate);
    }

    public void deleteDataWorld(DataWorld dataWorld) {
        coronaWorldRepository.delete(dataWorld);
    }
}