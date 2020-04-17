package com.valtech.statistics.service;

import com.valtech.statistics.model.DataGermany;
import com.valtech.statistics.repository.DataGermanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GermanyService {

    private final DataGermanyRepository dataGermanyRepository;

    public DataGermany saveDataGermany(DataGermany dataGermany) {
        log.info("Invoke save new data of germany.");
        return dataGermanyRepository.save(dataGermany);
    }

    public List<DataGermany> getAllDataGermany() {
        log.info("Invoke get all data of germany.");
        return dataGermanyRepository.findAll();
    }

    public DataGermany getLastEntryGermany() {
        log.info("Invoke get last entry germany.");
        return dataGermanyRepository.findTopByOrderByDataGermanyIdDesc();
    }

    public Optional<DataGermany> findDataGermanById(long id) {
        return dataGermanyRepository.findById(id);
    }

    public Optional<DataGermany> findDataGermanyByLastUpdate(String lastUpdate) {
        log.info("Invoke find data of germany by last update {}.", lastUpdate);
        return dataGermanyRepository.findDataGermanyByLastUpdate(lastUpdate);
    }

    public void deleteDataGermany(DataGermany dataGermany) {
        dataGermanyRepository.delete(dataGermany);
    }
}
