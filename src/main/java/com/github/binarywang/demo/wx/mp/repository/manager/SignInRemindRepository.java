package com.github.binarywang.demo.wx.mp.repository.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.*;
import com.github.binarywang.demo.wx.mp.repository.common.BaseJpaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
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
public class SignInRemindRepository extends BaseJpaRepository<LsSignInRemind,Long> {

    /**
     * 分页查询
     * @param
     * @return
     */
    public Page<LsSignInRemind> findPage(Map<String, String> params, Pageable pageable) {

        QLsSignInRemind qLsSignInRemind = QLsSignInRemind.lsSignInRemind;
        BooleanBuilder builder = new BooleanBuilder();
        JPQLQuery<LsSignInRemind> query = from(qLsSignInRemind);

        String phone;

        if (params.containsKey("phone")) {
            phone = params.get("phone");
            if (StringUtils.hasText(phone)) {
                builder.and(qLsSignInRemind.userPhone.eq(phone));
            }
        }

        query.where(builder).orderBy(qLsSignInRemind.createTime.desc());

        return findAll(query, pageable);
    }

    public List<LsSignInRemind> findByUserPhone(String phone) {

        QLsSignInRemind qLsSignInRemind = QLsSignInRemind.lsSignInRemind;
        BooleanBuilder builder = new BooleanBuilder();
        JPQLQuery<LsSignInRemind> query = from(qLsSignInRemind);
        builder.and(qLsSignInRemind.userPhone.eq(phone));
        query.where(builder).orderBy(qLsSignInRemind.createTime.desc());
        return findAll(query);
    }

}
