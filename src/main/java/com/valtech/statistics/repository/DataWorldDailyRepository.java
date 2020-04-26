package com.valtech.statistics.repository;

import com.valtech.statistics.model.DataWorldDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataWorldDailyRepository extends JpaRepository<DataWorldDaily, Long> {

    @Query("SELECT d FROM DataWorldDaily d WHERE d.localDate = ?1")
    Optional<DataWorldDaily> findDataWorldDailyByLastUpdate(String localDate);

    Optional<DataWorldDaily> findTopByOrderByDataWorldDailyIdDesc();
}