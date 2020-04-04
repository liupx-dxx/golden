package com.github.binarywang.demo.wx.mp.enums;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public enum AgreeStateEnum {

    /**
     *
     * */
    AGREE("0", "同意"),
    NO_AGREE("1", "不同意");

    private String code;
    private String desc;

    private AgreeStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
