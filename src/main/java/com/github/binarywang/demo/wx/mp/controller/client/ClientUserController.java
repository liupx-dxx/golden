package com.github.binarywang.demo.wx.mp.controller.client;

import com.github.binarywang.demo.wx.mp.config.intercepors.LoginInterceptor;
import com.github.binarywang.demo.wx.mp.entity.surce.LsClientUser;
import com.github.binarywang.demo.wx.mp.enums.ResultCodeEnum;
import com.github.binarywang.demo.wx.mp.service.client.ClientUserService;
import com.github.binarywang.demo.wx.mp.service.manager.SignInRemindService;
import com.github.binarywang.demo.wx.mp.service.manager.UserClassService;
import com.github.binarywang.demo.wx.mp.utils.ResultEntity;
import com.github.binarywang.demo.wx.mp.utils.ResultUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/client")
public class ClientUserController {
    ClientUserService userService;

    UserClassService userClassService;

    SignInRemindService signInRemindService;

    @RequestMapping("/to-personal")
    public String toPersonal(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        LsClientUser attribute = (LsClientUser) session.getAttribute(LoginInterceptor.CLIENT_SESSION_KEY);
        model.addAttribute("user",attribute.getPhone());
        return "client-user/tabbar";
    }

    @PostMapping(value="/personal")
    @ResponseBody
    public ResultEntity findAll(
        @NotNull(message = "对象不能为空")
        @RequestBody LsClientUser clientUser, HttpSession session) {
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
        //证明密码正确  将用户信息放入到session
        session.setAttribute("client-user",user);
        return ResultUtils.success();
    }

    @PostMapping("/getUserInfo")
    @ResponseBody
    public ResultEntity getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        LsClientUser lsClientUser = (LsClientUser) session.getAttribute(LoginInterceptor.CLIENT_SESSION_KEY);
        String num = userClassService.getUserSurplusByPhone(lsClientUser.getPhone());
        String noReadNum = signInRemindService.findNumByPhone(lsClientUser.getPhone());
        lsClientUser.setSurplus(num);
        lsClientUser.setNoReadNum(noReadNum);
        return ResultUtils.success(lsClientUser);
    }

    public static void main(String[] args) {
        System.out.println(8%5);
    }


}
