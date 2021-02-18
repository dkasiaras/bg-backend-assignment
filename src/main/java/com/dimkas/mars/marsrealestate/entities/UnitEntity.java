package com.dimkas.mars.marsrealestate.entities;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@Entity
@Table(name = "UNITS")
public class UnitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UNIT_ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "REGION")
    private String region;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE")
    private Double price;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "UNIT_ID",  insertable = false, updatable = false)
    private Set<ReviewEntity> reviews;

    @Formula("(SELECT IFNULL(AVG(R.RATE), -1) FROM REVIEWS R WHERE R.UNIT_ID = UNIT_ID)")
    private float avgRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Set<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(Set<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public float getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(float avgRate) {
        this.avgRate = avgRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UnitEntity that = (UnitEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
