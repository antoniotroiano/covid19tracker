package com.valtech.statistics.service;

import com.valtech.statistics.model.DataWorldSummary;
import com.valtech.statistics.repository.DataWorldSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorldSummaryService {

    private final DataWorldSummaryRepository dataWorldSummaryRepository;

    public DataWorldSummary saveDataWorldSummary(DataWorldSummary dataWorldSummary) {
        log.info("Invoke save new data world summary.");
        return dataWorldSummaryRepository.save(dataWorldSummary);
    }

    public List<DataWorldSummary> getAllDataWorldSummary() {
        log.info("Invoke get all data world summary.");
        return dataWorldSummaryRepository.findAll();
    }

    public Optional<DataWorldSummary> getLastEntryWorldSummary() {
        log.info("Invoke get last entry world summary.");
        return dataWorldSummaryRepository.findTopByOrderByDataWorldSummaryIdDesc();
    }

    public Optional<DataWorldSummary> findDataWorldSummaryById(long id) {
        return dataWorldSummaryRepository.findById(id);
    }

    public Optional<DataWorldSummary> findDataWorldSummaryByLocalDate(LocalDate localDate) {
        log.info("Invoke find data of world suummary by local date {}.", localDate);
        return dataWorldSummaryRepository.findDataWorldSummaryByLocalDate(localDate);
    }

    public void deleteDataWorldSummary(DataWorldSummary dataWorldSummary) {
        dataWorldSummaryRepository.delete(dataWorldSummary);
    }
}
