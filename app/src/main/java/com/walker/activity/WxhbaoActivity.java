package com.walker.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.bsx.baolib.view.CustomShapeImageView;
import com.bsx.baolib.view.WaveView;
import com.walker.R;
import com.walker.base.BasePureActivity;
import com.walker.constant.BaseParams;
import com.walker.constant.HbaoConfig;
import com.walker.data.sp.SPHelper;
import com.walker.data.storage.StorageHelper;
import com.walker.service.WxAutoHbaoService;
import com.walker.service.WxhbaoNotifyService;
import com.walker.utils.BitmapUtil;
import com.walker.utils.CameraUtil;
import com.walker.utils.StringUtil;

import java.io.File;

/**
 * summary :微信自动抢红包
 * time    :2016/8/10 09:37
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class WxhbaoActivity extends BasePureActivity implements View.OnClickListener {
    /**
     * 抢红包功能的开关
     */
    private CheckBox mCbFunAutoHbao;
    /**
     * 声音提醒的开关
     */
    private CheckBox mCbNotifySound;
    /**
     * 震动提醒的开关
     */
    private CheckBox mCbNotifyShake;
    /**
     * 夜间勿扰的开关
     */
    private CheckBox mCbNightNot;
    /**
     * 终极模式的开关
     */
    private CheckBox mCbModeFastest;
    /**
     * 设置抢红包模式
     */
    private TextView mTvModeHbao;
    /**
     * 抢红包模式设置区域
     */
    private View mViewModeHbao;
    /**
     * 设置抢红包模式
     */
    private TextView mTvModeNotify;
    /**
     * 抢红包模式设置区域
     */
    private View mViewModeNotify;
    /**
     * 设置抢包模式
     */
    private View mViewSetModeHbao;
    /**
     * 设置延迟时间
     */
    private View mViewSetDelay;
    /**
     * 设置打开红包后的操作
     */
    private View mViewSetAfterOpen;
    /**
     * 设置成功拆包后的操作
     */
    private View mViewSetAfterSuc;
    /**
     * 显示抢包模式
     */
    private TextView mTvShowModeHbao;
    /**
     * 显示延迟时间
     */
    private TextView mTvShowDelay;
    /**
     * 显示打开红包后的操作
     */
    private TextView mTvShowAfterOpen;
    /**
     * 显示成功拆包后的操作
     */
    private TextView mTvShowAfterSuc;
    /**
     * 幸运头像
     */
    private WaveView mBackDrop;
    /**
     * 设置幸运头像
     */
    private FloatingActionButton mFabLucky;
    /**
     * 幸运头像
     */
    private CustomShapeImageView mSivLucky;
    /**
     * 进度框
     */
    private ProgressDialog mProgress;
    /**
     * 抢包成功后的操作选项
     */
    private String[] mItemActSucVal = new String[]{"返回聊天界面", "返回系统桌面", "静静地呆着"};
    /**
     * 打开红包后的选择
     */
    private String[] mItemActOpenVal = new String[]{"自动拆包", "看看大家手气", "静静地呆着"};
    /**
     * 抢红包的模式分类
     */
    private String[] mItemHbaoModeVal = new String[]{"自动抢模式", "抢单聊红包，群聊仅通知", "抢群聊红包，单聊仅通知", "通知手动抢"};

    @Override
    protected int getContentViewId() {
        return R.layout.act_wxhbao;
    }

    @Override
    protected void initialization(Bundle savedInstanceState) {
        initView();
    }

    @Override
    protected void bindListener() {
        mCbFunAutoHbao.setOnCheckedChangeListener(onCheckBoxListener);
        mCbNotifySound.setOnCheckedChangeListener(onCheckBoxListener);
        mCbNotifyShake.setOnCheckedChangeListener(onCheckBoxListener);
        mCbNightNot.setOnCheckedChangeListener(onCheckBoxListener);
        mCbModeFastest.setOnCheckedChangeListener(onCheckBoxListener);

        mFabLucky.setOnClickListener(this);

        mTvModeHbao.setOnClickListener(this);
        mTvModeNotify.setOnClickListener(this);

        mViewSetModeHbao.setOnClickListener(this);
        mViewSetDelay.setOnClickListener(this);
        mViewSetAfterOpen.setOnClickListener(this);
        mViewSetAfterSuc.setOnClickListener(this);
    }

    @Override
    protected void unbindListener() {
        mCbFunAutoHbao.setOnCheckedChangeListener(null);
        mCbNotifySound.setOnCheckedChangeListener(null);
        mCbNotifyShake.setOnCheckedChangeListener(null);
        mCbNightNot.setOnCheckedChangeListener(null);
        mCbModeFastest.setOnCheckedChangeListener(null);

        mFabLucky.setOnClickListener(null);

        mTvModeHbao.setOnClickListener(null);
        mTvModeNotify.setOnClickListener(null);

        mViewSetModeHbao.setOnClickListener(null);
        mViewSetDelay.setOnClickListener(null);
        mViewSetAfterOpen.setOnClickListener(null);
        mViewSetAfterSuc.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mode_hbao:
                if (mViewModeHbao.getVisibility() == View.VISIBLE) {
                    mViewModeHbao.setVisibility(View.GONE);
                    mTvModeHbao.setText("设置抢包模式");
                } else {
                    mViewModeHbao.setVisibility(View.VISIBLE);
                    showHbaoSetting();
                    mTvModeHbao.setText("收起抢包模式设置");
                }
                break;
            case R.id.tv_mode_notify:
                if (mViewModeNotify.getVisibility() == View.VISIBLE) {
                    mViewModeNotify.setVisibility(View.GONE);
                    mTvModeNotify.setText("设置提醒模式");
                } else {
                    mViewModeNotify.setVisibility(View.VISIBLE);
                    showNotifySetting();
                    mTvModeNotify.setText("收起提醒模式设置");
                }
                break;
            case R.id.ll_set_mode_hb:
                showHbaoModeDialog();
                break;
            case R.id.ll_set_delay:
                showDelaySetDialog();
                break;
            case R.id.ll_set_after_open:
                showActOpenDialog();
                break;
            case R.id.ll_set_after_suc:
                showActSucDialog();
                break;
            case R.id.fab_set_lucky:
                CameraUtil.openGallery(WxhbaoActivity.this);
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hbao_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_auto_hbao:
                openAccessibilityServiceSettings();
                break;
            case R.id.menu_wx_notify:
                openNotificationServiceSettings();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAutoHbaoService();
        if (mBackDrop != null) {
            mBackDrop.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBackDrop != null) {
            mBackDrop.stop();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraUtil.GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                showProgress();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Uri uri = data.getData();
                        Bitmap bitmap = BitmapUtil.onConvert(WxhbaoActivity.this, uri, 200, 200);
                        File destFile = new File(getLuckyPath());
                        BitmapUtil.onSave(bitmap, 100, destFile, false);
                        mSivLucky.setImageBitmap(bitmap);
                        cancelProgress();
                    }
                }, 500);
            }
        }
    }


    /**
     * 初始化控件
     */
    private void initView() {
        initToolBar();

        mCbFunAutoHbao = (CheckBox) findViewById(R.id.cb_auto_wxhb);
        mCbNotifySound = (CheckBox) findViewById(R.id.cb_sound);
        mCbNotifyShake = (CheckBox) findViewById(R.id.cb_shake);
        mCbNightNot = (CheckBox) findViewById(R.id.cb_night_not);
        mCbModeFastest = (CheckBox) findViewById(R.id.cb_mode_fastest);

        mFabLucky = (FloatingActionButton) findViewById(R.id.fab_set_lucky);
        mSivLucky = (CustomShapeImageView) findViewById(R.id.siv_lucky);

        mTvModeHbao = (TextView) findViewById(R.id.tv_mode_hbao);
        mTvModeNotify = (TextView) findViewById(R.id.tv_mode_notify);

        mViewModeHbao = findViewById(R.id.ll_mode_hbao);
        mViewModeNotify = findViewById(R.id.ll_mode_notify);

        mBackDrop = (WaveView) findViewById(R.id.backdrop);
        mBackDrop.setDuration(2000);
        mBackDrop.setColor(Color.parseColor("#ffd700"));
        mBackDrop.setStyle(Paint.Style.STROKE);
        mBackDrop.start();

        mViewSetModeHbao = findViewById(R.id.ll_set_mode_hb);
        mViewSetDelay = findViewById(R.id.ll_set_delay);
        mViewSetAfterOpen = findViewById(R.id.ll_set_after_open);
        mViewSetAfterSuc = findViewById(R.id.ll_set_after_suc);

        mTvShowModeHbao = (TextView) findViewById(R.id.tv_show_mode_hb);
        mTvShowDelay = (TextView) findViewById(R.id.tv_show_delay);
        mTvShowAfterOpen = (TextView) findViewById(R.id.tv_show_open_hb);
        mTvShowAfterSuc = (TextView) findViewById(R.id.tv_show_after_succeed);

        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                Bitmap lucky = BitmapUtil.onThumbnail(getLuckyPath(), 200, 200, true);
                if (lucky != null) {
                    mSivLucky.setImageBitmap(lucky);
                } else {
                    mSivLucky.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_caishen));
                }
            }
        });

        boolean isFunAutoHbao = SPHelper.getBoolean(HbaoConfig.KEY_FUN_AUTO_HBAO, true);
        if (!isFunAutoHbao) {
            mCbFunAutoHbao.setChecked(isFunAutoHbao);
        }
        boolean isModeFastest = SPHelper.getBoolean(HbaoConfig.KEY_MODE_FASTEST, false);
        if (isModeFastest) {
            mCbModeFastest.setChecked(isModeFastest);
        }

    }

    /**
     * 初始化toolbar
     */
    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * 对开关的监听
     */
    OnCheckedChangeListener onCheckBoxListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.cb_auto_wxhb:
                    SPHelper.put(HbaoConfig.KEY_FUN_AUTO_HBAO, isChecked);
                    if (isChecked) {
                        setAutoHbaoService();
                    }
                    break;
                case R.id.cb_sound:
                    SPHelper.put(HbaoConfig.KEY_NOTIFY_SOUND, isChecked);
                    break;
                case R.id.cb_shake:
                    SPHelper.put(HbaoConfig.KEY_NOTIFY_SHAKE, isChecked);
                    break;
                case R.id.cb_night_not:
                    SPHelper.put(HbaoConfig.KEY_NIGHT_NOT, isChecked);
                    break;
                case R.id.cb_mode_fastest:
                    SPHelper.put(HbaoConfig.KEY_MODE_FASTEST, isChecked);
                    if (isChecked) {
                        setWxNotifyService();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 拆包成功后的操作
     */
    private void showActSucDialog() {
        new AlertDialog.Builder(this).setTitle("抢包成功后如何操作").setIcon(
                android.R.drawable.ic_dialog_info).setSingleChoiceItems(mItemActSucVal, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SPHelper.put(HbaoConfig.KEY_ACT_AFTER_SUC, which + 1);
                mTvShowAfterSuc.setText(mItemActSucVal[which]);
                dialog.dismiss();
            }
        }).setNegativeButton(android.R.string.cancel, null).show();
    }

    /**
     * 打开红包后的操作
     */
    private void showActOpenDialog() {
        new AlertDialog.Builder(this).setTitle("打开红包后如何操作").setIcon(
                android.R.drawable.ic_dialog_info).setSingleChoiceItems(mItemActOpenVal, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SPHelper.put(HbaoConfig.KEY_ACT_AFTER_OPEN, which + 1);
                mTvShowAfterOpen.setText(mItemActOpenVal[which]);
                dialog.dismiss();
            }
        }).setNegativeButton(android.R.string.cancel, null).show();
    }

    /**
     * 延迟设置
     */
    private void showDelaySetDialog() {
        final EditText inputDelay = new EditText(this);
        inputDelay.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this).setTitle("延迟设置").setIcon(
                android.R.drawable.ic_dialog_info).setView(inputDelay).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String val = inputDelay.getText().toString().trim();
                if (!TextUtils.isEmpty(val)) {
                    long delay = Long.parseLong(val);
                    if (0 < delay) {
                        SPHelper.put(HbaoConfig.KEY_AUTO_HBAO_DELAY, delay);
                        mTvShowDelay.setText("延迟" + delay / 1000 + "秒");
                    }
                }
            }
        }).setNegativeButton(android.R.string.cancel, null).show();
    }

    /**
     * 显示抢红包模式设置对话框
     */
    private void showHbaoModeDialog() {
        new AlertDialog.Builder(this).setTitle("设置抢红包的模式").setIcon(
                android.R.drawable.ic_dialog_info).setSingleChoiceItems(mItemHbaoModeVal, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SPHelper.put(HbaoConfig.KEY_MODE_AUTO_HBAO, which + 1);
                mTvShowModeHbao.setText(mItemHbaoModeVal[which]);
                dialog.dismiss();
            }
        }).setNegativeButton(android.R.string.cancel, null).show();
    }

    /**
     * 设置微信通知栏监听服务
     */
    private void setWxNotifyService() {
        boolean isModeFastest = SPHelper.getBoolean(HbaoConfig.KEY_MODE_FASTEST, false);
        if (isModeFastest) {
            if (!WxhbaoNotifyService.isRunning()) {
                View view = getLayoutInflater().inflate(R.layout.layout_notify_service, null);
                new AlertDialog.Builder(this).setTitle("极速模式需要开启一下设置").setView(view).setNegativeButton("好的，去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openNotificationServiceSettings();
                    }
                }).create().show();
            }
        }
    }

    /**
     * 设置为微信自动抢红包辅助服务
     */
    private void setAutoHbaoService() {
        boolean isFunAutoHbao = SPHelper.getBoolean(HbaoConfig.KEY_FUN_AUTO_HBAO, true);
        if (isFunAutoHbao) {
            if (!WxAutoHbaoService.isRunning()) {
                View view = getLayoutInflater().inflate(R.layout.layout_hbao_service, null);
                new AlertDialog.Builder(this).setTitle("自动抢红包需要开启一下设置").setView(view).setNegativeButton("好的，去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openAccessibilityServiceSettings();
                    }
                }).create().show();
            }
        }
    }

    /**
     * 打开辅助服务的设置
     */
    private void openAccessibilityServiceSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开通知栏设置
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private void openNotificationServiceSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示抢包模式的设置
     */
    private void showHbaoSetting() {
        int mode_auto_hbao = SPHelper.getInt(HbaoConfig.KEY_MODE_AUTO_HBAO, HbaoConfig.VAL_MODE_AUTO_HBAO_1);
        mTvShowModeHbao.setText(mItemHbaoModeVal[mode_auto_hbao - 1]);
        long delay = SPHelper.getLong(HbaoConfig.KEY_AUTO_HBAO_DELAY, (long) 0);
        if (0 < delay) {
            mTvShowDelay.setText("延迟" + delay / 1000 + "秒");
        }
        int act_after_open = SPHelper.getInt(HbaoConfig.KEY_ACT_AFTER_OPEN, HbaoConfig.VAL_ACT_AFTER_OPEN_1);
        mTvShowAfterOpen.setText(mItemActOpenVal[act_after_open - 1]);
        int act_after_suc = SPHelper.getInt(HbaoConfig.KEY_ACT_AFTER_SUC, HbaoConfig.VAL_ACT_AFTER_SUC_1);
        mTvShowAfterSuc.setText(mItemActSucVal[act_after_suc - 1]);
    }

    /**
     * 显示提醒设置
     */
    private void showNotifySetting() {
        boolean isSound = SPHelper.getBoolean(HbaoConfig.KEY_NOTIFY_SOUND, true);
        if (!isSound) {
            mCbNotifySound.setChecked(isSound);
        }
        boolean isShake = SPHelper.getBoolean(HbaoConfig.KEY_NOTIFY_SHAKE, true);
        if (!isShake) {
            mCbNotifyShake.setChecked(isShake);
        }
        boolean isNightNot = SPHelper.getBoolean(HbaoConfig.KEY_NIGHT_NOT, true);
        if (!isNightNot) {
            mCbNightNot.setChecked(isNightNot);
        }
    }

    /**
     * 获取幸运头像的存储路径
     *
     * @return String
     */

    private String getLuckyPath() {
        String luckyPath = StringUtil.pliceStr(StorageHelper.getWalkerRootPath(), File.separator, BaseParams.DIR_PIC);
        File file = new File(luckyPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return StringUtil.pliceStr(luckyPath, File.separator, "lucky.png");
    }

    /**
     * 显示进度框
     */
    private void showProgress() {
        if (mProgress == null) {
            mProgress = new ProgressDialog(this);
            mProgress.setMessage("请稍后...");
            mProgress.setCancelable(false);
        }
        if (!mProgress.isShowing()) {
            mProgress.show();
        }
    }

    /**
     * 取消进度框
     */
    private void cancelProgress() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.cancel();
        }
    }
}
