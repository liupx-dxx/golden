package com.github.binarywang.demo.wx.mp.service.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.LsClass;
import com.github.binarywang.demo.wx.mp.entity.surce.LsTeacher;
import com.github.binarywang.demo.wx.mp.repository.manager.ClassManagerRepository;
import com.github.binarywang.demo.wx.mp.repository.manager.TeacherManagerRepository;
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
public class ClassManagerService {


    ClassManagerRepository classManagerRepository;

    TeacherManagerRepository teacherManagerRepository;

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
    public void save(LsClass lsClass) {
        Long teacherId = lsClass.getTeacherId();
        if(teacherId!=null){
            LsTeacher one = teacherManagerRepository.findOne(teacherId);
            if(one!=null){
                lsClass.setTeacherName(one.getTeacherName());
            }
        }
        lsClass.setCreateTime(LocalDateTime.now());
        classManagerRepository.save(lsClass);
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
        return byId.get();
    }
    /**
     *
     * 批量删除
     * */
    @Transactional
    public void delById(List<LsClass> lsClasses) {
        classManagerRepository.deleteAll(lsClasses);
    }
}
