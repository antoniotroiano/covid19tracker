package com.valtech.statistics.repository;

import com.valtech.statistics.model.DataGermany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataGermanyRepository extends JpaRepository<DataGermany, Long> {

    @Query("SELECT d FROM DataGermany d WHERE d.lastUpdate = ?1")
    Optional<DataGermany> findDataGermanyByLastUpdate(String lastUpdate);

    Optional<DataGermany> findTopByOrderByDataGermanyIdDesc();
}
