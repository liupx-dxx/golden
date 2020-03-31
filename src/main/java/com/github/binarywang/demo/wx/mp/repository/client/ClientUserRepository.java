package com.github.binarywang.demo.wx.mp.repository.client;

import com.github.binarywang.demo.wx.mp.entity.advance.Advance;
import com.github.binarywang.demo.wx.mp.entity.advance.QAdvance;
import com.github.binarywang.demo.wx.mp.entity.surce.LsClientUser;
import com.github.binarywang.demo.wx.mp.entity.surce.QLsClientUser;
import com.github.binarywang.demo.wx.mp.repository.common.BaseJpaRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

@Repository
public class ClientUserRepository extends BaseJpaRepository<LsClientUser,Long> {

    /**
     *
     * 根据手机号查找用户
     * */

    public LsClientUser findByPhone(String phone) {
        QLsClientUser qLsClientUser = QLsClientUser.lsClientUser;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLsClientUser.phone.eq(phone));
        List<LsClientUser> all = findAll(builder);
        return CollectionUtils.isEmpty(all) ? null : all.get(0);
    }

}
