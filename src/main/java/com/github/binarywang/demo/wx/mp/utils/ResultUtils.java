package com.github.binarywang.demo.wx.mp.utils;

import com.github.binarywang.demo.wx.mp.enums.ResultCodeEnum;

public class ResultUtils {
    public ResultUtils() {
    }

    public static ResultEntity success() {
        return new ResultEntity(ResultCodeEnum.SUCCESS, (Object)null);
    }

    public static ResultEntity success(Object data) {
        return new ResultEntity(ResultCodeEnum.SUCCESS, data);
    }

    public static ResultEntity fail(ResultCodeEnum resultCode, String msg) {
        ResultEntity<Object> result = new ResultEntity(resultCode);
        result.setMsg(msg);
        return result;
    }

    public static ResultEntity fail(ResultCodeEnum resultCode) {
        return new ResultEntity(resultCode);
    }
}
