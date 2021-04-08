package com.sre.analysis.model.report;

import lombok.Data;

/**
 * @author wangyuan
 * @date 2020/8/18 18:58
 */
@Data
public class EchartOptionTitle {
    private String text;


    public EchartOptionTitle(String text) {
        this.text = text;
    }
}
