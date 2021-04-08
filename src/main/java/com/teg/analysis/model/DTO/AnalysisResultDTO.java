package com.teg.analysis.model.DTO;

import lombok.Data;

@Data
public class AnalysisResultDTO {
    /**
     * 问题描述
     */
    private String desc;

    /**
     * 工单号
     */
    private String workNo;

    /**
     * 是否误告警
     */
    private Boolean isFault;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 是否通用问题
     */
    private Boolean isNormal;

    /**
     * 工单标题/jira标题
     */
    private String title;

    /**
     * jira链接
     */
    private String jiraURL;

    /**
     * 泰岳标题
     */
    private String tyTitle;

    /**
     * 泰岳描述
     */
    private String tyDesc;


    /**
     * 泰岳问题根本原因分析
     */
    private String tyAnalysis;

    /**
     * 泰岳问题依据
     */
    private String tyReason;

    /**
     * 泰岳解决方案
     */
    private String tySolution;

    /**
     * 处理组
     */
    private String team;
}
