package com.teg.analysis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.teg.analysis.model.DTO.AnalysisResultDTO;
import com.teg.analysis.model.DTO.BrokenWorkOrderDTO;
import com.teg.analysis.model.REQ.BrokenWorkOrderEditREQ;
import com.teg.analysis.model.REQ.WorkOrderQueryREQ;
import com.teg.analysis.service.JIRAService;
import com.teg.analysis.service.WorkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * @author wangyuan
 * @date 2020/8/14 11:24
 */
@Service(value = "brokenWorkOrderService")
@Slf4j
public class BrokenWorkOrderServiceImpl implements WorkOrderService<BrokenWorkOrderDTO, BrokenWorkOrderEditREQ> {

    @Resource
    private JIRAService jiraService;

    @Override
    public int totalCount(WorkOrderQueryREQ workOrderQueryREQ) {
        return 0;
    }

    @Override
    public void update(BrokenWorkOrderEditREQ workOrder) {
        log.info("更新数据{}", JSONObject.toJSONString(workOrder));
    }

    @Override
    public List<BrokenWorkOrderDTO> list(WorkOrderQueryREQ workOrderQueryREQ) {
        return null;
    }

    @Override
    public void batchInsert(List<BrokenWorkOrderDTO> workOrderDTOS) {
        System.out.println("故障工单数量：" + workOrderDTOS.size());
    }


    /**
     * ty详情转工单
     *
     * @return
     */
    public BrokenWorkOrderDTO parseTYDetail(Map<String, String> tyDetailMap) {
        BrokenWorkOrderDTO workOrderDTO = new BrokenWorkOrderDTO();
        // workOrderDTO.setIsFault(fillIsFault(data));
        //  workOrderDTO.setIsNormal(fillIsNormal(tyDetailMap));
       /* fillJIRAInfo(data, workOrderDTO);
        fillPriority(data, workOrderDTO);
        fillTeam(data, workOrderDTO);*/
        // workOrderDTO.setTitle(String.valueOf(data.get(7)));
        //    workOrderDTO.setTyDesc(tyDetailMap.get("malfunctionDes"));
        //  workOrderDTO.setTyTitle(tyDetailMap.get("BaseSummary"));
        //  workOrderDTO.setTyAnalysis(tyDetailMap.get("malfunctionCause"));
        //   analysisResultDTO.setTyReason(tyDetailMap.get("malfunctionCause"));
        // workOrderDTO.setTySolution(tyDetailMap.get("solveScheme"));
        //   workOrderDTO.setWorkNo(String.valueOf(data.get(1)));
        return workOrderDTO;
    }


    private void fillPriority(Map<Integer, Object> data, AnalysisResultDTO analysisResultDTO) {
        //这里excel 有一个优先级  泰岳里也有一个 目前先直接取excel里的
        if (!StringUtils.isEmpty(data.get(22))) {
            analysisResultDTO.setPriority(String.valueOf(data.get(22)));
        }
    }

    private void fillTeam(Map<Integer, Object> data, AnalysisResultDTO analysisResultDTO) {
        if (!StringUtils.isEmpty(data.get(11))) {
            String str = String.valueOf(data.get(11));
            if (str.indexOf("OP") >= 0) {
                analysisResultDTO.setTeam("BC-OP");
            } else if (str.indexOf("SDN") >= 0) {
                analysisResultDTO.setTeam("SDN");
            } else if (str.indexOf("EC") >= 0) {
                analysisResultDTO.setTeam("BC-EC");
            } else if (str.indexOf("EBS") >= 0 || str.indexOf("ONEST") >= 0 || str.indexOf("NFS") >= 0) {
                analysisResultDTO.setTeam("BC-SDS");
            } else if (str.indexOf("DW") >= 0) {
                analysisResultDTO.setTeam("BC-DW/BC-CLM");
            }
        }
    }


    private void fillJIRAInfo(Map<Integer, Object> data, AnalysisResultDTO analysisResultDTO) throws URISyntaxException {
        if (!StringUtils.isEmpty(data.get(41)) && String.valueOf(data.get(41)).equals("是") &&
                !StringUtils.isEmpty(data.get(42))) {
            // 有关联问题单
            Map<String, String> jiraMap = jiraService.get2JIRA(String.valueOf(data.get(42)));
            if (!CollectionUtils.isEmpty(jiraMap)) {
                analysisResultDTO.setJiraURL(jiraMap.get("url"));
            }
        }
    }

    private Boolean fillIsNormal(Map<String, String> tyDetailMap) {
        String pooltype = tyDetailMap.get("pooltype");
        if (!StringUtils.isEmpty(pooltype) && pooltype.equals("共性问题")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private Boolean fillIsFault(Map<Integer, Object> data) {
        if (!StringUtils.isEmpty(data.get(40)) && String.valueOf(data.get(40)).indexOf("误告警") >= 0
                && String.valueOf(data.get(40)).indexOf("错误告警") == -1) {
            return Boolean.TRUE;  //一线预处理意见
        }
        if (!StringUtils.isEmpty(data.get(43)) && String.valueOf(data.get(43)).indexOf("误告警") >= 0
                && String.valueOf(data.get(43)).indexOf("错误告警") == -1) {
            return Boolean.TRUE;  //预处理结果
        }
        if (!StringUtils.isEmpty(data.get(44)) && String.valueOf(data.get(44)).indexOf("误告警") >= 0
                && String.valueOf(data.get(44)).indexOf("错误告警") == -1) {
            return Boolean.TRUE;  //预处理过程
        }
        if (!StringUtils.isEmpty(data.get(47)) && String.valueOf(data.get(47)).indexOf("误告警") >= 0
                && String.valueOf(data.get(47)).indexOf("错误告警") == -1) {
            return Boolean.TRUE;  //故障原因
        }
        if (!StringUtils.isEmpty(data.get(48)) && String.valueOf(data.get(48)).indexOf("误告警") >= 0
                && String.valueOf(data.get(48)).indexOf("错误告警") == -1) {
            return Boolean.TRUE;  //解决方案
        }
        if (!StringUtils.isEmpty(data.get(12)) && String.valueOf(data.get(12)).indexOf("误告警") >= 0
                && String.valueOf(data.get(12)).indexOf("错误告警") == -1) {
            return Boolean.TRUE;  //驳回退回记录
        }
        return Boolean.FALSE;
    }


    @Override
    public List<BrokenWorkOrderDTO> list4UnHandle(WorkOrderQueryREQ workOrderQueryREQ) {
        return null;
    }

    @Override
    public int totalCount4UnHandle(WorkOrderQueryREQ workOrderQueryREQ) {
        return 0;
    }
}
