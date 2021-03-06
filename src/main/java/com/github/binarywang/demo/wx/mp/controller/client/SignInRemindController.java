package com.github.binarywang.demo.wx.mp.controller.client;

import com.github.binarywang.demo.wx.mp.config.intercepors.LoginInterceptor;
import com.github.binarywang.demo.wx.mp.entity.surce.LsClientUser;
import com.github.binarywang.demo.wx.mp.entity.surce.LsSignInRemind;
import com.github.binarywang.demo.wx.mp.enums.ResultCodeEnum;
import com.github.binarywang.demo.wx.mp.service.manager.SignInRemindService;
import com.github.binarywang.demo.wx.mp.utils.ResultEntity;
import com.github.binarywang.demo.wx.mp.utils.ResultUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/client")
public class SignInRemindController {

    SignInRemindService signInRemindService;

    /**
     * 跳转到用户课程页面
     *
     *  */
    @RequestMapping("/signInRemind/to-list")
    public String toList(){
        return "sys/userClass-list";
    }


    /**
     * 获取个人购买信息
     * @return
     */
    @GetMapping("/signInRemind/findAllById")
    @ResponseBody
    public ResultEntity findAll(HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        HttpSession session = request.getSession();
        LsClientUser clientUser = (LsClientUser) session.getAttribute(LoginInterceptor.CLIENT_SESSION_KEY);
        if(clientUser==null){
            return ResultUtils.fail(ResultCodeEnum.INTERNAL_ERROR);
        }
        List<LsSignInRemind> signInReminds = signInRemindService.findByUserPhone(clientUser.getPhone());
        map.put("remindList",signInReminds);
        long noReadNum = signInRemindService.findNumByPhone(clientUser.getPhone());
        map.put("noReadNum",noReadNum);

        return ResultUtils.success(map);
    }

    /**
     * 根据提醒ID获取购买信息
     *
     *  */
    @GetMapping("/signInRemind/findById/{id}")
    @ResponseBody
    public LsSignInRemind findById(
        @NotNull(message = "id不可为空")
        @PathVariable("id") String id) {
        LsSignInRemind signInRemind = signInRemindService.findById(id);
        return signInRemind;
    }

    /*
     * 设置提醒已读
     *
     *  */
    @GetMapping("/signInRemind/remindAlreadySee/{id}")
    @ResponseBody
    public ResultEntity remindAlreadySee(
        @NotNull(message = "id不可为空")
        @PathVariable("id") String id,HttpServletRequest request) {
        HttpSession session = request.getSession();
        LsClientUser clientUser = (LsClientUser) session.getAttribute(LoginInterceptor.CLIENT_SESSION_KEY);
        if(clientUser==null){
            return ResultUtils.fail(ResultCodeEnum.INTERNAL_ERROR);
        }
        LsSignInRemind signInRemind = signInRemindService.findById(id);
        if(signInRemind==null){
            return ResultUtils.fail(ResultCodeEnum.PARAMETER_ERROR);
        }
        signInRemindService.remindAlreadySee(signInRemind,clientUser.getId());

        return ResultUtils.success();
    }

    /**
     * 跳转到提醒详情
     *
     *  */
    @GetMapping("/signInRemind/findNumById")
    @ResponseBody
    public ResultEntity findNumById(HttpServletRequest request) {
        HttpSession session = request.getSession();
        LsClientUser clientUser = (LsClientUser) session.getAttribute(LoginInterceptor.CLIENT_SESSION_KEY);
        if(clientUser==null){
            return ResultUtils.fail(ResultCodeEnum.INTERNAL_ERROR);
        }
        long num = signInRemindService.findNumByPhone(clientUser.getPhone());
        return ResultUtils.success(num);
    }

}
