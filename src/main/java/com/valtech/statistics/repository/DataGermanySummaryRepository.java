package com.valtech.statistics.repository;

import com.valtech.statistics.model.DataGermanySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface DataGermanySummaryRepository extends JpaRepository<DataGermanySummary, Long> {

    @Query("SELECT d FROM DataGermanySummary d WHERE d.localTime = ?1")
    Optional<DataGermanySummary> findDataGermanySummaryByLocalTime(LocalTime localTime);

    Optional<DataGermanySummary> findTopByOrderByDataGermanySummaryIdDesc();
}