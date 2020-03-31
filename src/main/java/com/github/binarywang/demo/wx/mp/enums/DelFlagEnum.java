package com.github.binarywang.demo.wx.mp.enums;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public enum DelFlagEnum {
    DELETE("0", "已删除"),
    NORMAL("1", "正常");

    private String code;
    private String desc;

    private DelFlagEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DelFlagEnum codeOf(String code) {
        byte var2 = -1;
        switch(code.hashCode()) {
            case 48:
                if (code.equals("0")) {
                    var2 = 0;
                }
                break;
            case 49:
                if (code.equals("1")) {
                    var2 = 1;
                }
        }

        switch(var2) {
            case 0:
                return DELETE;
            case 1:
                return NORMAL;
            default:
                return null;
        }
    }

    public static DelFlagEnum descOf(String desc) {
        byte var2 = -1;
        switch(desc.hashCode()) {
            case 876341:
                if (desc.equals("正常")) {
                    var2 = 1;
                }
                break;
            case 23802294:
                if (desc.equals("已删除")) {
                    var2 = 0;
                }
        }

        switch(var2) {
            case 0:
                return DELETE;
            case 1:
                return NORMAL;
            default:
                return null;
        }
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
