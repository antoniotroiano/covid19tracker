package com.valtech.statistics.service;

import com.valtech.statistics.model.DataGermanySummary;
import com.valtech.statistics.repository.DataGermanySummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GermanySummaryService {

    private final DataGermanySummaryRepository dataGermanySummaryRepository;

    public DataGermanySummary saveDataGermanySummary(DataGermanySummary dataGermanySummary) {
        log.info("Save new data of germany summary {}.", dataGermanySummary);
        return dataGermanySummaryRepository.save(dataGermanySummary);
    }

    public List<DataGermanySummary> getAllDataGermanySummary() {
        log.info("Get all data germany summary.");
        List<DataGermanySummary> allDataGermanySummary = dataGermanySummaryRepository.findAll();
        if (allDataGermanySummary.isEmpty()) {
            log.warn("No data germany summary found.");
            return allDataGermanySummary;
        }
        log.info("Got all data germany summary successfully.");
        return allDataGermanySummary;
    }

    public Optional<DataGermanySummary> getLastEntryGermanySummary() {
        log.info("Get last entry of germany summary.");
        Optional<DataGermanySummary> getLastEntryGermanySummary = dataGermanySummaryRepository.findTopByOrderByDataGermanySummaryIdDesc();
        if (getLastEntryGermanySummary.isPresent()) {
            log.info("Found last entry of data germany summary. {}", getLastEntryGermanySummary.get().getLocalDate());
            return getLastEntryGermanySummary;
        }
        log.warn("Found no last entry of data germany summary.");
        return getLastEntryGermanySummary;
    }

    public Optional<DataGermanySummary> findDataGermanySummaryById(long id) {
        log.info("Find data of germany summary by id {}.", id);
        return dataGermanySummaryRepository.findById(id);
    }

    public Optional<DataGermanySummary> findDataGermanySummaryByLocalTime(LocalTime localTime) {
        log.info("Find data of germany summary by last time {}.", localTime);
        Optional<DataGermanySummary> findDataGermanyByTime = dataGermanySummaryRepository.findDataGermanySummaryByLocalTime(localTime);
        if (findDataGermanyByTime.isPresent()) {
            log.info("Found data of germany summary by last time {}.", localTime);
            return findDataGermanyByTime;
        }
        log.warn("Found no last data of germany summary by local time {}.", localTime);
        return findDataGermanyByTime;
    }

    public void deleteDataGermanySummary(DataGermanySummary dataGermanySummary) {
        log.info("Delete data of germany summary {}.", dataGermanySummary);
        dataGermanySummaryRepository.delete(dataGermanySummary);
    }
}