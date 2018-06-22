package com.walker.entity;

/**
 * summary :简介的结构体
 * time    :2016/7/8 17:15
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class Summary {
    /**
     * 展示序号
     */
    private String SHOW_ID;
    /**
     * 图标
     */
    private String ICON;
    /**
     * 简介
     */
    private String SUMMARY;
    /**
     * 描述
     */
    private String DESCRIPTION;
    /**
     * 展示类名
     */
    private String CLASS_NAME;
    /**
     * class类型（fragment、activity）
     */
    private String CLASS_TYPE;
    /**
     * 简介隶属类型
     */
    private String SHOW_TYPE;
    /**
     * 星级
     */
    private String STAR_LEVEL;
    /**
     * 有效标志
     */
    private String ACTIVE;
    /**
     * 创建日期
     */
    private String CREAT_DATE;
    /**
     * 备注1
     */
    private String SPARE_1;
    /**
     * 备注2
     */
    private String SPARE_2;
    /**
     * 备注3
     */
    private String SPARE_3;

    public String getSHOW_ID() {
        return SHOW_ID;
    }

    public void setSHOW_ID(String SHOW_ID) {
        this.SHOW_ID = SHOW_ID;
    }

    public String getICON() {
        return ICON;
    }

    public void setICON(String ICON) {
        this.ICON = ICON;
    }

    public String getSUMMARY() {
        return SUMMARY;
    }

    public void setSUMMARY(String SUMMARY) {
        this.SUMMARY = SUMMARY;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getCLASS_NAME() {
        return CLASS_NAME;
    }

    public void setCLASS_NAME(String CLASS_NAME) {
        this.CLASS_NAME = CLASS_NAME;
    }

    public String getSTAR_LEVEL() {
        return STAR_LEVEL;
    }

    public void setSTAR_LEVEL(String STAR_LEVEL) {
        this.STAR_LEVEL = STAR_LEVEL;
    }


    public String getACTIVE() {
        return ACTIVE;
    }

    public void setACTIVE(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }

    public String getCREAT_DATE() {
        return CREAT_DATE;
    }

    public void setCREAT_DATE(String CREAT_DATE) {
        this.CREAT_DATE = CREAT_DATE;
    }

    public String getSPARE_1() {
        return SPARE_1;
    }

    public void setSPARE_1(String SPARE_1) {
        this.SPARE_1 = SPARE_1;
    }

    public String getSPARE_2() {
        return SPARE_2;
    }

    public void setSPARE_2(String SPARE_2) {
        this.SPARE_2 = SPARE_2;
    }

    public String getSPARE_3() {
        return SPARE_3;
    }

    public void setSPARE_3(String SPARE_3) {
        this.SPARE_3 = SPARE_3;
    }

    public String getCLASS_TYPE() {
        return CLASS_TYPE;
    }

    public void setCLASS_TYPE(String CLASS_TYPE) {
        this.CLASS_TYPE = CLASS_TYPE;
    }

    public String getSHOW_TYPE() {
        return SHOW_TYPE;
    }

    public void setSHOW_TYPE(String SHOW_TYPE) {
        this.SHOW_TYPE = SHOW_TYPE;
    }
}
