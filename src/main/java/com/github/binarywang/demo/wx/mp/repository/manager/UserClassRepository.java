package com.github.binarywang.demo.wx.mp.repository.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.LsUserClass;
import com.github.binarywang.demo.wx.mp.entity.surce.QLsUserClass;
import com.github.binarywang.demo.wx.mp.repository.common.BaseJpaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

@Repository
public class UserClassRepository extends BaseJpaRepository<LsUserClass,Long> {

    @Bean
    @Autowired
    public JPAQueryFactory jpaQuery(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

    @Autowired
    JPAQueryFactory queryFactory;


    /**
     * 分页查询
     * @param
     * @return
     */
    public Page<LsUserClass> findPage(Map<String, String> params, Pageable pageable) {

        QLsUserClass qLsUserClass = QLsUserClass.lsUserClass;
        BooleanBuilder builder = new BooleanBuilder();
        JPQLQuery<LsUserClass> query = from(qLsUserClass);

        String className;
        String phone;
        String userName;
        String classType;
        if (params.containsKey("phone")) {
            phone = params.get("phone");
            if (StringUtils.hasText(phone)) {
                builder.and(qLsUserClass.clientUserPhone.eq(phone));
            }
        }

        if (params.containsKey("userName")) {
            userName = params.get("userName");
            if (StringUtils.hasText(userName)) {
                builder.and(qLsUserClass.clientUserName.contains(userName));
            }
        }

        if (params.containsKey("classType")) {
            classType = params.get("classType");
            if (StringUtils.hasText(classType)) {
                builder.and(qLsUserClass.classType.eq(classType));
            }
        }

        if (params.containsKey("className")) {
            className = params.get("className");
            if (StringUtils.hasText(className)) {
                builder.and(qLsUserClass.className.like("%" + className + "%"));
            }
        }
        query.where(builder).orderBy(qLsUserClass.updateTime.desc());

        return findAll(query, pageable);
    }

    public List<LsUserClass> findByUserPhone(String phone) {

        QLsUserClass qLsUserClass = QLsUserClass.lsUserClass;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLsUserClass.clientUserPhone.eq(phone));

        return findAll(builder);
    }

    /**
     * 批量查询
     * @param
     * @return
     */
    public List<LsUserClass> findByIds(List<Long> ids) {
        QLsUserClass qLsUserClass = QLsUserClass.lsUserClass;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qLsUserClass.id.in(ids));


        return findAll(builder);
    }

    /**
     * 根据批量查询
     * @param
     * @return
     */
    public List<LsUserClass> findByClassId(Long id) {
        QLsUserClass qLsUserClass = QLsUserClass.lsUserClass;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qLsUserClass.classId.eq(id+""));
        builder.and(qLsUserClass.classHourNum.gt(0));
        List<LsUserClass> lsUserClasses = queryFactory.selectDistinct(qLsUserClass.clientUserPhone).select(qLsUserClass).where(builder).fetch();

        return lsUserClasses;
    }
}
