package com.walker.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.walker.R;
import com.walker.base.BaseFragment;
import com.walker.utils.ToastUtil;
import com.walker.view.FullSheetDialogFragment;

/**
 * summary :BottomSheet
 * time    :2016/9/8 10:39
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class BottomSheetFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 简单使用
     */
    private Button mBtnEasy;
    /**
     * 便捷弹框
     */
    private Button mBtnDialog;
    /**
     * 全屏
     */
    private Button mBtnDialogFragment;
    /**
     * 基础视图
     */
    private View mBaseView;

    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        mBaseView = baseView;
        mBtnEasy = (Button) mBaseView.findViewById(R.id.btn_easy);
        mBtnDialog = (Button) mBaseView.findViewById(R.id.btn_dialog);
        mBtnDialogFragment = (Button) mBaseView.findViewById(R.id.btn_dialog_fragment);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_bottom_sheet;
    }

    @Override
    protected void bindListener() {
        mBtnEasy.setOnClickListener(this);
        mBtnDialog.setOnClickListener(this);
        mBtnDialogFragment.setOnClickListener(this);
    }

    @Override
    protected void unbindListener() {
        mBtnEasy.setOnClickListener(null);
        mBtnDialog.setOnClickListener(null);
        mBtnDialogFragment.setOnClickListener(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_easy:
                showEasy();
                break;
            case R.id.btn_dialog:
                showDialog();
                break;
            case R.id.btn_dialog_fragment:
                showFullScreen();
                break;
            default:
                break;
        }
    }

    private void showEasy() {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(mBaseView.findViewById(R.id.nested_scroll));
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
//        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });
    }

    private void showDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(mHoldActivity);
        dialog.setTitle("BottomSheetDialog");
        ListView listView = new ListView(mHoldActivity);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mHoldActivity, android.R.layout.simple_list_item_1,
                new String[]{"影子1号", "影子2号", "影子3号", "影子4号", "影子5号", "影子6号", "影子7号", "影子8号",});
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ToastUtil.showShort("选择了第" + (i + 1 )+ "号");
                dialog.dismiss();
            }
        });
        dialog.setContentView(listView);
        dialog.show();
    }

    private void showFullScreen(){

        new FullSheetDialogFragment().show(this.getFragmentManager(), "dialog");
    }


}
