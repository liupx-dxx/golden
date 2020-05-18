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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class UserClassRepository extends BaseJpaRepository<LsUserClass,Long> {

    @PersistenceContext
    private EntityManager entityManager;


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
                builder.and(qLsUserClass.clientUserPhone.contains(phone));
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
    public List<LsUserClass> findByClassId(String classId) {
        /*QLsUserClass qLsUserClass = QLsUserClass.lsUserClass;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qLsUserClass.classId.eq(classId));
        builder.and(qLsUserClass.classHourNum.gt(0));*/

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT distinct client_user_phone,ID,CLASS_ID,class_name,create_time,update_time,id,");
        sql.append("client_user_id,client_user_name,class_type,class_time,price,class_hour_num");
        sql.append(" from cl_user_class");
        sql.append(" where class_id ="+classId);
        sql.append(" and class_hour_num > 0");
        Query query = entityManager.createNativeQuery(sql.toString(), LsUserClass.class);
        return query.getResultList();
    }

    /**
     *
     * 根据周单位查询
     *
     * */
    public List<LsUserClass> findByWeek(String week) {
        QLsUserClass qLsUserClass = QLsUserClass.lsUserClass;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLsUserClass.classTime.contains(week));
        builder.and(qLsUserClass.classHourNum.gt(0));
        return findAll(builder);
    }

    /**
     *
     * 获取总课时
     *
     * */
    public String getUserSurplusByPhone(String phone) {
        Query query = entityManager.createQuery("select sum(classHourNum) from LsUserClass where clientUserPhone=" + phone);
        Object singleResult = query.getSingleResult();
        return singleResult+"";
    }
}
