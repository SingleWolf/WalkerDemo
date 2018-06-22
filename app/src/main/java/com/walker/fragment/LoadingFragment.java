package com.walker.fragment;

import android.content.DialogInterface;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bsx.baolib.log.BaoLog;
import com.bsx.baolib.view.loading.CustomProgress;
import com.bsx.baolib.view.loading.FlikerProgressBar;
import com.bsx.baolib.view.loading.HorizontalProgressBar;
import com.bsx.baolib.view.loading.LoadingView;
import com.bsx.baolib.view.loading.RoundProgressBar;
import com.bsx.baolib.view.loading.RoundProgressBar2;
import com.walker.R;
import com.walker.base.BaseFragment;
import com.walker.utils.ToastUtil;

/**
 * summary :进度条专题
 * time    :2016/9/29 14:12
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class LoadingFragment extends BaseFragment {
    private FlikerProgressBar mFlikerProgressBar;
    private HorizontalProgressBar mHorizontalProgressBar;
    private RoundProgressBar mRoundProgressBar;
    private RoundProgressBar2 mRoundProgressBar2_1;
    private RoundProgressBar2 mRoundProgressBar2_2;
    private LoadingView mLoadingView;
    private Button mBtnCustomProgress;

    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        initView(baseView);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.frag_loading;
    }

    @Override
    protected void bindListener() {
    }

    @Override
    protected void unbindListener() {
    }

    /**
     * 初始化组件
     *
     * @param baseView 根视图
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView(View baseView) {
        mFlikerProgressBar = (FlikerProgressBar) baseView.findViewById(R.id.progress_fliker);
        mHorizontalProgressBar = (HorizontalProgressBar) baseView.findViewById(R.id.progress_h);
        mRoundProgressBar = (RoundProgressBar) baseView.findViewById(R.id.progress_r);
        mRoundProgressBar2_1 = (RoundProgressBar2) baseView.findViewById(R.id.progress_r2_1);
        mRoundProgressBar2_2 = (RoundProgressBar2) baseView.findViewById(R.id.progress_r2_2);
        mLoadingView = (LoadingView) baseView.findViewById(R.id.lv_load);
        mBtnCustomProgress = (Button) baseView.findViewById(R.id.btn_custom_progress);
        mBtnCustomProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomProgressDialog();
            }
        });

        ImageView iv_vector = (ImageView) baseView.findViewById(R.id.iv_svg_anim);
        AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) iv_vector.getDrawable();
        drawable.start();

        mFlikerProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFlikerProgressBar.isFinish()) {
                    mFlikerProgressBar.toggle();
                }
            }
        });
        showFlikerProgressBar();
    }

    /**
     * 显示自定义进度框
     */
    private void showCustomProgressDialog() {
        CustomProgress progressDialog = new CustomProgress(getHoldActivity());
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                ToastUtil.showShort("呦呵，取消啦啊~");
            }
        });
        progressDialog.show();
    }


    /**
     * 显示仿应用宝进度条
     */
    private void showFlikerProgressBar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(200);
                        Message message = handler.obtainMessage();
                        message.arg1 = i + 1;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        BaoLog.e("showFlikerProgressBar", e);
                    }
                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mFlikerProgressBar.setProgress(msg.arg1);
            mHorizontalProgressBar.setProgress(msg.arg1);
            mRoundProgressBar.setProgress(msg.arg1);
            mRoundProgressBar2_1.setProgress(msg.arg1);
            mRoundProgressBar2_2.setProgress(msg.arg1);
            if (msg.arg1 == 100) {
                mFlikerProgressBar.finishLoad();
            }
        }
    };
}
