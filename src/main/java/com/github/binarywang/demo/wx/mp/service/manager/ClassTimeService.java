package com.github.binarywang.demo.wx.mp.service.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.*;
import com.github.binarywang.demo.wx.mp.enums.ExamineStateEnum;
import com.github.binarywang.demo.wx.mp.enums.FeedbackStateEnum;
import com.github.binarywang.demo.wx.mp.enums.OperationTypeEnum;
import com.github.binarywang.demo.wx.mp.enums.ResultCodeEnum;
import com.github.binarywang.demo.wx.mp.repository.client.ClientUserRepository;
import com.github.binarywang.demo.wx.mp.repository.client.UserSignInRepository;
import com.github.binarywang.demo.wx.mp.repository.manager.ClassTimeRepository;
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
public class ClassTimeService {

    ClassTimeRepository classTimeRepository;

    /**
     * 根据课程ID查询该课程时间段
     *
     * */
    public List<LsClassTime> findById(String classId) {
        return classTimeRepository.findByclassId(Long.valueOf(classId));
    }
    /**
     * 根据周单位查询课程
     *
     * */
    public List<LsClassTime> findByWeek(String week) {
        return classTimeRepository.findByWeek(week);
    }
}
