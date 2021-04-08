package com.teg.analysis.model.DTO;

import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * @author wangyuan
 * @date 2020/8/14 11:28
 */
@Data
public class ComplainWorkOrderDTO extends WorkOrderDTO {

    public ComplainWorkOrderDTO(Map<String, String> tyDetailMap) {
        setDescript(tyDetailMap.get("describe"));
        setTyStatus(tyDetailMap.get("status"));

        if (!ObjectUtils.isEmpty(tyDetailMap.get("sheetid"))) {
            setWorkNo(tyDetailMap.get("sheetid"));
            setWorkNoDate(getWorkNoDate(tyDetailMap.get("workNoDate")));
        }
        setPriority(tyDetailMap.get("priority"));
        setBaseSummary(tyDetailMap.get("BaseSummary"));

        setSource(tyDetailMap.get("source"));

        setHappenTime(tyDetailMap.get("workNoDate"));
    }


    public ComplainWorkOrderDTO(Map<Integer, String> excelMap, Long timeStamp) {
        setWorkNo(excelMap.get(0));
        setBaseSummary(excelMap.get(2));
        setDescript(excelMap.get(3));
        setProtype(excelMap.get(4));
        setReasonClass(excelMap.get(8));
        setProblemReason(excelMap.get(11));
        setSolution(excelMap.get(12));
        setSysClass(excelMap.get(50));
        setPriority(excelMap.get(64));
        setTimeStamp(timeStamp);
        setThreeLineStatus(0);
        setTyStatus(excelMap.get(59));
        setWorkNoDate(getWorkNoDate(excelMap.get(33)));
        //todo
    }


    public String getWorkNoDate(String happenTime) {
        String[] asd = happenTime.substring(0, 10).split("-");
        String result = "";
        for (int i = 0; i < asd.length; i++) {
            result += asd[i];
        }
        return result;
    }

    public ComplainWorkOrderDTO() {
        super();
    }
}
