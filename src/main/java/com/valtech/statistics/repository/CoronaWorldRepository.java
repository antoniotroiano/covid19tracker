package com.valtech.statistics.repository;

import com.valtech.statistics.model.DataWorld;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoronaWorldRepository extends JpaRepository<DataWorld, Long> {

    @Query("SELECT d FROM DataWorld d WHERE d.lastUpdate = ?1")
    Optional<DataWorld> findDataWorldByLastUpdate(String lastUpdate);

    DataWorld findTopByOrderByDataWorldIdDesc();
}