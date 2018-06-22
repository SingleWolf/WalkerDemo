package com.walker.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.walker.R;
import com.walker.base.BaseFragment;

/**
 * summary :火箭发射
 * time    :2016/10/27 16:01
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */

public class RocketShootFragment extends BaseFragment implements View.OnClickListener {
    private View mViewRocket;
    private View mViewShoot;
    private View mViewCloud;
    private Button mBtnShoot;

    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        mViewRocket = baseView.findViewById(R.id.iv_rocket);
        mViewShoot = baseView.findViewById(R.id.iv_shoot);
        mViewCloud = baseView.findViewById(R.id.iv_cloud);
        mBtnShoot = (Button) baseView.findViewById(R.id.btn_getup);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_rocket;
    }

    @Override
    protected void bindListener() {
        mBtnShoot.setOnClickListener(this);
    }

    @Override
    protected void unbindListener() {
        mBtnShoot.setOnClickListener(null);
    }

    @Override
    public void onClick(View view) {
        shootRocket();
    }

    private void shootRocket() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Animation rocketAnimation = AnimationUtils.loadAnimation(getHoldActivity(), R.anim.anim_rocket);
                rocketAnimation
                        .setAnimationListener(new VisibilityAnimationListener(mViewRocket));
                mViewRocket.startAnimation(rocketAnimation);

                Animation cloudAnimation = AnimationUtils.loadAnimation(getHoldActivity(), R.anim.anim_cloud);
                cloudAnimation
                        .setAnimationListener(new VisibilityAnimationListener(mViewCloud));
                mViewCloud.startAnimation(cloudAnimation);

                Animation launcherAnimation = AnimationUtils.loadAnimation(getHoldActivity(), R.anim.anim_shoot);
                launcherAnimation
                        .setAnimationListener(new VisibilityAnimationListener(mViewShoot));
                mViewShoot.startAnimation(launcherAnimation);

            }
        }, 150);

    }


    public class VisibilityAnimationListener implements Animation.AnimationListener {

        private View mVisibilityView;

        public VisibilityAnimationListener(View view) {
            mVisibilityView = view;
        }

        public void setVisibilityView(View view) {
            mVisibilityView = view;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (mVisibilityView != null) {
                mVisibilityView.setVisibility(View.VISIBLE);
                // mVisibilityView.setVisibility(View.GONE);
            }


        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mVisibilityView != null) {
                mVisibilityView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }
}
