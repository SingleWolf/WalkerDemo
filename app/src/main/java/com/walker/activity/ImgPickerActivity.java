package com.walker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bsx.baolib.filepicker.FilePickerBuilder;
import com.bsx.baolib.filepicker.FilePickerConst;
import com.walker.R;
import com.walker.adapter.ImgPickerAdapter;
import com.walker.base.BasePureActivity;
import com.walker.utils.ToastUtil;

import java.util.ArrayList;

/**
 * summary :文件选择器
 * time    :2016/8/31 18:17
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class ImgPickerActivity extends BasePureActivity {
    private ArrayList<String> filePaths;

    @Override
    protected int getContentViewId() {
        return R.layout.act_file_picker;
    }

    @Override
    protected void initialization(Bundle savedInstanceState) {
        initToolBar();
        findViewById(R.id.btn_picker_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerBuilder.getInstance().setMaxCount(9)
                        .setSelectedFiles(filePaths)
                        .setActivityTheme(R.style.ImgPickerTheme)
                        .pickPhoto(ImgPickerActivity.this);
            }
        });
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);
                    addThemToView(filePaths);
                }
        }
    }

    /**
     * 初始化toolbar
     */
    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("图片选择器");
        setSupportActionBar(toolbar);
    }

    private void addThemToView(ArrayList<String> filePaths) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        if (recyclerView != null) {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
            layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            recyclerView.setLayoutManager(layoutManager);

            ImgPickerAdapter imageAdapter = new ImgPickerAdapter(this, filePaths);

            recyclerView.setAdapter(imageAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
        ToastUtil.showShort("Num of files selected: " + filePaths.size());
    }
}
