package com.walker.constant;

/**
 * summary :抢红包参数配置红包
 * time    :2016/8/10 14:32
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class HbaoConfig {
    /**
     * 开启自动抢红包功能
     */
    public static final String KEY_FUN_AUTO_HBAO = "fun_auto_hbao";
    /**
     * 自动抢红包的模式
     */
    public static final String KEY_MODE_AUTO_HBAO = "mode_auto_hbao";
    /**
     * 自动抢模式
     */
    public static final int VAL_MODE_AUTO_HBAO_1 = 1;
    /**
     * 抢单聊红包，群聊仅通知
     */
    public static final int VAL_MODE_AUTO_HBAO_2 = 2;
    /**
     * 抢群聊红包，单聊仅通知
     */
    public static final int VAL_MODE_AUTO_HBAO_3 = 3;
    /**
     * 通知手动抢
     */
    public static final int VAL_MODE_AUTO_HBAO_4 = 4;
    /**
     * 设置抢红包的延迟时间
     */
    public static final String KEY_AUTO_HBAO_DELAY = "auto_hbao_delay";
    /**
     * 打开红包后的操作
     */
    public static final String KEY_ACT_AFTER_OPEN = "act_after_open";
    /**
     * 打开红包后自动拆包
     */
    public static final int VAL_ACT_AFTER_OPEN_1 = 1;
    /**
     * 打开红包后看看大家手气
     */
    public static final int VAL_ACT_AFTER_OPEN_2 = 2;
    /**
     * 打开红包后静静看着
     */
    public static final int VAL_ACT_AFTER_OPEN_3 = 3;
    /**
     * 抢包成功后的操作
     */
    public static final String KEY_ACT_AFTER_SUC = "act_after_suc";
    /**
     * 抢包成功后返回聊天界面
     */
    public static final int VAL_ACT_AFTER_SUC_1 = 1;
    /**
     * 抢包成功后返回系统桌面
     */
    public static final int VAL_ACT_AFTER_SUC_2 = 2;
    /**
     * 抢包成功后静静地呆着
     */
    public static final int VAL_ACT_AFTER_SUC_3 = 3;
    /**
     * 声音提醒
     */
    public static final String KEY_NOTIFY_SOUND = "notify_sound";
    /**
     * 震动提醒
     */
    public static final String KEY_NOTIFY_SHAKE = "notify_shake";
    /**
     * 夜间免打扰
     */
    public static final String KEY_NIGHT_NOT = "night_not";
    /**
     * 终极模式（启动通知栏监听）
     */
    public static final String KEY_MODE_FASTEST = "mode_fastest";
}
