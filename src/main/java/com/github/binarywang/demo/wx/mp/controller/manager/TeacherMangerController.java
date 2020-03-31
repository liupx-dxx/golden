package com.github.binarywang.demo.wx.mp.controller.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.LsTeacher;
import com.github.binarywang.demo.wx.mp.service.manager.TeacherManagerService;
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
public class TeacherMangerController {

    TeacherManagerService teacherManagerService;

    /**
     * 跳转到课程页面
     *
     *  */
    @RequestMapping("/manager/teacher/to-list")
    public String toList(){
        return "sys/teacher-list";
    }

    /**
     * 分页获取所有课程信息
     * @return
     */
    @RequestMapping("/manager/teacher/findPage")
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
        Page<LsTeacher> pageList = teacherManagerService.findPage(map, pageable);
        List<LsTeacher> content = pageList.getContent();
        PageUtils pageUtils = new PageUtils();
        pageUtils.setRows(content);
        pageUtils.setPage(page);
        pageUtils.setSize(size);
        pageUtils.setTotal((int) pageList.getTotalElements());
        return pageUtils;

    }

    /**
     * 跳转到课程详情
     *
     *  */
    @GetMapping("/manager/teacher/findById/{id}")
    @ResponseBody
    public LsTeacher findById(
        @NotNull(message = "id不可为空")
            @PathVariable("id") String id) {
        LsTeacher lsTeacher = teacherManagerService.findById(id);
        return lsTeacher;
    }

    /**
     * 新增预约信息
     * @return
     */
    @PostMapping(value="/manager/teacher/update")
    @ResponseBody
    public ResultEntity save(
        @NotNull(message = "对象不可为空")
        @RequestBody LsTeacher lsTeacher) {
        teacherManagerService.save(lsTeacher);
        return ResultUtils.success();
    }
    /**
     * 获取老师
     * @retu<></>rn
     */
    @RequestMapping("/manager/teacher/findAll")
    @ResponseBody
    public List<LsTeacher> findAll() {
        return teacherManagerService.findAll();
    }

}
