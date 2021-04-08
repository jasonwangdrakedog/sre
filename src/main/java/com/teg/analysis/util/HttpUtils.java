package com.teg.analysis.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author wangyuan
 * @date 2020/8/6 18:35
 */
@Component
@Slf4j
public class HttpUtils {

    @Resource
    private RestTemplate restTemplate;

    public String getByArg(String url, MultiValueMap<String, String> headers) {
        headers.add("traceId", MDC.get("traceId"));
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        if (exchange.getStatusCode().equals(HttpStatus.OK)) {
            // log.info("get http request success");
        }
        return exchange.getBody();
    }


    public String get(String url) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        return getByArg(url, headers);
    }


    public String postByArg(HttpEntity<MultiValueMap<String, Object>> httpEntity, String url) {
        HttpHeaders headers = httpEntity.getHeaders();
        // headers.add("traceId", MDC.get("traceId"));
        // System.out.println("POST :url"+url+","+ JSONObject.toJSONString(httpEntity));
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        if (exchange.getStatusCode().equals(HttpStatus.OK)) {
            //    log.info("post http request success");
        }
        return exchange.getBody();
    }

    public String post(String url) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        return postByArg(httpEntity, url);
    }


}
