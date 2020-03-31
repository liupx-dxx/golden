package com.github.binarywang.demo.wx.mp.vo;

import com.github.binarywang.demo.wx.mp.entity.surce.LsUserClass;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class UserClassReq {

    @Getter@Setter
    private List<LsUserClass> userClassList;
    @Getter@Setter
    private String phone;
    @Getter@Setter
    private String userName;
}
