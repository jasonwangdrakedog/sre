package com.sre.analysis.model.report;

import lombok.Data;

import java.util.List;

/**
 * @author wangyuan
 * @date 2020/8/18 19:08
 */
@Data
public class Item {
    private String name;
    private int value;
    private String type;
    private List data;
    private int yAxisIndex;
    private Boolean smooth;
    private String seriesLayoutBy;
    private String id;

}
