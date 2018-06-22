package com.walker.activity;

import android.os.Bundle;

import com.walker.R;
import com.walker.base.BaseFragActivity;
import com.walker.base.BaseFragment;
import com.walker.fragment.ScrollPagerFragment;

/**
 * summary :滑动翻页专栏
 * time    :2016/9/30 09:59
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class ScrollPagerActivity extends BaseFragActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.act_scroll_pager;
    }

    @Override
    protected int getFragContentId() {
        return R.id.pager_content;
    }

    @Override
    protected void initialization(Bundle savedInstanceState) {

    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }

    @Override
    protected BaseFragment getFirstFragment() {
        return new ScrollPagerFragment();
    }
}
