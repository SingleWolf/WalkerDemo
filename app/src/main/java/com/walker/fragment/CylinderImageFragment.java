package com.walker.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.bsx.baolib.view.CylinderImageView;
import com.walker.R;
import com.walker.base.BaseFragment;

/**
 * summary :循环显示长图片
 * time    :2016/7/13 14:45
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class CylinderImageFragment extends BaseFragment {
    private CylinderImageView mCylinderImageView;

    public static CylinderImageFragment newInstance() {
        return new CylinderImageFragment();
    }

    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        mCylinderImageView = (CylinderImageView) baseView.findViewById(R.id.iv_cylinder);
        mCylinderImageView.setSourceBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_qmsht));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_cylinder;
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mCylinderImageView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCylinderImageView.pause();
    }
}
