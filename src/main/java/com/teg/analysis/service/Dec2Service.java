package com.teg.analysis.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wangyuan
 * @date 2020/12/3 16:08
 */


@Service
public class Dec2Service {


    /**
     * 挂载云硬盘分析
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {
        List<String> fileNames = Lists.newArrayList();
        fileNames.add("D:\\temp\\云硬盘挂载10");
        fileNames.add("D:\\temp\\云硬盘挂载30");
        fileNames.add("D:\\temp\\云硬盘挂载50");
        fileNames.add("D:\\temp\\云硬盘挂载80");
        fileNames.add("D:\\temp\\云硬盘挂载100");

        List<String> dataList = Lists.newArrayList();
        dataList.add("D:\\temp\\volume1");
        dataList.add("D:\\temp\\volume2");
        dataList.add("D:\\temp\\volume3");
        dataList.add("D:\\temp\\volume4");
        dataList.add("D:\\temp\\volume5");
        dataList.add("D:\\temp\\volume6");

        fileNames.forEach(s -> readFile(s, dataList));
    }


    private static void readFile(String fileName, List<String> dataList) {
        Set<String> reqIds = Sets.newHashSet();

        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                reqIds.add(str);
            }

            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(reqIds.size());


        Map<String, List<String>> map = Maps.newHashMap();

        dataList.forEach(s -> {
            System.out.println("读取日志：" + s);
            try {
                FileReader fr = new FileReader(s);
                BufferedReader bf = new BufferedReader(fr);
                String str;
                // 按行读取字符串
                while ((str = bf.readLine()) != null) {
                    int a = str.indexOf("reqId-");
                    if (a > 0) {
                        String reqId;
                        reqId = str.substring(a, a + 38);


                        if (str.contains("#VolumeController.mount 硬盘挂载") && reqIds.contains(reqId)
                                && !map.containsKey(reqId)) {
                            map.put(reqId, new LinkedList<>());
                        }
                        if (map.containsKey(reqId)) {
                            map.get(reqId).add(str);
                        }
                    }

                }

                bf.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        analysis(map);
    }


    private static void analysis(Map<String, List<String>> map) {

        if (CollectionUtils.isEmpty(map)) {
            return;
        }
        int count = 0;
        long total = 0l;
        Long max = null;
        Long min = null;
        String maxReqId = null;

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
          //  System.out.println("---------");
            List<String> asdsad = entry.getValue();
            String start = asdsad.get(0).substring(asdsad.get(0).indexOf("[") + 1, asdsad.get(0).indexOf("]"));
            String end = asdsad.get(asdsad.size() - 1).substring(asdsad.get(asdsad.size() - 1).indexOf("[") + 1, asdsad.get(asdsad.size() - 1).indexOf("]"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
          //  System.out.println("请求开始：" + start + ",reqId:" + entry.getKey());
           // System.out.println("请求结束：" + end);
            try {
                Date asd = sdf.parse(start);
                Date sad = sdf.parse(end);

                //System.out.println(sad.getTime() - asd.getTime() + "毫秒");
                count++;
                if (null == max || max < sad.getTime() - asd.getTime()) {
                    max = sad.getTime() - asd.getTime();
                    maxReqId = entry.getKey();
                }
                if (null == min || min > sad.getTime() - asd.getTime()) {
                    min = sad.getTime() - asd.getTime();
                }
                total = total + sad.getTime() - asd.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println("共:" + count + "个");
        System.out.println("平均耗时:" + total / count + "毫秒");
        System.out.println("最大耗时:" + max + "毫秒,reqID:" + maxReqId);
        System.out.println("最小耗时:" + min + "毫秒");
        System.out.println("最大耗时全日志:");
        map.get(maxReqId).forEach(s -> System.out.println(s));
    }


}
