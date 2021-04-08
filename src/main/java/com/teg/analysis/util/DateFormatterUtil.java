package com.teg.analysis.util;

import com.teg.analysis.exception.BusinessException;
import com.teg.analysis.model.DO.UserDO;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangyuan
 * @date 2020/8/7 15:29
 */
@Slf4j
public class DateFormatterUtil {


    private static ThreadLocal<SimpleDateFormat> outDateFormatHolder = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    private static ThreadLocal<SimpleDateFormat> inDateFormatHolder = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));

    public static Date parseDate(String date) {
        try {
            return inDateFormatHolder.get().parse(date);
        } catch (ParseException e) {
            log.error("时间格式转换异常", e);
            throw new BusinessException("时间格式异常");
        }
    }

    public static Date parseDate2(String date) {
        try {
            return outDateFormatHolder.get().parse(date);
        } catch (ParseException e) {
            log.error("时间格式转换异常", e);
            throw new BusinessException("时间格式异常");
        }
    }


    public static String formatDate(Date date) {
        return inDateFormatHolder.get().format(date);
    }

    public static String formatDate2(Date date) {
        return outDateFormatHolder.get().format(date);
    }

    public static void main(String[] args) {
        UserDO as = (UserDO) null;
        System.out.println(as);


    }
}