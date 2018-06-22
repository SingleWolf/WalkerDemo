package com.walker.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import com.walker.WalkerApplication;

import java.io.File;
import java.io.FileInputStream;

/**
 * summary :获取设备信息，比如mac、imei、手机号等
 * time    :2016/5/10 14:00
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class DeviceUtil {
    /**
     * 获取设备的mac地址
     * 这里要特别说明一下,mac地址不是一定能获取的到的,你可能要更优先使用设备ID
     *
     * @return String
     */
    public static String getMac() {

        String result = null;
        Context context = WalkerApplication.getContext();
        try {
            String path = "sys/class/net/wlan0/address";
            if ((new File(path)).exists()) {
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer = new byte[8192];
                int byteCount = fis.read(buffer);
                if (byteCount > 0) {
                    result = new String(buffer, 0, byteCount, "utf-8");
                }
            }
            if (TextUtils.isEmpty(result)) {
                path = "sys/class/net/eth0/address";
                FileInputStream fisName = new FileInputStream(path);
                byte[] bufferName = new byte[8192];
                int byteCountName = fisName.read(bufferName);
                if (byteCountName > 0) {
                    result = new String(bufferName, 0, byteCountName, "utf-8");
                }
            }

            if (TextUtils.isEmpty(result)) {
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifi.getConnectionInfo();
                result = wifiInfo.getMacAddress();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取手机imei
     *
     * @return String
     */
    public static String getImeiInfo() {
        Context context = WalkerApplication.getContext();
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 抓取设备所插入的手机号码
     *
     * @return String
     */
    public static String getPhoneNum() {
        String number = "";
        Context context = WalkerApplication.getContext();
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            number = tm.getLine1Number(); // 手机号码，有的可得，有的不可得
        } catch (Exception e) {
            e.printStackTrace();
        }
        return number;
    }

    /**
     * 获取设备的制造商
     *
     * @return 设备制造商
     */
    public static String getDeviceManufacture() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取设备名称
     *
     * @return 设备名称
     */
    public static String getDeviceName() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }
}

