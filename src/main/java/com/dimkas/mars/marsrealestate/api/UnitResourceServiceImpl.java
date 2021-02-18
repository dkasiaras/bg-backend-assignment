package com.dimkas.mars.marsrealestate.api;

import java.security.Principal;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dimkas.mars.marsrealestate.api.commands.ReviewCommand;
import com.dimkas.mars.marsrealestate.constants.DefaultPageAttributes;
import com.dimkas.mars.marsrealestate.dtos.Review;
import com.dimkas.mars.marsrealestate.dtos.Unit;
import com.dimkas.mars.marsrealestate.sercvices.UnitService;

@RestController
@RequestMapping(path = "/units")
public class UnitResourceServiceImpl  {

    private final UnitService unitService;

    public UnitResourceServiceImpl(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_UNITS')")
    public Page<Unit> getUnits(@RequestParam(defaultValue = DefaultPageAttributes.DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DefaultPageAttributes.DEFAULT_PAGE_SIZE) int pageSize,
        @RequestParam Optional<String> sortByReviews,
        @RequestParam Optional<String> region,
        @RequestParam Optional<String> title ) {

        Page<Unit> units =  unitService.findByTitleAndRegion(title, region, pageSize, page, sortByReviews);
        return units;
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('READ_UNITS')")
    public Unit getById(@PathVariable Long id) {
        return unitService.findById(id);
    }

    @PutMapping(path = "/{unitId}/review")
    @PreAuthorize("hasAuthority('WRITE_REVIEWS')")
    public Review updateReview(@RequestBody @Valid ReviewCommand reviewCommand, @PathVariable Long unitId, Principal principal) {

        Review review = new Review();
        review.setComment(reviewCommand.getComment());
        review.setRate(reviewCommand.getRate());

        return unitService.updateReviewOfUser(unitId, principal.getName(), review);
    }

}
