package com.github.binarywang.demo.wx.mp.controller.manager;

import com.github.binarywang.demo.wx.mp.config.intercepors.LoginInterceptor;
import com.github.binarywang.demo.wx.mp.entity.advance.Advance;
import com.github.binarywang.demo.wx.mp.entity.surce.LsUserClass;
import com.github.binarywang.demo.wx.mp.entity.sys.SysUser;
import com.github.binarywang.demo.wx.mp.enums.ResultCodeEnum;
import com.github.binarywang.demo.wx.mp.service.manager.AdvanceService;
import com.github.binarywang.demo.wx.mp.utils.PageUtils;
import com.github.binarywang.demo.wx.mp.utils.ResultEntity;
import com.github.binarywang.demo.wx.mp.utils.ResultUtils;
import com.github.binarywang.demo.wx.mp.vo.UserClassReq;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/manager")
public class AppointmentController {

    AdvanceService advanceService;

    /**
     * 分页获取所有预约信息
     * @return
     */
    @RequestMapping("/appointment/findAll")
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
    @RequestMapping("/appointment/to-list")
    public String toList(){
        return "sys/appointment-list";
    }

    /**
     * 跳转到预约页面
     *
     *  */
    @GetMapping("/appointment/findById/{id}")
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
    @PostMapping(value="/appointment/update")
    @ResponseBody
    public ResultEntity save(
        @NotNull(message = "对象不可为空")
        @RequestBody Advance advance) {
        advanceService.save(advance);
        return ResultUtils.success();
    }

    /**
     * 事件全流程统计-导出Excel报表
     *
     * @param map
     * @return
     */
    @PostMapping(path = "/appointment/exportExcel")
    @ResponseBody
    public ResultEntity exportExcel(HttpServletRequest request, @RequestBody Map<String,String> map){
        //获取用户
        HttpSession session = request.getSession();
        SysUser user = (SysUser)session.getAttribute(LoginInterceptor.MANAGER_SESSION_KEY);
        if (user == null) {
            return ResultUtils.fail(ResultCodeEnum.TORKEN_ERROR);
        }
        List<Advance> list = advanceService.getAdvanceByParm(map);
        String url = advanceService.exportExcel(user.getName(), list, map);
        if (url != null) {
            map.put("url",url);
            return ResultUtils.success(map);
        } else {
            return ResultUtils.fail(ResultCodeEnum.INTERNAL_ERROR);
        }
    }

}
