package com.github.binarywang.demo.wx.mp.enums;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public enum SignInStateEnum {

    /**
     *
     * */
    NO_SIGN_IN_LEAVE("0", "未签到未请假"),
    SIGN_IN("1", "已签到"),
    LEAVE("2", "已请假");

    private String code;
    private String desc;

    private SignInStateEnum(String code, String desc) {
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
