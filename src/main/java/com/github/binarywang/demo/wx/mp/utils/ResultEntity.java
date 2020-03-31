package com.github.binarywang.demo.wx.mp.utils;

import com.github.binarywang.demo.wx.mp.enums.ResultCodeEnum;

public class ResultEntity<T> {
    private int code;
    private String msg;
    private T data;

    public ResultEntity() {
    }

    public ResultEntity(ResultCodeEnum resultCode, T data) {
        this(resultCode);
        this.data = data;
    }

    public ResultEntity(ResultCodeEnum resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(final T data) {
        this.data = data;
    }
}
