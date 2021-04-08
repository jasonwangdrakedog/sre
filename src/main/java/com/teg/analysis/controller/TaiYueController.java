package com.teg.analysis.controller;


import com.google.common.collect.Lists;
import com.teg.analysis.aop.Log;
import com.teg.analysis.model.DTO.AnalysisResultDTO;
import com.teg.analysis.model.REQ.WorkOrderQueryREQ;
import com.teg.analysis.model.common.Page;
import com.teg.analysis.service.TaiYueService;
import com.teg.analysis.util.PageUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("ty")
public class TaiYueController {


    @Resource
    private TaiYueService taiYueService;

   /* @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    @Log(description = "获取工单列表")
    public Page<WorkOrderDTO> listWorkOrder(WorkOrderQueryREQ workOrderQueryREQ) {
        List<WorkOrderDTO> list = taiYueService.findWorkOrder(workOrderQueryREQ);
        Page<WorkOrderDTO> page = PageUtil.success(list);
        return page;
    }
*/

    @RequestMapping(value = "test", method = RequestMethod.POST)
    @ResponseBody
    @Log(description = "获取工单列表")
    public Page<AnalysisResultDTO> test(WorkOrderQueryREQ workOrderQueryREQ)  {
        Map<String, String> list = taiYueService.post2TYInfo(workOrderQueryREQ.getWorkNo());
        AnalysisResultDTO analysisResultDTO = new AnalysisResultDTO();
        analysisResultDTO.setIsFault(false);
        analysisResultDTO.setTeam(list.get("baseSchema"));
        analysisResultDTO.setWorkNo(workOrderQueryREQ.getWorkNo());
        List<AnalysisResultDTO> list1 = Lists.newArrayList();
        list1.add(analysisResultDTO);
        return PageUtil.success(list1);
    }
}
