package com.walker.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.bsx.baolib.log.BaoLog;
import com.walker.WalkerApplication;


/**
 * summary : APP辅助类，获取应用版本，名称，签名，清理缓存等
 * time    : 2016/5/10 13:42
 * e-mail  : singlewolf968@gmail.com
 *
 * @author : Walker
 */
public class APPUtil {
    /**
     * 获取app的版本数versionCode,比如38
     *
     * @return int
     */
    public static int getVersionCode() {
        int result = 0;
        Context context = WalkerApplication.getContext();
        String packageName = context.getPackageName();
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            result = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError(e);
        }
        return result;
    }

    /**
     * 获取app的版本名versionName,比如0.6.9
     *
     * @return String
     */
    public static String getVersionName() {
        String result = null;
        Context context = WalkerApplication.getContext();
        String packageName = context.getPackageName();
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            result = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError(e);
        }
        return result;
    }

    /**
     * 获取app的名称
     *
     * @return String
     */
    public static String getAppName() {
        String result = null;
        Context context = WalkerApplication.getContext();
        String packageName = context.getPackageName();
        ApplicationInfo applicationInfo;
        try {
            PackageManager packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            result = packageManager.getApplicationLabel(applicationInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取应用公钥签名
     *
     * @return Signature
     */
    public static Signature getSignature() {
        PackageInfo pi;
        Signature sign = null;
        Context context = WalkerApplication.getContext();
        try {
            pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            sign = pi.signatures[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

    /**
     * 比较当前签名HashCode和预设的HashCode
     *
     * @param presetHashCode 预设的签名hashcode
     * @return
     */
    public static boolean checkSignature(int presetHashCode) {
        Signature signature = getSignature();
        return signature.hashCode() == presetHashCode;
    }

    /**
     * 重启程序
     *
     * @param context 上下文
     */
    public static void reStart(Context context) {
        try {
            ActivityCollector.finishAll();
            /** 启动应用 */
            Intent launch = context.getPackageManager()
                    .getLaunchIntentForPackage(context.getPackageName());
            launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(launch);
        } catch (Exception e) {
            BaoLog.e("reStart", e);
        }
    }

}
