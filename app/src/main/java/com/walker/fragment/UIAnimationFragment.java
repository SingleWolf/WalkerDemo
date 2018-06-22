package com.walker.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.walker.R;
import com.walker.base.BaseFragment;

/**
 * summary :视图动画
 * time    :2016/10/27 15:42
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */

public class UIAnimationFragment extends BaseFragment implements View.OnClickListener {
    private Button mBtnRocket;
    private Button mBtnFlower;

    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        mBtnRocket = (Button) baseView.findViewById(R.id.btn_rocket);
        mBtnFlower = (Button) baseView.findViewById(R.id.btn_flower);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_ui_animation;
    }

    @Override
    protected void bindListener() {
        mBtnRocket.setOnClickListener(this);
        mBtnFlower.setOnClickListener(this);
    }

    @Override
    protected void unbindListener() {
        mBtnRocket.setOnClickListener(null);
        mBtnFlower.setOnClickListener(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_rocket:
                addFragment(new RocketShootFragment(), "rocketFragment");
                break;
            case R.id.btn_flower:
                addFragment(new FlowerFlyFragment(), "flowerFragment");
                break;
            default:
                break;
        }
    }
}
