package com.walker.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.bsx.baolib.log.BaoLog;
import com.bsx.baolib.view.loading.CustomProgress;
import com.walker.R;
import com.walker.WalkerApplication;
import com.walker.base.BasePureActivity;
import com.walker.constant.BaseParams;
import com.walker.data.storage.StorageHelper;
import com.walker.delegate.HostJsScope;
import com.walker.utils.HttpUtil;
import com.walker.utils.StringUtil;
import com.walker.utils.TaskExecutor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * summary :承载网页的活动
 * time    :2016/8/23 10:59
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class WebActivity extends BasePureActivity implements View.OnClickListener {
    /**
     * 显示error页面
     */
    private static final int CODE_ERROR_SHOW = 1;
    /**
     * 取消error页面
     */
    private static final int CODE_ERROR_CANCEL = 2;
    /**
     * 网页请求异常
     */
    private static final int CODE_REQUEST_ERR = 3;
    /**
     * 网页请求正常
     */
    private static final int CODE_REQUEST_OK = 4;
    /**
     * 呈现webview的布局
     */
    private FrameLayout mLayoutContent;
    /**
     * 进度条
     */
    private ProgressBar mProgress;
    /**
     * 进度框
     */
    private CustomProgress mProgressDialog;
    /**
     * webview组件
     */
    private WebView mWebView;
    /**
     * 错误视图
     */

    private View mViewError;
    /**
     * js交互接口
     */
    private HostJsScope mJsDelegate;

    /**
     * 开启活动
     *
     * @param context  上下文
     * @param url      url
     * @param isFinish 是否关闭启动者
     */
    public static void actionStart(Context context, String url, boolean isFinish) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
        if (isFinish) {
            ((Activity) context).finish();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_webview;
    }

    @Override
    protected void initialization(Bundle savedInstanceState) {
        initToolBar();
        mLayoutContent = (FrameLayout) findViewById(R.id.layout_content);
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        mViewError = findViewById(R.id.view_err);
        initWebView(savedInstanceState);
    }

    @Override
    protected void bindListener() {
        mViewError.setOnClickListener(this);
    }

    @Override
    protected void unbindListener() {
        mViewError.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_err:
                handler.sendEmptyMessage(CODE_ERROR_CANCEL);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            //在页面销毁的时候将webView移除
            mLayoutContent.removeView(mWebView);
            mWebView.stopLoading();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCoder, KeyEvent event) {
        if (keyCoder == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                // goBack()表示返回webView的上一页面
                mWebView.goBack();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCoder, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_close:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_goback);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
            }
        });
    }

    /**
     * 初始化webview
     * <p>
     * WebView对象并不是直接写在布局文件中的，而是通过一个FrameLayout容器，使用addview(webview)动态向里面添加的。
     * 另外需要注意创建webview需要使用applicationContext而不是activity的context，销毁时不再占有activity对象，
     * 最后离开的时候需要及时销毁webview，onDestory()中应该先从LinearLayout中remove掉webview,再调用webview.removeAllViews();webview.destory();
     *
     * @param savedInstanceState
     */
    private void initWebView(Bundle savedInstanceState) {
        //动态创建一个WebView对象并添加到FrameLayout中
        mWebView = new WebView(WalkerApplication.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        mLayoutContent.addView(mWebView);
        setCommonSetting();
        mJsDelegate = new HostJsScope(this, mWebView);
        mWebView.addJavascriptInterface(mJsDelegate, "HostApp");
        mWebView.setWebChromeClient(new BaoWebChromeClient());
        // 设置Web视图
        mWebView.setWebViewClient(new BaoWebViewClient());
        //通常webview渲染的界面中含有可以下载文件的链接，点击该链接后，应该开始执行下载的操作并保存文件到本地中。
        // mWebView.setDownloadListener(new MyDownloadListenter());
        setSafeWebView();

        try {
            if (null != savedInstanceState) {
                mWebView.restoreState(savedInstanceState);
            } else {
                String url = getIntent().getStringExtra("url");
                if (TextUtils.isEmpty(url)) {
                    mWebView.loadUrl("file:///android_asset/webTest.html");
                } else {
                    mWebView.loadUrl(url);
                }
            }
        } catch (Exception e) {
            BaoLog.e("loadUrl: ", e);
        }
    }

    /**
     * 常用设置方法
     */
    private void setCommonSetting() {
        try {
            WebSettings ws = mWebView.getSettings();
            //支持js
            ws.setJavaScriptEnabled(true);
            //开启DOM storage API功能（HTML5 提供的一种标准的接口，主要将键值对存储在本地，在页面加载完毕后可以通过 JavaScript 来操作这些数据。）
            ws.setDomStorageEnabled(true);
            //设置Application Caches缓存
            ws.setAppCacheEnabled(false);
            //设置Application Caches缓存目录
            //ws.setAppCachePath(cacheDirPath);
            //设置缓存方式
            ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
            //启用数据库
            ws.setDatabaseEnabled(true);
            String cacheDirPath = StringUtil.pliceStr(StorageHelper.getWalkerRootPath(), File.separator, BaseParams.DIR_DB, File.separator, "WebCache");
            //设置数据库缓存路径
            ws.setDatabasePath(cacheDirPath);
            //启用地理定位
            ws.setGeolocationEnabled(true);
            //设置定位的数据库路径
            ws.setGeolocationDatabasePath(cacheDirPath);
            //设置默认编码
            ws.setDefaultTextEncodingName("utf-8");
            //将图片调整到适合webview的大小
            ws.setUseWideViewPort(false);
            //支持缩放
            ws.setSupportZoom(true);
            //设置支持缩放
            ws.setBuiltInZoomControls(true);
            //支持内容重新布局
            ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            // 多窗口
            ws.supportMultipleWindows();
            //当webview调用requestFocus时为webview设置节点
            ws.setNeedInitialFocus(true);
            //支持通过JS打开新窗口
            ws.setJavaScriptCanOpenWindowsAutomatically(true);
            // 缩放至屏幕的大小
            ws.setLoadWithOverviewMode(true);
        } catch (Exception e) {
            BaoLog.e("setCommonSetting: ", e);
        }
    }

    /**
     * 鉴于WebView的一些漏洞，做出相应的防范措施
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setSafeWebView() {
        try {
            //移除控制不严的有弊端的JavaScript接口
            mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
            mWebView.removeJavascriptInterface("accessibility");
            mWebView.removeJavascriptInterface("accessibilityTraversal");
            //通过WebSettings.setSavePassword(false)关闭密码保存提醒功能
            WebSettings ws = mWebView.getSettings();
            ws.setSavePassword(false);
            //通过以下设置，防止越权访问，跨域等安全问题
            ws.setAllowFileAccess(false);//设置不可以访问文件
            ws.setAllowFileAccessFromFileURLs(false);
            ws.setAllowUniversalAccessFromFileURLs(false);
        } catch (Exception e) {
            BaoLog.e("setSafeWebView: ", e);
        }
    }

    /**
     * 异步处理
     */
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_ERROR_CANCEL:
                    showError(false);
                    break;
                case CODE_ERROR_SHOW:
                    showError(true);
                    break;
                case CODE_REQUEST_ERR:
                    showError(true);
                    if (mLayoutContent != null) {
                        mLayoutContent.setVisibility(View.GONE);
                    }
                    break;
                case CODE_REQUEST_OK:
                    if (mLayoutContent != null && mLayoutContent.getVisibility() != View.VISIBLE) {
                        mLayoutContent.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 显示error页面
     *
     * @param isShow 是否显示的标识
     */
    private void showError(boolean isShow) {
        try {
            if (isShow) {
                mViewError.setVisibility(View.VISIBLE);
                mWebView.stopLoading();
            } else {
                mViewError.setVisibility(View.GONE);
                mWebView.reload();
            }
        } catch (Exception e) {
            BaoLog.e("WebActivity——>showError", e);
        }
    }

    /**
     * 获取请求状态
     *
     * @param url url
     */
    private void getRespStatus(final String url) {
        if (url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://")) {
            TaskExecutor.executeNetTask(new Runnable() {
                @Override
                public void run() {
                    int status = HttpUtil.getRespStatus(url);
                    if (status != 200) {
                        handler.sendEmptyMessage(CODE_REQUEST_ERR);
                    } else {
                        handler.sendEmptyMessage(CODE_REQUEST_OK);
                    }
                }
            });
        }
    }

    /**
     * 主要帮助WebView处理各种通知、请求事件
     * <p>
     * 加快HTML网页加载完成速度（默认情况html代码下载到WebView后，webkit开始解析网页各个节点，发现有外部样式文件或者外部脚本文件时，会异步发起网络请求下载文件，但如果在这之前也有解析到image节点，那势必也会发起网络请求下载相应的图片。在网络情况较差的情况下，过多的网络请求就会造成带宽紧张，影响到css或js文件加载完成的时间，造成页面空白loading过久。解决的方法就是告诉WebView先不要自动加载图片，等页面finish后再发起图片加载。）
     */
    private class BaoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //获取url信息，做一些自定义操作
            if (url.toLowerCase().startsWith("customaction://")) {
                return true;
            }
            //http请求，则直接在当前页打开
            if (url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://")) {
                return false;
            }
            //特殊的统一资源定位器，看系统的activity是否能处理
            try {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri actionUrl = Uri.parse(url);
                intent.setData(actionUrl);
                startActivity(intent);
                return true;
            } catch (ActivityNotFoundException e) {
                BaoLog.e("WebActivity", e);
            }
            return true;
        }

        //加载网页时替换某个资源
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            WebResourceResponse response = null;
            if (request.getUrl().toString().contains("logo")) {
                try {
                    InputStream logo = getAssets().open("logo.png");
                    response = new WebResourceResponse("image/png", "UTF-8", logo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        //处理https请求，为WebView处理ssl证书设置WebView默认是不处理https请求的
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();  // 接受信任所有网站的证书
            // handler.cancel();   // 默认操作 不处理
            // handler.handleMessage(null);  // 可做其他处理
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showProgress("Loading...");
            getRespStatus(url);
            super.onPageStarted(view, url, favicon);
            try {
                //网页在加载的时候暂时不加载图片
                if (Build.VERSION.SDK_INT >= 19) {
                    //对系统API在19以上的版本作了兼容。因为4.4以上系统在onPageFinished时再恢复图片加载时,如果存在多张图片引用的是相同的src时，会只有一个image标签得到加载，因而对于这样的系统我们就先直接加载。
                    mWebView.getSettings().setLoadsImagesAutomatically(true);
                } else {
                    mWebView.getSettings().setLoadsImagesAutomatically(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //getRespStatus(url);
            super.onPageFinished(view, url);
            cancelProgress();
            try {
                //在html标签加载完成之后在加载图片内容
                if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
                    mWebView.getSettings().setLoadsImagesAutomatically(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            cancelProgress();
            handler.sendEmptyMessage(CODE_ERROR_SHOW);
        }
    }

    /**
     * 主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
     */
    public class BaoWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 final JsResult result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    WebActivity.this);
            builder.setMessage(message)
                    .setNeutralButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int arg1) {
                                    dialog.dismiss();
                                }
                            }).create().show();
            result.confirm();
            result.cancel();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message,
                                   final JsResult result) {
            AlertDialog.Builder b = new AlertDialog.Builder(WebActivity.this);
            b.setTitle("Confirm");
            b.setMessage(message);
            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            });
            b.create().show();
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (mProgress.getVisibility() != View.VISIBLE) {
                mProgress.setVisibility(View.VISIBLE);
            }
            if (100 <= newProgress) {
                mProgress.setVisibility(View.GONE);
                mProgress.setProgress(0);
            } else {
                mProgress.setProgress(newProgress);
            }
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
            callback.invoke(origin, true, false);
        }
    }

    class MyDownloadListenter implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            //下载任务...，主要有两种方式
            //（1）自定义下载任务
            //（2）调用系统的download的模块
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    /**
     * 显示进度框
     *
     * @param msg 提示信息
     */
    public void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new CustomProgress(this);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCancelable(false);
        } else {
            mProgressDialog.setMessage(msg);
        }
        mProgressDialog.show();
    }

    /**
     * 取消进度框
     */
    public void cancelProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }
}
