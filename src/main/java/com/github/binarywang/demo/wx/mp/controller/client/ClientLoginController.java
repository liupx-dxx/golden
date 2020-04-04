package com.github.binarywang.demo.wx.mp.controller.client;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.demo.wx.mp.config.TokenService;
import com.github.binarywang.demo.wx.mp.entity.surce.LsClientUser;
import com.github.binarywang.demo.wx.mp.enums.ResultCodeEnum;
import com.github.binarywang.demo.wx.mp.service.client.ClientUserService;
import com.github.binarywang.demo.wx.mp.utils.ResultEntity;
import com.github.binarywang.demo.wx.mp.utils.ResultUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
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

}
