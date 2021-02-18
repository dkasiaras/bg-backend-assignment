package com.dimkas.mars.marsrealestate.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dimkas.mars.marsrealestate.entities.UnitEntity;

public interface UnitCriteriaRepository {

    Page<UnitEntity> findByTitleAndRegion(Optional<String> title, Optional<String> region, Pageable pageable);

}
