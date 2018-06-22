package com.walker.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.walker.R;
import com.walker.entity.Summary;

import java.util.ArrayList;

/**
 * time    :2016/5/9 15:27
 * e-mail  :singlewolf968@gmail.com
 * summary :数据库操作类，处理有关sqlite相关的增删改查
 *
 * @author :Walker
 */
public class WalkerDB {
    /**
     * 本地数据库的上下文
     */
    private Context mContext;
    /**
     * 数据库管理实例
     */
    private DatabaseManager mDBManager;
    /**
     * 数据库操作者
     */
    private SQLiteDatabase mDB;

    public WalkerDB(Context context) {
        mContext = context;
        mDBManager = DatabaseManager.getInstance(context);
        mDB = mDBManager.getWritableDatabase();
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        if (mDBManager != null) {
            DatabaseManager.destoryInstance();
        }
    }

    /**
     * 获取示例
     *
     * @return ArrayList<Summary>
     */
    public ArrayList<Summary> getSummary() {
        ArrayList<Summary> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            String querySQL = mContext.getString(R.string.getSummary);
            cursor = mDB.rawQuery(querySQL, null);
            while (cursor.moveToNext() == true) {
                Summary entity = new Summary();
                String ICON = cursor.getString(cursor
                        .getColumnIndex("ICON"));
                entity.setICON(ICON);
                String SUMMARY = cursor.getString(cursor
                        .getColumnIndex("SUMMARY"));
                entity.setSUMMARY(SUMMARY);
                String DESCRIPTION = cursor.getString(cursor
                        .getColumnIndex("DESCRIPTION"));
                entity.setDESCRIPTION(DESCRIPTION);
                String CLASS_TYPE = cursor.getString(cursor
                        .getColumnIndex("CLASS_TYPE"));
                entity.setCLASS_TYPE(CLASS_TYPE);
                String CLASS_NAME = cursor.getString(cursor
                        .getColumnIndex("CLASS_NAME"));
                entity.setCLASS_NAME(CLASS_NAME);
                String STAR_LEVEL = cursor.getString(cursor
                        .getColumnIndex("STAR_LEVEL"));
                entity.setSTAR_LEVEL(STAR_LEVEL);
                String SHOW_TYPE = cursor.getString(cursor
                        .getColumnIndex("SHOW_TYPE"));
                entity.setSHOW_TYPE(SHOW_TYPE);
                String CREATE_DATE = cursor.getString(cursor
                        .getColumnIndex("CREATE_DATE"));
                entity.setCREAT_DATE(CREATE_DATE);
                String SPARE_1 = cursor.getString(cursor
                        .getColumnIndex("SPARE_1"));
                entity.setSPARE_1(SPARE_1);
                String SPARE_2 = cursor.getString(cursor
                        .getColumnIndex("SPARE_2"));
                entity.setSPARE_2(SPARE_2);
                String SPARE_3 = cursor.getString(cursor
                        .getColumnIndex("SPARE_3"));
                entity.setSPARE_3(SPARE_3);
                list.add(entity);
            }
        } catch (Exception e) {
            Log.e("getSummaryByType", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 获取简介
     *
     * @param type 类型
     * @return ArrayList<Summary>
     */
    public ArrayList<Summary> getSummaryByType(String type) {
        ArrayList<Summary> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            String querySQL = mContext.getString(R.string.getSummaryByType);
            cursor = mDB.rawQuery(querySQL, new String[]{type});
            while (cursor.moveToNext() == true) {
                Summary entity = new Summary();
                String ICON = cursor.getString(cursor
                        .getColumnIndex("ICON"));
                entity.setICON(ICON);
                String SUMMARY = cursor.getString(cursor
                        .getColumnIndex("SUMMARY"));
                entity.setSUMMARY(SUMMARY);
                String DESCRIPTION = cursor.getString(cursor
                        .getColumnIndex("DESCRIPTION"));
                entity.setDESCRIPTION(DESCRIPTION);
                String CLASS_TYPE = cursor.getString(cursor
                        .getColumnIndex("CLASS_TYPE"));
                entity.setCLASS_TYPE(CLASS_TYPE);
                String CLASS_NAME = cursor.getString(cursor
                        .getColumnIndex("CLASS_NAME"));
                entity.setCLASS_NAME(CLASS_NAME);
                String STAR_LEVEL = cursor.getString(cursor
                        .getColumnIndex("STAR_LEVEL"));
                entity.setSTAR_LEVEL(STAR_LEVEL);
                String SHOW_TYPE = cursor.getString(cursor
                        .getColumnIndex("SHOW_TYPE"));
                entity.setSHOW_TYPE(SHOW_TYPE);
                String CREATE_DATE = cursor.getString(cursor
                        .getColumnIndex("CREATE_DATE"));
                entity.setCREAT_DATE(CREATE_DATE);
                String SPARE_1 = cursor.getString(cursor
                        .getColumnIndex("SPARE_1"));
                entity.setSPARE_1(SPARE_1);
                String SPARE_2 = cursor.getString(cursor
                        .getColumnIndex("SPARE_2"));
                entity.setSPARE_2(SPARE_2);
                String SPARE_3 = cursor.getString(cursor
                        .getColumnIndex("SPARE_3"));
                entity.setSPARE_3(SPARE_3);
                list.add(entity);
            }
        } catch (Exception e) {
            Log.e("getSummaryByType", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 添加事例结构体
     *
     * @param entity Summary
     * @return boolean
     */
    public boolean addSummary(ArrayList<Summary> data) {
        boolean result = false;
        try {
            mDB.beginTransaction();
            for (Summary entity : data) {
                ContentValues values = new ContentValues();
                values.put("SHOW_ID", entity.getSHOW_ID());
                values.put("ICON", entity.getICON());
                values.put("SUMMARY", entity.getSUMMARY());
                values.put("DESCRIPTION", entity.getDESCRIPTION());
                values.put("CLASS_TYPE", entity.getCLASS_TYPE());
                values.put("CLASS_NAME", entity.getCLASS_NAME());
                values.put("STAR_LEVEL", entity.getSTAR_LEVEL());
                values.put("SHOW_TYPE", entity.getSHOW_TYPE());
                values.put("ACTIVE", entity.getACTIVE());
                values.put("CREATE_DATE", entity.getCREAT_DATE());
                values.put("SPARE_1", entity.getSPARE_1());
                values.put("SPARE_2", entity.getSPARE_2());
                values.put("SPARE_3", entity.getSPARE_3());
                long insert = mDB.insert("SUMMARY", null, values);
                if (0 < insert) {
                    result = true;
                }
            }
            mDB.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("addSummary", e.toString());
        } finally {
            mDB.endTransaction();
        }
        return result;
    }

    /**
     * 获取所有县级市信息
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getAllCounty() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            String querySQL = mContext.getString(R.string.getAllCounty);
            cursor = mDB.rawQuery(querySQL, null);
            while (cursor.moveToNext() == true) {
                list.add(cursor.getString(cursor.getColumnIndex("COUNTY_NAME")));
            }
        } catch (Exception e) {
            Log.e("getAllCounty", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }
}