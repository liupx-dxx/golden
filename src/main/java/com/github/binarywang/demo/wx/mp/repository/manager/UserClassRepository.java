package com.github.binarywang.demo.wx.mp.repository.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.LsUserClass;
import com.github.binarywang.demo.wx.mp.entity.surce.QLsUserClass;
import com.github.binarywang.demo.wx.mp.repository.common.BaseJpaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Repository
public class UserClassRepository extends BaseJpaRepository<LsUserClass,Long> {


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
}
