package com.dimkas.mars.marsrealestate.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "AUTHORITIES")
public class AuthorityEntity {

    @Id
    @Column(name = "AUTHORITY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column(name = "AUTHORITY", unique = true)
    String authority;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
