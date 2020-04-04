package com.github.binarywang.demo.wx.mp.controller.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.LsUserClass;
import com.github.binarywang.demo.wx.mp.enums.ClassTypeEnum;
import com.github.binarywang.demo.wx.mp.service.manager.UserClassService;
import com.github.binarywang.demo.wx.mp.utils.PageUtils;
import com.github.binarywang.demo.wx.mp.utils.ResultEntity;
import com.github.binarywang.demo.wx.mp.utils.ResultUtils;
import com.github.binarywang.demo.wx.mp.vo.UserClassReq;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/manager")
public class UserClassController {

    UserClassService userClassService;

    /**
     * 跳转到用户课程页面
     *
     *  */
    @RequestMapping("/userClass/to-list")
    public String toList(){
        return "sys/userClass-list";
    }

    /**
     * 分页获取所有购买信息
     * @return
     */
    @RequestMapping("/userClass/findPage")
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
        Page<LsUserClass> pageList = userClassService.findPage(map, pageable);
        List<LsUserClass> content = pageList.getContent();
        if(!CollectionUtils.isEmpty(content)){
            content.stream().forEach(item ->{
                String classType = item.getClassType();
                if(!StringUtils.isEmpty(classType)){
                    if(ClassTypeEnum.CLASS.getCode().equals(classType)){
                        item.setClassType(ClassTypeEnum.CLASS.getDesc());
                    }else if(ClassTypeEnum.GROUP_COURSE.getCode().equals(classType)){
                        item.setClassType(ClassTypeEnum.GROUP_COURSE.getDesc());
                    }else if(ClassTypeEnum.ONE_ON_ONE.getCode().equals(classType)){
                        item.setClassType(ClassTypeEnum.ONE_ON_ONE.getDesc());
                    }
                }

            });
        }
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
    @GetMapping("/userClass/findById/{id}")
    @ResponseBody
    public LsUserClass findById(
        @NotNull(message = "id不可为空")
            @PathVariable("id") String id) {
        LsUserClass userClass = userClassService.findById(id);
        return userClass;
    }

    /**
     * 删除用户购买信息
     *
     *  */
    @GetMapping("/userClass/delete/{id}")
    @ResponseBody
    public ResultEntity deleteById(
        @NotNull(message = "id不可为空")
        @PathVariable("id") String id) {
        userClassService.deleteById(id);
        return ResultUtils.success();
    }

    /**
     * 新增预约信息
     * @return
     */
    @PostMapping(value="/userClass/update")
    @ResponseBody
    public ResultEntity save(
        @NotNull(message = "对象不可为空")
        @RequestBody LsUserClass userClass) {
        userClassService.save(userClass);
        return ResultUtils.success();
    }

    /**
     * 批量新增用户购买课程信息
     * @return
     */
    @PostMapping(value="/userClass/saveAll")
    @ResponseBody
    public ResultEntity saveAll(
        @NotNull(message = "对象不可为空")
        @RequestBody UserClassReq userClassReq) {
        userClassService.saveAll(userClassReq);
        return ResultUtils.success();
    }

    /**
     * 批量删除该课程
     *
     *  */
    @PostMapping("/userClass/delByIds")
    @ResponseBody
    public ResultEntity delById(
        @NotNull(message = "请求参数不能为空")
            @RequestBody
        List<LsUserClass> userClassList) {
        userClassService.delById(userClassList);
        return ResultUtils.success();
    }

}
