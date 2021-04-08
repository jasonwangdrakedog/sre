package com.sre.analysis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sre.analysis.model.DTO.WorkOrderDTO;
import com.sre.analysis.service.WorkOrderService;
import com.sre.analysis.mapper.ComplainWorkOrderMapper;
import com.sre.analysis.model.DTO.ComplainWorkOrderDTO;
import com.sre.analysis.model.REQ.ComplainWorkOrderEditREQ;
import com.sre.analysis.model.REQ.WorkOrderQueryREQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author wangyuan
 * @date 2020/8/14 14:35
 */
@Service(value = "complainWorkOrderService")
@Slf4j
public class ComplainWorkOrderServiceImpl implements WorkOrderService<ComplainWorkOrderDTO, ComplainWorkOrderEditREQ> {

    @Resource
    private ComplainWorkOrderMapper complainWorkOrderMapper;

    @Override
    public void update(ComplainWorkOrderEditREQ workOrder) {
        log.info("更新数据{}", JSONObject.toJSONString(workOrder));
    }

    @Override
    public List<ComplainWorkOrderDTO> list4UnHandle(WorkOrderQueryREQ workOrderQueryREQ) {
        List<ComplainWorkOrderDTO> questionWorkOrderDTOS = complainWorkOrderMapper.list4UnHandle(workOrderQueryREQ);
        return questionWorkOrderDTOS;
    }


    @Override
    public List<ComplainWorkOrderDTO> list(WorkOrderQueryREQ workOrderQueryREQ) {
        List<ComplainWorkOrderDTO> questionWorkOrderDTOS = complainWorkOrderMapper.list(workOrderQueryREQ);
        //questionWorkOrderDTOS.forEach(questionWorkOrderDTO -> questionWorkOrderDTO.setIsFault(this.fillIsFault(questionWorkOrderDTO.getProblemReason(), questionWorkOrderDTO.getQuestionBasis(), questionWorkOrderDTO.getSolution())));
        return questionWorkOrderDTOS;
    }

    @Override
    public WorkOrderDTO parseTYDetail(Map<String, String> tyDetailMap) {
        ComplainWorkOrderDTO workOrderDTO = new ComplainWorkOrderDTO(tyDetailMap);
        return workOrderDTO;
    }

    @Override
    public void batchInsert(List<ComplainWorkOrderDTO> workOrderDTOS) {
        System.out.println("投诉工单数量：" + workOrderDTOS.size());
        if (!CollectionUtils.isEmpty(workOrderDTOS)) {
            complainWorkOrderMapper.batchCreateComplainWorkOrder(workOrderDTOS);
        }
    }

    @Override
    public int totalCount(WorkOrderQueryREQ workOrderQueryREQ) {
        return complainWorkOrderMapper.totalCount(workOrderQueryREQ);
    }

    @Override
    public int totalCount4UnHandle(WorkOrderQueryREQ workOrderQueryREQ) {
        return complainWorkOrderMapper.count4UnHandle(workOrderQueryREQ);
    }
}
