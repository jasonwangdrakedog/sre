package com.sre.analysis.model.report;

import lombok.Data;

import java.util.List;

/**
 * @author wangyuan
 * @date 2020/8/18 18:58
 */
@Data
public class EchartOption {

    private List<EchartOptionSub> echartOptionSubList;

    private List<String> legendData;
    private List<String> xAxisData;
    private List<String> timeLindeData;
    private List<Item> series;


}
