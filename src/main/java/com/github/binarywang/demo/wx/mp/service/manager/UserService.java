package com.github.binarywang.demo.wx.mp.service.manager;

import com.github.binarywang.demo.wx.mp.entity.sys.SysUser;
import com.github.binarywang.demo.wx.mp.repository.manager.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {


    UserRepository userRepository;


    public List<SysUser> findAll() {
        return userRepository.findAll();
    }


    public void save(SysUser sysUser) {
        //将密码加密
        String encode = Base64.getEncoder().encodeToString(sysUser.getPassword().getBytes());
        sysUser.setPassword(encode);
        userRepository.save(sysUser);
    }


    public SysUser getUser(String username) {
        return userRepository.findByName(username);
    }
}
