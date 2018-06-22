package com.walker.data.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.walker.WalkerApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * summary :偏好存储辅助类
 * time    :2016/5/10 14:58
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class SPHelper {

    /**
     * 偏好存储的默认文件名
     */
    public static final String SHARED_NAME = "walker_default_sp";

    /**
     * 存入数据
     *
     * @param key   键
     * @param value 值
     */
    public static <T> void put(String key, T value) {
        SharedPreferences sp = WalkerApplication.getContext().getSharedPreferences(SHARED_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (value == null) {
            // if value is null, just handler it as a String
            editor.putString(key, null);
        } else {
            if (value.getClass() == Boolean.class) {
                editor.putBoolean(key, (Boolean) value);
            } else if (value.getClass() == Float.class) {
                editor.putFloat(key, (Float) value);
            } else if (value.getClass() == Integer.class) {
                editor.putInt(key, (Integer) value);
            } else if (value.getClass() == Long.class) {
                editor.putLong(key, (Long) value);
            } else if (value.getClass() == String.class) {
                editor.putString(key, (String) value);
            } else {
                throw new RuntimeException("the put value type can't support.");
            }
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 根据键值获取数据
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return String
     */
    public static String getString(String key, String defaultValue) {
        SharedPreferences sp = WalkerApplication.getContext().getSharedPreferences(SHARED_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    /**
     * 根据键值获取数据
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return boolean
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sp = WalkerApplication.getContext().getSharedPreferences(SHARED_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * 根据键值获取数据
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return float
     */
    public static float getFloat(String key, float defaultValue) {
        SharedPreferences sp = WalkerApplication.getContext().getSharedPreferences(SHARED_NAME,
                Context.MODE_PRIVATE);
        return sp.getFloat(key, defaultValue);
    }

    /**
     * 根据键值获取数据
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return int
     */
    public static int getInt(String key, int defaultValue) {
        SharedPreferences sp = WalkerApplication.getContext().getSharedPreferences(SHARED_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    /**
     * 根据键值获取数据
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return long
     */
    public static long getLong(String key, long defaultValue) {
        SharedPreferences sp = WalkerApplication.getContext().getSharedPreferences(SHARED_NAME,
                Context.MODE_PRIVATE);
        return sp.getLong(key, defaultValue);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key 键
     * @return boolean
     */
    public static boolean isContains(String key) {
        SharedPreferences sp = WalkerApplication.getContext().getSharedPreferences(SHARED_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return Map<String, ?>
     */
    public static Map<String, ?> getAll() {
        SharedPreferences sp = WalkerApplication.getContext().getSharedPreferences(SHARED_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 根据键值移除对应的数据
     *
     * @param key 键
     */
    public static void remove(String key) {
        SharedPreferences sp = WalkerApplication.getContext().getSharedPreferences(SHARED_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除偏好存储的数据
     */
    public static void clear() {
        SharedPreferences sp = WalkerApplication.getContext().getSharedPreferences(SHARED_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }


    /**
     * Unlike commit(), which writes its preferences out to persistent storage synchronously,
     * apply() commits its changes to the in-memory SharedPreferencesimmediately
     * but starts an asynchronous commit to disk and you won't be notified of any failures.
     * If another editor on this SharedPreferences does a regularcommit() while a apply() is still outstanding,
     * the commit() will block until all async commits are completed as well as the commit itself.
     */
    private static class SharedPreferencesCompat {
        /**
         * 异步提交的方法
         */
        private static final Method APPLY_METHOD = findApplyMethod();

        /**
         * check apply mthod by reflect
         *
         * @return Method
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * if it has apply(), use apply() first;
         * else just use the commit().
         *
         * @param editor 编辑器
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (APPLY_METHOD != null) {
                    APPLY_METHOD.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            editor.commit();
        }
    }
}
