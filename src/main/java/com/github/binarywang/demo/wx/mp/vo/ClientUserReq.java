package com.github.binarywang.demo.wx.mp.vo;

import lombok.Getter;
import lombok.Setter;

public class ClientUserReq {
    @Getter@Setter
    private String newPassWd;
    @Getter@Setter
    private String oldPassWd;
}
