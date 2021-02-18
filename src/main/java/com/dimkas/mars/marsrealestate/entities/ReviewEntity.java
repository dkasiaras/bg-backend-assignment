package com.dimkas.mars.marsrealestate.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "REVIEWS")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "RATE")
    private Short rate;

    @Column(name = "COMMENT")
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.JOIN)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.JOIN)
    @JoinColumn(name = "UNIT_ID")
    private UnitEntity unit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getRate() {
        return rate;
    }

    public void setRate(Short rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UnitEntity getUnit() {
        return unit;
    }

    public void setUnit(UnitEntity unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReviewEntity that = (ReviewEntity) o;
        return Objects.equals(id, that.id) &&
            rate.equals(that.rate) &&
            Objects.equals(comment, that.comment) &&
            user.equals(that.user) &&
            unit.equals(that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rate, comment, user, unit);
    }
}
