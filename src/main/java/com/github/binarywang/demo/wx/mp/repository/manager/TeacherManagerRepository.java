package com.github.binarywang.demo.wx.mp.repository.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.LsTeacher;
import com.github.binarywang.demo.wx.mp.entity.surce.QLsTeacher;
import com.github.binarywang.demo.wx.mp.repository.common.BaseJpaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Map;

@Repository
public class TeacherManagerRepository extends BaseJpaRepository<LsTeacher,Long>{
    /**
     * 分页查询
     * @param
     * @return
     */
    public Page<LsTeacher> findPage(Map<String, String> params, Pageable pageable) {

        QLsTeacher qLsTeacher = QLsTeacher.lsTeacher;
        BooleanBuilder builder = new BooleanBuilder();
        JPQLQuery<LsTeacher> query = from(qLsTeacher);
        String teacherName;
        if (params.containsKey("teacherName")) {
            teacherName = params.get("teacherName");
            if (StringUtils.hasText(teacherName)) {
                builder.and(qLsTeacher.teacherName.like("%" + teacherName + "%"));
            }
        }
        query.where(builder).orderBy(qLsTeacher.createTime.desc());

        return findAll(query, pageable);
    }
}
