package com.github.binarywang.demo.wx.mp.enums;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public enum FeedbackStateEnum {

    /**
     * 课程类型  1、班课  2、小组课 3、一对一
     * */
    NO_FEEDBACK("0", "未反馈"),
    YES_FEEDBACK("1", "已反馈");

    private String code;
    private String desc;

    private FeedbackStateEnum(String code, String desc) {
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
