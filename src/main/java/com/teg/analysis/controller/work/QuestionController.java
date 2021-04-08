package com.teg.analysis.controller.work;

import com.teg.analysis.aop.Log;
import com.teg.analysis.exception.BusinessException;
import com.teg.analysis.model.DTO.WorkOrderDTO;
import com.teg.analysis.model.REQ.QuestionWorkOrderEditREQ;
import com.teg.analysis.model.REQ.WorkOrderQueryREQ;
import com.teg.analysis.model.common.Page;
import com.teg.analysis.model.common.Result;
import com.teg.analysis.service.WorkOrderService;
import com.teg.analysis.util.PageUtil;
import com.teg.analysis.util.ResultUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 问题工单
 *
 * @author wangyuan
 * @date 2020/11/20 9:54
 */

@RestController
@RequestMapping("work")
public class QuestionController {

    @Resource(name = "questionWorkOrderService")
    private WorkOrderService workOrderService;

    @RequestMapping(value = "question", method = RequestMethod.POST)
    @ResponseBody
    @Log(description = "获取问题工单列表")
    public Page<WorkOrderDTO> questionList(WorkOrderQueryREQ workOrderQueryREQ) {
        workOrderQueryREQ.setPage((workOrderQueryREQ.getPage() - 1) * workOrderQueryREQ.getPageSize());

        List list = workOrderService.list(workOrderQueryREQ);
        int total = workOrderService.totalCount(workOrderQueryREQ);
        return PageUtil.success(list, workOrderQueryREQ.getPage(), workOrderQueryREQ.getPageSize(), total);
    }


    @RequestMapping(value = "edit/question", method = RequestMethod.POST)
    @ResponseBody
    @Log(description = "更新问题工单")
    public Result editQuestionWorkOrder(QuestionWorkOrderEditREQ questionWorkOrderEditREQ) {
        if (questionWorkOrderEditREQ.getId() == null) {
            throw new BusinessException("工单id不能为空");
        }
        workOrderService.update(questionWorkOrderEditREQ);
        return ResultUtil.success();
    }


}
