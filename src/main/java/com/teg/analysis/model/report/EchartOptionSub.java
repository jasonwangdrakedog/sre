package com.teg.analysis.model.report;

import lombok.Data;

import java.util.List;

/**
 * @author wangyuan
 * @date 2020/8/18 19:02
 */
@Data
public class EchartOptionSub {

    private EchartOptionTitle title;
    private List<EchartOptionSeries> series;
}
