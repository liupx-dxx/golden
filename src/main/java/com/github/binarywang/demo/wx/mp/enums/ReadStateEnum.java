package com.github.binarywang.demo.wx.mp.enums;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public enum ReadStateEnum {

    /**
     *
     * */
    NO_READ("0", "未读"),
    READ("1", "已读");

    private String code;
    private String desc;

    private ReadStateEnum(String code, String desc) {
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
