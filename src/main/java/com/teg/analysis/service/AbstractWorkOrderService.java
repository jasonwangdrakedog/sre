package com.teg.analysis.service;

import org.springframework.util.StringUtils;

/**
 * @author wangyuan
 * @date 2020/8/18 15:13
 */
public abstract class AbstractWorkOrderService {

    public Boolean fillIsFault(String problemReason, String questionBasis, String solution) {
        if (!StringUtils.isEmpty(problemReason) && problemReason.indexOf("误告警") >= 0
                && problemReason.indexOf("错误告警") == -1) {
            return Boolean.TRUE;  //一线预处理意见
        }
        if (!StringUtils.isEmpty(questionBasis) && questionBasis.indexOf("误告警") >= 0
                && questionBasis.indexOf("错误告警") == -1) {
            return Boolean.TRUE;  //一线预处理意见
        }
        if (!StringUtils.isEmpty(solution) && solution.indexOf("误告警") >= 0
                && solution.indexOf("错误告警") == -1) {
            return Boolean.TRUE;  //一线预处理意见
        }
        return Boolean.FALSE;
    }
}
