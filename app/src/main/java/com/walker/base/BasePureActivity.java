package com.walker.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * summary :活动基类（纯粹的activity）
 * time    :2016/6/30 13:35
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public abstract class BasePureActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        initialization(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindListener();
    }


    @Override
    protected void onStop() {
        super.onStop();
        unbindListener();
    }

    /**
     * 布局文件ID
     *
     * @return
     */
    protected abstract int getContentViewId();

    /**
     * 初始化
     *
     * @param savedInstanceState 临时存储的数据
     */
    protected abstract void initialization(Bundle savedInstanceState);


    /**
     * 绑定监听器
     */
    protected abstract void bindListener();

    /**
     * 解绑监听器
     */
    protected abstract void unbindListener();
}
