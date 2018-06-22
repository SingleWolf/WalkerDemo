package com.walker.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.walker.R;
import com.walker.adapter.MagicImageAdapter;
import com.walker.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * summary :使用自定义的图片加载器实现图片加载
 * time    :2016/8/26 10:34
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class MagicImageFragment extends BaseFragment{
    /**
     * 圖片url
     */
    private List<String> mImageUrl;
    /**
     * 適配器
     */
    private MagicImageAdapter mMagicImageAdapter;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mImageUrl=new ArrayList<>();
        mImageUrl.add("http://b275.photo.store.qq.com/psb?/V11OurSW2ChhoY/5UJeoqwVzcwy.j4CdRG02VWKo9ZJXzyKaQhojUED5jA!/b/dDtI7qNYIQAA&bo=IAPDAQAAAAABB8E!&rf=viewer_4");
        mImageUrl.add("http://b277.photo.store.qq.com/psb?/V11OurSW2ChhoY/DdqxsraMqN6lOyGrFH7A6YbY26BWsmSbaY1fNVnYHUk!/b/dJ.ZIqWQCAAA&bo=IAPDAQAAAAABAMY!&rf=viewer_4");
        mImageUrl.add("http://b275.photo.store.qq.com/psb?/V11OurSW2ChhoY/W5cazdYiXSLA27WGsqGZgy5mNVaC6817StQpk8gLun0!/b/dKhe8aMTJAAA&bo=IAPDAQAAAAABAMY!&rf=viewer_4");
        mImageUrl.add("http://b276.photo.store.qq.com/psb?/V11OurSW2ChhoY/Tr*eEzs*yWHjDtzre2H62A1Wm6kChZtqn4obmEQD.dA!/b/dGv2hqT1IAAA&bo=IAPDAQAAAAABAMY!&rf=viewer_4");
        mImageUrl.add("http://b275.photo.store.qq.com/psb?/V11OurSW2ChhoY/vGL7HQDFAsHWWDPwsgCv*j4Al7sdgmI1qc.413drd64!/b/dHjv8qOXIAAA&bo=IAPDAQAAAAABAMY!&rf=viewer_4");
        mImageUrl.add("http://b275.photo.store.qq.com/psb?/V11OurSW2ChhoY/RLnAu6nehTNYDubAjGnr81.LgeWIv3OcsV7K5GGp8sg!/b/dJaI96OhIgAA&bo=IAPDAQAAAAABAMY!&rf=viewer_4");
        mImageUrl.add("http://b275.photo.store.qq.com/psb?/V11OurSW2ChhoY/54EAtZLO*19JvMH4wtJ0RcnJPswm.7eW*hgB07s0keI!/b/dNts96OtIAAA&bo=wwEgAwAAAAABAMY!&rf=viewer_4");
        mImageUrl.add("http://b275.photo.store.qq.com/psb?/V11OurSW2ChhoY/i*SlONc.FjUgEMXp2Q1YqrnLnIFaiT0QQDgx9mSkKTs!/b/dAED.aObIgAA&bo=wwEgAwAAAAABAMY!&rf=viewer_4");
        mImageUrl.add("http://b275.photo.store.qq.com/psb?/V11OurSW2ChhoY/CboZYsZzFmzIvgA.No2Zms4iL0hr5EE6Otw0Weilak4!/b/dD3f76MKJAAA&bo=IAPDAQAAAAABAMY!&rf=viewer_4");
        mImageUrl.add("http://b276.photo.store.qq.com/psb?/V11OurSW2ChhoY/cEuMnK4duC3x7XgNXAL.KkdV.9E9gHZkJVqzgCRDEn4!/b/dE3ihqQaHwAA&bo=IAPDAQAAAAABAMY!&rf=viewer_4");
        mImageUrl.add("http://b277.photo.store.qq.com/psb?/V11OurSW2ChhoY/4.hsqiy8s4P8pr9HtbSPv0Fgn7cN*xiucf7H7mPxubI!/b/dPMKIaX8BwAA&bo=IAPDAQAAAAABAMY!&rf=viewer_4");
        mImageUrl.add("http://b277.photo.store.qq.com/psb?/V11OurSW2ChhoY/3aocx1*GAzZrXtEJjyD6P29yc1QKsQPW9T9zUiLb*Y8!/b/dIuLJaVACAAA&bo=IAPDAQAAAAABAMY!&rf=viewer_4");
        mMagicImageAdapter =new MagicImageAdapter(mImageUrl);
    }

    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        RecyclerView recyclerView= (RecyclerView) baseView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mMagicImageAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_magicimage;
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }

    @Override
    public void onPause() {
        super.onPause();
        mMagicImageAdapter.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMagicImageAdapter.cancelAllTasks();
    }
}
