package com.teg.analysis.model.DTO;

import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.Map;

@Data
public class WorkOrderDTO {


    private Long id;


    private Date createDate;

    private Date updateDate;

    /**
     * 问题描述
     */
    private String descript;
    /**
     * 泰岳工单状态
     */
    private String tyStatus;


    /**
     * 工单号
     */
    private String workNo;

    /**
     * 工单号上的时间
     */
    private String workNoDate;


    /**
     * 优先级
     */
    private String priority;


    /**
     * 工单标题
     */
    private String baseSummary;

    /**
     * 问题来源
     */
    private String source;

    /**
     * 问题类型
     */
    private String protype;

    /**
     * 资源池类型
     */
    private String poolType;

    /**
     * 系统分类1级
     */
    private String sysClass;

    /**
     * 系统分类2级
     */
    private String sysClassSecond;

    /**
     * 发生时间
     */
    private String happenTime;

    /**
     * 产品分类1级
     */
    private String produceFirst;

    /**
     * 产品分类2级
     */
    private String produceSecond;

    /**
     * 原因分类1级
     */
    private String reasonClass;

    /**
     * 原因分类2级
     */
    private String reasonClassSecond;

    /**
     * 计划完成时间
     */
    private String planOver;


    /**
     * 泰岳问题根本原因分析
     */
    private String problemReason;

    /**
     * 泰岳问题依据
     */
    private String questionBasis;

    /**
     * 泰岳解决方案
     */
    private String solution;

    /**
     * 处理组
     */
    private String dealGroup;

    /**
     * 三线分析状态
     * 0 未分析 1 分析中 2 分析完毕
     */
    private Integer threeLineStatus = 0;

    private Boolean isFault;

    private Long timeStamp;


    public WorkOrderDTO(Map<String, String> tyDetailMap) {
        this.descript = tyDetailMap.get("descript");
        this.tyStatus = tyDetailMap.get("status");


        if (!ObjectUtils.isEmpty(tyDetailMap.get("id"))) {
            this.workNo = tyDetailMap.get("id");
            this.workNoDate = tyDetailMap.get("id").split("-")[1];
        }
        this.priority = tyDetailMap.get("priority");
        ;
        this.baseSummary = tyDetailMap.get("BaseSummary");
        ;
        this.source = tyDetailMap.get("source");
        ;
        this.protype = tyDetailMap.get("protype");
        ;
        this.poolType = tyDetailMap.get("pooltype");
        ;
        this.sysClass = tyDetailMap.get("sysclass");
        ;
        this.sysClassSecond = tyDetailMap.get("sysclass2");
        ;
        this.happenTime = tyDetailMap.get("happentime");
        ;
        this.produceFirst = tyDetailMap.get("produce1");
        ;
        this.produceSecond = tyDetailMap.get("produce2");
        ;
        this.reasonClass = tyDetailMap.get("reasonClass");
        ;
        this.reasonClassSecond = tyDetailMap.get("reasonClass2");
        ;
        this.planOver = tyDetailMap.get("planOver");
        ;
        this.problemReason = tyDetailMap.get("problemReason");
        ;
        this.questionBasis = tyDetailMap.get("questionBasis");
        ;
        this.solution = tyDetailMap.get("solution");
        ;
        this.dealGroup = tyDetailMap.get("dealGroup");
        ;
    }

    public WorkOrderDTO() {
    }
}
