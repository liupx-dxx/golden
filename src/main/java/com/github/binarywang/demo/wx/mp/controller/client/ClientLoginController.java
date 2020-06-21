package com.github.binarywang.demo.wx.mp.controller.client;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.demo.wx.mp.config.TokenService;
import com.github.binarywang.demo.wx.mp.config.intercepors.LoginInterceptor;
import com.github.binarywang.demo.wx.mp.entity.surce.LsClientUser;
import com.github.binarywang.demo.wx.mp.enums.ResultCodeEnum;
import com.github.binarywang.demo.wx.mp.service.client.ClientUserService;
import com.github.binarywang.demo.wx.mp.utils.ResultEntity;
import com.github.binarywang.demo.wx.mp.utils.ResultUtils;
import com.github.binarywang.demo.wx.mp.vo.ClientUserReq;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Controller
@AllArgsConstructor
@RequestMapping("/client")
public class ClientLoginController {

    ClientUserService userService;

    TokenService tokenService;

    @RequestMapping("/to-login")
    public String login() {
        return "client-user/login";
    }

    /**
     *
     * 用户登出
     *
     * */
    @RequestMapping("/login-out")
    public String loginOut(HttpSession session) {

        return "client-user/login";
    }

    /**
     *
     * 用户登陆
     *
     * */
    @PostMapping(value="/login")
    @ResponseBody
    public ResultEntity findAll(
        @NotNull(message = "对象不能为空")
        @RequestBody LsClientUser clientUser, HttpSession session, HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();
        LsClientUser user = userService.findByPhone(clientUser.getPhone());
        if(user==null){
            return ResultUtils.fail(ResultCodeEnum.PARAMETER_ERROR,"用户名不存在");
        }
        //证明用用户名存在 判断密码是否正确
        byte[] decode = Base64.getDecoder().decode(user.getPassword());
        String decodePass = new String(decode);
        //判断密码是否正确
        if(!decodePass.equals(clientUser.getPassword())){
            //证明密码不正确，返回提示
            return ResultUtils.fail(ResultCodeEnum.PARAMETER_ERROR,"密码错误");
        }
        /*String token = tokenService.getToken(user);
        jsonObject.put("token", token);
        //证明密码正确  将用户信息放入到session
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        response.addCookie(cookie);*/
        session.setAttribute("client_user",user);

        return ResultUtils.success();
    }

    @PostMapping(value="/user/save")
    public ResultEntity save(@RequestBody LsClientUser clientUser) {
        LsClientUser user = userService.findByPhone(clientUser.getPhone());
        if(user!=null){
            return ResultUtils.fail(ResultCodeEnum.PARAMETER_ERROR,"手机号已存在");
        }
        userService.save(clientUser);
        return ResultUtils.success();
    }


    @PostMapping(value="/user/updatePass")
    @ResponseBody
    public ResultEntity updatePass(
        @NotNull(message = "参数不能为空")
        @RequestBody ClientUserReq clientUserReq, HttpSession session) throws UnsupportedEncodingException {
        LsClientUser clientUser = (LsClientUser) session.getAttribute(LoginInterceptor.CLIENT_SESSION_KEY);
        if(clientUser==null){
            return ResultUtils.fail(ResultCodeEnum.INTERNAL_ERROR);
        }
        String newPassWd = clientUserReq.getNewPassWd();
        String oldPassWd = clientUserReq.getOldPassWd();
        if(StringUtils.isEmpty(newPassWd) || StringUtils.isEmpty(oldPassWd)){
            return ResultUtils.fail(ResultCodeEnum.PARAMETER_ERROR,"密码不能为空");
        }
        //判断原始密码是否正确
        String password = clientUser.getPassword();
        Base64.Decoder decoder = Base64.getDecoder();
        String decoderPassWd = new String(decoder.decode(password), "UTF-8");
        if(!oldPassWd.equals(decoderPassWd)){
            return ResultUtils.fail(ResultCodeEnum.PARAMETER_ERROR,"旧密码不正确");
        }
        LsClientUser user = userService.updatePass(clientUser, newPassWd);
        session.setAttribute("client_user",user);
        return ResultUtils.success();
    }

    /**
     *
     * 修改密码页面
     *
     * */
    @RequestMapping("/to-updatePW")
    public String loginOut() {

        return "client-user/uppasswd";
    }

}
