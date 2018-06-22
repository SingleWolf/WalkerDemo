package com.walker.fragment;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.walker.R;
import com.walker.WalkerApplication;
import com.walker.activity.LoginActivity;
import com.walker.base.BaseFragment;
import com.walker.utils.ToastUtil;

/**
 * summary :Chrome Custom Tabs
 * time    :2016/8/16 14:22
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class CustomTabsFragment extends BaseFragment implements View.OnClickListener {
    private Button mBtnCustomTab;

    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        mBtnCustomTab = (Button) baseView.findViewById(R.id.btn_customTabs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_custom_tabs;
    }

    @Override
    protected void bindListener() {
        mBtnCustomTab.setOnClickListener(this);
    }

    @Override
    protected void unbindListener() {
        mBtnCustomTab.setOnClickListener(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        //简单的预启动
        // CustomTabsClient.connectAndInitialize(getHoldActivity(), "com.android.chrome");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_customTabs:
                //launchUrl(getHoldActivity());
                break;
            default:
                break;
        }
    }


    /**
     * 访问url
     *
     * @param context 上下文
     */
    private void launchUrl(Context context) {
        ToastUtil.showShort("因版本限制，Chrome CustomTabs示例需要特殊编译");
        //String url = "https://www.baidu.com";
        //CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        //修改Toolbar颜色
        //builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        //添加action button  图标的最大尺寸是宽48dp高24dp。
        Intent actionIntent = new Intent(Intent.ACTION_SEND);
        actionIntent.setType("*/*");
        actionIntent.putExtra(Intent.EXTRA_EMAIL, "example@example.com");
        actionIntent.putExtra(Intent.EXTRA_SUBJECT, "example");
        //   PendingIntent act_btn = PendingIntent.getActivity(context, 0, actionIntent, 0);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_archive);//注意在正式项目中不要在UI线程读取图片
        // builder.setActionButton(icon, "send email", act_btn, true);
        //添加menu item
        //Chrome Custom Tabs的menu默认包含了三个显示为图标的item(Forward, Page Info, Refresh)和两个文字item(Find in page, Open in Browser)。我们可以再添加最多5个文字item
        Intent menuIntent = new Intent();
        menuIntent.setClass(WalkerApplication.getContext(), LoginActivity.class);
        PendingIntent menu_item = PendingIntent.getActivity(WalkerApplication.getContext(), 0, menuIntent, 0);
        //builder.addMenuItem("Menu entry 1", menu_item);
        //设置转场动画
       /* builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);*/
        //自定义关闭按钮
        //Custom Tabs左上角的关闭按钮默认是一个叉叉。如果你希望用户感觉到网页内容是APP的一部分，那么最好把叉叉换成返回按钮。
        //builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_hei));
        //launchUrl
      /*  CustomTabsIntent customTabsIntent = builder.build();
        if (context instanceof Activity) {
            //注意launchUrl这个方法的第一个参数是个Activity，是为了传入startAnimationBundle这个参数来实现自定义转场动画
            customTabsIntent.launchUrl((Activity) context, Uri.parse(url));
        } else {
            Intent intent = customTabsIntent.intent;
            intent.setData(Uri.parse(url));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                context.startActivity(intent, customTabsIntent.startAnimationBundle);
            } else {
                context.startActivity(intent);
            }
        }*/
    }

    /**
     * Returns a list of packages that support Custom Tabs.
     */
   /* public static ArrayList getCustomTabsPackages(Context context) {
        PackageManager pm = context.getPackageManager();
        // Get default VIEW intent handler.
        Intent activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"));

        // Get all apps that can handle VIEW intents.
        List resolvedActivityList = pm.queryIntentActivities(activityIntent, 0);
        ArrayList packagesSupportingCustomTabs = new ArrayList<>();
        for (ResolveInfo info : resolvedActivityList) {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction(ACTION_CUSTOM_TABS_CONNECTION);
            serviceIntent.setPackage(info.activityInfo.packageName);
            // Check if this package also resolves the Custom Tabs service.
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info);
            }
        }
        return packagesSupportingCustomTabs;
    }*/
}
