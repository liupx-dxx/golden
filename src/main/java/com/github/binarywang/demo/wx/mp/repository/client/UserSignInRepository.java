package com.github.binarywang.demo.wx.mp.repository.client;

import com.github.binarywang.demo.wx.mp.entity.surce.LsUserSignIn;
import com.github.binarywang.demo.wx.mp.entity.surce.QLsUserSignIn;
import com.github.binarywang.demo.wx.mp.repository.common.BaseJpaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Repository
public class UserSignInRepository extends BaseJpaRepository<LsUserSignIn,Long> {

    /**
     * 分页查询
     * @param
     * @return
     */
    public Page<LsUserSignIn> findPage(Map<String, String> params, Pageable pageable) {

        QLsUserSignIn qLsUserSignIn = QLsUserSignIn.lsUserSignIn;
        BooleanBuilder builder = new BooleanBuilder();
        JPQLQuery<LsUserSignIn> query = from(qLsUserSignIn);

        String className;
        String phone;
        String userName;
        String flag;
        if (params.containsKey("phone")) {
            phone = params.get("phone");
            if (StringUtils.hasText(phone)) {
                builder.and(qLsUserSignIn.phone.eq(phone));
            }
        }
        if (params.containsKey("flag")) {
            flag = params.get("flag");
            if (StringUtils.hasText(flag)) {
                builder.and(qLsUserSignIn.flag.eq(flag));
            }
        }


        if (params.containsKey("userName")) {
            userName = params.get("userName");
            if (StringUtils.hasText(userName)) {
                builder.and(qLsUserSignIn.userName.contains(userName));
            }
        }

        /*if (params.containsKey("classType")) {
            classType = params.get("classType");
            if (StringUtils.hasText(classType)) {
                builder.and(qLsUserSignIn.classType.eq(classType));
            }
        }*/

        if (params.containsKey("className")) {
            className = params.get("className");
            if (StringUtils.hasText(className)) {
                builder.and(qLsUserSignIn.className.like("%" + className + "%"));
            }
        }
        query.where(builder).orderBy(qLsUserSignIn.createTime.desc());

        return findAll(query, pageable);
    }


    /**
     * 根据班级ID查询签到人
     * @param
     * @return
     */
    public List<LsUserSignIn> findByClassId(String classId) {
        QLsUserSignIn qLsUserSignIn = QLsUserSignIn.lsUserSignIn;
        BooleanBuilder builder = new BooleanBuilder();
        //builder.and(qLsUserSignIn.classId.eq(classId));

        return findAll(builder);
    }

    public List<LsUserSignIn> findByUserPhone(String phone) {

        QLsUserSignIn qLsUserSignIn = QLsUserSignIn.lsUserSignIn;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLsUserSignIn.phone.eq(phone));

        return findAll(builder);
    }

    /**
     * 根据USERID、当前时间获取该用户的该课程今天是否
     *
     * */
    public LsUserSignIn findByUserIdAndUserClassId(long userClassId) {
        LocalDateTime after = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime before = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        QLsUserSignIn qLsUserSignIn = QLsUserSignIn.lsUserSignIn;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLsUserSignIn.userClassId.eq(userClassId));
        builder.and(qLsUserSignIn.createTime.after(after));
        builder.and(qLsUserSignIn.createTime.before(before));
        List<LsUserSignIn> signInList = findAll(builder);

        return signInList.size()>0?signInList.get(0):null;
    }

    public List<LsUserSignIn> findByUserId(Long userId) {
        LocalDateTime after = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime before = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        QLsUserSignIn qLsUserSignIn = QLsUserSignIn.lsUserSignIn;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLsUserSignIn.userId.eq(Long.valueOf(userId)));
        builder.and(qLsUserSignIn.createTime.after(after));
        builder.and(qLsUserSignIn.createTime.before(before));
        List<LsUserSignIn> signInList = findAll(builder);

        return signInList;
    }
}
