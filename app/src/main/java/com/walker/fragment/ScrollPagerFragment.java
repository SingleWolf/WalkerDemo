package com.walker.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.walker.R;
import com.walker.base.BaseFragment;

/**
 * summary :翻页引导
 * time    :2016/9/30 10:30
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class ScrollPagerFragment extends BaseFragment {
    private Button mBtnMagicPager;

    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        mBtnMagicPager = (Button) baseView.findViewById(R.id.btn_magic_pager);
        mBtnMagicPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new ClaimAFragment(), "ClaimAFragment");
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_scroll_pager;
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }
}
