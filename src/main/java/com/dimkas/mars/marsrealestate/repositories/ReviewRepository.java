package com.dimkas.mars.marsrealestate.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dimkas.mars.marsrealestate.entities.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Optional<ReviewEntity> findByUserUsernameAndUnitId(String username, Long unitId);
}
