package com.github.binarywang.demo.wx.mp.repository.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.LsClass;
import com.github.binarywang.demo.wx.mp.entity.surce.LsClassTime;
import com.github.binarywang.demo.wx.mp.entity.surce.QLsClass;
import com.github.binarywang.demo.wx.mp.entity.surce.QLsClassTime;
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
public class ClassTimeRepository extends BaseJpaRepository<LsClassTime,Long>{

    /**
     * 批量查询
     * @param
     * @return
     */
    public List<LsClassTime> findByclassId(Long classId) {
        QLsClassTime qLsClassTime = QLsClassTime.lsClassTime;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qLsClassTime.classId.eq(classId));

        return findAll(builder);
    }

    /**
     * 根据周单位查询该课程
     *
     * */
    public List<LsClassTime> findByWeek(String week) {
        QLsClassTime qLsClassTime = QLsClassTime.lsClassTime;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qLsClassTime.week.eq(week));

        return findAll(builder);
    }
}
