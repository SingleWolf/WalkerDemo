package com.walker.fragment;

import android.os.Bundle;
import android.view.View;

import com.bsx.baolib.view.TouchImageView;
import com.walker.R;
import com.walker.base.BaseFragment;

/**
 * summary :支持手势缩放的ImageView
 * time    :2016/9/12 20:26
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class TouchImgFragment extends BaseFragment {
    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        TouchImageView img = (TouchImageView) baseView.findViewById(R.id.touchImage);
        img.setMaxZoom(4f);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_touch_img;
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }
}
