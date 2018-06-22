package com.walker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.walker.R;
import com.walker.entity.Summary;

import java.util.List;

/**
 * summary :主页面数据显示适配器
 * time    :2016/7/4 15:14
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class SummyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Summary> mData;

    public SummyAdapter(List<Summary> data) {
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary, parent,
                false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Summary entity = mData.get(position);
            ((ItemViewHolder) holder).summary.setText(entity.getSUMMARY());
            ((ItemViewHolder) holder).desc.setText(entity.getDESCRIPTION());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() == 0 ? 0 : mData.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView summary;
        TextView desc;

        public ItemViewHolder(View view) {
            super(view);
            summary = (TextView) view.findViewById(R.id.tv_summary);
            desc = (TextView) view.findViewById(R.id.tv_desc);
        }
    }
}
