package com.dimkas.mars.marsrealestate.sercvices.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dimkas.mars.marsrealestate.constants.SortDirections;
import com.dimkas.mars.marsrealestate.dtos.Review;
import com.dimkas.mars.marsrealestate.dtos.Unit;
import com.dimkas.mars.marsrealestate.entities.ReviewEntity;
import com.dimkas.mars.marsrealestate.entities.UnitEntity;
import com.dimkas.mars.marsrealestate.entities.UserEntity;
import com.dimkas.mars.marsrealestate.repositories.UnitRepository;
import com.dimkas.mars.marsrealestate.repositories.ReviewRepository;
import com.dimkas.mars.marsrealestate.repositories.UserRepository;
import com.dimkas.mars.marsrealestate.sercvices.UnitService;

@Service
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UnitServiceImpl(UnitRepository repository, ReviewRepository reviewRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.unitRepository = repository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Unit findById(Long id) {
        Unit unit = null;
        Optional<UnitEntity> optionalUnit = this.unitRepository.findById(id);
        if(optionalUnit.isPresent()) {
            unit = modelMapper.map(optionalUnit.get(), Unit.class);
        }

        return unit;
    }

    @Override
    public Page<Unit> findByTitleAndRegion(Optional<String> title, Optional<String> region, int pageSize, int pageNum,  Optional<String> sortDirection) {
        PageRequest pageRequest;

        if(sortDirection.isPresent()) {
            Sort sort = SortDirections.ASC.equalsIgnoreCase(sortDirection.get()) ? Sort.by("review_rate").ascending() : Sort.by("review_rate").descending();
            pageRequest = PageRequest.of(pageNum, pageSize, sort);
        } else {
            pageRequest = PageRequest.of(pageNum, pageSize);
        }

        return  unitRepository.findByTitleAndRegion(title,region, pageRequest)
                              .map(entity -> modelMapper.map(entity, Unit.class));
    }

    @Override
    public Review updateReviewOfUser(Long unitId, String username, Review review) {
        Optional<ReviewEntity> oldReview = reviewRepository.findByUserUsernameAndUnitId(username, unitId);
        ReviewEntity newReview = modelMapper.map(review, ReviewEntity.class);

        oldReview.ifPresent((rev) -> {
            newReview.setId(rev.getId());
        });

        Optional<UnitEntity> unitEntity = unitRepository.findById(unitId);
        if(unitEntity.isEmpty()) {
            throw new RuntimeException("Unit does not exist");
        }
        newReview.setUnit(unitEntity.get());

        Optional<UserEntity> userEntity = userRepository.findByUsername(username);

        if(userEntity.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }
        newReview.setUser(userEntity.get());

        ReviewEntity result = reviewRepository.save(newReview);
        return modelMapper.map(result, Review.class);
    }
}
