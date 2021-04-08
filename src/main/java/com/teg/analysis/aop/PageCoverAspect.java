package com.teg.analysis.aop;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author wangyuan
 * @date 2020/8/6 17:22
 */

@Aspect
@Component
@Slf4j
public class PageCoverAspect {


    /**
     * 以自定义 @Log 注解为切点
     */
    @Pointcut("@annotation(com.teg.analysis.aop.PageCover)")
    public void pageCover() {
    }

    /**
     * 在切点之前织入
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before("pageCover()")
    public void doBefore(JoinPoint joinPoint) {
        Object[] obj = joinPoint.getArgs();
        Map<String, Integer> map = Maps.newHashMap();
        for (Object argItem : obj) {
            Field[] fields = argItem.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (field.getName().equals("page")) {
                    field.setAccessible(true);//去除权限
                    try {
                        Integer page = (Integer) field.get(argItem);
                        map.put("page", page);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
                if (field.getName().equals("pageSize")) {
                    field.setAccessible(true);//去除权限
                    try {
                        Integer pageSize = (Integer) field.get(argItem);
                        map.put("pageSize", pageSize);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!CollectionUtils.isEmpty(map) && map.size() == 2 && !ObjectUtils.isEmpty(map.get("page"))
                    && !ObjectUtils.isEmpty(map.get("pageSize"))) {
                try {
                    Field field = argItem.getClass().getDeclaredField("page");
                    field.setAccessible(true);//去除权限
                    try {
                        // map.put("page", map.get("page") - 1);
                        int asd = (map.get("page") - 1) * map.get("pageSize");
                        field.set(obj, new Integer(asd));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                break;
            }
            map.clear();

        }


    }


}
