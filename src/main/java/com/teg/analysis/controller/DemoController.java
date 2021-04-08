package com.teg.analysis.controller;


import com.alibaba.fastjson.JSONObject;
import com.teg.analysis.mapper.SecondLineMapper;
import com.teg.analysis.model.DO.SecondLineDO;
import com.teg.analysis.model.REQ.UserREQ;
import com.teg.analysis.model.common.Page;
import com.teg.analysis.model.common.Result;
import com.teg.analysis.service.DemoService;
import com.teg.analysis.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "测试接口Controller")
public class DemoController {

    @Resource
    private DemoService demoService;
    @Resource
    RestTemplate restTemplate;
    @Resource
    SecondLineMapper secondLineMapper;


    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public String test() throws InterruptedException {
        System.out.println("test start:" + Thread.currentThread().getId());
        //  Thread.sleep(4000l);
        System.out.println("test end:" + Thread.currentThread().getId());

        return "ok";
    }

    @RequestMapping(value = "test/template", method = RequestMethod.GET)
    @ResponseBody
    public String te2st() throws InterruptedException {
        System.out.println("start");
        String url = "http://127.0.0.1:8081/test";

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, null);
        try {
            while (true) {
                Runnable runnable = () -> {
                    ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
                    if (exchange.getStatusCode().equals(HttpStatus.OK)) {
                        // log.info("get http request success");
                        System.out.println("ok");
                    }

                };
                runnable.run();


            }
        } catch (Exception e) {
            System.out.println("error" + Thread.currentThread().getId());
            e.printStackTrace();
        }
        return null;

    }


    @ApiOperation(value = "测试用接口", notes = "测试用接口", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Result createUser(UserREQ userREQ) {
        demoService.createUser(userREQ);
        return null;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Page listUser() {
        List<SecondLineDO> asd = secondLineMapper.listMenu();
        System.out.println(JSONObject.toJSONString(asd));

        return PageUtil.success(demoService.listUser());
    }


    /**
     *
     */

}
