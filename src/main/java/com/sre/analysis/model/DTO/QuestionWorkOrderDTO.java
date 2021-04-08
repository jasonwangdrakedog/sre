package com.sre.analysis.model.DTO;

import lombok.Data;

import java.util.Map;

/**
 * @author wangyuan
 * @date 2020/8/14 11:28
 */
@Data
public class QuestionWorkOrderDTO extends WorkOrderDTO {

    public QuestionWorkOrderDTO(Map<String, String> tyDetailMap) {
        super(tyDetailMap);
    }

    public QuestionWorkOrderDTO() {
        super();
    }
}
