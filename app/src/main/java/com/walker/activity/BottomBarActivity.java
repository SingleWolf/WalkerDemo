package com.walker.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.walker.R;
import com.walker.base.BasePureActivity;
import com.walker.utils.ToastUtil;

/**
 * summary :底部导航栏
 * time    :2016/8/30 14:15
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class BottomBarActivity extends BasePureActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.act_bottom_bar;
    }

    @Override
    protected void initialization(Bundle savedInstanceState) {
        initToolBar();
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTabPosition(1);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_favorites:
                        ToastUtil.showShort("选择了：tab_favorites");
                        break;
                    case R.id.tab_friends:
                        ToastUtil.showShort("选择了：tab_friends");
                        break;
                    case R.id.tab_nearby:
                        ToastUtil.showShort("选择了：tab_nearby");
                        break;
                }
            }
        });
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_favorites:
                        ToastUtil.showShort("重新选择：tab_favorites");
                        break;
                    case R.id.tab_friends:
                        ToastUtil.showShort("重新选择：tab_friends");
                        break;
                    case R.id.tab_nearby:
                        ToastUtil.showShort("重新选择：tab_nearby");
                        break;
                }
            }
        });
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }

    /**
     * 初始化toolbar
     */
    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("底部导航栏");
        setSupportActionBar(toolbar);
    }
}
