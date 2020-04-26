package com.valtech.statistics.service;

import com.valtech.statistics.model.DataWorldDaily;
import com.valtech.statistics.repository.DataWorldDailyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorldDailyService {

    private final DataWorldDailyRepository dataWorldDailyRepository;

    public DataWorldDaily saveDataWorldDaily(DataWorldDaily dataWorldDaily) {
        log.info("Save new data of world daily {}.", dataWorldDaily);
        return dataWorldDailyRepository.save(dataWorldDaily);
    }

    public Optional<DataWorldDaily> getLastEntryWorldDaily() {
        log.info("Get last entry of world daily.");
        Optional<DataWorldDaily> getLastEntryWorldDaily = dataWorldDailyRepository.findTopByOrderByDataWorldDailyIdDesc();
        if (getLastEntryWorldDaily.isPresent()) {
            log.info("Found last entry of data world. {}", getLastEntryWorldDaily.get().getLocalDate());
            return getLastEntryWorldDaily;
        }
        log.warn("Found no last entry of data world.");
        return getLastEntryWorldDaily;
    }
}