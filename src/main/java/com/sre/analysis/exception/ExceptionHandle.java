package com.sre.analysis.exception;

import com.sre.analysis.model.common.Result;
import com.sre.analysis.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author wangyuan
 * @date 2020/8/7 16:22
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandle {


    /**
     * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionGet(Exception e) {
        if (e instanceof BusinessException) {
            BusinessException MyException = (BusinessException) e;
            log.error("【业务异常】{}", e);
            return ResultUtil.fail(MyException.getMessage());
        }

        log.error("【系统异常】{}", e);
        return ResultUtil.fail(ExceptionEnum.UNKNOW_ERROR);
    }
}