package com.walker.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsx.baolib.log.BaoLog;
import com.bsx.baolib.transformer.RotateYTransformer;
import com.bsx.baolib.view.CircleTextView;
import com.bsx.baolib.view.loading.CustomProgress;
import com.walker.R;
import com.walker.adapter.ClaimAPager;
import com.walker.base.BaseFragment;
import com.walker.constant.BaseParams;
import com.walker.data.storage.StorageHelper;
import com.walker.entity.TakePhoto;
import com.walker.utils.CameraUtil;
import com.walker.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * summary :理赔A方案
 * time    :2016/9/30 13:53
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class ClaimAFragment extends BaseFragment {
    private ViewPager mViewPager;
    private ImageView mIvGuide;
    private CircleTextView mCtvTake;
    private CircleTextView mCtvCancel;
    private TextView mTvLabel;
    private ArrayList<TakePhoto> mPhotoData;
    private int mCurrentPos;
    private String mCurrentPath;
    private ClaimAPager mClaimAPager;
    private CustomProgress mProgressDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPhotoData = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            TakePhoto entity = new TakePhoto();
            entity.setDesc("添加图片");
            mPhotoData.add(entity);
        }
        mClaimAPager = new ClaimAPager(mPhotoData);
    }

    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        mCtvTake = (CircleTextView) baseView.findViewById(R.id.ctv_take);
        mCtvCancel = (CircleTextView) baseView.findViewById(R.id.ctv_cancel);
        mTvLabel = (TextView) baseView.findViewById(R.id.tv_label);
        mCtvTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showProgressDialog();
                    mCurrentPath = getPhotoPath();
                    File f = new File(mCurrentPath);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    if (intent.resolveActivity(mHoldActivity.getPackageManager()) != null) {
                        startActivityForResult(intent, CameraUtil.CAMERA_REQUEST_CODE);
                    }
                } catch (Exception e) {
                    BaoLog.e("ClaimAFragment OpenCamera", e);
                }
            }
        });
        mCtvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentPath = "";
                mPhotoData.get(mCurrentPos).setSavePath(mCurrentPath);
                mClaimAPager.notifyDataSetChanged();
            }
        });

        mViewPager = (ViewPager) baseView.findViewById(R.id.pager_take);
        mIvGuide = (ImageView) baseView.findViewById(R.id.iv_guide);
        mIvGuide.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_claim_guide));

        mViewPager.setPageMargin(20);//设置page间间距，自行根据需求设置
        mViewPager.setOffscreenPageLimit(3);//>=3

        mViewPager.setAdapter(mClaimAPager);

        //setPageTransformer 决定动画效果
        mViewPager.setPageTransformer(true, new RotateYTransformer(45));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPos = position;
                TakePhoto claim = mPhotoData.get(position);
                mTvLabel.setText(claim.getDesc());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(1);
        mCurrentPos = 1;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraUtil.CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mHandler.sendEmptyMessageDelayed(0x123, 500);
            } else {
                cancelProgressDialog();
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_claim_a;
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }

    private String getPhotoPath() {
        String luckyPath = StringUtil.pliceStr(StorageHelper.getWalkerRootPath(), File.separator, BaseParams.DIR_PIC);
        File file = new File(luckyPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return StringUtil.pliceStr(luckyPath, File.separator, "walker_", System.currentTimeMillis(), ".jpg");
    }

    /**
     * 显示自定义进度框
     */
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new CustomProgress(getHoldActivity());
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void cancelProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                mPhotoData.get(mCurrentPos).setSavePath(mCurrentPath);
                mClaimAPager.notifyDataSetChanged();
                cancelProgressDialog();
            }
        }
    };
}
