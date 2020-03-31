package com.github.binarywang.demo.wx.mp.repository.common;

import com.github.binarywang.demo.wx.mp.enums.DelFlag;
import com.github.binarywang.demo.wx.mp.enums.DelFlagEnum;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.*;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.function.LongSupplier;

@Transactional(
    readOnly = true
)
public class BaseJpaRepository<T, ID extends Serializable> implements JpaRepository<T, ID>, QuerydslPredicateExecutor<T> {
    private EntityManager em;
    private SimpleJpaRepository<T, ID> repository;
    private QuerydslJpaPredicateExecutor<T> querydslJpaPredicateExecutor;
    protected Querydsl querydsl;
    private PathBuilder<T> pathBuilder;
    private JPAQueryFactory jpaQueryFactory;

    public BaseJpaRepository() {
    }

    @Autowired
    private void setEm(EntityManager entityManager) {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        this.em = entityManager;
        Type type = this.getClass().getGenericSuperclass();
        Type[] parameterizedType = ((ParameterizedType)type).getActualTypeArguments();
        Class<T> domainClass = (Class)parameterizedType[0];
        JpaEntityInformation<T, ID> entityInformation = new JpaMetamodelEntityInformation(domainClass, this.em.getMetamodel());
        this.repository = new SimpleJpaRepository(entityInformation, this.em);
        EntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
        this.pathBuilder = new PathBuilder(domainClass, domainClass.getSimpleName());
        this.querydsl = new Querydsl(this.em, this.pathBuilder);
        this.jpaQueryFactory = new JPAQueryFactory(this.em);
        this.querydslJpaPredicateExecutor = new QuerydslJpaPredicateExecutor(entityInformation, this.em, resolver, (CrudMethodMetadata)null);
    }

    public List<T> findAll() {
        return this.repository.findAll();
    }

    public List<T> findAll(Sort sort) {
        return this.repository.findAll(sort);
    }

    public List<T> findAllById(Iterable<ID> ids) {
        return this.repository.findAllById(ids);
    }

    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        return this.repository.saveAll(entities);
    }

    public Optional<T> findById(ID id) {
        return this.repository.findById(id);
    }

    public boolean existsById(ID id) {
        return this.repository.existsById(id);
    }

    public Page<T> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    /** @deprecated */
    @Deprecated
    public List<T> findAll(Iterable<ID> ids) {
        return this.repository.findAllById(ids);
    }

    public long count() {
        return this.repository.count();
    }

    public void deleteById(ID id) {
        this.repository.deleteById(id);
    }

    /** @deprecated */
    @Deprecated
    @Transactional
    public void delete(ID id) {
        this.repository.deleteById(id);
    }

    @Transactional
    public void delete(T entity) {
        this.repository.delete(entity);
    }

    public void deleteAll(Iterable<? extends T> iterable) {
        this.repository.deleteAll(iterable);
    }

    @Transactional
    public <S extends T> S deleteLogic(S entity, DelFlagEnum delFlagEnum) {
        ((DelFlag)entity).setDelFlag(delFlagEnum.getCode());
        return this.repository.save(entity);
    }

    /** @deprecated */
    @Deprecated
    @Transactional
    public void delete(Iterable<? extends T> entities) {
        this.repository.deleteAll(entities);
    }

    @Transactional
    public void deleteAll() {
        this.repository.deleteAll();
    }

    @Transactional
    public <S extends T> S save(S entity) {
        return this.repository.save(entity);
    }

    /** @deprecated */
    @Deprecated
    @Transactional
    public <S extends T> List<S> save(Iterable<S> entities) {
        return this.repository.saveAll(entities);
    }

    /** @deprecated */
    @Deprecated
    public T findOne(ID id) {
        Optional<T> optional = this.repository.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    /** @deprecated */
    @Deprecated
    public boolean exists(ID id) {
        return this.repository.existsById(id);
    }

    @Transactional
    public void flush() {
        this.repository.flush();
    }

    @Transactional
    public <S extends T> S saveAndFlush(S entity) {
        return this.repository.saveAndFlush(entity);
    }

    @Transactional
    public void deleteInBatch(Iterable<T> entities) {
        this.repository.deleteInBatch(entities);
    }

    @Transactional
    public void deleteAllInBatch() {
        this.repository.deleteAllInBatch();
    }

    public T getOne(ID id) {
        return this.repository.getOne(id);
    }

    public <S extends T> Optional<S> findOne(Example<S> example) {
        return this.repository.findOne(example);
    }

    public <S extends T> List<S> findAll(Example<S> example) {
        return this.repository.findAll(example);
    }

    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return this.repository.findAll(example, sort);
    }

    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        return this.repository.findAll(example, pageable);
    }

    public <S extends T> long count(Example<S> example) {
        return this.repository.count(example);
    }

    public <S extends T> boolean exists(Example<S> example) {
        return this.repository.exists(example);
    }

    public Optional<T> findOne(Predicate predicate) {
        return this.querydslJpaPredicateExecutor.findOne(predicate);
    }

    public List<T> findAll(Predicate predicate) {
        return this.querydslJpaPredicateExecutor.findAll(predicate);
    }

    public List<T> findAll(Predicate predicate, Sort sort) {
        return this.querydslJpaPredicateExecutor.findAll(predicate, sort);
    }

    public List<T> findAll(Predicate predicate, OrderSpecifier... orderSpecifiers) {
        return this.querydslJpaPredicateExecutor.findAll(predicate, orderSpecifiers);
    }

    public List<T> findAll(OrderSpecifier... orderSpecifiers) {
        return this.querydslJpaPredicateExecutor.findAll(orderSpecifiers);
    }

    public Page<T> findAll(Predicate predicate, Pageable pageable) {
        return this.querydslJpaPredicateExecutor.findAll(predicate, pageable);
    }

    public long count(Predicate predicate) {
        return this.querydslJpaPredicateExecutor.count(predicate);
    }

    public boolean exists(Predicate predicate) {
        return this.querydslJpaPredicateExecutor.exists(predicate);
    }

    public <X> List<X> findAll(Class<X> xEntityClass, Expression<?>[] selectExpressions, EntityPath<?>[] entityPaths, Predicate[] wherePredicates) {
        JPQLQuery<X> query = this.getXjpqlQuery(xEntityClass, selectExpressions, entityPaths, wherePredicates);
        return query.fetch();
    }

    public <X> List<X> findAll(Class<X> xEntityClass, Expression<?>[] selectExpressions, EntityPath<?>[] entityPaths, Predicate[] wherePredicates, Sort sort) {
        JPQLQuery<X> query = this.getXjpqlQuery(xEntityClass, selectExpressions, entityPaths, wherePredicates);
        if (sort != null) {
            this.querydsl.applySorting(sort, query);
        }

        return query.fetch();
    }

    public <X> Page<X> findAll(@Nonnull Class<X> xEntityClass, Expression<?>[] selectExpressions, EntityPath<?>[] entityPaths, Predicate[] wherePredicates, @Nonnull Pageable pageable) {
        Assert.notNull(pageable, "分页对象信息不能为空！");
        final JPQLQuery<X> query = this.getXjpqlQuery(xEntityClass, selectExpressions, entityPaths, wherePredicates);
        this.querydsl.applyPagination(pageable, query);
        return PageableExecutionUtils.getPage(query.fetch(), pageable, new LongSupplier() {
            public long getAsLong() {
                return query.fetchResults().getTotal();
            }
        });
    }

    public <X> List<X> findAll(JPQLQuery<X> query) {
        return query.fetch();
    }

    public <X> Page<X> findAll(JPQLQuery<X> query, @Nonnull Pageable pageable) {
        this.querydsl.applyPagination(pageable, query);
        return PageableExecutionUtils.getPage(query.fetch(), pageable, new LongSupplier() {
            public long getAsLong() {
                return query.fetchResults().getTotal();
            }
        });
    }

    protected JPAUpdateClause querydslUpdate(EntityPath<?> path) {
        return this.jpaQueryFactory.update(path);
    }

    protected JPADeleteClause querydslDelete(EntityPath<?> path) {
        return this.jpaQueryFactory.delete(path);
    }

    private <X> JPQLQuery<X> getXjpqlQuery(Class<X> xEntityClass, Expression<?>[] selectExpressions, EntityPath<?>[] entityPaths, Predicate[] wherePredicates) {
        JPQLQuery<X> query = this.querydsl.createQuery().select(Projections.bean(xEntityClass, selectExpressions)).from(entityPaths);
        if (wherePredicates != null && wherePredicates.length != 0) {
            query.where(wherePredicates);
        }

        return query;
    }

    protected <X> JPQLQuery<X> from(EntityPath... paths) {
        JPQLQuery query = this.querydsl.createQuery(paths);
        return query;
    }
}
