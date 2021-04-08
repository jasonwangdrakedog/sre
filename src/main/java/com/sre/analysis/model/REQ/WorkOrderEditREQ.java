package com.sre.analysis.model.REQ;

import lombok.Data;

/**
 * @author wangyuan
 * @date 2020/8/18 14:06
 */
@Data
public class WorkOrderEditREQ {

    private Long id;

    private String threeLineAnalysis;

    private Integer threeLineStatus;

    private Boolean isFault;

    private String operatorName;

}
