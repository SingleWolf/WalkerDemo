package com.bsx.baolib.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * summary :
 * time    :2016/9/30 10:14
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class NonPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        page.setScaleX(0.999f);//hack
    }

    public static final ViewPager.PageTransformer INSTANCE = new NonPageTransformer();
}