package com.walker.data.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bsx.baolib.log.BaoLog;
import com.walker.BuildConfig;
import com.walker.R;
import com.walker.WalkerApplication;
import com.walker.constant.BaseParams;
import com.walker.constant.SQLiteConfig;
import com.walker.data.storage.StorageHelper;
import com.walker.utils.StringUtil;
import com.walker.utils.TaskExecutor;

import java.io.File;

/**
 * summary : 数据库管理者
 * time    : 2016/5/9 15:18
 * email   : singlewolf968@gmail.com
 *
 * @author : Walker
 */
public class DatabaseManager extends SQLiteOpenHelper {
    /**
     * 数据库管理实例
     */
    private static volatile DatabaseManager INSTANCE;

    /**
     * 通过单例模式双锁获取数据库实例
     *
     * @param context 上下文
     * @return 数据库管理实例
     */
    public static synchronized DatabaseManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseManager.class) {
                if (INSTANCE == null) {
                    if (BuildConfig.DEBUG) {
                        String dbPath = StringUtil.pliceStr(StorageHelper.getWalkerRootPath(), File.separator, BaseParams.DIR_DB, File.separator, BaseParams.FILE_DB);
                        INSTANCE = new DatabaseManager(context, dbPath);
                    } else {
                        INSTANCE = new DatabaseManager(context, BaseParams.FILE_DB);
                    }
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 注销数据库实例
     */
    public static synchronized void destoryInstance() {
        if (INSTANCE != null) {
            INSTANCE.close();
        }
    }

    /**
     * 数据库管理的构造方法
     *
     * @param context 上下文
     * @param dbPath  文件路径
     */
    public DatabaseManager(Context context, String dbPath) {
        super(context, dbPath, null, SQLiteConfig.DB_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        TaskExecutor.executeTask(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = WalkerApplication.getContext();
                    String sql = context.getString(R.string.create_summary);
                    db.execSQL(sql);
                    sql = context.getString(R.string.create_province);
                    db.execSQL(sql);
                    sql = context.getString(R.string.create_city);
                    db.execSQL(sql);
                    sql = context.getString(R.string.create_county);
                    db.execSQL(sql);
                    sql = context.getString(R.string.insert_province);
                    db.execSQL(sql);
                    sql = context.getString(R.string.insert_city);
                    db.execSQL(sql);
                    sql = context.getString(R.string.insert_county_1);
                    db.execSQL(sql);
                    sql = context.getString(R.string.insert_county_2);
                    db.execSQL(sql);
                } catch (SQLException e) {
                    BaoLog.e("SQLiteDatabase", e);
                }
            }
        });
        BaoLog.e("*** SQLiteDatabase oncreate ***");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
    }
}
