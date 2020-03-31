package com.github.binarywang.demo.wx.mp.utils.message;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
public class WinXinRespUtil {

    public String resp(String content) throws Exception{
        String APIKEY = "d0c3879cc61947e38ba75df870decf75";
        //String question = "知道我是谁吗？";//这是上传给云机器人的问题
        //String INFO = URLEncoder.encode("北京今日天气", "utf-8");
        String INFO = URLEncoder.encode(content, "utf-8");
        String getURL = "http://api.avatardata.cn/Tuling/Ask?key=" + APIKEY + "&info=" + INFO;
        URL getUrl = new URL(getURL);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.connect();

        // 取得输入流，并使用Reader读取
        BufferedReader reader = new BufferedReader(new InputStreamReader( connection.getInputStream(), "utf-8"));
        StringBuffer sb = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        // 断开连接
        connection.disconnect();
        //解析json
        JSONObject jsonObject = JSONObject.parseObject(sb.toString());
        int error_code = jsonObject.getIntValue("error_code");
        if(error_code!=0){
            return "系统故障请稍后再试";
        }
        //解析result对象
        JSONObject result = jsonObject.getJSONObject("result");
        String text = result.getString("text");
        return text;
    }
}
