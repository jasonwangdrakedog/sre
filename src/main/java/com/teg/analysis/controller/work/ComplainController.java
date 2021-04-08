package com.teg.analysis.controller.work;

import com.teg.analysis.aop.Log;
import com.teg.analysis.model.DTO.WorkOrderDTO;
import com.teg.analysis.model.REQ.WorkOrderQueryREQ;
import com.teg.analysis.model.common.Page;
import com.teg.analysis.service.WorkOrderService;
import com.teg.analysis.util.PageUtil;
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
