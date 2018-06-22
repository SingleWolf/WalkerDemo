package com.walker.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.walker.R;
import com.walker.base.BaseFragment;

/**
 * summary :win10风格的UI
 * time    :2016/9/23 10:37
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class Win10UIFragment extends BaseFragment implements View.OnClickListener {
    private Button mShowProgressDialog;

    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        mShowProgressDialog = (Button) baseView.findViewById(R.id.btn_showProgressDialog);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_win10_ui;
    }

    @Override
    protected void bindListener() {
        mShowProgressDialog.setOnClickListener(this);
    }

    @Override
    protected void unbindListener() {
        mShowProgressDialog.setOnClickListener(null);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_showProgressDialog) {
            AlertDialog.Builder progressDialog = new AlertDialog.Builder(getHoldActivity(), R.style.Win10_Dialog_Theme);
            View progressView = LayoutInflater.from(getHoldActivity()).inflate(R.layout.layout_win10_progress, null);
            progressDialog.setView(progressView);
            progressDialog.create().show();
        }
    }
}
