package com.walker.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * summary :活动管理器
 * time    :2016/5/9 15:46
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class ActivityCollector {
    /**
     * 所有开启活动的集合
     */
    private static List<Activity> ACTIVITYS = new ArrayList<Activity>();

    /**
     * 获取活动集合
     *
     * @return List<Activity>
     */
    public static List<Activity> getActivitys() {
        return ACTIVITYS;
    }

    /**
     * 添加新开启的活动
     *
     * @param activity 目标活动
     */
    public static void addActivity(Activity activity) {
        ACTIVITYS.add(activity);
    }

    /**
     * 移出活动
     *
     * @param activity 目标活动
     */
    public static void removeActivity(Activity activity) {
        ACTIVITYS.remove(activity);
    }

    /**
     * 终止所有活动
     */
    public static void finishAll() {
        for (Activity activity : ACTIVITYS) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 判断活动栈是否为空
     *
     * @return boolean
     */
    public static boolean isEmpty() {
        return ACTIVITYS.isEmpty();
    }
}
