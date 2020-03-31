package com.github.binarywang.demo.wx.mp.utils;

import com.alibaba.fastjson.JSONObject;

import com.github.binarywang.demo.wx.mp.config.WxMpConfiguration;
import com.github.binarywang.demo.wx.mp.config.WxMpProperties;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Component

public class WeChatUtils {

    @Autowired
    WxMpProperties wxMpProperties;

    @Autowired
    WxMpConfiguration wxMpConfiguration;

    /**
     * 获取access_token
     *
     * */
    public String getAccess_token() {
        String accessToken = null;
        WxMpService wxMpService = wxMpConfiguration.wxMpService();
        try {
            accessToken = wxMpService.getAccessToken();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

}
