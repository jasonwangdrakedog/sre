package com.sre.analysis.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author wangyuan
 * @date 2020/12/3 16:08
 */

/**
 *
 */


@Service
public class DecService {


    /**
     * 云主机创建分析
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {
        List<String> fileList = Lists.newArrayList();
        fileList.add("D:\\temp\\compute1");
        fileList.add("D:\\temp\\compute2");
        fileList.add("D:\\temp\\compute3");
        fileList.add("D:\\temp\\compute4");
        fileList.add("D:\\temp\\compute5");
        fileList.add("D:\\temp\\compute6");
        Map<String, List<String>> allMap = Maps.newHashMap();
        Map<String, List<String>> map2 = Maps.newHashMap();
        Map<String, List<String>> map31 = Maps.newHashMap();

        fileList.forEach(s -> {
            System.out.println("读取日志：" + s);
            try {
                FileReader fr = new FileReader(s);
                BufferedReader bf = new BufferedReader(fr);
                String str;
                // 按行读取字符串
                while ((str = bf.readLine()) != null) {
                    int a = str.indexOf("reqId-");
                    String reqId;
                    if (a > 0) {
                        reqId = str.substring(a, a + 38);
                        if (str.contains("userName:csb_guyunbin")) {
                            if (!allMap.containsKey(reqId)) {
                                allMap.put(reqId, new LinkedList<>());
                            }
                        }
                        if (allMap.containsKey(reqId)) {
                            allMap.get(reqId).add(str);
                        }
                        if (str.contains("CreateMessageDispatcher收到消息") && str.contains("SubsystemCreateReq")
                                && str.contains("8dbf3d8afe064083b2a233031a195bcb")) {
                            if (!map31.containsKey(reqId)) {
                                map31.put(reqId, new LinkedList<>());
                            }
                        }
                        if (map31.containsKey(reqId)) {
                            map31.get(reqId).add(str);
                        }
                    }
                }

                allMap.entrySet().forEach(entry -> {
                    List<String> asdsad = entry.getValue();
                    asdsad.forEach(ss -> {
                        if (ss.contains("ServerAclController.createServerOrder")) {
                            map2.put(entry.getKey(), entry.getValue());
                        }
                    });
                });
                allMap.clear();
                bf.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("共记一段请求:" + map2.size());
        System.out.println("共记二段请求:" + map31.size());
        System.out.println("一段请求时长");
        analysis(map2);
        System.out.println("二段请求时长");
        analysis(map31);
    }


    private static void analysis(Map<String, List<String>> map) {
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
            //System.out.println("请求结束：" + end);
            //  System.out.println("请求结束22：" + asdsad.get(0));
            try {
                Date asd = sdf.parse(start);
                Date sad = sdf.parse(end);

                //  System.out.println(sad.getTime() - asd.getTime() + "毫秒");
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


    /**
     *
     */
}
