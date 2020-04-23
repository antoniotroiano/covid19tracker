package com.valtech.statistics.repository;

import com.valtech.statistics.model.DataWorldSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface DataWorldSummaryRepository extends JpaRepository<DataWorldSummary, Long> {

    @Query("SELECT d FROM DataWorldSummary d WHERE d.localTime = ?1")
    Optional<DataWorldSummary> findDataWorldSummaryByLocalTime(LocalTime localTime);

    Optional<DataWorldSummary> findTopByOrderByDataWorldSummaryIdDesc();
}