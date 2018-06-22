package com.walker.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bsx.baolib.filepicker.utils.image.FrescoFactory;
import com.facebook.drawee.view.SimpleDraweeView;
import com.walker.R;

import java.io.File;
import java.util.ArrayList;


/**
 * summary :图片选择适配器
 * time    :2016/8/15 14:14
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class ImgPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<String> paths;
    private int imageSize;

    public ImgPickerAdapter(Context context, ArrayList<String> paths) {
        this.paths = paths;
        setColumnNumber(context, 3);
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / columnNum;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imgpicker, parent, false);

        return new FileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FileViewHolder) {
            String path = paths.get(position);
            FrescoFactory.getLoader().showImage(((FileViewHolder) holder).imageView, Uri.fromFile(new File(path)), FrescoFactory.newOption(imageSize, imageSize));
        }
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView imageView;

        public FileViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.iv_photo);
        }
    }
}
