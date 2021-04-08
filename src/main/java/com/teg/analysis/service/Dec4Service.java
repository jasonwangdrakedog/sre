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
public class Dec4Service {


    /**
     * 云主机退订
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {
        List<String> fileNames = Lists.newArrayList();
        fileNames.add("D:\\temp\\云主机退订10");
        fileNames.add("D:\\temp\\云主机退订30");
        fileNames.add("D:\\temp\\云主机退订50");
        fileNames.add("D:\\temp\\云主机退订80");
        fileNames.add("D:\\temp\\云主机退订100");

        List<String> dataList = Lists.newArrayList();
        dataList.add("D:\\temp\\web1");
        dataList.add("D:\\temp\\web2");
        dataList.add("D:\\temp\\web3");
        dataList.add("D:\\temp\\web4");
        dataList.add("D:\\temp\\web5");
        dataList.add("D:\\temp\\web6");

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


                        if (reqIds.contains(reqId)
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
            System.out.println("---------");
            List<String> asdsad = entry.getValue();
            String start = asdsad.get(0).substring(asdsad.get(0).indexOf("[") + 1, asdsad.get(0).indexOf("]"));
            String end = asdsad.get(asdsad.size() - 1).substring(asdsad.get(asdsad.size() - 1).indexOf("[") + 1, asdsad.get(asdsad.size() - 1).indexOf("]"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            System.out.println("请求开始：" + start + ",reqId:" + entry.getKey());
            System.out.println("请求结束：" + end);
            //  System.out.println("请求结束22：" + asdsad.get(0));
            try {
                Date asd = sdf.parse(start);
                Date sad = sdf.parse(end);

                System.out.println(sad.getTime() - asd.getTime() + "毫秒");
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
