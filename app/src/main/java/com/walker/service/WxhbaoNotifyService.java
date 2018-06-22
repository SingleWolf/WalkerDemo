package com.walker.service;

import android.annotation.TargetApi;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Pair;

import com.bsx.baolib.log.BaoLog;
import com.walker.constant.HbaoConfig;
import com.walker.data.sp.SPHelper;

/**
 * summary :抢红包快速监听通知
 * time    :2016/8/11 09:36
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class WxhbaoNotifyService extends NotificationListenerService {
    /**
     * 微信的包名
     */
    public static final String WECHAT_PACKAGENAME = "com.tencent.mm";
    /**
     * 本服务实例
     */
    private static WxhbaoNotifyService mInstance;

    @Override
    public void onDestroy() {
        super.onDestroy();
        mInstance = null;
        BaoLog.i("-->WxhbaoNotifyService onDestroy");
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        mInstance = this;
        BaoLog.i("-->WxhbaoNotifyService onListenerConnected");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if(!TextUtils.equals(WECHAT_PACKAGENAME,sbn.getPackageName())){
            return;
        }
        boolean isStart = SPHelper.getBoolean(HbaoConfig.KEY_MODE_FASTEST, false);
        if (!isStart) {
            return;
        }
        if (WxAutoHbaoService.isRunning()) {
            WxAutoHbaoService.onNotificationPosted(Pair.create(sbn.getPackageName(), sbn.getNotification()));
            BaoLog.i("-->WxhbaoNotifyService onNotificationPosted 时WxAutoHbaoService正在运行");
        } else {
            BaoLog.i("-->WxhbaoNotifyService onNotificationPosted 时WxAutoHbaoService已经断开");
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    /**
     * 是否启动通知栏监听
     */
    public static boolean isRunning() {
        if (mInstance == null) {
            return false;
        }
        return true;
    }
}
