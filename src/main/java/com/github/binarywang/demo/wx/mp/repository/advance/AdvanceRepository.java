package com.github.binarywang.demo.wx.mp.repository.advance;

import com.github.binarywang.demo.wx.mp.entity.advance.Advance;
import com.github.binarywang.demo.wx.mp.entity.advance.QAdvance;
import com.github.binarywang.demo.wx.mp.repository.common.BaseJpaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Map;

@Repository
public class AdvanceRepository extends BaseJpaRepository<Advance,Long> {

    /**
     * 分页查询
     * @param
     * @return
     */
    public Page<Advance> findPage(Map<String, String> params, Pageable pageable) {

        QAdvance advance = QAdvance.advance;
        BooleanBuilder builder = new BooleanBuilder();
        JPQLQuery<Advance> query = from(advance);

        String userName;
        if (params.containsKey("userName")) {
            userName = params.get("userName");
            if (StringUtils.hasText(userName)) {
                builder.and(advance.userName.like("%" + userName + "%"));
            }
        }
        String phone;
        if (params.containsKey("phone")) {
            phone = params.get("phone");
            if (StringUtils.hasText(phone)) {
                builder.and(advance.phone.like("%" + phone + "%"));
            }
        }
        query.where(builder).orderBy(advance.createTime.desc());

        return findAll(query, pageable);
    }
}
