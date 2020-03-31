package com.github.binarywang.demo.wx.mp.enums;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public enum ClassTypeEnum {

    /**
     * 课程类型  1、班课  2、小组课 3、一对一
     * */
    CLASS("1", "班课"),
    GROUP_COURSE("2", "小组课"),
    ONE_ON_ONE("3", "一对一");

    private String code;
    private String desc;

    private ClassTypeEnum(String code, String desc) {
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
