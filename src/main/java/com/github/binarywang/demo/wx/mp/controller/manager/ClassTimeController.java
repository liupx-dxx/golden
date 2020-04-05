package com.github.binarywang.demo.wx.mp.controller.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.LsClass;
import com.github.binarywang.demo.wx.mp.entity.surce.LsClassTime;
import com.github.binarywang.demo.wx.mp.entity.surce.LsClientUser;
import com.github.binarywang.demo.wx.mp.entity.sys.SysUser;
import com.github.binarywang.demo.wx.mp.enums.ResultCodeEnum;
import com.github.binarywang.demo.wx.mp.service.client.ClientUserService;
import com.github.binarywang.demo.wx.mp.service.manager.ClassManagerService;
import com.github.binarywang.demo.wx.mp.service.manager.ClassTimeService;
import com.github.binarywang.demo.wx.mp.service.manager.UserService;
import com.github.binarywang.demo.wx.mp.utils.ResultEntity;
import com.github.binarywang.demo.wx.mp.utils.ResultUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/manager")
public class ClassTimeController {

    ClassTimeService classTimeService;

    ClassManagerService classManagerService;


    /**
     * 跳转到课程详情
     *
     *  */
    @GetMapping("/classTime/findByClassId/{classId}")
    @ResponseBody
    public ResultEntity findByClassId(
        @NotNull(message = "id不可为空")
        @PathVariable("classId") String classId) {
        LsClass lsClass = classManagerService.findById(classId);
        if(lsClass == null){
            return ResultUtils.fail(ResultCodeEnum.PARAMETER_ERROR);
        }
        List<LsClassTime> classTimeList = classTimeService.findById(classId);
        return ResultUtils.success(classTimeList);
    }


}
