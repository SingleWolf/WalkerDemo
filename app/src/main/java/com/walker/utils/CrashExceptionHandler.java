package com.walker.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.view.WindowManager;

import com.bsx.baolib.log.BaoLog;


/**
 * summary :app崩溃异常处理器
 * time    :2016/5/23 10:52
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {

    /**
     * 上下文
     */
    private Context mApplicationContext;
    /**
     * 向远程服务器发送错误信息
     */
    private CrashExceptionRemoteReport mCrashExceptionRemoteReport;

    /**
     * @param context 上下文
     */
    public CrashExceptionHandler(Context context) {
        mApplicationContext = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        handleException(ex);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //杀死进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 配置远程传回log到服务器的设置
     *
     * @param crashExceptionRemoteReport
     */
    public void setRemoteReport(CrashExceptionRemoteReport crashExceptionRemoteReport) {
        this.mCrashExceptionRemoteReport = crashExceptionRemoteReport;
    }

    /**
     * 处理异常
     *
     * @param ex 异常信息
     */
    private void handleException(Throwable ex) {
        if (ex == null) {
            return;
        } else {
            saveCrashInfoToFile(ex);
            sendCrashInfoToServer(ex);

            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    try {
                        showRestartDialog();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Looper.loop();
                }
            }.start();
        }
    }

    /**
     * 保存闪退信息到本地文件中
     *
     * @param ex 异常信息
     */
    private void saveCrashInfoToFile(Throwable ex) {
        //记录闪退环境的信息
        BaoLog.e("************** REPORT START ******************" + "\n");
        BaoLog.e("------------Crash Environment Info------------" + "\n");
        BaoLog.e("------------Manufacture: " + DeviceUtil.getDeviceManufacture() + "------------" + "\n");
        BaoLog.e("------------DeviceName: " + DeviceUtil.getDeviceName() + "------------" + "\n");
        BaoLog.e("------------SystemVersion: " + DeviceUtil.getSystemVersion() + "------------" + "\n");
        BaoLog.e("------------DeviceIMEI: " + DeviceUtil.getImeiInfo() + "------------" + "\n");
        BaoLog.e("------------AppVersion: " + APPUtil.getVersionName() + "------------" + "\n" + "\n");
        BaoLog.e("------------Crash Exception Info------------" + "\n");
        BaoLog.e(ex.toString() + "\n");
        BaoLog.e("*************** REPORT END *******************" + "\n");
    }

    /**
     * 发送发送闪退信息到远程服务器
     *
     * @param ex 异常信息
     */
    private void sendCrashInfoToServer(Throwable ex) {
        if (mCrashExceptionRemoteReport != null) {
            mCrashExceptionRemoteReport.onCrash(ex);
        }
    }

    /**
     * 闪退日志远程奔溃接口，主要考虑不同app下，把log回传给服务器的方式不一样，所以此处留一个对外开放的接口
     */
    public static interface CrashExceptionRemoteReport {
        /**
         * 当闪退发生时，回调此接口函数
         *
         * @param ex 异常信息
         */
        public void onCrash(Throwable ex);
    }

    /**
     * 询问是否重启程序的对话框
     */
    private void showRestartDialog() {
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(mApplicationContext);
        dialogbuilder.setTitle("保神先");
        dialogbuilder.setMessage("哎呀，程序报错了。。。");
        dialogbuilder.setCancelable(false);
        dialogbuilder.setPositiveButton("重新启动", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                APPUtil.reStart(mApplicationContext);
            }
        });
        AlertDialog alertDialog = dialogbuilder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        alertDialog.show();
    }
}
