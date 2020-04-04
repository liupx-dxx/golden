package com.github.binarywang.demo.wx.mp.controller.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.LsClientUser;
import com.github.binarywang.demo.wx.mp.entity.sys.SysUser;
import com.github.binarywang.demo.wx.mp.enums.ResultCodeEnum;
import com.github.binarywang.demo.wx.mp.service.client.ClientUserService;
import com.github.binarywang.demo.wx.mp.service.manager.UserService;
import com.github.binarywang.demo.wx.mp.utils.ResultEntity;
import com.github.binarywang.demo.wx.mp.utils.ResultUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/manager")
public class UserController {
    UserService userService;
    ClientUserService clientUserService;


    @GetMapping(value="/user/findAll")
    public ResultEntity findAll() {
        List<SysUser> all = userService.findAll();
        return ResultUtils.success(all);

    }

    @PostMapping(value="/user/save")
    public ResultEntity save(@RequestBody SysUser sysUser) {
        userService.save(sysUser);
        return ResultUtils.success();
    }

    @PostMapping(value="/clientUser/save")
    public ResultEntity saveClientUser(@RequestBody LsClientUser clientUser) {
        LsClientUser user = clientUserService.findByPhone(clientUser.getPhone());
        if(user!=null){
            return ResultUtils.fail(ResultCodeEnum.PARAMETER_ERROR,"手机号已存在");
        }
        clientUserService.save(clientUser);
        return ResultUtils.success();
    }

}
