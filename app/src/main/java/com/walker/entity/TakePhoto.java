package com.walker.entity;

/**
 * summary :拍照实体
 * time    :2016/9/5 17:07
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class TakePhoto {
    /**
     * 保存路径
     */
    private String mSavePath;
    /**
     * 备注
     */
    private String mDesc;
    /**
     * 类型
     */
    private int mType;

    public String getSavePath() {
        return mSavePath;
    }

    public void setSavePath(String mSavePath) {
        this.mSavePath = mSavePath;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }
}
