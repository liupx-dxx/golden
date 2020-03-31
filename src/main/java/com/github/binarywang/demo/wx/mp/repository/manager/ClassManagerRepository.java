package com.github.binarywang.demo.wx.mp.repository.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.LsClass;
import com.github.binarywang.demo.wx.mp.entity.surce.QLsClass;
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
public class ClassManagerRepository extends BaseJpaRepository<LsClass,Long>{
    /**
     * 分页查询
     * @param
     * @return
     */
    public Page<LsClass> findPage(Map<String, String> params, Pageable pageable) {

        QLsClass qLsClass = QLsClass.lsClass;
        BooleanBuilder builder = new BooleanBuilder();
        JPQLQuery<LsClass> query = from(qLsClass);

        String className;
        String teacherName;
        if (params.containsKey("className")) {
            className = params.get("className");
            if (StringUtils.hasText(className)) {
                builder.and(qLsClass.className.contains(className));
            }
        }

        if (params.containsKey("teacherName")) {
            teacherName = params.get("teacherName");
            if (StringUtils.hasText(teacherName)) {
                builder.and(qLsClass.teacherName.contains(teacherName));
            }
        }
        query.where(builder).orderBy(qLsClass.createTime.desc());


        return findAll(query, pageable);
    }
    /**
     * 批量查询
     * @param
     * @return
     */
    public List<LsClass> findByIds(List<Long> ids) {
        QLsClass qLsClass = QLsClass.lsClass;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qLsClass.id.in(ids));


        return findAll(builder);
    }
}
