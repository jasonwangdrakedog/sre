package com.sre.analysis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sre.analysis.mapper.QuestionWorkOrderMapper;
import com.sre.analysis.model.DTO.QuestionWorkOrderDTO;
import com.sre.analysis.model.DTO.WorkOrderDTO;
import com.sre.analysis.model.REQ.QuestionWorkOrderEditREQ;
import com.sre.analysis.service.WorkOrderService;
import com.sre.analysis.model.REQ.WorkOrderQueryREQ;
import com.sre.analysis.service.AbstractWorkOrderService;
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
@Service(value = "questionWorkOrderService")
@Slf4j
public class QuestionWorkOrderServiceImpl extends AbstractWorkOrderService
        implements WorkOrderService<QuestionWorkOrderDTO, QuestionWorkOrderEditREQ> {

    @Resource
    private QuestionWorkOrderMapper questionWorkOrderMapper;

    @Override
    public void update(QuestionWorkOrderEditREQ workOrder) {
        log.info("更新数据{}", JSONObject.toJSONString(workOrder));
    }

    @Override
    public WorkOrderDTO parseTYDetail(Map<String, String> tyDetailMap) {
        QuestionWorkOrderDTO workOrderDTO = new QuestionWorkOrderDTO(tyDetailMap);
        return workOrderDTO;
    }

    @Override
    public List<QuestionWorkOrderDTO> list(WorkOrderQueryREQ workOrderQueryREQ) {
        List<QuestionWorkOrderDTO> questionWorkOrderDTOS = questionWorkOrderMapper.list(workOrderQueryREQ);
        questionWorkOrderDTOS.forEach(questionWorkOrderDTO -> questionWorkOrderDTO.setIsFault(this.fillIsFault(questionWorkOrderDTO.getProblemReason(), questionWorkOrderDTO.getQuestionBasis(), questionWorkOrderDTO.getSolution())));
        return questionWorkOrderDTOS;
    }

    @Override
    public int totalCount(WorkOrderQueryREQ workOrderQueryREQ) {
        System.out.println(JSONObject.toJSONString(workOrderQueryREQ));
        return questionWorkOrderMapper.totalCount(workOrderQueryREQ);
    }

    @Override
    public void batchInsert(List<QuestionWorkOrderDTO> workOrderDTOS) {
        log.info("问题工单数量：" + workOrderDTOS.size());
        if (!CollectionUtils.isEmpty(workOrderDTOS)) {
            questionWorkOrderMapper.batchCreateQuestionWorkOrder(workOrderDTOS);
        }
    }


    @Override
    public List<QuestionWorkOrderDTO> list4UnHandle(WorkOrderQueryREQ workOrderQueryREQ) {
        return null;
    }

    @Override
    public int totalCount4UnHandle(WorkOrderQueryREQ workOrderQueryREQ) {
        return 0;
    }
}
