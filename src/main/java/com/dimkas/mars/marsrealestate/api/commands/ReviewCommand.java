package com.dimkas.mars.marsrealestate.api.commands;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

@Component
public class ReviewCommand {

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    private short rate;

    private String comment;

    public short getRate() {
        return rate;
    }

    public void setRate(short rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
