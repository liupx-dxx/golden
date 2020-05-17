package com.github.binarywang.demo.wx.mp.service.manager;
import com.github.binarywang.demo.wx.mp.entity.surce.LsUserSignIn;

import com.github.binarywang.demo.wx.mp.enums.AgreeStateEnum;
import com.github.binarywang.demo.wx.mp.enums.ExamineStateEnum;
import com.github.binarywang.demo.wx.mp.repository.client.UserSignInRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserSignInService {

    UserSignInRepository userSignInRepository;

    /**
     * 分页获取所有信息
     * @return
     */
    public Page<LsUserSignIn> findPage(Map<String, String> params, Pageable pageable) {
        return userSignInRepository.findPage(params,pageable);
    }


    public List<LsUserSignIn> findAll() {
        return userSignInRepository.findAll();
    }


    /**
     * 新增用户课程
     *
     * */
    public void save(LsUserSignIn userSignIn) {

    }


    public void deleteById(String id) {
        LsUserSignIn one = userSignInRepository.findOne(Long.valueOf(id));
        if(one!=null){
            userSignInRepository.delete(one);
        }
    }

    public LsUserSignIn findById(String id) {
        Optional<LsUserSignIn> byId = userSignInRepository.findById(Long.valueOf(id));
        if(byId==null){
            return null;
        }
        return byId.get();
    }


    /**
     * 根据用户查询个人签到记录
     *
     * */
    public List<LsUserSignIn> findByUserPhone(String phone) {
        return userSignInRepository.findByUserPhone(phone);
    }


    /**
     *
     * 批量删除
     * */
    @Transactional(rollbackFor = Exception.class)
    public void delById(List<LsUserSignIn> userSignInList) {
        userSignInRepository.deleteAll(userSignInList);
    }

    /**
     *
     * 批量删除
     * */
    public void examine(LsUserSignIn signIn) {
        Long id = signIn.getId();
        LsUserSignIn userSignIn = userSignInRepository.findOne(id);
        //赋值审核结果和审核意见
        userSignIn.setAgreeState(signIn.getAgreeState());
        userSignIn.setExamineFlag(ExamineStateEnum.YES_EXAMINE.getCode());
        userSignIn.setExamineIdea(signIn.getExamineIdea());
        userSignIn.setExamineTime(LocalDateTime.now());
        userSignInRepository.save(userSignIn);
    }

    /**
     * 批量删除
     *
     * */
    @Transactional(rollbackFor = Exception.class)
    public void delByIds(List<LsUserSignIn> userSignInList) {
        userSignInRepository.deleteAll(userSignInList);
    }
}
