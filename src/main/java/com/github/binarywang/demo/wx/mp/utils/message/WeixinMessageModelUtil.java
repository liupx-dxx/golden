package com.github.binarywang.demo.wx.mp.utils.message;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>Company: B505信息技术研究所 </p>
 * @Description: 封装微信回复消息，各种回复消息对应不同的方法
 * @Create Date: 2017年10月23日上午11:33:36
 * @Version: V1.00
 * @Author:来日可期
 */
@Component
public class WeixinMessageModelUtil {

    @Autowired
    private WeixinMessageUtil weixinMessageUtil;

    /**
     * @Description: 当系统出错时，默认回复的文本消息
     * @Parameters: WeixinMessageModelUtil
     * @Return: 系统出错回复的消息
     * @Create Date: 2017年10月23日上午11:55:17
     * @Version: V1.00
     * @author:来日可期
     */
    public String systemErrorResponseMessageModel(WeixinMessageInfo weixinMessageInfo ){

        // 回复文本消息
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(weixinMessageInfo.getFromUserName());
        textMessage.setFromUserName(weixinMessageInfo.getToUserName());
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setMsgType(weixinMessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setFuncFlag(0);
        textMessage.setContent("系统出错啦，请稍后再试");
        return weixinMessageUtil.textMessageToXml(textMessage);
    }
}
