package com.dimkas.mars.marsrealestate.sercvices;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.dimkas.mars.marsrealestate.dtos.Review;
import com.dimkas.mars.marsrealestate.dtos.Unit;

public interface UnitService {

    Unit findById(Long id);
    Page<Unit> findByTitleAndRegion(Optional<String> title, Optional<String> region, int pageSize, int pageNum, Optional<String> sortDirection);
    Review updateReviewOfUser(Long unitId, String username, Review review);

}
