package com.walker.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bsx.baolib.log.BaoLog;


/**
 * summary :采用灰色手法尽可能守护程序进程
 * time    :2016/6/1 10:37
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class GrayService extends Service {
    /**
     * 守护服务id
     */
    private final static int GRAY_SERVICE_ID = 1001;
    /**
     * 启动服务的通信意图
     */
    private static Intent SERVICE_INTENT;

    /**
     * 开启守护服务
     *
     * @param context 上下文
     */
    public static void startAction(Context context) {
        SERVICE_INTENT = new Intent(context, GrayService.class);
        context.startService(SERVICE_INTENT);
        BaoLog.d("手动开启守护服务");
    }

    /**
     * 终止守护
     *
     * @param context 上下文
     */
    public static void stopAction(Context context) {
        if (SERVICE_INTENT != null) {
            context.stopService(SERVICE_INTENT);
        }
        BaoLog.d("手动终止守护服务");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());
        } else {
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }
        flags = START_STICKY;// START_STICKY是service被kill掉后自动重写创建
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (SERVICE_INTENT != null) {
            SERVICE_INTENT = null;
        }
        BaoLog.d("守护服务onDestroy");
    }

    /**
     * 内置服务，配合完成进程守护
     */
    public static class GrayInnerService extends Service {

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
    }
}
