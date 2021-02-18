package com.dimkas.mars.marsrealestate.sercvices.impl

import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import com.dimkas.mars.marsrealestate.dtos.Review
import com.dimkas.mars.marsrealestate.dtos.Unit
import com.dimkas.mars.marsrealestate.entities.ReviewEntity
import com.dimkas.mars.marsrealestate.entities.UnitEntity
import com.dimkas.mars.marsrealestate.entities.UserEntity
import com.dimkas.mars.marsrealestate.repositories.ReviewRepository
import com.dimkas.mars.marsrealestate.repositories.UnitRepository
import com.dimkas.mars.marsrealestate.repositories.UserRepository

import spock.lang.Specification

class UnitServiceImplSpec extends Specification {

    private static final long UNIT_ID = 1L
    private static final long UNIT_ID_2 = 2L
    private static final long USER_ID_2 = 1L
    private static final int PAGE_SIZE = 10
    private static final int PAGE_NUM = 0
    private static final Pageable PAGE_ASC = PageRequest.of(PAGE_NUM, PAGE_SIZE, Sort.by("review_rate").ascending())
    private static final UnitEntity UNIT_ENTITY_1 = new UnitEntity(id: UNIT_ID, title: 'title', region: 'region', description: 'desc', price: 22.0, reviews: [] as Set)
    private static final UnitEntity UNIT_ENTITY_2 = new UnitEntity(id: UNIT_ID_2, title: 'title', region: 'region', description: 'desc', price: 22.0, reviews: [] as Set)
    private static final String USERNAME = 'user1'
    private static final UserEntity USER_ENTITY = new UserEntity(id: USER_ID_2, username: USERNAME, password: 'pass', email: 'aa@aa.aa', firstName: 'dim', lastName: 'kas', enable: true)
    private static final ReviewEntity REVIEW_ENTITY = new ReviewEntity(id: 1L, rate: 2, comment: 'comment', user: USER_ENTITY)
    private static final Review NEW_REVIEW = new Review(rate: 3, comment: 'newComment')

    private UnitServiceImpl unitService

    def setup() {

        ModelMapper modelMapper = Mock(ModelMapper)
        ReviewRepository reviewRepository = Mock(ReviewRepository)
        UserRepository userRepository = Mock(UserRepository)
        UnitRepository unitRepository = Mock(UnitRepository)

        unitService = new UnitServiceImpl(unitRepository, reviewRepository, userRepository, modelMapper)
    }

    def 'FindById'() {
        given: 'An optional with a unit entity'
            Optional<UnitEntity> optionalUnitEntity = Optional.of(new UnitEntity(id: UNIT_ID, title: 'title', region: 'region', description: 'desc', price: 22.0, reviews: [] as Set))
        when: 'The find by id is called'
            unitService.findById(UNIT_ID)
        then: 'The user is retrieved by the repository'
            1 * unitService.unitRepository.findById(UNIT_ID) >> optionalUnitEntity
        then: 'The entity is transformed to dto '
            1 * unitService.modelMapper.map(optionalUnitEntity.get(), Unit.class)
        then: 'No more interactions happens'
            0 * _
    }

    def 'Find By Title And Region Without Sorting'() {
        given: 'The empty title region and sortDirection'
            Optional<String> optionalTitle = Optional.empty()
            Optional<String> optionalRegion = Optional.empty()
            Optional<String> optionalSortDirection = Optional.empty()
        when: 'The findByTitleAndRegion is called'
            unitService.findByTitleAndRegion(optionalTitle, optionalRegion, PAGE_SIZE, PAGE_NUM, optionalSortDirection)
        then: 'The repository is called with a request without sorting'
            1 * unitService.unitRepository.findByTitleAndRegion(optionalTitle, optionalRegion, PageRequest.of(PAGE_NUM, PAGE_SIZE)) >> Page.empty()
        then: 'No more interactions happens'
            0 * _
    }

    def 'Find By Title And Region With Sorting'() {
        given: 'Empty title and region but sorting with value asc'
            Optional<String> optionalTitle = Optional.empty()
            Optional<String> optionalRegion = Optional.empty()
            Optional<String> optionalSortDirection = Optional.of("asc")
        when: 'The find by title and region is called'
            unitService.findByTitleAndRegion(optionalTitle, optionalRegion, PAGE_SIZE, PAGE_NUM, optionalSortDirection)
        then: 'The repository us called with a page request that has sorting asc'
            1 * unitService.unitRepository.findByTitleAndRegion(optionalTitle, optionalRegion, PAGE_ASC) >> getEntities()
        then: 'The list of entities is mapped to dtos'
            1 * unitService.modelMapper.map(UNIT_ENTITY_1, Unit.class)
            1 * unitService.modelMapper.map(UNIT_ENTITY_2, Unit.class)
        then: 'No more interactions happens'
            0 * _
    }

    def 'update existing Review Of User'() {
        given: 'A review Entity that we expect to be saved and a newReviewEntity'
            ReviewEntity newReviewEntity = new ReviewEntity(rate: 3, comment: 'newComment')
            ReviewEntity reviewEntityToSave = new ReviewEntity(id: 1L, rate: 3, comment: 'newComment', user: USER_ENTITY, unit: UNIT_ENTITY_1)
        when: 'We update a review with a user'
            Review result = unitService.updateReviewOfUser(UNIT_ID, USERNAME, NEW_REVIEW)
        then: 'The review entity is retrieved from the database'
            1 * unitService.reviewRepository.findByUserUsernameAndUnitId(USERNAME, UNIT_ID) >> Optional.of(REVIEW_ENTITY)
        then: 'The new review is converted to entity'
            1 * unitService.modelMapper.map(NEW_REVIEW, ReviewEntity.class) >> newReviewEntity
        then: 'The Unit of the review is retrieved by the database'
            1 * unitService.unitRepository.findById(UNIT_ID) >> Optional.of(UNIT_ENTITY_1)
        then: 'The User who made the review is retrieved by the db'
            1 * unitService.userRepository.findByUsername(USERNAME) >> Optional.of(USER_ENTITY)
        then: 'The new review is saved in the database'
            1 * unitService.reviewRepository.save(reviewEntityToSave) >> newReviewEntity
        then: 'The new saved entity is converted to review dto'
            1 * unitService.modelMapper.map(newReviewEntity, Review.class) >> NEW_REVIEW
        then: 'No more interactions happens'
            0 * _
        and: 'The returned object is the expected'
            result == NEW_REVIEW
    }

    def 'Add new Review Of User'() {
        given: 'A review Entity that we expect to be saved and a new review entity'
            ReviewEntity newReviewEntity = new ReviewEntity(rate: 3, comment: 'newComment')
            ReviewEntity reviewEntityToSaveWithoutId = new ReviewEntity(rate: 3, comment: 'newComment', user: USER_ENTITY, unit: UNIT_ENTITY_1)
        when: 'We update a review with a user'
            Review result = unitService.updateReviewOfUser(UNIT_ID, USERNAME, NEW_REVIEW)
        then: 'There is not review for this unit from the user'
            1 * unitService.reviewRepository.findByUserUsernameAndUnitId(USERNAME, UNIT_ID) >> Optional.empty()
        then: 'The new review is converted to entity'
            1 * unitService.modelMapper.map(NEW_REVIEW, ReviewEntity.class) >> newReviewEntity
        then: 'The Unit of the review is retrieved by the database'
            1 * unitService.unitRepository.findById(UNIT_ID) >> Optional.of(UNIT_ENTITY_1)
        then: 'The User who made the review is retrieved by the db'
            1 * unitService.userRepository.findByUsername(USERNAME) >> Optional.of(USER_ENTITY)
        then: 'The new review is saved in the database'
            1 * unitService.reviewRepository.save(reviewEntityToSaveWithoutId) >> newReviewEntity
        then: 'The new saved entity is converted to review dto'
            1 * unitService.modelMapper.map(newReviewEntity, Review.class) >> NEW_REVIEW
        then: 'No more interactions happens'
            0 * _
        and: 'The returned object is the expected'
            result == NEW_REVIEW
    }

    def 'Add new Review Of User that does not exists'() {
        given: 'A new review Entity'
            ReviewEntity newReviewEntity = new ReviewEntity(rate: 3, comment: 'newComment')
        when: 'We update a review with a user'
            Review result = unitService.updateReviewOfUser(UNIT_ID, USERNAME, NEW_REVIEW)
        then: 'There is not review for this unit from the user'
            1 * unitService.reviewRepository.findByUserUsernameAndUnitId(USERNAME, UNIT_ID) >> Optional.empty()
        then: 'The new review is converted to entity'
            1 * unitService.modelMapper.map(NEW_REVIEW, ReviewEntity.class) >> newReviewEntity
        then: 'The Unit of the review is retrieved by the database'
            1 * unitService.unitRepository.findById(UNIT_ID) >> Optional.of(UNIT_ENTITY_1)
        then: 'The User who made the review does not exist'
            1 * unitService.userRepository.findByUsername(USERNAME) >> Optional.empty()
        then: 'No more interactions happens'
            0 * _
        and: 'An exception is thrown'
            Throwable e = thrown()
            RuntimeException == e.class
    }

    def 'Add new Review Of User for a Unit that does not exist'() {
        given: 'A new review Entity'
            ReviewEntity newReviewEntity = new ReviewEntity(rate: 3, comment: 'newComment')
        when: 'We update a review with a user'
            Review result = unitService.updateReviewOfUser(UNIT_ID, USERNAME, NEW_REVIEW)
        then: 'There is not review for this unit from the user'
            1 * unitService.reviewRepository.findByUserUsernameAndUnitId(USERNAME, UNIT_ID) >> Optional.empty()
        then: 'The new review is converted to entity'
            1 * unitService.modelMapper.map(NEW_REVIEW, ReviewEntity.class) >> newReviewEntity
        then: 'The Unit of the review is retrieved by the database'
            1 * unitService.unitRepository.findById(UNIT_ID) >> Optional.empty()
        then: 'No more interactions happens'
            0 * _
        and: 'An exception is thrown'
            Throwable e = thrown()
            RuntimeException == e.class
    }

    private Page<UnitEntity> getEntities() {
        return new PageImpl<>(List.of(UNIT_ENTITY_1, UNIT_ENTITY_2), PAGE_ASC, 2)
    }
}
