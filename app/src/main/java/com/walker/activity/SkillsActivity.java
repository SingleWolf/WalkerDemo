package com.walker.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RemoteViews;

import com.walker.R;
import com.walker.base.BasePureActivity;
import com.walker.utils.ToastUtil;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;

/**
 * summary :实用小技巧
 * time    :2016/9/19 10:11
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class SkillsActivity extends BasePureActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.act_skills;
    }

    @Override
    protected void initialization(Bundle savedInstanceState) {
        initToolBar();
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }

    /**
     * 初始化toolbar
     */
    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * 判断是否Root
     *
     * @param v 触发组件
     */
    public void onIsRoot(View v) {
        if (isDeviceRooted()) {
            ToastUtil.showShort("The mobile is had been Rooted !");
        } else {
            ToastUtil.showShort("The mobile is not Rooted !");
        }

    }

    /**
     * 判断机器Android是否已经root，即是否获取root权限
     *
     * @return boolean
     */
    private boolean isDeviceRooted() {
        if (checkBuildTags()) {
            return true;
        }
        if (checkSuperuser()) {
            return true;
        }
        int i = execRootCmdSilent("echo test"); // 通过执行测试命令来检测
        if (i != -1) {
            return true;
        }
        return false;
    }

    /**
     * 检查buildTags是否包含test-keys
     *
     * @return boolean
     */
    public boolean checkBuildTags() {
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }
        return false;
    }

    /**
     * 检查是否有Superuser.apk
     *
     * @return boolean
     */
    public boolean checkSuperuser() {
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 通过执行测试命令来检测
     *
     * @param paramString 命令语句
     * @return int
     */
    private int execRootCmdSilent(String paramString) {
        Process localProcess = null;
        int result = -1;
        try {
            localProcess = Runtime.getRuntime().exec("su");
            Object localObject = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(
                    (OutputStream) localObject);
            String str = String.valueOf(paramString);
            localObject = str + "\n";
            localDataOutputStream.writeBytes((String) localObject);
            localDataOutputStream.flush();
            localDataOutputStream.writeBytes("exit\n");
            localDataOutputStream.flush();
            localProcess.waitFor();
            result = localProcess.exitValue();
        } catch (Exception localException) {
            localException.printStackTrace();
        } finally {
            if (localProcess != null) {
                try {
                    localProcess.destroy();
                } catch (Exception e) {
                }
            }
            return result;
        }
    }

    /**
     * 通过su文件简单判断是否root
     *
     * @param v
     */
    public void onIsRootBySu(View v) {
        boolean root = false;

        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                root = false;
            } else {
                root = true;
            }

        } catch (Exception e) {
        }
        if (root) {
            ToastUtil.showShort("The mobile is had been Rooted !");
        } else {
            ToastUtil.showShort("The mobile is not Rooted !");
        }
    }

    /**
     * 发送默认通知
     *
     * @param v
     */
    public void onSendDefaultNotify(View v) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("这是通知标题");
        builder.setContentText("这是通知内容");
        builder.setColor(Color.parseColor("#EAA935"));
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
       // builder.setPriority(1000);
        builder.setTicker("waker default notify");
        Uri sound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrate = {0, 100, 200, 300};
        builder.setSound(sound);
        builder.setVibrate(vibrate);
        builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        /*builder.setAutoCancel(true);//点击后自动清除
        builder.setShowWhen(false);//不显示时间
        builder.setOngoing(true);//常驻通知
        Intent intent = new Intent(this, WebActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);*/
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR
                | Notification.FLAG_ONGOING_EVENT;
        manager.notify(1, notification);
    }

    /**
     * 发送自定义通知
     *
     * @param v
     */
    public void onSendCustomeNotify(View v) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custome_notify);
        builder.setContent(remoteViews);
        Intent intent = new Intent(this, WebActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.tv_reply, pendingIntent);
        //builder.setPriority(1000);
        builder.setTicker("waker custome notify");
        Uri sound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrate = {0, 100, 200, 300};
        builder.setSound(sound);
        builder.setVibrate(vibrate);
        builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR
                | Notification.FLAG_ONGOING_EVENT;
        manager.notify(2, notification);
    }

}
