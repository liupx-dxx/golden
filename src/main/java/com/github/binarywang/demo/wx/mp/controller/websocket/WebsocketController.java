package com.github.binarywang.demo.wx.mp.controller.websocket;

import lombok.AllArgsConstructor;
import org.apache.coyote.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;


@Controller
public class WebsocketController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(value = "/websocket/{name}")
    public String webSocket(@PathVariable String name, Model model, HttpSession session){
        try{
            String onlineNumber = "0";
            logger.info("跳转到websocket的页面上");
            if(session.getAttribute("onlineNumber") !=null){
                onlineNumber = session.getAttribute("onlineNumber").toString();
            }
            model.addAttribute("username",name);
            model.addAttribute("onlineNumber",onlineNumber);
            return "websocket/websocket";
        }
        catch (Exception e){
            logger.info("跳转到websocket的页面上发生异常，异常信息是："+e.getMessage());
            return "error";
        }
    }


}
