package com.teg.analysis.util;

import com.teg.analysis.exception.ExceptionEnum;
import com.teg.analysis.model.common.Result;

/**
 * @author wangyuan
 * @date 2020/8/7 16:05
 */
public class ResultUtil {


    /**
     * 返回成功，传入返回体具体出參
     *
     * @param object
     * @return
     */
    public static Result success(Object object) {
        Result result = new Result();
        result.setSuccess(Boolean.TRUE);
        result.setData(object);
        return result;
    }

    /**
     * 提供给部分不需要出參的接口
     *
     * @return
     */
    public static Result success() {
        return success(null);
    }

    /**
     * 自定义错误信息
     *
     * @param msg
     * @return
     */
    public static Result fail(String msg) {
        Result result = new Result();
        result.setSuccess(Boolean.FALSE);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    /**
     * 返回异常信息，在已知的范围内
     *
     * @param exceptionEnum
     * @return
     */
    public static Result fail(ExceptionEnum exceptionEnum) {
        Result result = new Result();
        result.setSuccess(Boolean.FALSE);
        result.setMsg(exceptionEnum.getMsg());
        result.setData(null);
        return result;
    }

}
