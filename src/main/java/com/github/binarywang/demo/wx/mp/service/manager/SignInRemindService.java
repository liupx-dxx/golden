package com.github.binarywang.demo.wx.mp.service.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.*;
import com.github.binarywang.demo.wx.mp.enums.*;
import com.github.binarywang.demo.wx.mp.repository.client.ClientUserRepository;
import com.github.binarywang.demo.wx.mp.repository.client.UserSignInRepository;
import com.github.binarywang.demo.wx.mp.repository.manager.SignInRemindRepository;
import com.github.binarywang.demo.wx.mp.repository.manager.UserClassRepository;
import com.github.binarywang.demo.wx.mp.utils.ResultEntity;
import com.github.binarywang.demo.wx.mp.utils.ResultUtils;
import com.github.binarywang.demo.wx.mp.vo.UserClassReq;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SignInRemindService {

    SignInRemindRepository signInRemindRepository;

    UserSignInRepository userSignInRepository;

    /**
     * 分页获取所有信息
     * @return
     */
    public Page<LsSignInRemind> findPage(Map<String, String> params, Pageable pageable) {
        return signInRemindRepository.findPage(params,pageable);
    }


    public List<LsSignInRemind> findAll() {
        return signInRemindRepository.findAll();
    }


    /**
     * 新增用户课程
     *
     * */
    public void save(LsSignInRemind signInRemind) {
        signInRemindRepository.save(signInRemind);
    }

    /**
     * 批量保存用户课程
     *
     * */
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(List<LsSignInRemind> signInRemindList) {
        signInRemindRepository.saveAll(signInRemindList);
    }

    /**
     * 用户课程
     *
     * */
    @Transactional(rollbackFor = Exception.class)
    public List<LsSignInRemind> findByUserPhone(String phone,Long userId) {
        List<LsSignInRemind> remindList = signInRemindRepository.findByUserPhone(phone);
        //获取该用户本周签到、请假的课程
        //List<LsUserSignIn> userSignIns = userSignInRepository.findByUserId(userId);
        if(!CollectionUtils.isEmpty(remindList)){
            remindList.stream().forEach(item ->{

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
        return remindList;
    }

    /**
     * 获取详情
     *
     * */
    @Transactional(rollbackFor = Exception.class)
    public LsSignInRemind findById(String id) {
        Optional<LsSignInRemind> byId = signInRemindRepository.findById(Long.valueOf(id));
        if(byId==null){
            return null;
        }
        LsSignInRemind signInRemind = byId.get();
        return signInRemind;

    }
    /**
     * 获取详情
     *
     * */
    public long findNumByPhone(String phone) {
        return signInRemindRepository.findNumByPhone(phone);
    }
    /**
     * 修改已读状态
     *
     * */
    public LsSignInRemind remindAlreadySee(LsSignInRemind signInRemind,Long userId) {
        //修改已读状态
        signInRemind.setReadState(ReadStateEnum.READ.getCode());
        LsSignInRemind remind = signInRemindRepository.save(signInRemind);
        //获取该用户本周签到、请假的课程
        List<LsUserSignIn> userSignIns = userSignInRepository.findByUserId(userId);
        if(!CollectionUtils.isEmpty(userSignIns)){
                    userSignIns.stream().forEach(userSignIn ->{
                        if(remind.getUserClassId().equals(userSignIn.getUserClassId())){
                            String flag = userSignIn.getFlag();
                            if(OperationTypeEnum.SIGN_IN.getCode().equals(flag)){
                                remind.setFlag(SignInStateEnum.SIGN_IN.getCode());
                            }else{
                                remind.setFlag(SignInStateEnum.LEAVE.getCode());
                            }
                        }
                    });
                }else{
                    remind.setFlag(SignInStateEnum.NO_SIGN_IN_LEAVE.getCode());
                }
        return remind;
    }
}
