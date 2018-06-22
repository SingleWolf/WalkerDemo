package com.walker.fragment;

import android.os.Bundle;
import android.view.View;

import com.walker.R;
import com.walker.activity.SteepModeActivity;
import com.walker.base.BaseFragment;

/**
 * summary :沉浸探索（具有沉浸效果的導航欄/真正的沉浸式模式）
 * time    :2016/8/29 11:14
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class SteepFragment extends BaseFragment {
    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        baseView.findViewById(R.id.btn_steepStyle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SteepModeActivity.actionStart(getHoldActivity(), false);
            }
        });
        baseView.findViewById(R.id.btn_steepMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SteepModeActivity.actionStart(getHoldActivity(), true);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_steep;
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }
}
