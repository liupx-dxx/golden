package com.github.binarywang.demo.wx.mp.controller.manager;

import com.github.binarywang.demo.wx.mp.entity.sys.SysUser;
import com.github.binarywang.demo.wx.mp.enums.ResultCodeEnum;
import com.github.binarywang.demo.wx.mp.service.manager.UserService;
import com.github.binarywang.demo.wx.mp.utils.ResultEntity;
import com.github.binarywang.demo.wx.mp.utils.ResultUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/manager")
public class LoginController {
    UserService userService;

    @RequestMapping("/to-login")
    public String login() {
        return "sys/login";
    }

    @PostMapping(value="/login")
    @ResponseBody
    public ResultEntity findAll(
        @NotNull(message = "对象不能为空")
        @RequestBody SysUser sysUser, HttpSession session) {
        SysUser user = userService.getUser(sysUser.getName());
        if(user==null){
            return ResultUtils.fail(ResultCodeEnum.PARAMETER_ERROR,"用户名不存在");
        }
        //证明用用户名存在 判断密码是否正确
        byte[] decode = Base64.getDecoder().decode(user.getPassword());
        String decodePass = new String(decode);
        //判断密码是否正确
        if(!decodePass.equals(sysUser.getPassword())){
            //证明密码不正确，返回提示
            return ResultUtils.fail(ResultCodeEnum.PARAMETER_ERROR,"密码错误");
        }
        //证明密码正确  将用户信息放入到session
        session.setAttribute("user",user);
        String url = "/sys/index.html";
        /*if(session.getAttribute("manager-url")!=null){
            url = session.getAttribute("manager-url").toString();
        }*/
        Map<String,String> map = new HashMap<>();
        map.put("pathUrl",url);
        return ResultUtils.success(map);
    }
}
