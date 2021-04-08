package com.teg.analysis.service;

import com.teg.analysis.model.DTO.WorkOrderDTO;
import com.teg.analysis.model.REQ.WorkOrderQueryREQ;

import java.util.List;
import java.util.Map;

/**
 * @author wangyuan
 * @date 2020/8/14 11:23
 */

public interface WorkOrderService<T,K> {

    WorkOrderDTO parseTYDetail(Map<String, String> tyDetailMap);

    void batchInsert(List<T> workOrderDTOS);

    List<T> list(WorkOrderQueryREQ workOrderQueryREQ);

    List<T> list4UnHandle(WorkOrderQueryREQ workOrderQueryREQ);

    int totalCount(WorkOrderQueryREQ workOrderQueryREQ);

    int totalCount4UnHandle(WorkOrderQueryREQ workOrderQueryREQ);

    void update(K workOrder);
}
