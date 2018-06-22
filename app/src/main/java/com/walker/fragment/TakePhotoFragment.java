package com.walker.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.bsx.baolib.log.BaoLog;
import com.walker.R;
import com.walker.adapter.TakePhotoAdapter;
import com.walker.base.BaseFragment;
import com.walker.constant.BaseParams;
import com.walker.data.storage.StorageHelper;
import com.walker.delegate.OnRecyclerItemClickListener;
import com.walker.entity.TakePhoto;
import com.walker.utils.CameraUtil;
import com.walker.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * summary :
 * time    :2016/9/5 15:35
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class TakePhotoFragment extends BaseFragment {
    private int mItemSize;
    private ArrayList<TakePhoto> mPhotoData;
    private TakePhotoAdapter mAdapter;
    private int mPhotoIndex;
    private String mCurrentPath;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        WindowManager wm = (WindowManager) mHoldActivity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mItemSize = metrics.widthPixels / 3;

        mPhotoData = new ArrayList<>();
        for (int i = 0; i < 38; i++) {
            TakePhoto entity = new TakePhoto();
            entity.setDesc("添加图片");
            mPhotoData.add(entity);
        }

        mAdapter = new TakePhotoAdapter(mPhotoData, mItemSize);
    }

    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        RecyclerView recyclerView = (RecyclerView) baseView.findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                mPhotoIndex = vh.getLayoutPosition();
                try {
                    mCurrentPath=getPhotoPath();
                    File f = new File(mCurrentPath);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //系统大于N的统一用FileProvider处理
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                        //将文件转换成content://Uri形式
                        Uri photoUri= FileProvider.getUriForFile(mHoldActivity,"com.walker.provider",f);
                        //申请临时读写文件权限
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    }else{
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    }

                    if (intent.resolveActivity(mHoldActivity.getPackageManager()) != null) {
                       startActivityForResult(intent, CameraUtil.CAMERA_REQUEST_CODE);
                    }
                } catch (OutOfMemoryError e) {
                    System.gc();
                    BaoLog.e("TakePhotoFragment OpenCamera", e);
                } catch (Exception e) {
                    BaoLog.e("TakePhotoFragment OpenCamera", e);
                }
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                mPhotoData.get(vh.getLayoutPosition()).setDesc("desc has change");
                mAdapter.notifyItemChanged(vh.getLayoutPosition());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_take_photo;
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraUtil.CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mPhotoData.get(mPhotoIndex).setSavePath(mCurrentPath);
                mAdapter.notifyItemChanged(mPhotoIndex);
            }
        }
    }

    private String getPhotoPath() {
        String luckyPath = StringUtil.pliceStr(StorageHelper.getWalkerRootPath(), File.separator, BaseParams.DIR_PIC);
        File file = new File(luckyPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return StringUtil.pliceStr(luckyPath, File.separator, "walker_", System.currentTimeMillis(), ".jpg");
    }
}