package com.github.binarywang.demo.wx.mp.enums;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public enum ExamineStateEnum {

    /**
     * 课程类型  1、班课  2、小组课 3、一对一
     * */
    UN_EXAMINE("0", "未审核"),
    YES_EXAMINE("1", "已审核"),
    NO_EXAMINE("2", "不用审核");

    private String code;
    private String desc;

    private ExamineStateEnum(String code, String desc) {
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
