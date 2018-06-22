package com.bsx.baolib.filepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bsx.baolib.R;
import com.bsx.baolib.filepicker.fragments.PhotoPickerFragment;
import com.bsx.baolib.filepicker.utils.FragmentUtil;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;


public class FilePickerActivity extends AppCompatActivity implements PhotoPickerFragment.PhotoPickerFragmentListener, PickerManagerListener {

    private static final String TAG = FilePickerActivity.class.getSimpleName();
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(PickerManager.getInstance().getTheme());
        super.onCreate(savedInstanceState);
        //针对SimpleDraweeView未引用到问题，特此初始化Fresco
        Fresco.initialize(this);
        setContentView(R.layout.activity_file_picker);

        if (savedInstanceState == null) {
            initView();
        }
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            setUpToolbar();

            ArrayList<String> selectedPaths = intent.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);
            type = intent.getIntExtra(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.PHOTO_PICKER);

            setToolbarTitle(0);

            PickerManager.getInstance().setPickerManagerListener(this);
            openSpecificFragment(type, selectedPaths);
        }
    }

    private void setUpToolbar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void openSpecificFragment(int type, ArrayList<String> selectedPaths) {
        if (type == FilePickerConst.PHOTO_PICKER) {
            PhotoPickerFragment photoFragment = PhotoPickerFragment.newInstance(selectedPaths);
            FragmentUtil.addFragment(this, R.id.container, photoFragment);
        }
    }

    private void setToolbarTitle(int count) {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (count > 0)
                actionBar.setTitle("已 选(" + count + "/" + PickerManager.getInstance().getMaxCount() + ")");
            else {
                if (type == FilePickerConst.PHOTO_PICKER) {
                    actionBar.setTitle("选择图片");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picker_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_done) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS, PickerManager.getInstance().getSelectedFilePaths());

            setResult(RESULT_OK, intent);
            finish();

            return true;
        } else if (i == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(int currentCount) {
        setToolbarTitle(currentCount);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }
}
