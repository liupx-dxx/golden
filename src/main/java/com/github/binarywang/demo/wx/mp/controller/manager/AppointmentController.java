package com.github.binarywang.demo.wx.mp.controller.manager;

import com.github.binarywang.demo.wx.mp.entity.advance.Advance;
import com.github.binarywang.demo.wx.mp.service.manager.AdvanceService;
import com.github.binarywang.demo.wx.mp.utils.PageUtils;
import com.github.binarywang.demo.wx.mp.utils.ResultEntity;
import com.github.binarywang.demo.wx.mp.utils.ResultUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class AppointmentController {

    AdvanceService advanceService;

    /**
     * 分页获取所有预约信息
     * @return
     */
    @RequestMapping("/manager/appointment/findAll")
    @ResponseBody
    public PageUtils findAll(
        @NotNull(message = "请求参数不能为空")
        @RequestBody Map<String,String> map) {

        int page = 0;
        int size = 10;
        if(map.containsKey("pageNumber") && map.containsKey("pageSize")){
            page = Integer.valueOf(map.get("pageNumber"))-1;
            size = Integer.valueOf(map.get("pageSize"));
        }
        Pageable pageable = new PageRequest(page,size);
        Page<Advance> pageList = advanceService.findPage(map, pageable);
        List<Advance> content = pageList.getContent();
        PageUtils pageUtils = new PageUtils();
        pageUtils.setRows(content);
        pageUtils.setPage(page);
        pageUtils.setSize(size);
        pageUtils.setTotal((int) pageList.getTotalElements());
        return pageUtils;

    }
    /**
     * 跳转到预约页面
     *
     *  */
    @RequestMapping("/manager/appointment/to-list")
    public String toList(){
        return "sys/appointment-list";
    }

    /**
     * 跳转到预约页面
     *
     *  */
    @GetMapping("/manager/appointment/findById/{id}")
    @ResponseBody
    public Advance findById(
        @NotNull(message = "id不可为空")
            @PathVariable("id") String id) {
        Advance advance = advanceService.findById(id);
        return advance;
    }

    /**
     * 新增预约信息
     * @return
     */
    @PostMapping(value="/manager/appointment/update")
    @ResponseBody
    public ResultEntity save(
        @NotNull(message = "对象不可为空")
        @RequestBody Advance advance) {
        advanceService.save(advance);
        return ResultUtils.success();
    }

}
