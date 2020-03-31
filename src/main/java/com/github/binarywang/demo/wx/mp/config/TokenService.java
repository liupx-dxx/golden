package com.github.binarywang.demo.wx.mp.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.binarywang.demo.wx.mp.entity.surce.LsClientUser;
import org.springframework.stereotype.Service;

import java.util.Date;

/***
 * token 下发
 * @Title: TokenService.java
 * @author qiaoyn
 * @date 2019/06/14
 * @version V1.0
 */
@Service
public class TokenService {

    public String getToken(LsClientUser clientUser) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60* 60 * 1000;//一小时有效时间
        Date end = new Date(currentTime);
        String token = "";

        token = JWT.create().withAudience(clientUser.getId()+"").withIssuedAt(start).withExpiresAt(end)
            .sign(Algorithm.HMAC256(clientUser.getPassword()));
        return token;
    }
}



