package com.dimkas.mars.marsrealestate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dimkas.mars.marsrealestate.entities.AuthorityEntity;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity , Long> {
    AuthorityEntity findByAuthority(String authority);
}
