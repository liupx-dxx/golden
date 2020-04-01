package com.github.binarywang.demo.wx.mp.controller.client;

import com.github.binarywang.demo.wx.mp.config.intercepors.LoginInterceptor;
import com.github.binarywang.demo.wx.mp.entity.surce.LsClientUser;
import com.github.binarywang.demo.wx.mp.entity.surce.LsUserClass;
import com.github.binarywang.demo.wx.mp.enums.ClassTypeEnum;
import com.github.binarywang.demo.wx.mp.enums.ResultCodeEnum;
import com.github.binarywang.demo.wx.mp.service.manager.UserClassService;
import com.github.binarywang.demo.wx.mp.utils.PageUtils;
import com.github.binarywang.demo.wx.mp.utils.ResultEntity;
import com.github.binarywang.demo.wx.mp.utils.ResultUtils;
import com.github.binarywang.demo.wx.mp.vo.UserClassReq;
import lombok.AllArgsConstructor;
import org.hibernate.mapping.Collection;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class ClientUserClassController {

    UserClassService userClassService;

    /**
     * 跳转到用户课程页面
     *
     *  */
    @RequestMapping("/client/userClass/to-list")
    public String toList(){
        return "sys/userClass-list";
    }

    /**
     * 分页获取所有购买信息
     * @return
     */
    @RequestMapping("/client/userClass/findPage")
    @ResponseBody
    public PageUtils findPage(
        @NotNull(message = "请求参数不能为空")
        @RequestBody Map<String,String> map) {
        PageUtils pageUtils = new PageUtils();
        boolean flag = map.containsKey("phone");
        if(!flag){
            pageUtils.setCode(405);
            pageUtils.setMsg("参数错误，请重试");
        }
        String phone = map.get("phone");
        if(StringUtils.isEmpty(phone)){
            pageUtils.setCode(405);
            pageUtils.setMsg("参数错误，请重试");
        }

        int page = 0;
        int size = 10;
        if(map.containsKey("pageNumber") && map.containsKey("pageSize")){
            page = Integer.valueOf(map.get("pageNumber"))-1;
            size = Integer.valueOf(map.get("pageSize"));
        }
        Pageable pageable = new PageRequest(page,size);
        Page<LsUserClass> pageList = userClassService.findPage(map, pageable);
        List<LsUserClass> content = pageList.getContent();

        pageUtils.setRows(content);
        pageUtils.setPage(page);
        pageUtils.setSize(size);
        pageUtils.setTotal((int) pageList.getTotalElements());
        pageUtils.setCode(200);
        return pageUtils;

    }

    /**
     * 获取个人购买信息
     * @return
     */
    @GetMapping("/client/userClass/findAllById")
    @ResponseBody
    public ResultEntity findAll(HttpServletRequest request) {
        HttpSession session = request.getSession();
        LsClientUser clientUser = (LsClientUser) session.getAttribute(LoginInterceptor.CLIENT_SESSION_KEY);
        if(clientUser==null){
            return ResultUtils.fail(ResultCodeEnum.INTERNAL_ERROR);
        }
        List<LsUserClass> userClassList = userClassService.findByUserPhone(clientUser.getPhone());
        if(!CollectionUtils.isEmpty(userClassList)){
            userClassList.stream().forEach(item ->{
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
        return ResultUtils.success(userClassList);
    }

    /**
     * 跳转到课程详情
     *
     *  */
    @GetMapping("/client/userClass/findById/{id}")
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
    @GetMapping("/client/userClass/delete/{id}")
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
    @PostMapping(value="/client/userClass/update")
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
    @PostMapping(value="/client/userClass/saveAll")
    @ResponseBody
    public ResultEntity saveAll(
        @NotNull(message = "对象不可为空")
        @RequestBody UserClassReq userClassReq) {
        userClassService.saveAll(userClassReq);
        return ResultUtils.success();
    }

    /**
     * 签到
     *
     *  */
    @GetMapping("/client/userClass/signIn/{id}")
    @ResponseBody
    public ResultEntity signIn(
        @NotNull(message = "id不可为空")
        @PathVariable("id") String id,HttpServletRequest request) {
        HttpSession session = request.getSession();
        LsClientUser clientUser = (LsClientUser) session.getAttribute(LoginInterceptor.CLIENT_SESSION_KEY);
        return userClassService.userSignIn(id,clientUser);
    }

}
