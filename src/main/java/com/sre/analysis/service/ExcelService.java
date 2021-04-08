package com.sre.analysis.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sre.analysis.model.DTO.AnalysisResultDTO;
import com.sre.analysis.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExcelService {

    @Value("${excel.write}")
    private String excelWriteUrl;


    public Map<Integer, Map<Integer, String>> readExcelFromUpload(MultipartFile file) {
        Map<Integer, Map<Integer, String>> map = Maps.newHashMap();
        try {
            map = ExcelUtils.readExcelFromUpload(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //excel数据存在map里，map.get(0).get(0)为excel第1行第1列的值，此处可对数据进行处理
        return map;
    }

    public Map<Integer, Map<Integer, String>> readExcelFromPath(String path) {
        log.info("读取文件地址:" + path);
        Map<Integer, Map<Integer, String>> map = Maps.newHashMap();
        try {
            map = ExcelUtils.readExcelFromPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 问题描述	来源	工单号	同一根因工单号	误告警	优先级	通用问题	工单标题/jira标题	jira链接	问题原因归类	IaaS产品	" 问题定位
     * （J类定位为产品问题需要填写）"	典型问题分类	质量属性	是否漏测	优化
     *
     * @param data
     * @throws Exception
     */
    public void exportAnalysisResult(List<AnalysisResultDTO> data) {
        //  System.out.println("export start:");
        List<String> titles = Lists.newLinkedList();
        titles.add("问题描述");
        titles.add("泰岳标题（辅助）");
        titles.add("泰岳描述（辅助）");
        titles.add("泰岳问题根本原因分析（辅助）");
        titles.add("泰岳解决方案（辅助）");
        titles.add("来源");
        titles.add("工单号");
        titles.add("同一根因工单号");
        titles.add("误告警");
        titles.add("优先级");
        titles.add("通用问题");
        titles.add("工单标题/jira标题");
        titles.add("jira链接");
        titles.add("问题原因归类");
        titles.add("IaaS产品");
        titles.add("问题定位");
        titles.add("典型问题分类");
        titles.add("质量属性");
        titles.add("是否漏测");
        titles.add("优化(非必填项)");
        List<List<String>> dataList = Lists.newLinkedList();
        data.forEach(analysisResultDTO -> {
            List<String> rowList = Lists.newLinkedList();
            rowList.add("");
            rowList.add(analysisResultDTO.getTyTitle());
            rowList.add(analysisResultDTO.getTyDesc());
            rowList.add(analysisResultDTO.getTyAnalysis());
            // rowList.add(analysisResultDTO.getTyAnalysis());
            rowList.add(analysisResultDTO.getTySolution());
            rowList.add("泰岳工单");
            rowList.add(analysisResultDTO.getWorkNo());
            rowList.add("");
            rowList.add(analysisResultDTO.getIsFault() ? "是" : "否");
            rowList.add(analysisResultDTO.getPriority());
            rowList.add(analysisResultDTO.getIsNormal() ? "是" : "否");
            rowList.add(analysisResultDTO.getTitle());
            rowList.add(analysisResultDTO.getJiraURL());
            rowList.add("");
            rowList.add(analysisResultDTO.getTeam());
            rowList.add("");
            rowList.add("");
            rowList.add("");
            rowList.add("");
            rowList.add("");
            dataList.add(rowList);
        });
        Workbook workbook = ExcelUtils.generateWorkbook(titles.toArray(new String[0]), dataList);
        createDropDownList(workbook.getSheet("sheet1"), dataList.size());
        ExcelUtils.export(excelWriteUrl, workbook);
        log.info("export end:");
    }


    /**
     * 创建下拉列表选项(单元格下拉框数据小于255字节时使用)
     *
     * @param sheet 所在Sheet页面
     */
    public static void createDropDownList(Sheet sheet, int dataSize) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        CellRangeAddressList addressList = new CellRangeAddressList(1, dataSize, 5, 5);
        String[] values = new String[]{"综合服务支撑系统", "泰岳工单", "实施", "工行", "日常运维"};
        DataValidationConstraint constraint = helper.createExplicitListConstraint(values);
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        sheet.addValidationData(dataValidation);


        CellRangeAddressList addressList2 = new CellRangeAddressList(1, dataSize, 8, 8);
        String[] values2 = new String[]{"是", "否"};
        DataValidationConstraint constraint2 = helper.createExplicitListConstraint(values2);
        DataValidation dataValidation2 = helper.createValidation(constraint2, addressList2);
        sheet.addValidationData(dataValidation2);

        CellRangeAddressList addressList3 = new CellRangeAddressList(1, dataSize, 9, 9);
        String[] values3 = new String[]{"高", "中", "低"};
        DataValidationConstraint constraint3 = helper.createExplicitListConstraint(values3);
        DataValidation dataValidation3 = helper.createValidation(constraint3, addressList3);
        sheet.addValidationData(dataValidation3);


        CellRangeAddressList addressList4 = new CellRangeAddressList(1, dataSize, 10, 10);
        DataValidation dataValidation4 = helper.createValidation(constraint2, addressList4);
        sheet.addValidationData(dataValidation4);


        CellRangeAddressList addressList5 = new CellRangeAddressList(1, dataSize, 13, 13);
        String[] values5 = new String[]{"IaaS产品", "厂商产品", "部署", "方案", "客户操作不当", "运维", "硬件", "业务波动", "原因未定位", "其他"};
        DataValidationConstraint constraint5 = helper.createExplicitListConstraint(values5);
        DataValidation dataValidation5 = helper.createValidation(constraint5, addressList5);
        sheet.addValidationData(dataValidation5);


        CellRangeAddressList addressList6 = new CellRangeAddressList(1, dataSize, 14, 14);
        String[] values6 = new String[]{"BC-EC", "BC-OP", "BC-EPC", "BC-DW/BC-CLM", "BC-SDS", "BC-Linux（镜像、qemu、libvirtd）",
                "UniDeploy/fuel", "中间件", "K8S平台", "安全子系统", "SDN"};
        DataValidationConstraint constraint6 = helper.createExplicitListConstraint(values6);
        DataValidation dataValidation6 = helper.createValidation(constraint6, addressList6);
        sheet.addValidationData(dataValidation6);


        CellRangeAddressList addressList7 = new CellRangeAddressList(1, dataSize, 15, 15);
        String[] values7 = new String[]{"文档(方案)", "代码", "架构", "部署工具", "其他"};
        DataValidationConstraint constraint7 = helper.createExplicitListConstraint(values7);
        DataValidation dataValidation7 = helper.createValidation(constraint7, addressList7);
        sheet.addValidationData(dataValidation7);


        CellRangeAddressList addressList8 = new CellRangeAddressList(1, dataSize, 16, 16);
        String[] values8 = new String[]{"订购", "退订", "数据不一致", "测试账号"};
        DataValidationConstraint constraint8 = helper.createExplicitListConstraint(values8);
        DataValidation dataValidation8 = helper.createValidation(constraint8, addressList8);
        sheet.addValidationData(dataValidation8);


        CellRangeAddressList addressList9 = new CellRangeAddressList(1, dataSize, 17, 17);
        String[] values9 = new String[]{"稳定性", "功能性", "性能性", "安全性", "可维护性", "兼容性"};
        DataValidationConstraint constraint9 = helper.createExplicitListConstraint(values9);
        DataValidation dataValidation9 = helper.createValidation(constraint9, addressList9);
        sheet.addValidationData(dataValidation9);


        CellRangeAddressList addressList10 = new CellRangeAddressList(1, dataSize, 18, 18);
        DataValidation dataValidation10 = helper.createValidation(constraint2, addressList10);
      /*  if (dataValidation10 instanceof HSSFDataValidation) {
            dataValidation10.setSuppressDropDownArrow(false);
        } else {
            dataValidation10.setSuppressDropDownArrow(true);
            dataValidation10.setShowErrorBox(true);
        }*/
        sheet.addValidationData(dataValidation10);
    }
}
