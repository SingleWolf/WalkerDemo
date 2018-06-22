
package com.walker.delegate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.walker.activity.WebActivity;
import com.walker.utils.ToastUtil;

/**
 * summary :辅助客户端与JS交互
 * <p>
 * time    : 2016/5/30 13:42
 * e-mail  : singlewolf968@gmail.com
 *
 * @author : Walker
 */
public class HostJsScope {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * webview
     */
    private WebView mWebView;
    /**
     * 进度框
     */
    private ProgressDialog mProgress;

    public HostJsScope(Context context, WebView webView) {
        super();
        mWebView = webView;
        mContext = context;
    }

    @JavascriptInterface
    public void alert(String message) {
        // 构建一个Builder来显示网页中的alert对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }


    /**
     * 显示进度框
     */
    @JavascriptInterface
    public void showProgress() {
        if (mProgress == null) {
            mProgress = new ProgressDialog(mContext);
            mProgress.setMessage("请稍候...");
            // mProgress.setCancelable(false);
        }
        mProgress.show();
    }

    /**
     * 显示进度框
     *
     * @param msg 提示信息
     */
    @JavascriptInterface
    public void showProgress(String msg) {
        if (mProgress == null) {
            mProgress = new ProgressDialog(mContext);
            mProgress.setMessage(msg);
            mProgress.setCancelable(false);
        } else {
            mProgress.setMessage(msg);
        }
        mProgress.show();
    }

    /**
     * 取消进度框
     */
    @JavascriptInterface
    public void cancelProgress() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.cancel();
        }
    }

    /**
     * 信息提示
     *
     * @param msg 提示信息
     */
    @JavascriptInterface
    public void toast(String msg) {
        ToastUtil.showCenterShort(msg);
    }


    /**
     * 跳入新的WebView
     *
     * @param url url
     */
    @JavascriptInterface
    public void anotherView(String url) {
        WebActivity.actionStart(mContext, url, false);
    }

}