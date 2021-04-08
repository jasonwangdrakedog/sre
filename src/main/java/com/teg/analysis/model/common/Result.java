package com.teg.analysis.model.common;

import lombok.Data;

/**
 * @author wangyuan
 * @date 2020/8/6 17:27
 */
@Data
public class Result<T> {

    private Boolean success;

    private String msg;

    private T data;


}
