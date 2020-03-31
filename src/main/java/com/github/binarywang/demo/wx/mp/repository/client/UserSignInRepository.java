package com.github.binarywang.demo.wx.mp.repository.client;

import com.github.binarywang.demo.wx.mp.entity.surce.LsUserSignIn;
import com.github.binarywang.demo.wx.mp.entity.surce.QLsUserSignIn;
import com.github.binarywang.demo.wx.mp.repository.common.BaseJpaRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserSignInRepository extends BaseJpaRepository<LsUserSignIn,Long> {


    /**
     * 根据班级ID查询签到人
     * @param
     * @return
     */
    public List<LsUserSignIn> findByClassId(String classId) {
        QLsUserSignIn qLsUserSignIn = QLsUserSignIn.lsUserSignIn;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLsUserSignIn.classId.eq(classId));

        return findAll(builder);
    }

    /*public List<LsUserClass> findByUserPhone(String phone) {

        QLsUserClass qLsUserClass = QLsUserClass.lsUserClass;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLsUserClass.clientUserPhone.eq(phone));

        return findAll(builder);
    }*/
}
