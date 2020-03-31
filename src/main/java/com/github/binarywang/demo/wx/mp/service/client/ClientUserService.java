package com.github.binarywang.demo.wx.mp.service.client;

import com.github.binarywang.demo.wx.mp.entity.surce.LsClientUser;
import com.github.binarywang.demo.wx.mp.repository.client.ClientUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientUserService {


    ClientUserRepository userRepository;


    public List<LsClientUser> findAll() {
        return userRepository.findAll();
    }


    /**
     * 新增用户   初始密码为 123456
     *
     * */
    public void save(LsClientUser clientUser) {
        String password = "123456";
        //将密码加密
        String encode = Base64.getEncoder().encodeToString(password.getBytes());
        clientUser.setPassword(encode);
        clientUser.setCreateTime(LocalDateTime.now());
        userRepository.save(clientUser);
    }


    public LsClientUser findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public LsClientUser findById(Long id) {
        Optional<LsClientUser> byId = userRepository.findById(id);
        if(byId==null){
            return null;
        }
        return byId.get();
    }
}
