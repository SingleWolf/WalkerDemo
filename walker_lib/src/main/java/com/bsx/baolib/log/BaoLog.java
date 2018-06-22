package com.bsx.baolib.log;

import android.content.Context;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * summary :日志工具类
 * time    :2016/5/11 16:33
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class BaoLog {
    /**
     * 调试信息标识
     */

    public static final int DEBUG = 1;

    /**
     * 重要数据标识
     */

    public static final int INFO = 2;

    /**
     * 警告信息标识
     */

    public static final int WARN = 3;

    /**
     * 错误信息标识
     */

    public static final int ERROR = 4;

    /**
     * 全部打印标识
     */

    public static final int NOTHING = 5;

    /**
     * 选择的最低打印级别
     */

    private static int MINLEVEL;

    /**
     * 上下文引用
     */

    private final Context mContext;

    /**
     * 日志文件名
     */

    private String mLogFileName;

    /**
     * 最大备份文件数
     */

    private int mMaxBackupSize;

    /**
     * 单个文件最大存储
     */

    private long mMaxFileSize;

    /**
     * log4j下日志级别
     */

    private Level mLogLevel;

    /**
     * 保存文件标识
     */

    private boolean mNeedSaveFile;

    /**
     * 显示在LogCat标识
     */

    private boolean mShowLogCat;

    /**
     * 文件保存在程序私有file文件目录下的log.txt,<br>
     * 默认最大备份文件是2个，单个文件最大5M<br>
     * 保存在log.txt文件中，在LogCat中显示
     */

    public BaoLog(Context context) {
        this.mContext = context;
        init();
    }

    /**
     * 初始化操作
     */

    private void init() {
        mLogFileName = mContext.getFileStreamPath("daliy.txt").getAbsolutePath();
        mMaxBackupSize = 1;
        mMaxFileSize = 1024 * 1024 * 5;
        mLogLevel = Level.ALL;
        mNeedSaveFile = true;
        mShowLogCat = false;
        MINLEVEL = DEBUG;
        logConfig();
    }

    /**
     * 设置配置参数<br>
     * 默认最大备份文件是2个，单个文件最大5M
     *
     * @param logFileName  日志保存文件路径，为null时，保存在程序私有file文件目录下的daliy.txt
     * @param needSaveFile 是否保存到文件
     * @param showLogCat   是否显示在LogCat
     * @param minLevel     最小显示级别
     */

    public void setArgument(String logFileName, boolean needSaveFile,
                            boolean showLogCat, int minLevel) {
        mLogFileName = logFileName == null ? mContext.getFileStreamPath(
                "daliy.txt").getAbsolutePath() : logFileName;
        mNeedSaveFile = needSaveFile;
        mShowLogCat = showLogCat;
        BaoLog.MINLEVEL = minLevel;
        logConfig();
    }

    /**
     * 设置配置参数
     *
     * @param logFileName   日志保存文件路径，为null时，保存在程序私有file文件目录下的daliy.txt
     * @param maxBackupSize 最大备份文件数
     * @param maxFileSize   单个文件最大保存多少数据
     * @param needSaveFile  是否保存到文件
     * @param showLogCat    是否显示在LogCat
     * @param minLevel      最小显示级别
     */

    public void setArgument(String logFileName, int maxBackupSize,
                            long maxFileSize, boolean needSaveFile, boolean showLogCat,
                            int minLevel) {
        mLogFileName = logFileName == null ? mContext.getFileStreamPath(
                "daliy.txt").getAbsolutePath() : logFileName;
        mMaxBackupSize = maxBackupSize;
        mMaxFileSize = maxFileSize;
        mNeedSaveFile = needSaveFile;
        mShowLogCat = showLogCat;
        BaoLog.MINLEVEL = minLevel;
        logConfig();
    }

    /**
     * 设置默认参数
     */

    public void logConfig() {
        LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(mLogFileName);
        logConfigurator.setMaxBackupSize(mMaxBackupSize);
        logConfigurator.setMaxFileSize(mMaxFileSize);
        logConfigurator.setRootLevel(mLogLevel);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.setUseFileAppender(mNeedSaveFile);
        logConfigurator.setUseLogCatAppender(mShowLogCat);
        logConfigurator.configure();
    }

    /**
     * 打印调试信息
     *
     * @param message 信息
     */

    public static void d(String message) {
        if (MINLEVEL <= DEBUG) {
            Logger.getRootLogger().debug(message);
        }
    }

    /**
     * 打印调试信息
     *
     * @param message 信息
     * @param t       异常
     */

    public static void d(String message, Throwable t) {
        if (MINLEVEL <= DEBUG) {
            Logger.getRootLogger().debug(message, t);
        }
    }

    /**
     * 打印重要数据
     *
     * @param message 信息
     */

    public static void i(String message) {
        if (MINLEVEL <= INFO) {
            Logger.getRootLogger().info(message);
        }
    }

    /**
     * 打印重要数据
     *
     * @param message 信息
     * @param t       异常
     */

    public static void i(String message, Throwable t) {
        if (MINLEVEL <= INFO) {
            Logger.getRootLogger().info(message, t);
        }
    }

    /**
     * 打印警告信息
     *
     * @param message 信息
     */

    public static void w(String message) {
        if (MINLEVEL <= WARN) {
            Logger.getRootLogger().warn(message);
        }
    }

    /**
     * 打印警告信息
     *
     * @param message 信息
     * @param t       异常
     */

    public static void w(String message, Throwable t) {
        if (MINLEVEL <= WARN) {
            Logger.getRootLogger().warn(message, t);
        }
    }

    /**
     * 打印错误信息
     *
     * @param message 信息
     */

    public static void e(String message) {
        if (MINLEVEL <= ERROR) {
            Logger.getRootLogger().error(message);
        }
    }

    /**
     * 打印错误信息
     *
     * @param message 信息
     * @param t       异常
     */

    public static void e(String message, Throwable t) {
        if (MINLEVEL <= ERROR) {
            Logger.getRootLogger().error(message, t);
        }
    }
}
