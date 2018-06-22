package com.walker.utils;


import com.bsx.baolib.log.BaoLog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * summary :时间日期辅助类
 * time    :2016/5/23 13:30
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class DateTimeUtil {
    /**
     * 获取通用的时间（精确到秒）
     *
     * @return 通用的时间
     */
    public static String getNormalDate() {
        String time = "";
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            time = sf.format(date);
        } catch (Exception e) {
            BaoLog.e("getNormalDate", e);
        }
        return time;
    }

    /**
     * 获取当前时间点
     *
     * @return 当前时间的long型值
     */
    public static long getCurrentTime() {
        long time = 0;
        try {
            Date date = new Date();
            time = date.getTime();
        } catch (Exception e) {
            BaoLog.e("getCurrentTime", e);
        }
        return time;
    }

}
