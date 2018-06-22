package com.walker.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * summary :内存信息收集器
 * time    :2016/5/9 15:56
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class MemoryCollector {
    /**
     * 内存信息存储集合
     */
    private static List<IMemoryInfo> MEMERY_INFO_LIST = new ArrayList<IMemoryInfo>();

    /**
     * 释放内存的委托接口
     */
    public interface IMemoryInfo {
        /**
         * 释放内存动作
         */
        void goodTimeToReleaseMemory();
    }

    /**
     * 获取内存信息存储集合
     *
     * @return List<IMemoryInfo>
     */
    public static List<IMemoryInfo> getMemeryList() {
        return MEMERY_INFO_LIST;
    }

    /**
     * 监听内存
     *
     * @param implementor 委托接口
     */
    public static void registerMemoryListener(IMemoryInfo implementor) {
        MEMERY_INFO_LIST.add(implementor);
    }

    /**
     * 取消监听内存
     *
     * @param implementor 委托接口
     */
    public static void unregisterMemoryListener(IMemoryInfo implementor) {
        MEMERY_INFO_LIST.remove(implementor);
    }
}
