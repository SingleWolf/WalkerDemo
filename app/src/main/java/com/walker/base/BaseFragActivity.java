package com.walker.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * summary :活动基类(内置对fragment的操作)
 * time    :2016/7/5 10:29
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public abstract class BaseFragActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        setFirstFragment();
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
     * @return int
     */
    protected abstract int getContentViewId();

    /**
     * 布局中Fragment的布局文件ID
     *
     * @return int
     */
    protected abstract int getFragContentId();

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

    /**
     * 设置第一个fragment
     */
    protected void setFirstFragment() {
        //避免重复添加Fragment
        if (null == getSupportFragmentManager().getFragments()) {
            BaseFragment firstFragment = getFirstFragment();
            if (null != firstFragment) {
                addFragment(firstFragment, "FirstFragment");
            }
        }
    }

    /**
     * 添加fragment
     *
     * @param fragment 目标fragment
     * @param tag      标签
     */
    protected void addFragment(BaseFragment fragment, String tag) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(getFragContentId(), fragment, tag)
                    .addToBackStack(tag)
                    .commitAllowingStateLoss();
        }
    }

    /**
     * 添加fragment
     *
     * @param fragment 目标fragment
     * @param tag      标签
     * @param isBack   事务被保存到back stack的标识
     */
    protected void addFragment(BaseFragment fragment, String tag, boolean isBack) {
        if (fragment != null) {
            if (isBack) {
                getSupportFragmentManager().beginTransaction()
                        .replace(getFragContentId(), fragment, tag)
                        .addToBackStack(tag)
                        .commitAllowingStateLoss();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(getFragContentId(), fragment, tag)
                        .commitAllowingStateLoss();
            }
        }
    }

    /**
     * 移除fragment
     */
    protected void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    /**
     * 获取第一个fragment
     *
     * @return BaseFragment
     */
    protected abstract BaseFragment getFirstFragment();
}
