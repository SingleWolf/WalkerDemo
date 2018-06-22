package com.walker;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;

import com.bsx.baolib.log.BaoLog;
import com.bsx.baolib.okhttp.OkHttpUtils;
import com.squareup.leakcanary.LeakCanary;
import com.walker.constant.BaseParams;
import com.walker.data.storage.StorageHelper;
import com.walker.service.GrayService;
import com.walker.utils.CrashExceptionHandler;
import com.walker.utils.MemoryCollector;
import com.walker.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * summary :项目的Application
 * time    :2016/5/9 14:54
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class WalkerApplication extends Application {
    /**
     * 全局的上下文
     */
    private static Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        initLeakCanary();
        initData();
        initLog();
        initCrash();
        initOkHttp();
        GrayService.startAction(this);
    }


    /**
     * 初始化数据
     */
    private void initData() {
        CONTEXT = this;
    }

    /**
     * 初始化日志
     */
    private void initLog() {
        // 初始化日志记录
        BaoLog log = new BaoLog(this);
        // 日志保存的文件全路径（默认保存在本程序目录）
        String filePath = StringUtil.pliceStr(StorageHelper.getWalkerRootPath(), File.separator, BaseParams.DIR_LOG, File.separator, BaseParams.FILE_LOG);
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 需要保存到文件
        boolean needSaveFile = true;
        // 在LogCat显示
        boolean showLogCat = BuildConfig.DEBUG;
        //控制级别
        int minLevel;
        if (BuildConfig.DEBUG) {
            minLevel = BaoLog.DEBUG;
        } else {
            minLevel = BaoLog.DEBUG;
        }
        log.setArgument(filePath, needSaveFile, showLogCat, minLevel);
    }

    /**
     * 初始化异常捕获
     */
    private void initCrash() {
        CrashExceptionHandler crashExceptionHandler = new CrashExceptionHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(crashExceptionHandler);
    }

    /**
     * 初始化Okhttp
     */
    private void initOkHttp() {
        if (BuildConfig.DEBUG) {
            OkHttpUtils.getInstance().debug("WalkerHttp:", true).setConnectTimeout(100000, TimeUnit.MILLISECONDS);
        } else {
            OkHttpUtils.getInstance().setConnectTimeout(100000, TimeUnit.MILLISECONDS);
        }
        //使用https，但是默认信任全部证书
        OkHttpUtils.getInstance().setCertificates();
    }

    /**
     * debug模式初始化性能监测
     */
    private void initLeakCanary() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
    }


    /*
    * 获取Application级的上下文(不可用于启动activity、显示Dialog、Layout Inflation)
    * */
    public static Context getContext() {
        return CONTEXT;
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // don't compare with == as intermediate stages also can be reported,
        // always better to check >= or <=
        if (level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
            try {
                // Activity at the front will get earliest than activity at the
                // back
                for (int index = MemoryCollector.getMemeryList().size() - 1; index >= 0; index--) {
                    try {
                        MemoryCollector.getMemeryList().get(index)
                                .goodTimeToReleaseMemory();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
