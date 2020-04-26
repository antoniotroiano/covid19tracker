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
        log.info("Get all data of germany.");

        List<DataGermany> allDataGermany = dataGermanyRepository.findAll();
        if (allDataGermany.isEmpty()) {
            log.warn("No data germany found.");
            return allDataGermany;
        }
        log.info("Got all data germany successfully.");
        return allDataGermany;
    }

    public Optional<DataGermany> getLastEntryGermany() {
        log.info("Get last entry of germany.");
        Optional<DataGermany> getLastEntryGermany = dataGermanyRepository.findTopByOrderByDataGermanyIdDesc();
        if (getLastEntryGermany.isPresent()) {
            log.info("Found last entry of data germany. {}", getLastEntryGermany.get().getLocalDate());
            return getLastEntryGermany;
        }
        log.warn("Found no last entry of data germany.");
        return getLastEntryGermany;
    }

    public Optional<DataGermany> findDataGermanById(long id) {
        log.info("Find data of germany by id {}.", id);
        return dataGermanyRepository.findById(id);
    }

    public Optional<DataGermany> findDataGermanyByLastUpdate(String lastUpdate) {
        log.info("Find data of germany by last update {}.", lastUpdate);
        Optional<DataGermany> findDataGermanyByLastUpdate = dataGermanyRepository.findDataGermanyByLastUpdate(lastUpdate);
        if (findDataGermanyByLastUpdate.isPresent()) {
            log.info("Found data of germany by last update {}.", lastUpdate);
            return findDataGermanyByLastUpdate;
        }
        log.warn("Found no last data of germany by last update {}.", lastUpdate);
        return findDataGermanyByLastUpdate;
    }

    public void deleteDataGermany(DataGermany dataGermany) {
        log.info("Delete data of germany {}.", dataGermany);
        dataGermanyRepository.delete(dataGermany);
    }
}