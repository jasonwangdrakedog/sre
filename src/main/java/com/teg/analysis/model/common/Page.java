package com.teg.analysis.model.common;

import lombok.Data;

import java.util.List;

/**
 * @author wangyuan
 * @date 2020/8/6 17:27
 */
@Data
public class Page<T> {
    private List<T> data;

    private String msg;

    private Integer page = 0;

    private Integer pageSize = 0;

    private Integer total = 0;

    private Boolean success;


}
