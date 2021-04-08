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
