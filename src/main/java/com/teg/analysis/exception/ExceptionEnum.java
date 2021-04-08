package com.teg.analysis.exception;

/**
 * @author wangyuan
 * @date 2020/8/7 16:04
 */
public enum ExceptionEnum {

    UNKNOW_ERROR(-1, "未知错误"),
    USER_NOT_FIND(-101, "用户不存在"),
    ;
    private Integer code;

    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
