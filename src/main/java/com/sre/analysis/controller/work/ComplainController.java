package com.sre.analysis.controller.work;

import com.sre.analysis.model.DTO.WorkOrderDTO;
import com.sre.analysis.model.common.Page;
import com.sre.analysis.service.WorkOrderService;
import com.sre.analysis.util.PageUtil;
import com.sre.analysis.aop.Log;
import com.sre.analysis.model.REQ.WorkOrderQueryREQ;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 投诉工单
 *
 * @author wangyuan
 * @date 2020/11/20 9:54
 */
@RestController
@RequestMapping("work")
public class ComplainController {
    @Resource(name = "complainWorkOrderService")
    private WorkOrderService workOrderService;

    @RequestMapping(value = "complain", method = RequestMethod.POST)
    @ResponseBody
    @Log(description = "获取未分析投诉工单列表")
    public Page<WorkOrderDTO> complainList(WorkOrderQueryREQ workOrderQueryREQ) {
        workOrderQueryREQ.setPage((workOrderQueryREQ.getPage() - 1) * workOrderQueryREQ.getPageSize());

        List list = workOrderService.list4UnHandle(workOrderQueryREQ);
        int total = workOrderService.totalCount4UnHandle(workOrderQueryREQ);
        return PageUtil.success(list, workOrderQueryREQ.getPage(), workOrderQueryREQ.getPageSize(), total);
    }
}
