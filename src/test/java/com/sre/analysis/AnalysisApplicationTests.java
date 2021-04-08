package com.sre.analysis;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sre.analysis.exception.BusinessException;
import com.sre.analysis.mapper.ComplainWorkOrderMapper;
import com.sre.analysis.mapper.SecondLineMapper;
import com.sre.analysis.mapper.UserMapper;
import com.sre.analysis.model.DO.SecondLineDO;
import com.sre.analysis.model.DO.UserDO;
import com.sre.analysis.model.DTO.ComplainWorkOrderDTO;
import com.sre.analysis.model.REQ.UserREQ;
import com.sre.analysis.model.REQ.WorkOrderQueryREQ;
import com.sre.analysis.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@SpringBootTest
class AnalysisApplicationTests {

    @Value("${ty.cookie}")
    private String tyCookie;

    @Value("${jira.cookie}")
    private String jiraCookie;


    @Value("${jira.token}")
    private String jiraToken;

    @Value("${excel.read}")
    private String excelRead;


    @Resource
    private TaiYueService taiYueService;
    @Resource
    private JIRAService jiraService;
    @Resource
    private ExcelService excelService;
    @Resource(name = "brokenWorkOrderService")
    private WorkOrderService workOrderService;



 /*   @Test
    void finalTest() throws Exception {
        LocalDateTime beginTime = LocalDateTime.now();
        List<AnalysisResultDTO> list = analysisService.analysis(excelRead);
        Long opetime = Duration.between(beginTime, LocalDateTime.now()).toMillis();
        System.out.println("解析文件花费时间（毫秒）：" + opetime);
        excelService.exportAnalysisResult(list);
        System.out.println();
    }*/


    @Test
    void testJIRAUrl() {
        String orderNo = "004-20200624-0011";
        jiraService.get2JIRA(orderNo);
        System.out.println(1);
    }


    @Test
    void testTyUrl() {
        String orderNo = "004-20200701-0009";
        Map<String, String> tyInfo = taiYueService.post2TYInfo(orderNo);
        taiYueService.get2TYDetail(tyInfo.get("baseSchema"), tyInfo.get("baseId"));
    }


    @Test
    void testReadExcel() {
        LocalDateTime beginTime = LocalDateTime.now();
        Map<Integer, Map<Integer, String>> result = excelService.readExcelFromPath(excelRead);
        Long opetime = Duration.between(beginTime, LocalDateTime.now()).toMillis();
        System.out.println("解析文件花费时间（毫秒）：" + opetime);
    }

    @Resource
    DemoService demoService;
    @Resource
    RestTemplate restTemplate;


    @Test
    void testCreateUser() {
        String url = "http://127.0.0.1:8081/test/template";
        Map<String, String> result = Maps.newHashMap();
        //  String url = MessageFormat.format(url, orderNo, String.valueOf(System.currentTimeMillis()));


        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, null);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        if (exchange.getStatusCode().equals(HttpStatus.OK)) {
            // log.info("get http request success");
            System.out.println("ok");
        }
    }

    @Test
    void testUpdateUser() {
        UserREQ u = new UserREQ();
        u.setUserId(8l);
        u.setBirthday("1995-03-14");
        u.setWeight(80);
        u.setUserName("欧阳锋33");
        u.setUserAddress("白驼山");
        u.setProvince("江苏");
        u.setSex(0);
        System.out.println(demoService.updateUser(u));
    }


    @Test
    void testBatchCreateUser() {
        UserREQ u1 = new UserREQ();
        u1.setBirthday("1957-02-14");
        u1.setHeight(181);
        u1.setUserName("任盈盈2");
        u1.setSex(0);
        u1.setUserAddress("黑木崖5");

        UserREQ u2 = new UserREQ();
        u2.setBirthday("1958-02-14");
        u2.setHeight(182);
        u2.setUserName("任盈盈3");
        u2.setSex(1);
        u2.setUserAddress("黑木崖6");
        List<UserREQ> userREQList = Lists.newArrayList();
        userREQList.add(u1);
        userREQList.add(u2);
        demoService.batchCreateUser(userREQList);
    }

    @Resource(name = "questionWorkOrderService")
    private WorkOrderService questionWorkOrderService;


    @Test
    void testException() {
        log.info("start");
        try {
            tt();
        } catch (Exception e) {
            log.info("error:{}", e.getMessage(), e);
        }
    }

    private void tt() {
        throw new BusinessException("kkk");
    }

    @Test
    void testQuery() {
        WorkOrderQueryREQ workOrderQueryREQ = new WorkOrderQueryREQ();
        workOrderQueryREQ.setWorkType(1);
        workOrderQueryREQ.setEnd("2020-08-01 12:07:38");
        workOrderQueryREQ.setStart("2020-06-01 12:07:38");
        workOrderQueryREQ.setPage(1);
        workOrderQueryREQ.setPageSize(20);
        System.out.println(questionWorkOrderService.totalCount(workOrderQueryREQ));

    }


    @Resource
    private UserMapper UserMapper;

    @Test
    void testQue2ry() {
        List<UserDO> asd = UserMapper.queryAllUsers();
        System.out.println(JSONObject.toJSONString(asd));
    }

    @Resource
    private SecondLineMapper secondLineMapper;
    @Test
    void testQue22ry() {
        List<SecondLineDO> asd = secondLineMapper.listMenu();
        System.out.println(JSONObject.toJSONString(asd));
    }



    /**
     * 投诉工单每天每小时占比 6-9月
     */
    @Resource
    private ComplainWorkOrderMapper complainWorkOrderMapper;
    @Resource
    private ExecutorService executorService;


    /**
     *
     */
    @Test
    void testGetComplainByTime() {
        WorkOrderQueryREQ workOrderQueryREQ = new WorkOrderQueryREQ();
        workOrderQueryREQ.setPageSize(50);
        workOrderQueryREQ.setPage(0);


        int totalCount = complainWorkOrderMapper.totalCount(workOrderQueryREQ);
        log.info("total:" + totalCount);


        Map<String, Map<Integer, Integer>> result = new TreeMap<>(Comparator.reverseOrder());
        List<ComplainWorkOrderDTO> tempList;
        do {
            tempList = complainWorkOrderMapper.list(workOrderQueryREQ);
            tempList.forEach(complainWorkOrderDTO -> {
                String date = complainWorkOrderDTO.getWorkNoDate();
                if (!result.containsKey(date)) {
                    TreeMap<Integer, Integer> treeMap = new TreeMap<>(Comparator.reverseOrder());
                    result.put(date, treeMap);
                }
                Map<Integer, Integer> littleMap = result.get(date);
                String hour = complainWorkOrderDTO.getHappenTime().substring(11, 13);
                Integer hourInt = new Integer(hour);
                if (!littleMap.containsKey(hourInt)) {
                    littleMap.put(hourInt, 0);
                }
                Integer count = littleMap.get(hourInt);
                littleMap.put(hourInt, count + 1);


                Integer asd = workOrderQueryREQ.getPage();
                workOrderQueryREQ.setPage(asd + 1);
            });
        }
        while (tempList.size() > 0);


        System.out.println(JSONObject.toJSONString(result));

        result.forEach((k, v) -> {
            System.out.println("日期" + k);
            AtomicInteger noCount = new AtomicInteger();
            AtomicInteger total = new AtomicInteger();
            v.forEach((k1, v1) -> {
                System.out.println("整点:" + k1 + "," + v1 + "个");
                if (k1 < 9 || k1 >= 23) {
                    noCount.set(noCount.get() + v1);
                }
                total.set(total.get() + v1);
            });
            System.out.println("23点后，9点前投诉工单：" + noCount.get());
            System.out.println("全部投诉工单：" + total.get());
            System.out.println();
            BigDecimal b = new BigDecimal((float) noCount.get() / total.get());


            Double radio = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            System.out.println("比例 ：" + radio);
            System.out.println("===================");
        });


    }


    public static void main(String[] args) {
        String path = "D:\\work\\op源码\\java\\op-order-manage";
        File file = new File(path);
        File[] tempList = file.listFiles();
        rename(tempList, file);
    }


    private static void rename(File[] tempList, File file) {
        for (int i = 0; i < tempList.length; i++) {
            if (!tempList[i].isFile()) {
                rename(tempList[i].listFiles(), tempList[i].getAbsoluteFile());
            }
            String fileName = tempList[i].getName();
            System.out.println(fileName);
            String[] asd = fileName.split("\\.");
            if (asd.length > 1) {
                String asdsad = asd[asd.length - 1];
                if (asdsad.equals("class")) {
                    String newName = asd[0] + ".java";
                    if (!file.renameTo(new File(file.getParent() + "\\" + newName))) {
                        // log.warn("重命名文件失败：{}", file.getParent() + "\\" + newName);
                    }
                }
            }

        }
    }
}
