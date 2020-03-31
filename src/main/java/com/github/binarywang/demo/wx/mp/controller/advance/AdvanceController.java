package com.github.binarywang.demo.wx.mp.controller.advance;

import com.github.binarywang.demo.wx.mp.entity.advance.Advance;
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
@RequestMapping("advance")
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
        advanceService.save(advance);
        return ResultUtils.success();
    }

}
