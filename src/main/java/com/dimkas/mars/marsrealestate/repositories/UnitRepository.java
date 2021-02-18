package com.dimkas.mars.marsrealestate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dimkas.mars.marsrealestate.entities.UnitEntity;

public interface UnitRepository extends JpaRepository<UnitEntity, Long>, UnitCriteriaRepository {
}
