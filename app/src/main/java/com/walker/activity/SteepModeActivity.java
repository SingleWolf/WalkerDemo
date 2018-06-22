package com.walker.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.walker.R;
import com.walker.base.BasePureActivity;

/**
 * summary :沉浸式模式
 * time    :2016/8/29 10:39
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class SteepModeActivity extends BasePureActivity {
    /**
     * 沉浸式模式的标识
     */
    private boolean mIsSteepMode;

    /**
     * 開啓活動
     *
     * @param activity    出發activity
     * @param isSteepMode 是否為真正沉浸式模式
     */
    public static void actionStart(Activity activity, boolean isSteepMode) {
        Intent intent = new Intent(activity, SteepModeActivity.class);
        intent.putExtra("isSteepMode", isSteepMode);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_steep_mode;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initialization(Bundle savedInstanceState) {
        mIsSteepMode = getIntent().getBooleanExtra("isSteepMode", false);

        if (!mIsSteepMode) {
            if (21 <= Build.VERSION.SDK_INT) {
                View decorView = getWindow().getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                getWindow().setNavigationBarColor(Color.TRANSPARENT);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
           /* ActionBar actionBar = getSupportActionBar();
            actionBar.hide();*/
        }
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (mIsSteepMode) {
            if (hasFocus && 19 <= Build.VERSION.SDK_INT) {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }
}
