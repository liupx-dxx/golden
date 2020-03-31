package com.github.binarywang.demo.wx.mp.utils.message;
/**
 *
 * @Description: 文本消息
 * @Parameters:
 * @Return:
 * @Create Date:
 * @Version: V1.00
 * @author: 来日可期
 */
public class TextMessage extends BaseMessage{
    //回复的消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

}


