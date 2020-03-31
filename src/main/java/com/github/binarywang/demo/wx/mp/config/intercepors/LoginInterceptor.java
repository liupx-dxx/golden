package com.github.binarywang.demo.wx.mp.config.intercepors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.binarywang.demo.wx.mp.entity.surce.LsClientUser;
import com.github.binarywang.demo.wx.mp.entity.sys.SysUser;
import com.github.binarywang.demo.wx.mp.service.client.ClientUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);

    /**
     * 管理url
     */
    public final static String MANAGER_PATH = "manager";

    /**
     * 客户端url
     */
    public final static String CLIENT_PATH = "client";

    /**
     * 登录session key
     */
    public final static String MANAGER_SESSION_KEY = "user";

    /**
     * 客户端SESSION
     */
    public final static String CLIENT_SESSION_KEY = "client_user";

    @Autowired
    ClientUserService clientUserService;

    //这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        StringBuffer requestURL = request.getRequestURL();
        LOGGER.info("请求URL地址为 {}",requestURL);
        String servletPath = request.getServletPath();
        LOGGER.info("请求路径为 {}",servletPath);
        String pathUrl = servletPath.split("/")[1];
        LOGGER.info("路径截取之后 {}",pathUrl);
        //每一个项目对于登陆的实现逻辑都有所区别，我这里使用最简单的Session提取User来验证登陆。
        HttpSession session = request.getSession();
        //判断是后台管理还是客户端
        if(MANAGER_PATH.equals(pathUrl)){
            //这里的User是登陆时放入session的
            SysUser user = (SysUser) session.getAttribute(MANAGER_SESSION_KEY);
            //如果session中没有user，表示没登陆
            if (user == null){
                session.setAttribute("manager-url",servletPath);
                response.sendRedirect("/manager/to-login");
                return false;
            }else {
                return true;    //如果session里有user，表示该用户已经登陆，放行，用户即可继续调用自己需要的接口
            }
        }else if(CLIENT_PATH.equals(pathUrl)){
            /*boolean flag = clientToken(request, object);
            if(flag){
                return flag;
            }*/
            /*response.sendRedirect("/client/to-login");
            return true;*/
            //这里的User是登陆时放入session的
            LsClientUser clientUser = (LsClientUser) session.getAttribute(CLIENT_SESSION_KEY);
            //如果session中没有user，表示没登陆
            if (clientUser == null){
                session.setAttribute("client-url",servletPath);
                response.sendRedirect("/client/to-login");
                return false;
            }else {
                return true;    //如果session里有user，表示该用户已经登陆，放行，用户即可继续调用自己需要的接口
            }
        }
        return false;

    }

    /*返回客户端数据*/
    private void returnJson(HttpServletResponse response, String json) throws Exception{
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }


    private boolean clientToken(HttpServletRequest request, Object object){
        String token = request.getHeader("token");// 从 http 请求头中取出 token

        if (token == null) {
            throw new RuntimeException("无token，请重新登录");
        }
        // 获取 token 中的 user id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("401");
        }
        LsClientUser user = clientUserService.findByPhone(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在，请重新登录");
        }
        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new RuntimeException("401");
        }
        return true;
    }
}


