package com.dimkas.mars.marsrealestate.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dimkas.mars.marsrealestate.entities.UserEntity;

public interface UserRepository  extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
    @Override
    Optional<UserEntity> findById(Long id);
}
