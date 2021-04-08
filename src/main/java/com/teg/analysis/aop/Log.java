package com.teg.analysis.aop;

import java.lang.annotation.*;

/**
 * @author wangyuan
 * @date 2020/8/6 17:15
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Log {


    /**
     * 日志描述信息
     *
     * @return
     */
    String description() default "";

}
