package com.sre.analysis.mapper;

import com.sre.analysis.model.DTO.QuestionWorkOrderDTO;
import com.sre.analysis.model.REQ.WorkOrderQueryREQ;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangyuan
 * @date 2020/8/17 10:19
 */
@Mapper
public interface QuestionWorkOrderMapper {


    void batchCreateQuestionWorkOrder(@Param("list") List<QuestionWorkOrderDTO> questionWorkOrderDTOS);


    List<QuestionWorkOrderDTO> list(WorkOrderQueryREQ workOrderQueryREQ);

    int totalCount(WorkOrderQueryREQ workOrderQueryREQ);
}
