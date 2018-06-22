package com.walker.utils;

import java.util.ArrayList;

/**
 * summary : 字符串工具类
 * time    : 2016/5/11 15:05
 * e-mail  : singlewolf968@gmail.com
 *
 * @author : Walker
 */
public class StringUtil {

    /**
     * 拼接字符串
     *
     * @param val_1 拼接目标1
     * @param val_2 拼接目标2
     * @return String
     */
    public static String pliceStr(Object val_1, Object val_2) {
        StringBuilder builder = new StringBuilder();
        builder.append(val_1).append(val_2);
        String result = builder.toString().trim();
        builder = null;
        return result;
    }

    /**
     * 拼接字符串
     *
     * @param val_1 拼接目标1
     * @param val_2 拼接目标2
     * @param val_3 拼接目标3
     * @return String
     */
    public static String pliceStr(Object val_1, Object val_2, Object val_3) {
        StringBuilder builder = new StringBuilder();
        builder.append(val_1).append(val_2).append(val_3);
        String result = builder.toString().trim();
        builder = null;
        return result;
    }

    /**
     * 拼接字符串
     *
     * @param val_1 拼接目标1
     * @param val_2 拼接目标2
     * @param val_3 拼接目标3
     * @param val_4 拼接目标4
     * @return String
     */
    public static String pliceStr(Object val_1, Object val_2, Object val_3, Object val_4) {
        StringBuilder builder = new StringBuilder();
        builder.append(val_1).append(val_2).append(val_3).append(val_4);
        String result = builder.toString().trim();
        builder = null;
        return result;
    }

    /**
     * 拼接字符串
     *
     * @param val_1 拼接目标1
     * @param val_2 拼接目标2
     * @param val_3 拼接目标3
     * @param val_4 拼接目标4
     * @param val_5 拼接目标5
     * @return String
     */
    public static String pliceStr(Object val_1, Object val_2, Object val_3, Object val_4, Object val_5) {
        StringBuilder builder = new StringBuilder();
        builder.append(val_1).append(val_2).append(val_3).append(val_4).append(val_5);
        String result = builder.toString().trim();
        builder = null;
        return result;
    }

    /**
     * 拼接字符串
     *
     * @param vallist 拼接集合
     * @return String
     */
    public static String pliceStr(ArrayList<Object> vallist) {
        StringBuilder builder = new StringBuilder();
        for (Object val : vallist) {
            builder.append(val);
        }
        String result = builder.toString().trim();
        vallist.clear();
        builder = null;
        return result;
    }
}
