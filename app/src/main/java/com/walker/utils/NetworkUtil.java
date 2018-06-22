package com.walker.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bsx.baolib.log.BaoLog;
import com.walker.WalkerApplication;

import java.io.IOException;

/**
 * summary :手机网络相关的工具类
 * time    :2016/5/16 10:18
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class NetworkUtil {

    /**
     * 是否网络在线
     *
     * @return boolean
     */
    public static boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) WalkerApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 通过ping百度的网址判断网络是否真正可用
     *
     * @return boolean
     */
    public static boolean isNetOK() {
        if (!isOnline()) {
            return false;
        } else {
            boolean isNetOK=false;
            try {
                String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
                Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
                // ping的状态
                int status = p.waitFor();
                if (status == 0) {
                    isNetOK = true;
                }
            } catch (IOException e) {
                BaoLog.e("isNetOK",e);
                isNetOK = false;
            } catch (InterruptedException e) {
                BaoLog.e("isNetOK",e);
                isNetOK = false;
            } finally {
                return isNetOK;
            }
        }
    }

    /**
     * 判断是否为WIFI连接
     *
     * @return boolean
     */
    public static boolean isWifi() {
        ConnectivityManager cm = (ConnectivityManager) WalkerApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            return false;
        }
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 打开网络设置界面
     *
     * @param activity 触发活动
     */

    public static void openSetting(Activity activity) {
        try {
            Intent intent = null;
            // 判断手机系统的版本 即API大于10 就是3.0或以上版本
            if (android.os.Build.VERSION.SDK_INT > 10) {
                intent = new Intent(
                        android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            } else {
                intent = new Intent();
                ComponentName component = new ComponentName(
                        "com.android.settings",
                        "com.android.settings.WirelessSettings");
                intent.setComponent(component);
                intent.setAction("android.intent.action.VIEW");
            }
            activity.startActivity(intent);
        } catch (Exception e) {
            BaoLog.e("openSetting",e);
        }
    }

}
