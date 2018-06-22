package com.walker.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * summary :fragment的基类
 * time    :2016/7/5 10:44
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public abstract class BaseFragment extends Fragment {
    /**
     * 宿主Activity
     * 防止内存不足时fragment调用个体Activity()时报空指针
     */
    protected BaseFragActivity mHoldActivity;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHoldActivity = (BaseFragActivity) activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        buildView(view, savedInstanceState);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        bindListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindListener();
    }

    /**
     * 构建视图组件
     *
     * @param baseView  fragment的设置视图
     * @param savedInst 临时存储
     */
    protected abstract void buildView(View baseView, Bundle savedInst);

    /**
     * 获取布局文件ID
     *
     * @return int
     */
    protected abstract int getLayoutId();

    /**
     * 绑定监听器
     */
    protected abstract void bindListener();

    /**
     * 解绑监听器
     */
    protected abstract void unbindListener();

    /**
     * 获取宿主Activity
     *
     * @return BaseFragActivity
     */
    protected BaseFragActivity getHoldActivity() {
        return mHoldActivity;
    }

    /**
     * 添加fragment
     *
     * @param fragment 目标fragment
     * @param tag      标签
     */
    protected void addFragment(BaseFragment fragment, String tag) {
        if (null != fragment) {
            mHoldActivity.addFragment(fragment, tag);
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
        if (null != fragment) {
            mHoldActivity.addFragment(fragment, tag, isBack);
        }
    }

    /**
     * 移除fragment
     */
    protected void removeFragment() {
        mHoldActivity.removeFragment();
    }
}
