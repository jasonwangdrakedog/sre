package com.sre.analysis.mapper;

import com.sre.analysis.model.REQ.WorkOrderQueryREQ;
import com.sre.analysis.model.DTO.ComplainWorkOrderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangyuan
 * @date 2020/8/17 10:19
 */
@Mapper
public interface ComplainWorkOrderMapper {


    void batchCreateComplainWorkOrder(@Param("list") List<ComplainWorkOrderDTO> complainWorkOrderDTOS);


    List<ComplainWorkOrderDTO> list(WorkOrderQueryREQ workOrderQueryREQ);

    int totalCount(WorkOrderQueryREQ workOrderQueryREQ);

    List<ComplainWorkOrderDTO> list4UnHandle(WorkOrderQueryREQ workOrderQueryREQ);

    int count4UnHandle(WorkOrderQueryREQ workOrderQueryREQ);
}
