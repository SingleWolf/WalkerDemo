package com.bsx.baolib.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * summary :自定义ViewPager，旨在滑动更流畅
 * time    :2016/10/10 10:37
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class CustomViewPager extends ViewPager {

    private int preX = 0;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CustomViewPager(Context context) {
        super(context);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent even) {

        if (even.getAction() == MotionEvent.ACTION_DOWN) {
            preX = (int) even.getX();
        } else {
            if (Math.abs((int) even.getX() - preX) > 10) {
                return true;
            } else {
                preX = (int) even.getX();
            }
        }
        return super.onInterceptTouchEvent(even);
    }
}
