package com.github.binarywang.demo.wx.mp.service.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.LsTeacher;
import com.github.binarywang.demo.wx.mp.repository.manager.TeacherManagerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeacherManagerService {


    TeacherManagerRepository teacherManagerRepository;

    /**
     * 分页获取所有信息
     * @return
     */
    public Page<LsTeacher> findPage(Map<String, String> params, Pageable pageable) {
        return teacherManagerRepository.findPage(params,pageable);
    }


    public List<LsTeacher> findAll() {
        return teacherManagerRepository.findAll();
    }


    /**
     * 新增课程
     *
     * */
    public void save(LsTeacher lsTeacher) {
        lsTeacher.setCreateTime(LocalDateTime.now());
        teacherManagerRepository.save(lsTeacher);
    }


    public void deleteById(Long id) {
        LsTeacher one = teacherManagerRepository.findOne(id);
        if(one!=null){
            teacherManagerRepository.delete(one);
        }
    }

    public LsTeacher findById(String id) {
        Optional<LsTeacher> byId = teacherManagerRepository.findById(Long.valueOf(id));
        if(byId==null){
            return null;
        }
        return byId.get();
    }
}
