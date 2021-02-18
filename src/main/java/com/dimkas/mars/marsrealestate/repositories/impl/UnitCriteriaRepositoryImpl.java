package com.dimkas.mars.marsrealestate.repositories.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.dimkas.mars.marsrealestate.entities.ReviewEntity;
import com.dimkas.mars.marsrealestate.entities.UnitEntity;
import com.dimkas.mars.marsrealestate.repositories.UnitCriteriaRepository;

@Repository
public class UnitCriteriaRepositoryImpl implements UnitCriteriaRepository {

    private final EntityManager em;

    public UnitCriteriaRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Page<UnitEntity> findByTitleAndRegion(Optional<String> title, Optional<String> region, Pageable pageable) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UnitEntity> cq = cb.createQuery(UnitEntity.class);

        Root<UnitEntity> unit = cq.from(UnitEntity.class);

        List<Predicate> predicates = createWhereClause(title, region, unit, cq, cb);
        createGroupByClause(unit, cq);
        createOrderByClause(unit, cq, cb, pageable);

        TypedQuery<UnitEntity>  query = em.createQuery(cq);
        query.setFirstResult(pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());
        List<UnitEntity> result = query.getResultList();

        long count = countRecords(cb, predicates);
        return new PageImpl<>(result, pageable, count);
    }

    private  List<Predicate> createWhereClause(Optional<String> title, Optional<String> region, Root<UnitEntity> root ,  CriteriaQuery<UnitEntity> cq,  CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        title.ifPresent((t) -> predicates.add(cb.like(root.get("title"), "%"+t+"%")));
        region.ifPresent((r) -> predicates.add(cb.like(root.get("region"),"%"+r+"%")));

        cq.where(predicates.toArray(new Predicate[0]));
        return predicates;
    }

    private void createGroupByClause(Root<UnitEntity> root ,  CriteriaQuery<UnitEntity> cq){
        List<Expression<?>> groupByExpressions = new LinkedList<>();
        groupByExpressions.add(root.get("id"));
        cq.groupBy(groupByExpressions);
    }

    private void createOrderByClause(Root<UnitEntity> root ,  CriteriaQuery<UnitEntity> cq,  CriteriaBuilder cb, Pageable pageable) {
        Metamodel m = em.getMetamodel();
        EntityType<UnitEntity> unit_ = m.entity(UnitEntity.class);

        SetJoin<UnitEntity, ReviewEntity> join = root.join(unit_.getSet("reviews", ReviewEntity.class),  JoinType.LEFT);

        List<Order> orders = new ArrayList<>();
        Expression<Double> orderExpression = null;
        Sort sort = pageable.getSort();

        for(Sort.Order order : sort) {
            if("review_rate".equals(order.getProperty())) {
                Sort.Direction dir = order.getDirection();

               orderExpression = cb.<Double>avg(join.get("rate"));
                if(dir.isAscending()){
                    orders.add(cb.asc(orderExpression));
                } else {
                    orders.add(cb.desc(orderExpression));
                }
                cq.orderBy(orders);
            }
        }
    }

    private long countRecords(CriteriaBuilder cb, List<Predicate> predicates) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<UnitEntity> booksRootCount = countQuery.from(UnitEntity.class);
        countQuery.select(cb.count(booksRootCount)).where(cb.and(predicates.toArray(new Predicate[0])));
        return em.createQuery(countQuery).getSingleResult();
    }
}
