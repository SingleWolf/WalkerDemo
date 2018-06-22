package com.walker.activity;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.walker.R;
import com.walker.base.BasePureActivity;

import java.lang.ref.WeakReference;

/**
 * summary :登录页面
 * time    :2016/6/30 13:40
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class LoginActivity extends BasePureActivity implements View.OnClickListener {
    /**
     * 邮箱
     */
    private EditText mEdtEmail;
    /**
     * 密码
     */
    private EditText mEdtPassWord;
    /**
     * 登录按钮
     */
    private Button mBtnLogin;
    /**
     * 登录区域
     */
    private View mViewLogin;
    /**
     * 进度条
     */
    private ProgressBar mProgressBar;

    @Override
    protected int getContentViewId() {
        return R.layout.act_login;
    }

    @Override
    protected void initialization(Bundle savedInstanceState) {
        initView();
        initToolBar();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        mEdtEmail = (EditText) findViewById(R.id.edt_email);
        mEdtPassWord = (EditText) findViewById(R.id.edt_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mViewLogin = findViewById(R.id.login_form);
        mProgressBar = (ProgressBar) findViewById(R.id.login_progress);
    }

    /**
     * 初始化toolbar
     */
    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("登  录");
        setSupportActionBar(toolbar);
    }

    @Override
    protected void bindListener() {
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    protected void unbindListener() {
        mBtnLogin.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                mBtnLogin.startAnimation(scaleBigAnim(300));
                onLogin();
                break;
            default:
                break;
        }
    }

    /**
     * 登录操作
     */
    private void onLogin() {
        if (checkLogin()) {
            showProgress(true);
            new StaticHandler(LoginActivity.this).postDelayed(new Runnable() {
                @Override
                public void run() {
                    showProgress(false);
                    MainActivity.actionStart(LoginActivity.this, true);
                }
            }, 3000);
        }
    }

    /**
     * 放大动画
     *
     * @param duration 效果延迟秒数
     * @return Animation
     */
    private Animation scaleBigAnim(int duration) {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);

        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);

        animationSet.setDuration(duration);
        animationSet.setFillAfter(false);
        return animationSet;

    }

    /**
     * 显示进度
     *
     * @param isShow 是否显示
     */
    private void showProgress(boolean isShow) {
        if (isShow) {
            mProgressBar.setVisibility(View.VISIBLE);
            mViewLogin.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mViewLogin.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 登录校验
     *
     * @return boolean
     */
    private boolean checkLogin() {
        String email = mEdtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mEdtEmail.setError("请输入邮箱");
            return false;
        }
        if (email.contains("@") == false) {
            mEdtEmail.setError("邮箱格式有误");
            return false;
        }
        String password = mEdtPassWord.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            mEdtPassWord.setError("请输入密码");
            return false;
        }
        return true;
    }

    /**
     * 静态Handler
     */
    public static class StaticHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public StaticHandler(final Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}
