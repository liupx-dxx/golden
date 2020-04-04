package com.github.binarywang.demo.wx.mp.enums;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public enum OperationTypeEnum {

    /**
     * 课程类型  1、班课  2、小组课 3、一对一
     * */
    SIGN_IN("0", "已签到"),
    LEAVE("1", "已请假");

    private String code;
    private String desc;

    private OperationTypeEnum(String code, String desc) {
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
