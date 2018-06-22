package com.walker.adapter;

import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.walker.R;
import com.walker.entity.TakePhoto;
import com.walker.utils.BitmapUtil;

import java.util.ArrayList;

/**
 * summary :A方案理赔适配器
 * time    :2016/10/8 10:37
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class ClaimAPager extends PagerAdapter {
    private ArrayList<TakePhoto> mData;
    private int mChildCount;

    public ClaimAPager(ArrayList<TakePhoto> data) {
        mData = data;
        mChildCount = 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_claim_take, null);
        TakePhoto claim = mData.get(position);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_photo);
        if (!TextUtils.isEmpty(claim.getSavePath())) {
            iv.setImageBitmap(BitmapUtil.onThumbnail(claim.getSavePath(), 300, 200, true));
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

}