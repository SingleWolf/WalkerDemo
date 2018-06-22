package com.walker.fragment;

import android.os.Bundle;
import android.view.View;

import com.bsx.baolib.view.TemperatureView;
import com.walker.R;
import com.walker.base.BaseFragment;

/**
 * summary :自定义温度仪表盘
 * time    :2016/8/31 16:19
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class TemperatureViewFragment extends BaseFragment {
    private TemperatureView mTemperatureView;

    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        mTemperatureView = (TemperatureView) baseView.findViewById(R.id.temperature_view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (float i = 0; i <= 40; i++) {
                    mTemperatureView.setCurrentTemp(i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_temperature_view;
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }
}
