package com.sre.analysis.enums;

/**
 * @author wangyuan
 * @date 2020/8/14 14:29
 */
public enum TyWorkTypeEnum {
    // QUESTION("questionWorkOrderService", "BOMC_PROBLEM", "2c90518456a32f3401570ecd46670286"),
    /*   BROKEN("brokenWorkOrderService", "ITOM_GD_MALFUNCTION", "2c90518455cec46f0155d8cd95950036"),*/
    COMPLAIN("complainWorkOrderService", "BOMC_CPT", "2c90518455cec46f0155d8cd2e420035"),
    ;


    TyWorkTypeEnum(String clazzName, String baseSchema, String baseId) {
        this.clazzName = clazzName;
        this.baseSchema = baseSchema;
        this.baseId = baseId;
    }

    private String clazzName;

    private String baseSchema;

    private String baseId;

    public String getClazzName() {
        return clazzName;
    }

    public String getBaseSchema() {
        return baseSchema;
    }

    public String getBaseId() {
        return baseId;
    }
}
