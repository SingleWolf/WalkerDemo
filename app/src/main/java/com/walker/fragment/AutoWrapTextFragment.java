package com.walker.fragment;

import android.os.Bundle;
import android.view.View;

import com.bsx.baolib.view.AutoWrapTextView;
import com.walker.R;
import com.walker.base.BaseFragment;

/**
 * Created by Walker on 2017/4/18.
 */

public class AutoWrapTextFragment extends BaseFragment{
    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        AutoWrapTextView autoWrapText= (AutoWrapTextView) baseView.findViewById(R.id.awtextview);
        String msg = "密码：jokG5456KL542356jsjdherGHS";
        autoWrapText.setText(msg);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_auto_wrap;
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }
}
