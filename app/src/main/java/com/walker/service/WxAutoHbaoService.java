package com.walker.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.bsx.baolib.log.BaoLog;
import com.walker.WalkerApplication;
import com.walker.constant.HbaoConfig;
import com.walker.data.sp.SPHelper;
import com.walker.utils.AccessibilityUtil;
import com.walker.utils.NotifyUtil;
import com.walker.utils.ToastUtil;

import java.util.List;

/**
 * summary :微信自动抢红包服务
 * time    :2016/8/5 15:40
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class WxAutoHbaoService extends AccessibilityService {

    private static final String TAG = "Walker";
    /**
     * 微信的包名
     */
    public static final String WECHAT_PACKAGENAME = "com.tencent.mm";

    /**
     * 红包消息的关键字
     */
    private static final String HONGBAO_TEXT_KEY = "[微信红包]";

    private static final String BUTTON_CLASS_NAME = "android.widget.Button";
    /**
     * 不能再使用文字匹配的最小版本号
     * 6.3.8 对应code为680,6.3.9对应code为700
     */
    private static final int USE_ID_MIN_VERSION = 700;

    private static final int WINDOW_NONE = 0;
    private static final int WINDOW_LUCKYMONEY_RECEIVEUI = 1;
    private static final int WINDOW_LUCKYMONEY_DETAIL = 2;
    private static final int WINDOW_LAUNCHER = 3;
    private static final int WINDOW_OTHER = -1;

    private int mCurrentWindow = WINDOW_NONE;

    private boolean mIsReceivingHbao;
    private int mWechatVersion;
    private Handler mHandler = null;
    private static WxAutoHbaoService mInstance;
    private KeyguardManager.KeyguardLock mKeyguardLock;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(!TextUtils.equals(WECHAT_PACKAGENAME,event.getPackageName())){
            return;
        }
        switch (event.getEventType()) {
            //通知栏事件
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                Parcelable data = event.getParcelableData();
                if (data == null || !(data instanceof Notification)) {
                    return;
                }
                boolean isModeFastest = SPHelper.getBoolean(HbaoConfig.KEY_MODE_FASTEST, false);
                //开启快速模式，不处理
                if (isModeFastest && WxhbaoNotifyService.isRunning()) {
                    return;
                }
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) {
                    String text = String.valueOf(texts.get(0));
                    notificationEvent(text, (Notification) data);
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                openHongBao(event);
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                //不在聊天界面或聊天列表，不处理
                if (mCurrentWindow != WINDOW_LAUNCHER) {
                    return;
                }
                if (mIsReceivingHbao) {
                    exeChatListHongBao();
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onInterrupt() {
        mInstance = null;
        ToastUtil.showCenterShort("抢红包服务已中断");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mInstance = this;
        try {
            PackageInfo wechatPackage = WalkerApplication.getContext().getPackageManager().getPackageInfo(WECHAT_PACKAGENAME, 0);
            mWechatVersion = wechatPackage.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            BaoLog.e("onServiceConnected", e);
            mWechatVersion = 0;
        }
        ToastUtil.showCenterShort("抢红包服务已连接");
    }

    /**
     * 是否为群聊天
     */
    private boolean isMemberChatUi(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return false;
        }
        String id = "com.tencent.mm:id/ces";
        if (mWechatVersion <= 680) {
            id = "com.tencent.mm:id/ew";
        } else if (mWechatVersion <= 700) {
            id = "com.tencent.mm:id/cbo";
        }
        String title = null;
        AccessibilityNodeInfo target = AccessibilityUtil.findNodeInfosById(nodeInfo, id);
        if (target != null) {
            title = String.valueOf(target.getText());
        }

        String keyBack = "返回";
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(keyBack);

        if (list != null && !list.isEmpty()) {
            AccessibilityNodeInfo parent = null;
            for (AccessibilityNodeInfo node : list) {
                if (!"android.widget.ImageView".equals(node.getClassName())) {
                    continue;
                }
                String desc = String.valueOf(node.getContentDescription());
                if (!keyBack.equals(desc)) {
                    continue;
                }
                parent = node.getParent();
                break;
            }
            if (parent != null) {
                parent = parent.getParent();
            }
            if (parent != null) {
                if (parent.getChildCount() >= 2) {
                    AccessibilityNodeInfo node = parent.getChild(1);
                    if ("android.widget.TextView".equals(node.getClassName())) {
                        title = String.valueOf(node.getText());
                    }
                }
            }
        }


        if (title != null && title.endsWith(")")) {
            return true;
        }
        return false;
    }

    /**
     * 通知栏事件
     */
    private void notificationEvent(String ticker, final Notification nf) {
        String text = ticker;
        int index = text.indexOf(":");
        if (index != -1) {
            text = text.substring(index + 1);
        }
        text = text.trim();
        if (text.contains(HONGBAO_TEXT_KEY)) { //红包消息
            boolean isLock = NotifyUtil.isLockScreen(WalkerApplication.getContext());
            if (isLock) {
                wakeAndUnlock();
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        newHongBaoNotification(nf);
                    }
                }, 1000);
            } else {
                newHongBaoNotification(nf);
            }
        }
    }

    /**
     * 打开通知栏消息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void newHongBaoNotification(Notification notification) {
        mIsReceivingHbao = true;
        //以下是精华，将微信的通知栏消息打开
        PendingIntent pendingIntent = notification.contentIntent;
        NotifyUtil.send(pendingIntent);
        if (SPHelper.getInt(HbaoConfig.KEY_MODE_AUTO_HBAO, HbaoConfig.VAL_MODE_AUTO_HBAO_1) != HbaoConfig.VAL_MODE_AUTO_HBAO_1) {
            NotifyUtil.playEffect(WalkerApplication.getContext());
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openHongBao(AccessibilityEvent event) {
        if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI".equals(event.getClassName())) {
            mCurrentWindow = WINDOW_LUCKYMONEY_RECEIVEUI;
            //点中了红包，下一步就是去拆红包
            exeLuckyMoneyOpen();
        } else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI".equals(event.getClassName())) {
            mCurrentWindow = WINDOW_LUCKYMONEY_DETAIL;
            //拆完红包后看详细的纪录界面
            int act = SPHelper.getInt(HbaoConfig.KEY_ACT_AFTER_SUC, HbaoConfig.VAL_ACT_AFTER_SUC_1);
            switch (act) {
                //抢包成功后返回聊天界面
                case HbaoConfig.VAL_ACT_AFTER_SUC_1:
                    AccessibilityUtil.performBack(this);
                    break;
                //抢包成功后返回系统桌面
                case HbaoConfig.VAL_ACT_AFTER_SUC_2:
                    AccessibilityUtil.performHome(this);
                    break;
                //抢包成功后静静地呆着
                case HbaoConfig.VAL_ACT_AFTER_SUC_3:
                    break;
                default:
                    break;
            }
            release();
        } else if ("com.tencent.mm.ui.LauncherUI".equals(event.getClassName())) {
            mCurrentWindow = WINDOW_LAUNCHER;
            //在聊天界面,去点中红包
            exeChatListHongBao();
        } else {
            mCurrentWindow = WINDOW_OTHER;
        }
    }

    /**
     * 点击聊天里的红包后，显示的界面
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void exeLuckyMoneyOpen() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.w(TAG, "rootWindow为空");
            return;
        }

        AccessibilityNodeInfo targetNode = null;
        int event = SPHelper.getInt(HbaoConfig.KEY_ACT_AFTER_OPEN, HbaoConfig.VAL_ACT_AFTER_OPEN_1);
        switch (event) {
            //打开红包后自动拆包
            case HbaoConfig.VAL_ACT_AFTER_OPEN_1:
                if (mWechatVersion < USE_ID_MIN_VERSION) {
                    targetNode = AccessibilityUtil.findNodeInfosByText(nodeInfo, "拆红包");
                } else {
                    //针对繁体字“开”寻找触发器
                    if (3 <= nodeInfo.getChildCount()) {
                        targetNode = nodeInfo.getChild(3);
                    }

                    if (targetNode == null) {
                        String buttonId = "com.tencent.mm:id/b43";
                        if (mWechatVersion == 700) {
                            buttonId = "com.tencent.mm:id/b2c";
                        }

                        if (buttonId != null) {
                            targetNode = AccessibilityUtil.findNodeInfosById(nodeInfo, buttonId);
                        }
                    }

                    if (targetNode == null) {
                        //分别对应固定金额的红包 拼手气红包
                        AccessibilityNodeInfo textNode = AccessibilityUtil.findNodeInfosByTexts(nodeInfo, "发了一个红包", "给你发了一个红包", "发了一个红包，金额随机");
                        if (textNode != null) {
                            for (int i = 0; i < textNode.getChildCount(); i++) {
                                AccessibilityNodeInfo node = textNode.getChild(i);
                                if (BUTTON_CLASS_NAME.equals(node.getClassName())) {
                                    targetNode = node;
                                    break;
                                }
                            }
                        }
                    }
                    if (targetNode == null) { //通过组件查找
                        targetNode = AccessibilityUtil.findNodeInfosByClassName(nodeInfo, BUTTON_CLASS_NAME);
                    }
                }
                break;
            //打开红包后看看大家手气
            case HbaoConfig.VAL_ACT_AFTER_OPEN_2:
                if (mWechatVersion < USE_ID_MIN_VERSION) { //低版本才有 看大家手气的功能
                    targetNode = AccessibilityUtil.findNodeInfosByText(nodeInfo, "看看大家的手气");
                }
                break;
            //打开红包后静静看着
            case HbaoConfig.VAL_ACT_AFTER_OPEN_3:
                break;
            default:
                break;
        }

        if (targetNode != null) {
            final AccessibilityNodeInfo n = targetNode;
            long sDelayTime = SPHelper.getLong(HbaoConfig.KEY_AUTO_HBAO_DELAY, 0);
            if (sDelayTime != 0) {
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AccessibilityUtil.performClick(n);
                    }
                }, sDelayTime);
            } else {
                AccessibilityUtil.performClick(n);
            }
        }
    }

    /**
     * 收到聊天里的红包
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void exeChatListHongBao() {
        int mode = SPHelper.getInt(HbaoConfig.KEY_MODE_AUTO_HBAO, HbaoConfig.VAL_MODE_AUTO_HBAO_1);
        if (mode == HbaoConfig.VAL_MODE_AUTO_HBAO_4) { //只通知模式
            return;
        }

        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.w(TAG, "rootWindow为空");
            return;
        }

        if (mode != HbaoConfig.VAL_MODE_AUTO_HBAO_1) {
            boolean isMember = isMemberChatUi(nodeInfo);
            if (mode == HbaoConfig.VAL_MODE_AUTO_HBAO_2 && isMember) {//过滤群聊
                return;
            } else if (mode == HbaoConfig.VAL_MODE_AUTO_HBAO_3 && !isMember) { //过滤单聊
                return;
            }
        }

        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("领取红包");

        if (list != null && list.isEmpty()) {
            // 从消息列表查找红包
            AccessibilityNodeInfo node = AccessibilityUtil.findNodeInfosByText(nodeInfo, "[微信红包]");
            if (node != null) {
                mIsReceivingHbao = true;
                AccessibilityUtil.performClick(nodeInfo);
            }
        } else if (list != null) {
            if (mIsReceivingHbao) {
                //最新的红包领起
                AccessibilityNodeInfo node = list.get(list.size() - 1);
                AccessibilityUtil.performClick(node);
                mIsReceivingHbao = false;
            }
        }
    }

    /**
     * 快速监听通知栏
     */
    public static void onNotificationPosted(Pair data) {
        Notification nf = (Notification) data.second;
        String text = String.valueOf(nf.tickerText);
        mInstance.notificationEvent(text, nf);
        BaoLog.i("-->WxAutoHbaoService onNotificationPosted");
    }

    /**
     * 唤醒且解锁
     */
    private void wakeAndUnlock() {
        //获取电源管理器对象
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");

        //点亮屏幕
        wl.acquire(1000);

        //得到键盘锁管理器对象
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        mKeyguardLock = km.newKeyguardLock("unLock");
        //解锁
        mKeyguardLock.disableKeyguard();
    }

    /**
     * 释放
     */
    private void release() {
        if (mKeyguardLock != null) {
            BaoLog.i("maptrix" + "release the lock");
            //得到键盘锁管理器对象
            mKeyguardLock.reenableKeyguard();
        }
    }

    private Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    /**
     * 是否启动抢红包功能
     */
    public static boolean isRunning() {
        if (mInstance == null) {
            return false;
        }
        return true;
    }
}