package com.bsx.baolib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bsx.baolib.R;

/**
 * summary :主题自定义对话框
 * time    :2016/5/26 16:25
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class BaoDialog {
    /**
     * 上下文
     */

    private Context mContext;
    /**
     * 标题
     */

    private TextView mTxtTitle;

    /**
     * 描述信息
     */

    private TextView mTxtMessage;

    /**
     * 确定按钮
     */

    private Button mBtnOK;

    /**
     * 取消按钮
     */

    private Button mBtnCancel;

    /**
     * 其他功能按钮
     */

    private Button mBtnAnother;

    /**
     * 主内容容器
     */

    private FrameLayout mViewContent;

    /**
     * 对话框实例
     */

    private AlertDialog mAlertDialog;

    public BaoDialog(Context context) {
        mContext = context;
        initView();
    }

    /**
     * 初始化组建
     */

    @SuppressLint("InflateParams")
    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_baodialog, null);
        mTxtTitle = (TextView) view.findViewById(R.id.txt_title);
        mTxtMessage = (TextView) view.findViewById(R.id.txt_message);
        mBtnOK = (Button) view.findViewById(R.id.btn_ok);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBtnAnother = (Button) view.findViewById(R.id.btn_another);
        mViewContent = (FrameLayout) view.findViewById(R.id.layout_content);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        mAlertDialog = builder.setView(view).create();
        mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * setCancelable
     *
     * @param isCancel
     */
    public void setCancelable(boolean isCancel) {
        if (mAlertDialog != null) {
            mAlertDialog.setCancelable(isCancel);
        }
    }

    /**
     * setCanceledOnTouchOutside
     *
     * @param isCancel
     */
    public void setCanceledOnTouchOutside(boolean isCancel) {
        if (mAlertDialog != null) {
            mAlertDialog.setCanceledOnTouchOutside(isCancel);
        }
    }

    /**
     * 设置标题
     *
     * @param titleId 标题资源id
     */

    public void setTitle(int titleId) {
        if (mTxtTitle != null) {
            mTxtTitle.setText(titleId);
        }
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */

    public void setTitle(String title) {
        if (mTxtTitle != null) {
            mTxtTitle.setText(title);
        }
    }

    /**
     * 设置提示信息
     *
     * @param messageId 描述信息的资源id
     */

    public void setMessage(int messageId) {
        if (mTxtMessage != null) {
            mTxtMessage.setText(messageId);
        }
    }

    /**
     * 设置提示信息
     *
     * @param message 提示信息
     */

    public void setMessage(String message) {
        if (mTxtMessage != null) {
            mTxtMessage.setText(message);
        }
    }

    /**
     * 设置确定按钮
     *
     * @param labelId  标签id
     * @param listener 监听器
     */

    public void setButtonOk(int labelId, View.OnClickListener listener) {
        if (mBtnOK != null) {
            mBtnOK.setText(labelId);
            mBtnOK.setVisibility(View.VISIBLE);
            mBtnOK.setOnClickListener(listener);
        }
    }

    /**
     * 设置确定按钮
     *
     * @param label    标签
     * @param listener 监听器
     */

    public void setButtonOk(String label, View.OnClickListener listener) {
        if (mBtnOK != null) {
            mBtnOK.setText(label);
            mBtnOK.setVisibility(View.VISIBLE);
            mBtnOK.setOnClickListener(listener);
        }
    }

    /**
     * 设置取消按钮
     *
     * @param labelId  标签id
     * @param listener 监听器
     */
    public void setButtonCancel(int labelId, View.OnClickListener listener) {
        if (mBtnCancel != null) {
            mBtnCancel.setText(labelId);
            mBtnCancel.setVisibility(View.VISIBLE);
            mBtnCancel.setOnClickListener(listener);
        }
    }

    /**
     * 设置取消按钮
     *
     * @param label    标签
     * @param listener 监听器
     */
    public void setButtonCancel(String label, View.OnClickListener listener) {
        if (mBtnCancel != null) {
            mBtnCancel.setText(label);
            mBtnCancel.setVisibility(View.VISIBLE);
            mBtnCancel.setOnClickListener(listener);
        }
    }

    /**
     * 设置其他按钮
     *
     * @param labelId  标签id
     * @param listener 监听器
     */
    public void setButtonAnother(int labelId, View.OnClickListener listener) {
        if (mBtnAnother != null) {
            mBtnAnother.setText(labelId);
            mBtnAnother.setVisibility(View.VISIBLE);
            mBtnAnother.setOnClickListener(listener);
        }
    }

    /**
     * 设置其他按钮
     *
     * @param label    标签
     * @param listener 监听器
     */
    public void setButtonAnother(String label, View.OnClickListener listener) {
        if (mBtnAnother != null) {
            mBtnAnother.setText(label);
            mBtnAnother.setVisibility(View.VISIBLE);
            mBtnAnother.setOnClickListener(listener);
        }
    }

    /**
     * 设置显示内容
     *
     * @param view 填充布局
     */

    public void setContent(View view) {
        if (mViewContent != null) {
            if (view != null) {
                mViewContent.addView(view);
            }
        }
    }

    /**
     * 显示
     */

    public void show() {
        if (mAlertDialog != null && mAlertDialog.isShowing() == false) {
            mAlertDialog.show();
        }
    }

    /**
     * 消失
     */

    public void dismiss() {
        if (mAlertDialog != null && mAlertDialog.isShowing() == true) {
            mAlertDialog.dismiss();
        }
    }
}
