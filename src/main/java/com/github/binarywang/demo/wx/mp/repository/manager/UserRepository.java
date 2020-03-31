package com.github.binarywang.demo.wx.mp.repository.manager;

import com.github.binarywang.demo.wx.mp.entity.sys.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface UserRepository extends JpaRepository<SysUser,Long> , Serializable {

    //通过name查找用户
    SysUser findByName(String name);
}
