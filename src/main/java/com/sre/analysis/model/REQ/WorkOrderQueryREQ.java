package com.sre.analysis.model.REQ;

import lombok.Data;

import java.util.List;


/**
 * 工单查询请求参数
 */
@Data
public class WorkOrderQueryREQ {

    private String workNo;

    //工单类型 0 故障 1问题 2投诉
    private Integer workType;

    private String start;

    private String end;

    private Integer page;

    private Integer pageSize;

    private List<String> tyStatus;

    private Integer handled;//0 未处理过  1 以处理
}
