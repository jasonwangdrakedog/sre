package com.teg.analysis.job;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.teg.analysis.mapper.ComplainWorkOrderMapper;
import com.teg.analysis.model.DTO.ComplainWorkOrderDTO;
import com.teg.analysis.service.ExcelService;
import com.teg.analysis.util.DateFormatterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangyuan
 * @date 2020/8/13 16:52
 */
@Configuration
@EnableScheduling
@Slf4j
public class ComplainOriginTask {
    Lock lock = new ReentrantLock();


    @Resource
    private ExcelService excelService;

    @Resource
    private ComplainWorkOrderMapper complainWorkOrderMapper;


    //3.添加定时任务
    //@Scheduled(cron = "0 0 17 * * ?")
    //或直接指定时间间隔，例如：5秒
  //  @Scheduled(fixedRate = 500000)
    private void configureTasks() {
        log.info("定时任务start:");
        String dateStr = DateFormatterUtil.formatDate(new Date());
        String filePath = "D:\\workOrder\\origin\\complain\\";

        File file = new File(filePath + dateStr + "\\");
        File[] tempList = file.listFiles();
        List<File> unCompletedFiles = Lists.newArrayList();

        for (int i = 0; i < tempList.length; i++) {
            if (!tempList[i].isFile()) {
                continue;
            }
            if (tempList[i].getName().startsWith("DONE")) {
                continue;
            }
            unCompletedFiles.add(tempList[i]);
        }

        log.info("unCompletedFiles：" + unCompletedFiles.size());
        if (!CollectionUtils.isEmpty(unCompletedFiles)) {
            boolean hasLock = lock.tryLock();
            if (!hasLock) {
                return;
            }
            try {
                handleFile(unCompletedFiles);
            } catch (Exception e) {
                log.error("解析excel失败", e);
            } finally {
                lock.unlock();
            }
        }

    }

    /**
     * E列去除“服务请求”，“使用咨询”，“商机账务”，I列筛选“移动云自身原因”，通过AY列“业务分类”分析自己负责的产品；
     * 通过观察L列“原因分析”和M列“解决方案”来进行投诉根因的提炼和分析，可在后面再加一列“产品原因”，“非产品原因”
     */
    private void handleFile(List<File> files) {
        List<ComplainWorkOrderDTO> complainWorkOrderDTOLists = Lists.newArrayList();

        Map<String, ComplainWorkOrderDTO> complainWorkOrderDTOMap = Maps.newHashMap();

        files.forEach(file -> {
            Map<Integer, Map<Integer, String>> detailDataMap = excelService.readExcelFromPath(file.getAbsolutePath());
            String[] nameArray = file.getName().split("\\.")[0].split("_");
            Long timeStamp = Long.parseLong(nameArray[nameArray.length - 1]);
            detailDataMap.entrySet().forEach(entry -> {
                log.info("key:value = " + entry.getKey() + ":" + entry.getValue());
                Map<Integer, String> lineData = entry.getValue();
                if (!(lineData.get(4).equals("服务请求")
                        || lineData.get(4).equals("商机账务") || lineData.get(4).equals("使用咨询") || StringUtils.isEmpty(lineData.get(4)))
                        && lineData.get(8).equals("移动云自身原因")) {
                    //  E列去除“服务请求”，“使用咨询”，“商机账务”
                    String orderNo = lineData.get(0);//工单号
                    if (complainWorkOrderDTOMap.containsKey(orderNo)) {
                        if (complainWorkOrderDTOMap.get(orderNo).getTimeStamp() < timeStamp) {
                            complainWorkOrderDTOMap.put(orderNo, new ComplainWorkOrderDTO(lineData, timeStamp));
                        }
                    } else {
                        complainWorkOrderDTOMap.put(orderNo, new ComplainWorkOrderDTO(lineData, timeStamp));
                    }
                }
            });
            this.rename(file);
        });
        complainWorkOrderDTOLists.addAll(complainWorkOrderDTOMap.values());

        complainWorkOrderMapper.batchCreateComplainWorkOrder(complainWorkOrderDTOLists);

    }

    private void rename(File file) {
        String newName = "DONE" + file.getName();
        if (!file.renameTo(new File(file.getParent() + "\\" + newName))) {
            log.warn("重命名文件失败：{}", file.getParent() + "\\" + newName);
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Lock lock = new ReentrantLock();
        Thread t = new Thread(() -> {
            System.out.println("start1:");
            lock.lock();
            try {
                Thread.sleep(10000l);
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        Thread.sleep(1000l);
        System.out.println("after 1");
        Thread t2 = new Thread(() -> {
            System.out.println("start2:");
            try {
                boolean asd = lock.tryLock();
                if (asd) {
                    System.out.println("true");
                } else {
                    System.out.println("false");
                    return;
                }
            } catch (Exception e) {
                System.out.println("exception");
            } finally {
                System.out.println("final");
            }
        });
        t2.start();
    }

}
