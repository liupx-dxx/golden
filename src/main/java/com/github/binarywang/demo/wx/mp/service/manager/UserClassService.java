package com.github.binarywang.demo.wx.mp.service.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.LsClass;
import com.github.binarywang.demo.wx.mp.entity.surce.LsClientUser;
import com.github.binarywang.demo.wx.mp.entity.surce.LsUserClass;
import com.github.binarywang.demo.wx.mp.entity.surce.LsUserSignIn;
import com.github.binarywang.demo.wx.mp.enums.*;
import com.github.binarywang.demo.wx.mp.repository.client.ClientUserRepository;
import com.github.binarywang.demo.wx.mp.repository.client.UserSignInRepository;
import com.github.binarywang.demo.wx.mp.repository.manager.UserClassRepository;
import com.github.binarywang.demo.wx.mp.utils.ResultEntity;
import com.github.binarywang.demo.wx.mp.utils.ResultUtils;
import com.github.binarywang.demo.wx.mp.vo.UserClassReq;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class UserClassService {

    UserClassRepository userClassRepository;

    ClassManagerService classManagerService;

    ClientUserRepository clientUserRepository;

    UserSignInRepository userSignInRepository;

    /**
     * 分页获取所有信息
     * @return
     */
    public Page<LsUserClass> findPage(Map<String, String> params, Pageable pageable) {
        return userClassRepository.findPage(params,pageable);
    }


    public List<LsUserClass> findAll() {
        return userClassRepository.findAll();
    }


    /**
     * 新增用户课程
     *
     * */
    @Transactional(rollbackFor = Exception.class)
    public void save(LsUserClass userClass) {
        userClass.setCreateTime(LocalDateTime.now());
        userClass.setUpdateTime(LocalDateTime.now());
        String classId = userClass.getClassId();
        if(!StringUtils.isEmpty(classId)){
            LsClass byId = classManagerService.findById(classId);
            if(byId!=null){
                userClass.setClassName(byId.getClassName());
            }
        }
        userClassRepository.save(userClass);
        String clientUserPhone = userClass.getClientUserPhone();
        if(!StringUtils.isEmpty(clientUserPhone)){
            LsClientUser byPhone = clientUserRepository.findByPhone(clientUserPhone);
            if(byPhone==null){
                //证明该人还么有在该机构购买过课程
                //保存该用户信息
                LsClientUser clientUser = new LsClientUser();
                clientUser.setPhone(clientUserPhone);
                String password = "123456";
                //将密码加密
                String encode = Base64.getEncoder().encodeToString(password.getBytes());
                clientUser.setPassword(encode);
                clientUser.setUserName(userClass.getClientUserName());
                clientUser.setCreateTime(LocalDateTime.now());
                clientUserRepository.save(clientUser);
            }
        }
    }

    /**
     * 批量保存用户课程
     *
     * */
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(UserClassReq userClassReq) {
        List<LsUserClass> userClassList = userClassReq.getUserClassList();
        if(!CollectionUtils.isEmpty(userClassList)){
            userClassList.stream().forEach(item ->{
                item.setCreateTime(LocalDateTime.now());
            });
        }
        userClassRepository.saveAll(userClassList);
        if(StringUtils.isEmpty(userClassReq.getPhone())){
            //证明该人还么有在该机构购买过课程
            //保存该用户信息
            LsClientUser clientUser = new LsClientUser();
            clientUser.setPhone(userClassReq.getPhone());
            String password = "123456";
            //将密码加密
            String encode = Base64.getEncoder().encodeToString(password.getBytes());
            clientUser.setPassword(encode);
            clientUser.setUserName(userClassReq.getUserName());
            clientUser.setCreateTime(LocalDateTime.now());
            clientUserRepository.save(clientUser);
        }

    }


    public void deleteById(String id) {
        LsUserClass one = userClassRepository.findOne(Long.valueOf(id));
        if(one!=null){
            userClassRepository.delete(one);
        }
    }

    public LsUserClass findById(String id) {
        Optional<LsUserClass> byId = userClassRepository.findById(Long.valueOf(id));
        if(byId==null){
            return null;
        }
        return byId.get();
    }


    /**
     * 根据用户查询个人购买课程
     *
     * */
    public List<LsUserClass> findByUserPhone(String phone,Long userId) {
        //获取该用户今天签到、请假的课程
        List<LsUserSignIn> userSignIns = userSignInRepository.findByUserId(userId);

        List<LsUserClass> userClassList = userClassRepository.findByUserPhone(phone);
        if(!CollectionUtils.isEmpty(userClassList)){
            userClassList.stream().forEach(item ->{
                if(!CollectionUtils.isEmpty(userSignIns)){
                    userSignIns.stream().forEach(userSignIn ->{
                        if(item.getClassId().equals(userSignIn.getClassId())){
                            String flag = userSignIn.getFlag();
                            if(OperationTypeEnum.SIGN_IN.getCode().equals(flag)){
                                item.setSignInFlag(SignInStateEnum.SIGN_IN.getCode());
                            }else{
                                item.setSignInFlag(SignInStateEnum.LEAVE.getCode());
                            }
                        }
                    });
                }else{
                    item.setSignInFlag(SignInStateEnum.NO_SIGN_IN_LEAVE.getCode());
                }

                //查看该课程今天是否签到
                /*LsUserSignIn lsUserSignIn = userSignInRepository.findByUserIdAndClassId(userId + "", item.getClassId());
                if(lsUserSignIn!=null && !StringUtils.isEmpty(lsUserSignIn.getFlag())){
                    String flag = lsUserSignIn.getFlag();
                    if(OperationTypeEnum.SIGN_IN.getCode().equals(flag)){
                        item.setSignInFlag(SignInStateEnum.SIGN_IN.getCode());
                    }else{
                        item.setSignInFlag(SignInStateEnum.LEAVE.getCode());
                    }
                }else{
                    item.setSignInFlag(SignInStateEnum.NO_SIGN_IN_LEAVE.getCode());
                }*/
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
        return userClassList;
    }
    /**
     * 用户签到扣除相应的课时
     *
     * */
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity userSignIn(String id, LsClientUser clientUser) {
        //查询该购买记录
        LsUserClass userClass = userClassRepository.findOne(Long.valueOf(id));
        if(userClass==null){
            return ResultUtils.fail(ResultCodeEnum.PARAMETER_ERROR);
        }
        //扣除相应的课时
        int classHourNum = userClass.getClassHourNum();
        if(classHourNum<=1){
            return ResultUtils.fail(ResultCodeEnum.OPERATE_FAIL,"剩余课时不足");
        }
        //证明他购买的该课程还有剩余课时
        //课时扣除1
        classHourNum = classHourNum-2;
        userClass.setClassHourNum(classHourNum);
        userClass.setUpdateTime(LocalDateTime.now());
        userClassRepository.save(userClass);
        //记录当前人签到记录
        LsUserSignIn lsUserSignIn = new LsUserSignIn();
        lsUserSignIn.setClassId(userClass.getClassId());
        lsUserSignIn.setUserName(clientUser.getUserName());
        lsUserSignIn.setClassType(userClass.getClassType());
        lsUserSignIn.setUserId(clientUser.getId());
        lsUserSignIn.setClassName(userClass.getClassName());
        lsUserSignIn.setFlag(OperationTypeEnum.SIGN_IN.getCode());
        lsUserSignIn.setExamineFlag(ExamineStateEnum.NO_EXAMINE.getCode());
        lsUserSignIn.setFeedbackFlag(FeedbackStateEnum.NO_FEEDBACK.getCode());
        lsUserSignIn.setCreateTime(LocalDateTime.now());
        userSignInRepository.save(lsUserSignIn);
        return ResultUtils.success();
    }


    /**
     * 用户请假  不应扣课时
     *
     * */
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity leave(String id, LsClientUser clientUser) {
        //查询该购买记录
        LsUserClass userClass = userClassRepository.findOne(Long.valueOf(id));
        if(userClass==null){
            return ResultUtils.fail(ResultCodeEnum.PARAMETER_ERROR);
        }
        //记录当前人请假记录
        LsUserSignIn lsUserSignIn = new LsUserSignIn();
        lsUserSignIn.setClassId(userClass.getClassId());
        lsUserSignIn.setClassName(userClass.getClassName());
        lsUserSignIn.setUserName(clientUser.getUserName());
        lsUserSignIn.setUserId(clientUser.getId());
        lsUserSignIn.setClassType(userClass.getClassType());
        lsUserSignIn.setFlag(OperationTypeEnum.LEAVE.getCode());
        lsUserSignIn.setExamineFlag(ExamineStateEnum.UN_EXAMINE.getCode());
        lsUserSignIn.setFeedbackFlag(FeedbackStateEnum.NO_FEEDBACK.getCode());
        lsUserSignIn.setCreateTime(LocalDateTime.now());
        userSignInRepository.save(lsUserSignIn);
        return ResultUtils.success();
    }

    /**
     *
     * 批量删除
     * */
    @Transactional(rollbackFor = Exception.class)
    public void delById(List<LsUserClass> userClassList) {
        userClassRepository.deleteAll(userClassList);
    }

    /**
     *
     * 批量删除
     * */
    public List<LsUserClass> findByClassId(Long id) {
        return userClassRepository.findByClassId(id+"");
    }

    /**
     *
     * 根据周单位查询
     *
     * */
    public List<LsUserClass> findByWeek(String week) {
        return userClassRepository.findByWeek(week);
    }

    /**
     *
     * 获取总课时
     *
     * */
    public String getUserSurplusByPhone(String phone) {
        return userClassRepository.getUserSurplusByPhone(phone);
    }
}
