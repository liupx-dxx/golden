package com.github.binarywang.demo.wx.mp.service.manager;

import com.github.binarywang.demo.wx.mp.entity.advance.Advance;
import com.github.binarywang.demo.wx.mp.repository.advance.AdvanceRepository;
import com.github.binarywang.demo.wx.mp.utils.WeChatUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AdvanceService {
    AdvanceRepository advanceRepository;

    WeChatUtils weChatUtils;

    /**
     * 获取所有信息
     * @return
     */
    public List<Advance> findAll() {
        return advanceRepository.findAll();
    }

    /**
     * 获取单个信息
     * @return
     */
    public Advance findById(String id) {

        return advanceRepository.findById(Long.valueOf(id)).get();
    }

    /**
     * 分页获取所有信息
     * @return
     */
    public Page<Advance> findPage(Map<String, String> params, Pageable pageable) {
        return advanceRepository.findPage(params,pageable);
    }


    /**
     * 新增预约信息
     * @return
     */
    public void save(Advance entity) {
        entity.setCreateTime(LocalDateTime.now());
        advanceRepository.save(entity);
        //保存成功后告诉公众号有人预约报名
        //weChatUtils.getAccess_token();
    }
}
