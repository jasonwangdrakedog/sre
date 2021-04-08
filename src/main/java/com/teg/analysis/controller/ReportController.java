package com.teg.analysis.controller;


import com.teg.analysis.aop.Log;
import com.teg.analysis.model.common.Result;
import com.teg.analysis.model.report.EchartOption;
import com.teg.analysis.service.ReportService;
import com.teg.analysis.util.ResultUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wangyuan
 * @date 2020/8/13 9:07
 */
@RestController
public class ReportController {

    @Resource
    private ReportService reportService;


    @RequestMapping(value = "reports/bar", method = RequestMethod.GET)
    @ResponseBody
    @Log(description = "bar e-chart")
    public Result<EchartOption> bar() {
        return ResultUtil.success(reportService.generateBar1());
    }


    @RequestMapping(value = "reports/pie", method = RequestMethod.GET)
    @ResponseBody
    @Log(description = "pie e-chart")
    public Result<EchartOption> pie() {
        return ResultUtil.success(reportService.generatePie1());
    }


    /**
     * 按单号的时间分月份
     * 按produce first 作横坐标 为空的算在其他里
     * 按priority 为统计指标 为空的算其他里
     *
     * @return
     */
    @Deprecated
    @RequestMapping(value = "reports/test", method = RequestMethod.GET)
    @ResponseBody
    @Log(description = "echarts")
    public EchartOption testReport() {
        return reportService.testReport();
    }


}
