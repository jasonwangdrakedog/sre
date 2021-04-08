package com.teg.analysis.service;


import com.google.common.collect.Lists;
import com.teg.analysis.util.HtmlParserUtil;
import com.teg.analysis.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class TaiYueService {

    @Value("${ty.cookie}")
    private String tyCookie;
    /**
     * 根据工单号查询对应的baseId
     */
    private final static String URL = "http://172.20.42.86:58088/ultrabpp/sheet/baseInfoQuery.action";

    /**
     * 泰岳工单搜索详情
     */
    private final static String DETAIL_URL = "http://172.20.42.86:58088/ultrabpp/ultrabpp/view.action?baseSchema={0}&baseID={1}&taskID=&mode=MODIFY";


    @Resource
    private HttpUtils httpUtils;


    public Map<String, String> post2TYInfo(String orderNo) {
        log.info("workOrderNo:" + orderNo);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        List cookieList = Lists.newArrayList();
        cookieList.add("JSESSIONID=" + tyCookie);
        requestHeaders.put("Cookie", cookieList);
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("workSheetSerialnum", orderNo);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, requestHeaders);
        return HtmlParserUtil.parseTaiYueForOne(httpUtils.postByArg(httpEntity, URL));

    }


    public Map<String, String> get2TYDetail(String baseSchema, String baseId) {
        String url = MessageFormat.format(DETAIL_URL, baseSchema, baseId);
        HttpHeaders requestHeaders = new HttpHeaders();
        List cookieList = Lists.newArrayList();
        cookieList.add("JSESSIONID=" + tyCookie);
        requestHeaders.put("Cookie", cookieList);
        return HtmlParserUtil.parseTaiYueDetail(httpUtils.getByArg(url, requestHeaders));
    }


 /*   public List<WorkOrderDTO> findWorkOrder(WorkOrderQueryREQ workOrderQueryREQ) {
        List<WorkOrderDTO> dtoList = Lists.newArrayList();
        List<WorkOrderDO> doList = workOrderMapper.listWorkOrder(workOrderQueryREQ);
        if (!CollectionUtils.isEmpty(doList)) {
            doList.forEach(workOrderDO1 -> {
                WorkOrderDTO workOrderDTO = new WorkOrderDTO();
                BeanUtils.copyProperties(workOrderDO1, workOrderDTO);
                dtoList.add(workOrderDTO);
            });
        }
        return dtoList;
    }
*/

    public Map<String,String> loadTYOneDay(String start, String end, String baseSchema, String id, int currentPage) {
        // System.out.println(start + end);
        String url = URL + "?baseSchema=" + baseSchema + "&id=" + id;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        List cookieList = Lists.newArrayList();
        cookieList.add("JSESSIONID=" + tyCookie);
        requestHeaders.put("Cookie", cookieList);
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("createstarttime", start);
        paramMap.add("createendtime", end);
        paramMap.add("var_currentpage", currentPage);
        paramMap.add("var_istranfer", 1);
        paramMap.add("var_pagesize", 20);
        paramMap.add("var_sorttype", 0);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, requestHeaders);
        return HtmlParserUtil.parseTaiYueSearchResultHtml(httpUtils.postByArg(httpEntity, url));
    }


}
