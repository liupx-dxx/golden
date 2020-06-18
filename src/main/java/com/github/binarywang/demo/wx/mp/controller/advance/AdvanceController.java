package com.github.binarywang.demo.wx.mp.controller.advance;

import com.github.binarywang.demo.wx.mp.entity.advance.Advance;
import com.github.binarywang.demo.wx.mp.enums.ResultCodeEnum;
import com.github.binarywang.demo.wx.mp.service.manager.AdvanceService;
import com.github.binarywang.demo.wx.mp.utils.ResultEntity;
import com.github.binarywang.demo.wx.mp.utils.ResultUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/advance")
@AllArgsConstructor
public class AdvanceController {

    AdvanceService advanceService;


    /**
     * 新增预约信息
     * @return
     */
    @PostMapping(value="/save")
    public ResultEntity save(
        @NotNull(message = "对象不可为空")
        @RequestBody Advance advance) {
        //查询该手机号今天是否预约过
        String phone = advance.getPhone();
        int num = advanceService.findByPhone(phone);
        if(num>0){
            return ResultUtils.fail(ResultCodeEnum.PARAMETER_ERROR,"您今天已经预约过了哦");
        }
        advanceService.save(advance);
        return ResultUtils.success();
    }

}
