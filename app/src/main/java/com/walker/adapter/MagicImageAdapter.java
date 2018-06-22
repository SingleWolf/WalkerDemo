package com.walker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.walker.R;
import com.walker.logic.MagicImage;

import java.util.List;

/**
 * summary :加载图片适配器
 * time    :2016/8/26 10:48
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class MagicImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    /**
     * 图片加载器
     */
    private MagicImage mLoadImage;
    /**
     * 待加载图片的url
     */
    private List<String> mImageUrl;

    public MagicImageAdapter(List<String> imageUrl) {
        mImageUrl = imageUrl;
        mLoadImage = MagicImage.newInstance(MagicImage.MODE_LOAD_SHOW);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ImageView iv = ((ItemViewHolder) holder).icon;
            String iconUrl = mImageUrl.get(position);
            iv.setTag(iconUrl);
            mLoadImage.loadShow(iv, iconUrl);
        }
    }

    @Override
    public int getItemCount() {
        return mImageUrl.size() == 0 ? 0 : mImageUrl.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;

        public ItemViewHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.iv_icon);
        }
    }
    /**
     * 刷新缓存
     */
    public void flushCache() {
        mLoadImage.flushCache();
    }

    /**
     * 取消所有任务
     */
    public void cancelAllTasks() {
        mLoadImage.cancelAllTasks();
    }
}
