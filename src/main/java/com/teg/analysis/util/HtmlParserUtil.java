package com.teg.analysis.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class HtmlParserUtil {


    public static Map<String, String> parseTaiYueForOne(String text) {
        Map<String, String> map = Maps.newHashMap();
        if (StringUtils.isEmpty(text)) {
            return map;
        }
        String split = text.substring(text.indexOf("onclick=\"openSheet(")).substring(0, text.substring(text.indexOf("onclick=\"openSheet(")).indexOf(")"));
        String[] splitArray = split.split("'");
        map.put("baseSchema", splitArray[1]);
        map.put("baseId", splitArray[3]);
        return map;

    }


    public static Map<String, String> parseTaiYueDetail(String text) {
        Map<String, String> map = Maps.newHashMap();
        if (StringUtils.isEmpty(text)) {
            return map;
        }
        List<Node> nodeList = Jsoup.parse(text).body().childNodes();
        nodeList.forEach(node -> {
            if (node instanceof FormElement && ((FormElement) node).id().equals("bpp_WorksheetForm")) {
                FormElement formElement = (FormElement) node;
                formElement.elements().forEach(e -> {
                    if (!StringUtils.isEmpty(e.val())) {
                        e.attributes().forEach(attribute -> {
                            if (attribute.getKey().equals("name")) {
                                map.put(attribute.getValue(), e.val());
                            }
                        });
                    }
                });
            }
        });
        log.info(JSONObject.toJSONString(map));
        return map;
    }


/*
    public static Set<String> parseTaiYueSearchResultHtml(String text) {
        Set<String> workOrderSet = Sets.newHashSet();
        if (StringUtils.isEmpty(text)) {
            return workOrderSet;
        }
        List<Node> nodeList = Jsoup.parse(text).body().childNodes();
        nodeList.forEach(node -> {
            if (node instanceof FormElement && ((FormElement) node).id().equals("form1")) {
                FormElement formElement = (FormElement) node;
                List<Node> secondChildNodes = formElement.childNodes();
                secondChildNodes.forEach(node1 -> {
                    if (node1 instanceof Element) {
                        Element element = (Element) node1;
                        List<Node> nodeList1 = element.childNodes();
                        nodeList1.forEach(node2 -> {
                            if (node2 instanceof Element && node2.attr("class").equals("scroll_div")) {
                                Element e = (Element) node2;
                                if (e.childNodeSize() > 0) {
                                    Element element1 = e.child(0);
                                    element1.childNodes().forEach(node3 -> {
                                        if (node3 instanceof Element) {
                                            Elements odd = ((Element) node3).getElementsByClass("odd");
                                            for (int i = 0; i < odd.size(); i++) {
                                                if (i % 2 == 0) {
                                                    workOrderSet.add(odd.get(i).child(0).child(0).childNode(0).toString());
                                                }
                                            }
                                            Elements even = ((Element) node3).getElementsByClass("even");
                                            for (int i = 0; i < even.size(); i++) {
                                                if (i % 2 == 0) {
                                                    workOrderSet.add(even.get(i).child(0).child(0).childNode(0).toString());
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });

            }
        });
        return workOrderSet;
    }
*/




    public static Map<String,String> parseTaiYueSearchResultHtml(String text) {
        Map<String,String> workOrderMap = Maps.newHashMap();
        if (StringUtils.isEmpty(text)) {
            return workOrderMap;
        }
        List<Node> nodeList = Jsoup.parse(text).body().childNodes();
        nodeList.forEach(node -> {
            if (node instanceof FormElement && ((FormElement) node).id().equals("form1")) {
                FormElement formElement = (FormElement) node;
                List<Node> secondChildNodes = formElement.childNodes();
                secondChildNodes.forEach(node1 -> {
                    if (node1 instanceof Element) {
                        Element element = (Element) node1;
                        List<Node> nodeList1 = element.childNodes();
                        nodeList1.forEach(node2 -> {
                            if (node2 instanceof Element && node2.attr("class").equals("scroll_div")) {
                                Element e = (Element) node2;
                                if (e.childNodeSize() > 0) {
                                    Element element1 = e.child(0);
                                    element1.childNodes().forEach(node3 -> {
                                        if (node3 instanceof Element) {
                                            Elements odd = ((Element) node3).getElementsByClass("odd");
                                            for (int i = 0; i < odd.size(); i++) {
                                                if (i % 2 == 0) {
                                                    workOrderMap.put(odd.get(i).child(0).child(0).childNode(0).toString()
                                                    ,odd.get(i).child(3).child(0).childNode(0).toString());
                                                }
                                            }
                                            Elements even = ((Element) node3).getElementsByClass("even");
                                            for (int i = 0; i < even.size(); i++) {
                                                if (i % 2 == 0) {
                                                    workOrderMap.put(even.get(i).child(0).child(0).childNode(0).toString()
                                                            ,even.get(i).child(3).child(0).childNode(0).toString());
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });

            }
        });
        return workOrderMap;
    }
}
