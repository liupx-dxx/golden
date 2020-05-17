package com.github.binarywang.demo.wx.mp.controller.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.LsUserClass;
import com.github.binarywang.demo.wx.mp.entity.surce.LsUserSignIn;
import com.github.binarywang.demo.wx.mp.enums.*;
import com.github.binarywang.demo.wx.mp.service.manager.UserSignInService;
import com.github.binarywang.demo.wx.mp.utils.PageUtils;
import com.github.binarywang.demo.wx.mp.utils.ResultEntity;
import com.github.binarywang.demo.wx.mp.utils.ResultUtils;
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
public class UserSignInController {

    UserSignInService userSignInService;

    /**
     * 跳转到用户签到页面
     *
     *  */
    @RequestMapping("/userSignIn/to-list")
    public String toList(){
        return "sys/userSignIn-list";
    }

    /**
     * 分页获取所有签到信息
     * @return
     */
    @RequestMapping("/userSignIn/findPage")
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
        Page<LsUserSignIn> pageList = userSignInService.findPage(map, pageable);
        List<LsUserSignIn> content = pageList.getContent();
        //翻译字段信息
        List<LsUserSignIn> translate = translate(content);
        PageUtils pageUtils = new PageUtils();
        pageUtils.setRows(translate);
        pageUtils.setPage(page);
        pageUtils.setSize(size);
        pageUtils.setTotal((int) pageList.getTotalElements());
        return pageUtils;

    }

    /**
     * 跳转到签到详情
     *
     *  */
    @GetMapping("/userSignIn/findById/{id}")
    @ResponseBody
    public LsUserSignIn findById(
        @NotNull(message = "id不可为空")
            @PathVariable("id") String id) {
        LsUserSignIn userClass = userSignInService.findById(id);
        return userClass;
    }

    /**
     * 删除用户签到信息
     *
     *  */
    @GetMapping("/userSignIn/delete/{id}")
    @ResponseBody
    public ResultEntity deleteById(
        @NotNull(message = "id不可为空")
        @PathVariable("id") String id) {
        userSignInService.deleteById(id);
        return ResultUtils.success();
    }

    /**
     * 批量删除该课程
     *
     *  */
    @PostMapping("/userSignIn/delByIds")
    @ResponseBody
    public ResultEntity delById(
        @NotNull(message = "请求参数不能为空")
        @RequestBody
            List<LsUserSignIn> userSignInList) {
        userSignInService.delByIds(userSignInList);
        return ResultUtils.success();
    }

    /**
     * 新增签到信息
     * @return
     */
    @PostMapping(value="/userSignIn/update")
    @ResponseBody
    public ResultEntity save(
        @NotNull(message = "对象不可为空")
        @RequestBody LsUserSignIn userSignIn) {
        userSignInService.save(userSignIn);
        return ResultUtils.success();
    }

    /**
     *
     * 翻译
     * */
    private List<LsUserSignIn> translate(List<LsUserSignIn> content){
        if(!CollectionUtils.isEmpty(content)){
            content.stream().forEach(item ->{
                String classType = item.getClassType();
                String flag = item.getFlag();
                String examineFlag = item.getExamineFlag();
                String feedbackFlag = item.getFeedbackFlag();
                if(!StringUtils.isEmpty(classType)){
                    if(ClassTypeEnum.CLASS.getCode().equals(classType)){
                        item.setClassType(ClassTypeEnum.CLASS.getDesc());
                    }else if(ClassTypeEnum.GROUP_COURSE.getCode().equals(classType)){
                        item.setClassType(ClassTypeEnum.GROUP_COURSE.getDesc());
                    }else if(ClassTypeEnum.ONE_ON_ONE.getCode().equals(classType)){
                        item.setClassType(ClassTypeEnum.ONE_ON_ONE.getDesc());
                    }
                }
                if(!StringUtils.isEmpty(flag)){
                    if(OperationTypeEnum.SIGN_IN.getCode().equals(flag)){
                        item.setFlag(OperationTypeEnum.SIGN_IN.getDesc());
                    }else if(OperationTypeEnum.LEAVE.getCode().equals(flag)) {
                        item.setFlag(OperationTypeEnum.LEAVE.getDesc());
                    }
                }
                if(!StringUtils.isEmpty(examineFlag)){
                    if(ExamineStateEnum.UN_EXAMINE.getCode().equals(examineFlag)){
                        item.setExamineFlag(ExamineStateEnum.UN_EXAMINE.getDesc());
                    }else if(ExamineStateEnum.YES_EXAMINE.getCode().equals(examineFlag)){
                        item.setExamineFlag(ExamineStateEnum.YES_EXAMINE.getDesc());
                    }else if(ExamineStateEnum.NO_EXAMINE.getCode().equals(examineFlag)){
                        item.setExamineFlag(ExamineStateEnum.NO_EXAMINE.getDesc());
                    }
                }
                if(!StringUtils.isEmpty(feedbackFlag)){
                    if(FeedbackStateEnum.NO_FEEDBACK.getCode().equals(feedbackFlag)){
                        item.setFeedbackFlag(FeedbackStateEnum.NO_FEEDBACK.getDesc());
                    }else if(FeedbackStateEnum.YES_FEEDBACK.getCode().equals(feedbackFlag)){
                        item.setFeedbackFlag(FeedbackStateEnum.YES_FEEDBACK.getDesc());
                    }
                }
            });
        }
        return content;
    }

    /**
     * 审核
     * @return
     */
    @PostMapping(value="/userSignIn/examine")
    @ResponseBody
    public ResultEntity examine(
        @NotNull(message = "对象不可为空")
        @RequestBody LsUserSignIn userSignIn) {
        userSignInService.examine(userSignIn);
        return ResultUtils.success();
    }

}
