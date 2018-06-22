package com.walker.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.walker.WalkerApplication;


/**
 * summary : Toast统一管理类
 * time    : 2016/5/10 14:40
 * e-mail  : singlewolf968@gmail.com
 *
 * @author : Walker
 */
public class ToastUtil {
    /**
     * 短时间显示Toast
     *
     * @param message 提示信息
     */
    public static void showShort(CharSequence message) {
        Toast.makeText(WalkerApplication.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时间显示Toast
     *
     * @param messageId 提示信息的资源id
     */
    public static void showShort(int messageId) {
        Toast.makeText(WalkerApplication.getContext(), messageId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message 提示信息
     */

    public static void showLong(CharSequence message) {
        Toast.makeText(WalkerApplication.getContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param messageId 提示信息的资源id
     */
    public static void showLong(int messageId) {
        Toast.makeText(WalkerApplication.getContext(), messageId, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间居中显示
     *
     * @param message 提示信息
     */
    public static void showCenterShort(CharSequence message) {
        Toast toast = Toast.makeText(WalkerApplication.getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 短时间居中显示
     *
     * @param messageId 提示信息的资源id
     */
    public static void showCenterShort(int messageId) {
        Toast toast = Toast.makeText(WalkerApplication.getContext(), messageId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 长时间居中显示
     *
     * @param message 提示信息
     */
    public static void showCenterLong(CharSequence message) {
        Toast toast = Toast.makeText(WalkerApplication.getContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 长时间居中显示
     *
     * @param messageId 提示信息的资源id
     */
    public static void showCenterLong(int messageId) {
        Toast toast = Toast.makeText(WalkerApplication.getContext(), messageId, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message  提示信息
     * @param duration 时长
     */
    public static void show(CharSequence message, int duration) {
        Toast.makeText(WalkerApplication.getContext(), message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param messageId 提示信息的资源id
     * @param duration  时长
     */
    public static void show(int messageId, int duration) {
        Toast.makeText(WalkerApplication.getContext(), messageId, duration).show();
    }
}
