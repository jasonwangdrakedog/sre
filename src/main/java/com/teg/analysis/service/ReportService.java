package com.teg.analysis.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.teg.analysis.model.DTO.WorkOrderDTO;
import com.teg.analysis.model.REQ.WorkOrderQueryREQ;
import com.teg.analysis.model.report.*;
import com.teg.analysis.util.DateFormatterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.DecimalFormat;
import java.util.*;

@Service
@Slf4j
public class ReportService implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public EchartOption generateBar1() {
        EchartOption result = new EchartOption();
        Calendar calendar = Calendar.getInstance();
        List originList = getOriginData();
        if (CollectionUtils.isEmpty(originList)) {
            return result;
        }
        //图例
        Set<String> legendSet = Sets.newLinkedHashSet();
        //横坐标
        Set<String> xAxisSet = Sets.newLinkedHashSet();
        // status month value
        Map<String, Map<String, Integer>> totalMap = Maps.newHashMap();

        for (int i = originList.size() - 1; i >= 0; i--) {
            WorkOrderDTO workOrderDTO = (WorkOrderDTO) originList.get(i);
            legendSet.add(workOrderDTO.getTyStatus());

            Date workNoDate = DateFormatterUtil.parseDate(workOrderDTO.getWorkNoDate());
            calendar.setTime(workNoDate);

            String key = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1);
            xAxisSet.add(key);

            if (!totalMap.containsKey(workOrderDTO.getTyStatus())) {
                totalMap.put(workOrderDTO.getTyStatus(), Maps.newHashMap());
            }
            Map<String, Integer> month2ValueMap = totalMap.get(workOrderDTO.getTyStatus());
            if (!month2ValueMap.containsKey(key)) {
                month2ValueMap.put(key, 0);
            }
            Integer count = month2ValueMap.get(key);
            count++;
            month2ValueMap.put(key, count);
        }


        legendSet.add("完成度");
        result.setLegendData(Lists.newLinkedList(legendSet));
        result.setXAxisData(Lists.newLinkedList(xAxisSet));
        System.out.println(JSONObject.toJSONString(totalMap));
        List<Item> items = Lists.newLinkedList();

        for (int i = 0; i < result.getLegendData().size() - 1; i++) {
            Item item = new Item();
            item.setName(result.getLegendData().get(i));
            item.setType("bar");
            List<Integer> data = Lists.newLinkedList();
            for (String time : result.getXAxisData()) {
                Integer count = totalMap.get(result.getLegendData().get(i)).get(time);
                data.add(null == count ? 0 : count);
            }
            item.setData(data);
            items.add(item);
        }

        //完成度 单独一列
        Item rightItem = new Item();
        rightItem.setType("line");
        rightItem.setYAxisIndex(1);
        rightItem.setName(result.getLegendData().get(result.getLegendData().size() - 1));
        rightItem.setData(getComplete(items, result.getXAxisData().size()));
        //  items.add(rightItem);

        result.setSeries(items);
        System.out.println(JSONObject.toJSONString(result));
        return result;
    }


    public List<List<Object>> generatePie1() {
        List<List<Object>> result = Lists.newArrayList();
        Calendar calendar = Calendar.getInstance();
        List originList = getOriginData();
        if (CollectionUtils.isEmpty(originList)) {
            return result;
        }
        // status month value
        Map<String, Map<String, Integer>> totalMap = Maps.newHashMap();

        List<Object> monthList = Lists.newArrayList();
        monthList.add("month");
        result.add(monthList);
        Set<String> monthSet = Sets.newHashSet();

        Set<String> producerFirstSet = Sets.newHashSet();
        for (int i = originList.size() - 1; i >= 0; i--) {
            WorkOrderDTO workOrderDTO = (WorkOrderDTO) originList.get(i);
            Date workNoDate = DateFormatterUtil.parseDate(workOrderDTO.getWorkNoDate());
            calendar.setTime(workNoDate);

            String key = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1);
            if (!monthSet.contains(key)) {
                monthSet.add(key);
                monthList.add(key);
            }
            String produceFirst = workOrderDTO.getProduceFirst() == null ? "其他" : workOrderDTO.getProduceFirst();
            if (!producerFirstSet.contains(produceFirst)) {
                producerFirstSet.add(produceFirst);
            }

            if (!totalMap.containsKey(produceFirst)) {
                totalMap.put(produceFirst, Maps.newHashMap());
            }
            Map<String, Integer> month2ValueMap = totalMap.get(produceFirst);
            if (!month2ValueMap.containsKey(key)) {
                month2ValueMap.put(key, 0);
            }
            Integer count = month2ValueMap.get(key);
            count++;
            month2ValueMap.put(key, count);
        }

        producerFirstSet.forEach(s -> {
            List<Object> list = Lists.newArrayList();
            list.add(s);
            Map<String, Integer> month2ValueMap = totalMap.get(s);
            for (int i = 1; i < monthList.size(); i++) {
                Integer count = month2ValueMap.get(monthList.get(i)) == null ? 0 : month2ValueMap.get(monthList.get(i));
                list.add(count);
            }
            result.add(list);
        });

        System.out.println(JSONObject.toJSONString(result));
        return result;
    }

    private List<String> getComplete(List<Item> basicItemList, int xAxisIndex) {
        List<String> data = Lists.newArrayList();
        for (int i = 0; i < xAxisIndex; i++) {
            int sub = 0;
            int total = 0;
            for (int j = 0; j < basicItemList.size(); j++) {
                Item item = basicItemList.get(j);
                int count = (int) item.getData().get(i);
                total = total + count;
                if (item.getName().equals("已关闭") || item.getName().equals("已作废") || item.getName().equals("待关闭")) {
                    sub = sub + count;
                }
            }
            DecimalFormat df = new DecimalFormat("0.00");//设置保留位数

            String re = df.format((float) sub * 100 / total);
            data.add(re);
        }
        return data;

    }


    private List getOriginData() {
        Calendar calendar = Calendar.getInstance();
        WorkOrderService workOrderService = getBean(1);

        Date now = new Date();
        String endQuery = DateFormatterUtil.formatDate(now);
        calendar.setTime(now);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 6);
        String startQuery = DateFormatterUtil.formatDate(calendar.getTime());
        WorkOrderQueryREQ req = new WorkOrderQueryREQ();
        req.setStart(startQuery);
        req.setEnd(endQuery);
        req.setPageSize(1000);
        List allList = Lists.newArrayList();
        List tempList;
        int currentPage = 0;
        do {
            req.setPage(currentPage);
            tempList = workOrderService.list(req);//todo取最近6个月

            allList.addAll(tempList);
            currentPage = currentPage + req.getPageSize();
        } while (tempList.size() > 0);
        return allList;
    }


    public EchartOption testReport() {
        Calendar calendar = Calendar.getInstance();
        WorkOrderService workOrderService = getBean(1);

        Date now = new Date();
        String endQuery = DateFormatterUtil.formatDate(now);
        calendar.setTime(now);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 6);
        String startQuery = DateFormatterUtil.formatDate(calendar.getTime());
        WorkOrderQueryREQ req = new WorkOrderQueryREQ();
        req.setStart(startQuery);
        req.setEnd(endQuery);
        req.setPageSize(1000);
        List allList = Lists.newArrayList();
        List tempList;
        int currentPage = 0;
        do {
            req.setPage(currentPage);
            tempList = workOrderService.list(req);//todo取最近6个月

            allList.addAll(tempList);
            currentPage = currentPage + req.getPageSize();
        } while (tempList.size() > 0);


        Map<String, List<WorkOrderDTO>> map = Maps.newHashMap();
        tempList = null;
        Set<String> legendDataSet = Sets.newHashSet();
        legendDataSet.add("工单个数");

        Set<String> xAxisDataSet = Sets.newHashSet();

        allList.forEach(o -> {
            WorkOrderDTO workOrderDTO = (WorkOrderDTO) o;
            legendDataSet.add(workOrderDTO.getPriority());
            xAxisDataSet.add(workOrderDTO.getProduceFirst());


            Date workNoDate = DateFormatterUtil.parseDate(workOrderDTO.getWorkNoDate());
            calendar.setTime(workNoDate);

            String key = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1);
            if (org.apache.commons.collections4.CollectionUtils.isEmpty(map.get(key))) {
                map.put(key, Lists.newArrayList());
            }
            map.get(key).add(workOrderDTO);

        });
        xAxisDataSet.add("其他");
        EchartOption echartOption = new EchartOption();
        echartOption.setLegendData(Lists.newArrayList(legendDataSet));
        echartOption.setXAxisData(Lists.newArrayList(xAxisDataSet));

        List<String> timeLineList = Lists.newLinkedList();
        List<EchartOptionSub> echartOptionSubs = Lists.newLinkedList();

        map.forEach((k, v) -> {
            timeLineList.add(k);
            EchartOptionSub echartOptionSub1 = new EchartOptionSub();
            echartOptionSubs.add(echartOptionSub1);
            echartOptionSub1.setTitle(new EchartOptionTitle(k + "工单分析"));
            List<EchartOptionSeries> echartOptionSeriesLinkedList = Lists.newLinkedList();
            echartOptionSub1.setSeries(echartOptionSeriesLinkedList);


            Map<String, Map<String, Integer>> dataMap = Maps.newHashMap();
            Map<String, List<Item>> asd = Maps.newHashMap();
            v.forEach(workOrderDTO -> {
                String key = workOrderDTO.getProduceFirst();
                if (!dataMap.containsKey(key)) {
                    dataMap.put(key, Maps.newHashMap());
                    asd.put(key, Lists.newArrayList());
                }
                Map<String, Integer> secondMap = dataMap.get(key);
                if (!secondMap.containsKey(workOrderDTO.getPriority())) {
                    secondMap.put(workOrderDTO.getPriority(), 0);
                }
                int count = secondMap.get(workOrderDTO.getPriority());
                count = count + 1;
                secondMap.put(workOrderDTO.getPriority(), count);
            });

            dataMap.forEach((k1, v1) -> {
                Item item1 = new Item();
                item1.setName(k1);
                int count = 0;
                for (Map.Entry<String, Integer> entry : v1.entrySet()) {
                    count += entry.getValue();
                }
                item1.setValue(count);
                asd.get(k1).add(item1);
            });

            dataMap.forEach((k1, v1) -> {
                Item item1 = new Item();
                item1.setName(k1);
                item1.setValue(ObjectUtils.isEmpty(v1.get("Critical")) ? 0 : v1.get("Critical"));
                asd.get(k1).add(item1);
            });

            dataMap.forEach((k1, v1) -> {
                Item item1 = new Item();
                item1.setName(k1);
                item1.setValue(ObjectUtils.isEmpty(v1.get("Minor")) ? 0 : v1.get("Minor"));
                asd.get(k1).add(item1);
            });


            dataMap.forEach((k1, v1) -> {
                Item item1 = new Item();
                item1.setName(k1);
                item1.setValue(ObjectUtils.isEmpty(v1.get("Major")) ? 0 : v1.get("Major"));
                asd.get(k1).add(item1);
            });


            dataMap.forEach((k1, v1) -> {
                Item item1 = new Item();
                item1.setName(k1);
                item1.setValue(ObjectUtils.isEmpty(v1.get("Trivial")) ? 0 : v1.get("Trivial"));
                asd.get(k1).add(item1);
            });


            dataMap.forEach((k1, v1) -> {
                Item item1 = new Item();
                item1.setName(k1);
                item1.setValue(ObjectUtils.isEmpty(v1.get("Blocker")) ? 0 : v1.get("Blocker"));
                asd.get(k1).add(item1);
            });

            dataMap.forEach((k1, v1) -> {
                EchartOptionSeries echartOptionSeries1 = new EchartOptionSeries();

                echartOptionSeriesLinkedList.add(echartOptionSeries1);
                echartOptionSeries1.setData(asd.get(k1));
            });


 /*           Item item1 = new Item();
            item1.setName("统一监控平台");
            item1.setValue(2);
            itemLists.add(item1);
            Item item2 = new Item();
            item2.setName("移动云门户");
            item2.setValue(2);
            itemLists.add(item2);
            Item item3 = new Item();
            item3.setName("弹性计算");
            item3.setValue(2);
            itemLists.add(item3);
            Item item4 = new Item();
            item4.setName("云存储");
            item4.setValue(2);
            itemLists.add(item4);
            Item item5 = new Item();
            item5.setName("云网络");
            item5.setValue(2);
            itemLists.add(item5);
            Item item6 = new Item();
            item6.setName("开放云市场");
            item6.setValue(2);
            itemLists.add(item6);*/
            //data.put("2020-07", itemLists);
        });
        echartOption.setTimeLindeData(timeLineList);
        echartOption.setEchartOptionSubList(echartOptionSubs);


        System.out.println(JSONObject.toJSONString(echartOption));
        return echartOption;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static <T> T getBean(Integer workType) {
        String beanName = "brokenWorkOrderService";
        switch (workType) {
            case 0:
                beanName = "brokenWorkOrderService";
                break;
            case 1:
                beanName = "questionWorkOrderService";
                break;
            case 2:
                beanName = "complainWorkOrderService";
                break;
        }
        if (applicationContext.containsBean(beanName)) {
            return (T) applicationContext.getBean(beanName);
        } else {
            return null;
        }
    }

}
