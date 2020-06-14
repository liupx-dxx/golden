package com.github.binarywang.demo.wx.mp.service.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.LsClass;
import com.github.binarywang.demo.wx.mp.entity.surce.LsClassTime;
import com.github.binarywang.demo.wx.mp.entity.surce.LsTeacher;
import com.github.binarywang.demo.wx.mp.repository.manager.ClassManagerRepository;
import com.github.binarywang.demo.wx.mp.repository.manager.ClassTimeRepository;
import com.github.binarywang.demo.wx.mp.repository.manager.TeacherManagerRepository;
import com.github.binarywang.demo.wx.mp.utils.UpdateToolUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.binarywang.demo.wx.mp.entity.surce.QLsClassTime.lsClassTime;

@Service
@AllArgsConstructor
public class ClassManagerService {


    ClassManagerRepository classManagerRepository;

    TeacherManagerRepository teacherManagerRepository;

    ClassTimeRepository classTimeRepository;

    /**
     * 分页获取所有信息
     * @return
     */
    public Page<LsClass> findPage(Map<String, String> params, Pageable pageable) {
        return classManagerRepository.findPage(params,pageable);
    }


    public List<LsClass> findAll() {
        return classManagerRepository.findAll();
    }


    /**
     * 新增课程
     *
     * */
    @Transactional(rollbackFor = Exception.class)
    public void save(LsClass lsClass) {
        if(StringUtils.isEmpty(lsClass.getId())){
            lsClass.setCreateTime(LocalDateTime.now());
        }else{
            LsClass entity = classManagerRepository.findOne(lsClass.getId());
            UpdateToolUtil.copyNullProperties(entity,lsClass);
        }

        LsClass classEntity = classManagerRepository.save(lsClass);
        List<LsClassTime> timeList = classTimeRepository.findByclassId(classEntity.getId());
        if(!CollectionUtils.isEmpty(timeList)){
            //如果原先存在 就把之前的删除
            classTimeRepository.deleteAll(timeList);
        }
        //增加课程上课时间
        List<LsClassTime> classTimes = lsClass.getClassTimes();
        if(!CollectionUtils.isEmpty(classTimes)){
            classTimes.stream().forEach(item ->{
                LsClassTime lsClassTime = new LsClassTime();
                lsClassTime.setId(item.getId());
                lsClassTime.setClassId(classEntity.getId());
                lsClassTime.setWeek(item.getWeek());
                lsClassTime.setStartTime(item.getStartTime());
                lsClassTime.setEndTime(item.getEndTime());
                classTimeRepository.save(lsClassTime);
            });
        }
    }


    public void deleteById(Long id) {
        LsClass one = classManagerRepository.findOne(id);
        if(one!=null){
            classManagerRepository.delete(one);
        }
    }

    public LsClass findById(String id) {
        Optional<LsClass> byId = classManagerRepository.findById(Long.valueOf(id));
        if(byId==null){
            return null;
        }
        LsClass lsClass = byId.get();
        if(lsClass!=null){
            Long classId = lsClass.getId();
            List<LsClassTime> timeList = classTimeRepository.findByclassId(classId);
            lsClass.setClassTimes(timeList);
        }
        return lsClass;
    }
    /**
     *
     * 批量删除
     * */
    @Transactional(rollbackFor = Exception.class)
    public void delById(List<LsClass> lsClasses) {
        classManagerRepository.deleteAll(lsClasses);
    }

    public List<LsClass> findByIds(List<Long> classIds) {
        return classManagerRepository.findByIds(classIds);
    }
}
