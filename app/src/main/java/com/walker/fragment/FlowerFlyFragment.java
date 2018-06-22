package com.walker.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.walker.R;
import com.walker.animation.FlowerAnimation;
import com.walker.base.BaseFragment;

/**
 * summary :散花动画
 * time    :2016/10/27 16:44
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */

public class FlowerFlyFragment extends BaseFragment {
    private Button mBtnFly;
    private RelativeLayout mViewAnim;
    private FlowerAnimation mFlowerAnim;

    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        mBtnFly = (Button) baseView.findViewById(R.id.btn_run);
        mViewAnim = (RelativeLayout) baseView.findViewById(R.id.rlt_anim);
        mViewAnim.setVisibility(View.VISIBLE);

        mFlowerAnim = new FlowerAnimation(getHoldActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mFlowerAnim.setLayoutParams(params);
        mViewAnim.addView(mFlowerAnim);


        mBtnFly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlowerAnim.startAnimation();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_flower;
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }
}
