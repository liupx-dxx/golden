package com.github.binarywang.demo.wx.mp.controller.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.LsClass;
import com.github.binarywang.demo.wx.mp.service.manager.ClassManagerService;
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
public class ClassMangerController {

    ClassManagerService classManagerService;

    /**
     * 跳转课程页面
     *
     *  */
    @RequestMapping("/manager/class/to-list")
    public String toList(){
        return "sys/userClass-list";
    }

    /**
     * 分页获取所有课程信息
     * @return
     */
    @RequestMapping("/manager/class/findPage")
    @ResponseBody
    public PageUtils findPage(
        @NotNull(message = "请求参数不能为空")
        @RequestBody Map<String,String> map) {

        int page = 0;
        int size = 10;
        if(map.containsKey("pageNumber") && map.containsKey("pageSize")){
            page = Integer.valueOf(map.get("pageNumber"))-1;
            size = Integer.valueOf(map.get("pageSize"));
        }
        Pageable pageable = new PageRequest(page,size);
        Page<LsClass> pageList = classManagerService.findPage(map, pageable);
        List<LsClass> content = pageList.getContent();
        PageUtils pageUtils = new PageUtils();
        pageUtils.setRows(content);
        pageUtils.setPage(page);
        pageUtils.setSize(size);
        pageUtils.setTotal((int) pageList.getTotalElements());
        return pageUtils;

    }


    /**
     * 分页获取所有课程信息
     * @return
     */
    @RequestMapping("/manager/class/findAll")
    @ResponseBody
    public List<LsClass> findAll() {
        return classManagerService.findAll();
    }

    /**
     * 跳转到课程详情
     *
     *  */
    @GetMapping("/manager/class/findById/{id}")
    @ResponseBody
    public LsClass findById(
        @NotNull(message = "id不可为空")
            @PathVariable("id") String id) {
        LsClass lsClass = classManagerService.findById(id);
        return lsClass;
    }

    /**
     * 新增课程信息
     * @return
     */
    @PostMapping(value="/manager/class/update")
    @ResponseBody
    public ResultEntity save(
        @NotNull(message = "对象不可为空")
        @RequestBody LsClass lsClass) {
        classManagerService.save(lsClass);
        return ResultUtils.success();
    }

    /**
     * 批量删除该课程
     *
     *  */
    @PostMapping("/manager/class/delByIds")
    @ResponseBody
    public ResultEntity delById(
        @NotNull(message = "请求参数不能为空") @RequestBody List<LsClass> lsClasses) {
        classManagerService.delById(lsClasses);
        return ResultUtils.success();
    }

}
