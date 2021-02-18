package com.dimkas.mars.marsrealestate.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "USERNAME", unique = true)
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL",  nullable = false)
    private String email;

    @Column(name = "FIRST_NAME",  nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME",  nullable = false)
    private String lastName;

    @Column(name = "ENABLE",  nullable = false)
    private boolean enable;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "USERS_AUTHORITIES", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID"))
    private Set<AuthorityEntity> authorityEntities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Set<AuthorityEntity> getAuthorityEntities() {
        return authorityEntities;
    }

    public void setAuthorityEntities(Set<AuthorityEntity> authorityEntities) {
        this.authorityEntities = authorityEntities;
    }

    public void addAuthority(AuthorityEntity authorityEntity) {
        if(authorityEntities == null) {
            this.authorityEntities = new HashSet<>();
        }
        authorityEntities.add(authorityEntity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserEntity that = (UserEntity) o;
        return id.equals(that.id) &&
            username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
