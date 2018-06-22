package com.walker.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.walker.R;
import com.walker.entity.TakePhoto;
import com.walker.utils.BitmapUtil;

import java.util.ArrayList;

/**
 * summary :拍照列适配器
 * time    :2016/8/15 14:14
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class TakePhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TakePhoto> mData;
    private static int mItemSize;

    public TakePhotoAdapter(ArrayList<TakePhoto> data, int itemSize) {
        mData = data;
        mItemSize = itemSize;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_take_photo, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            TakePhoto takePhoto = mData.get(position);
            ((ItemViewHolder) holder).tvDesc.setText(takePhoto.getDesc());

            String savePath = takePhoto.getSavePath();
            if (!TextUtils.isEmpty(savePath)) {
                ((ItemViewHolder) holder).ivIcon.setImageBitmap(BitmapUtil.onThumbnail(savePath, mItemSize, mItemSize, true));
            } else {
                ((ItemViewHolder) holder).ivIcon.setImageResource(R.drawable.ic_add_pic);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon;
        TextView tvDesc;

        public ItemViewHolder(View itemView) {
            super(itemView);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mItemSize, mItemSize);
            itemView.setLayoutParams(lp);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
        }
    }
}
