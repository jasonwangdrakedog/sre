package com.teg.analysis.job;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.teg.analysis.enums.TyWorkTypeEnum;
import com.teg.analysis.model.DTO.WorkOrderDTO;
import com.teg.analysis.service.TaiYueService;
import com.teg.analysis.service.WorkOrderService;
import com.teg.analysis.util.DateFormatterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @author wangyuan
 * @date 2020/8/13 16:52
 */
@Configuration
@EnableScheduling
@Slf4j
@Deprecated
public class WorkScheduleTask implements ApplicationContextAware {

    private static ApplicationContext applicationContext;


    @Resource
    private TaiYueService taiYueService;

    @Resource
    private ExecutorService executorService;

    @Resource(name = "brokenWorkOrderService")
    private WorkOrderService brokenWorkOrderService;

    //3.添加定时任务
    //@Scheduled(cron = "0 0 17 * * ?")
    //或直接指定时间间隔，例如：5秒
   //  @Scheduled(fixedRate = 500000)
    private void configureTasks() {
        log.info("定时任务start:");
        Date dNow = new Date(); //当前时间
        Date dBefore;
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        String defaultEndDate = DateFormatterUtil.formatDate2(calendar.getTime()).substring(0, 10) + " 00:00:00";

        calendar.add(Calendar.DAY_OF_MONTH, -1); //设置为前一天
        dBefore = calendar.getTime(); //得到前一天的时间
        String defaultStartDate = DateFormatterUtil.formatDate2(dBefore); //格式化前一天
        defaultStartDate = defaultStartDate.substring(0, 10) + " 00:00:00";

        defaultStartDate = "2020-06-01 00:00:00";//todo
        defaultEndDate = "2020-06-30 23:59:59";

        TyWorkTypeEnum[] tyWorkTypeEnums = TyWorkTypeEnum.values();
        for (int i = 0; i < tyWorkTypeEnums.length; i++) {
            TyWorkTypeEnum tyWorkTypeEnum = tyWorkTypeEnums[i];
            handle(defaultStartDate, defaultEndDate, tyWorkTypeEnum.getBaseSchema(), tyWorkTypeEnum.getBaseId(), tyWorkTypeEnum.getClazzName());
        }
    }


    private void handle(String start, String end, String baseSchema, String baseId, String service) {

        int currentPage = 1;
        Map<String,String> workOrderMap = Maps.newHashMap();
        Map<String,String> tempMap;
        do {
            log.info("开始查询泰岳工单列表：开始时间：" + start + ",结束时间：" + end + "," + currentPage + "页,工单类型:" + service);
            tempMap = taiYueService.loadTYOneDay(start, end,
                    baseSchema, baseId, currentPage);
            log.info("查询泰岳工单列表结果返回：" + tempMap.size() + "条");
            workOrderMap.putAll(tempMap);
            log.info("累计查询工单：" + workOrderMap.size() + "条");
            currentPage++;
        } while (tempMap.size() > 0);

        if (!CollectionUtils.isEmpty(workOrderMap)) {
            List<WorkOrderDTO> workOrderDTOS = fillWorkOrderInfo(workOrderMap, service);
            WorkOrderService bean = this.getBean(service);
            bean.batchInsert(workOrderDTOS);
        }
    }

    private List<WorkOrderDTO> fillWorkOrderInfo(Map<String,String> workOrderMap, String service) {
        //  System.out.println(workOrderSet.size());
        CountDownLatch countDownLatch = new CountDownLatch(workOrderMap.size());

        List<WorkOrderDTO> workOrderDTOS = Lists.newCopyOnWriteArrayList();
        if (CollectionUtils.isEmpty(workOrderMap)) {
            return workOrderDTOS;
        }
        workOrderMap.keySet().forEach(s -> executorService.execute(() -> {
            Map<String, String> tyInfo = taiYueService.post2TYInfo(s);
            Map<String, String> detailMap = taiYueService.get2TYDetail(tyInfo.get("baseSchema"), tyInfo.get("baseId"));
            detailMap.put("workNoDate", workOrderMap.get(s));
            WorkOrderService bean = this.getBean(service);
            workOrderDTOS.add(bean.parseTYDetail(detailMap));
            countDownLatch.countDown();
        }));
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return workOrderDTOS;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static <T> T getBean(String beanName) {
        if (applicationContext.containsBean(beanName)) {
            return (T) applicationContext.getBean(beanName);
        } else {
            return null;
        }
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> baseType) {
        return applicationContext.getBeansOfType(baseType);
    }


}
