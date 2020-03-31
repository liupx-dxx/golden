package com.github.binarywang.demo.wx.mp.enums;

public enum ResultCodeEnum {
    SUCCESS(2000, "操作成功"),
    OPERATE_FAIL(4000, "操作失败"),
    CAPTCHA_ERROR(4001, "验证码出错"),
    PASSWORD_ERROR(4002, "用户名或密码错误"),
    PARAMETER_ERROR(4003, "参数不合法"),
    INTERNAL_ERROR(5000, "后台逻辑错误"),
    TORKEN_ERROR(5001, "令牌失效或过期"),
    UNKNOWN_ERROR(5009, "未知错误");

    private int code;
    private String msg;

    private ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean is2xxxSuccessful() {
        return ResultCodeEnum.CodeSeries.SUCCESSFUL.equals(this.series());
    }

    public boolean is4xxxClientError() {
        return ResultCodeEnum.CodeSeries.CLIENT_ERROR.equals(this.series());
    }

    public boolean is5xxxServerError() {
        return ResultCodeEnum.CodeSeries.SERVER_ERROR.equals(this.series());
    }

    public ResultCodeEnum.CodeSeries series() {
        return ResultCodeEnum.CodeSeries.valueOf(this);
    }

    public static ResultCodeEnum valueOf(int code) {
        ResultCodeEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ResultCodeEnum status = var1[var3];
            if (status.code == code) {
                return status;
            }
        }

        throw new IllegalArgumentException("No matching constant for [" + code + "]");
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static enum CodeSeries {
        SUCCESSFUL(2),
        CLIENT_ERROR(4),
        SERVER_ERROR(5);

        private final int value;

        private CodeSeries(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static ResultCodeEnum.CodeSeries valueOf(int status) {
            int seriesCode = status / 1000;
            ResultCodeEnum.CodeSeries[] var2 = values();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                ResultCodeEnum.CodeSeries series = var2[var4];
                if (series.value == seriesCode) {
                    return series;
                }
            }

            throw new IllegalArgumentException("No matching constant for [" + status + "]");
        }

        public static ResultCodeEnum.CodeSeries valueOf(ResultCodeEnum status) {
            return valueOf(status.code);
        }
    }
}
