package com.sre.analysis.controller.work;

import com.sre.analysis.model.DTO.WorkOrderDTO;
import com.sre.analysis.aop.Log;
import com.sre.analysis.model.REQ.WorkOrderQueryREQ;
import com.sre.analysis.model.common.Page;
import com.sre.analysis.service.WorkOrderService;
import com.sre.analysis.util.PageUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 故障工单
 *
 * @author wangyuan
 * @date 2020/11/20 9:54
 */
@RestController
@RequestMapping("work")
public class BrokenController {

    @Resource(name = "brokenWorkOrderService")
    private WorkOrderService workOrderService;


    @RequestMapping(value = "break", method = RequestMethod.POST)
    @ResponseBody
    @Log(description = "获取故障工单列表")
    public Page<WorkOrderDTO> breakList(WorkOrderQueryREQ workOrderQueryREQ) {
        if (workOrderQueryREQ.getWorkType() == null) {
            workOrderQueryREQ.setWorkType(1);
        }
        workOrderQueryREQ.setPage((workOrderQueryREQ.getPage() - 1) * workOrderQueryREQ.getPageSize());

        List list = workOrderService.list(workOrderQueryREQ);
        int total = workOrderService.totalCount(workOrderQueryREQ);
        return PageUtil.success(list, workOrderQueryREQ.getPage(), workOrderQueryREQ.getPageSize(), total);
    }

}
