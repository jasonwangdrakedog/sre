package com.sre.analysis.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sre.analysis.util.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Service
public class JIRAService {
    @Value("${jira.cookie}")
    private String jiraCookie;


    @Value("${jira.token}")
    private String jiraToken;

    /**
     * eg: 1595990149375
     */
    private final static String URL = "http://jira.cmss.com/rest/quicksearch/1.0/productsearch/search?q={0}&_={1}";

    @Resource
    private HttpUtils httpUtils;

    public Map<String, String> get2JIRA(String orderNo) {
        Map<String, String> result = Maps.newHashMap();
        String url = MessageFormat.format(URL, orderNo, String.valueOf(System.currentTimeMillis()));
        HttpHeaders requestHeaders = new HttpHeaders();
        List cookieList = Lists.newArrayList();
        cookieList.add("JSESSIONID=" + jiraCookie + ";atlassian.xsrf.token=" + jiraToken);
        requestHeaders.put("Cookie", cookieList);


        String resp = httpUtils.getByArg(url, requestHeaders);
        if (!StringUtils.isEmpty(resp)) {
            List<JSONObject> jsonObjectList = JSONArray.parseArray(resp, JSONObject.class);
            jsonObjectList.forEach(jsonObject -> {
                if (jsonObject.getString("id").equals("quick-search-issues")) {
                    JSONArray items = jsonObject.getJSONArray("items");
                    if (!CollectionUtils.isEmpty(items)) {
                        JSONObject object = (JSONObject) items.get(0);
                        result.put("url", object.getString("url"));
                        result.put("title", object.getString("title"));
                    }
                }
            });
        }
        return result;
    }


}
