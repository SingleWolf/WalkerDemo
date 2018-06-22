package com.walker.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.bsx.baolib.view.RecordView;
import com.walker.R;
import com.walker.base.BaseFragment;
import com.walker.utils.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

import static com.bsx.baolib.view.RecordView.MODEL_PLAY;

/**
 * Created by Walker on 2017/4/10.
 */

public class RecordViewFragment extends BaseFragment implements View.OnTouchListener,View.OnClickListener{

    private RecordView mRecorfView;
    private Button mBtnAction;
    private Button mBtnMode;
    private TimerTask timeTask;
    private Timer timeTimer = new Timer(true);

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int db = (int) (Math.random()*100);
            mRecorfView.setVolume(db);
        }
    };
    private int nowModel = RecordView.MODEL_RECORD;

    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        initView(baseView);
    }

    private void initView(View baseView) {
        mRecorfView= (RecordView) baseView.findViewById(R.id.record_view);
        mBtnAction= (Button) baseView.findViewById(R.id.btn_act);
        mBtnMode= (Button) baseView.findViewById(R.id.btn_mode);

        mRecorfView.setCountdownTime(20);
        mRecorfView.setModel(RecordView.MODEL_RECORD);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_record_view;
    }

    @Override
    protected void bindListener() {
        mBtnAction.setOnTouchListener(this);
        mBtnMode.setOnClickListener(this);
    }

    @Override
    protected void unbindListener() {
        mBtnAction.setOnTouchListener(null);
        mBtnMode.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {

        if(nowModel == MODEL_PLAY){
            mRecorfView.setModel(RecordView.MODEL_RECORD);
            nowModel = RecordView.MODEL_RECORD;
        }else{
            mRecorfView.setModel(MODEL_PLAY);
            nowModel = MODEL_PLAY;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            mRecorfView.start();
            timeTimer.schedule(timeTask = new TimerTask() {
                public void run() {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }, 20, 20);
            mRecorfView.setOnCountDownListener(new RecordView.OnCountDownListener() {
                @Override
                public void onCountDown() {
                    ToastUtil.showCenterLong("计时结束啦~~");
                }
            });
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            mRecorfView.cancel();
        }
        return false;
    }
}
