package com.sre.analysis.util;

import com.sre.analysis.exception.ExceptionEnum;
import com.sre.analysis.model.common.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyuan
 * @date 2020/8/7 16:05
 */
public class PageUtil {


    /**
     * 返回成功，传入返回体具体出參
     *
     * @param object
     * @return
     */
    public static Page success(List<Object> object, int page, int pageSize, int total) {
        Page pageResult = new Page();
        pageResult.setSuccess(Boolean.TRUE);
        pageResult.setData(object);
        pageResult.setPage(page);
        pageResult.setPageSize(pageSize);
        pageResult.setTotal(total);
        return pageResult;
    }

    public static Page success(List object) {
        Page pageResult = new Page();
        pageResult.setSuccess(Boolean.TRUE);
        pageResult.setData(object);
        return pageResult;
    }

    /**
     * 提供给部分不需要出參的接口
     *
     * @return
     */
    public static Page success() {
        return success(new ArrayList<>());
    }

    /**
     * 自定义错误信息
     *
     * @param msg
     * @return
     */
    public static Page fail(String msg) {
        Page result = new Page();
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
    public static Page error(ExceptionEnum exceptionEnum) {
        Page result = new Page();
        result.setSuccess(Boolean.FALSE);
        result.setMsg(exceptionEnum.getMsg());
        result.setData(null);
        return result;
    }

}
