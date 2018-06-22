package com.walker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.walker.R;
import com.walker.WalkerApplication;
import com.walker.base.BaseFragActivity;
import com.walker.base.BaseFragment;
import com.walker.constant.SQLiteConfig;
import com.walker.data.sp.SPHelper;
import com.walker.data.sqlite.WalkerDB;
import com.walker.entity.Summary;
import com.walker.fragment.SummyFragment;
import com.walker.utils.DateTimeUtil;
import com.walker.utils.ToastUtil;

import java.util.ArrayList;

/**
 * summary :主活动页面
 * time    :2016/6/30 15:59
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class MainActivity extends BaseFragActivity implements View.OnClickListener {
    /**
     * 抽屉布局
     */
    private DrawerLayout mDrawerLayout;
    /**
     * 侧滑导航
     */
    private NavigationView mNavigation;
    /**
     * 搜索视图
     */
    private SearchView mSearchView;
    /**
     * 添加简介
     */
    private FloatingActionButton mFabAdd;

    /**
     * 开启本活动
     *
     * @param activity 活动触发者
     * @param isFinish 是否终止触发者
     */
    public static void actionStart(Activity activity, boolean isFinish) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        if (isFinish) {
            activity.finish();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_main;
    }

    @Override
    protected int getFragContentId() {
        return R.id.fragment_container;
    }

    @Override
    protected void initialization(Bundle savedInstanceState) {
        initToolBar();
        initNavigation();
        initView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_search:
                Toast.makeText(MainActivity.this, "menu_search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_about:
                break;
            case R.id.menu_exit:
                this.finish();
                break;
            case R.id.filter_ui:
                if (findFragByTag("frag_ui") != null) {
                    addFragment((BaseFragment) findFragByTag("frag_ui"), "frag_ui");
                } else {
                    addFragment(SummyFragment.newInstance(SummyFragment.TYPE_UI), "frag_ui");
                }
                break;
            case R.id.filter_data:
                if (findFragByTag("frag_data") != null) {
                    addFragment((BaseFragment) findFragByTag("frag_data"), "frag_data");
                } else {
                    addFragment(SummyFragment.newInstance(SummyFragment.TYPE_LOGIC), "frag_data");
                }
                break;
            case R.id.filter_test:
                if (findFragByTag("frag_test_menu") != null) {
                    addFragment((BaseFragment) findFragByTag("frag_test_menu"), "frag_test_menu");
                } else {
                    addFragment(SummyFragment.newInstance(SummyFragment.TYPE_TEST), "frag_test_menu");
                }
                break;
            case R.id.filter_other:
                if (findFragByTag("frag_other") != null) {
                    addFragment((BaseFragment) findFragByTag("frag_other"), "frag_other");
                } else {
                    addFragment(SummyFragment.newInstance(SummyFragment.TYPE_OTHER), "frag_other");
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 初始化toolbar
     */
    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_align);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 初始化侧滑导航
     */
    private void initNavigation() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigation = (NavigationView) findViewById(R.id.nav_view);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_ui:
                        if (findFragByTag("frag_ui") != null) {
                            addFragment((BaseFragment) findFragByTag("frag_ui"), "frag_ui");
                        } else {
                            addFragment(SummyFragment.newInstance(SummyFragment.TYPE_UI), "frag_ui");
                        }
                        break;
                    case R.id.item_data:
                        if (findFragByTag("frag_data") != null) {
                            addFragment((BaseFragment) findFragByTag("frag_data"), "frag_data");
                        } else {
                            addFragment(SummyFragment.newInstance(SummyFragment.TYPE_LOGIC), "frag_data");
                        }
                        break;
                    case R.id.item_test:
                        if (findFragByTag("frag_test") != null) {
                            addFragment((BaseFragment) findFragByTag("frag_test"), "frag_test");
                        } else {
                            addFragment(SummyFragment.newInstance(SummyFragment.TYPE_TEST), "frag_test");
                        }
                        break;
                    case R.id.item_other:
                        if (findFragByTag("frag_other") != null) {
                            addFragment((BaseFragment) findFragByTag("frag_other"), "frag_other");
                        } else {
                            addFragment(SummyFragment.newInstance(SummyFragment.TYPE_OTHER), "frag_other");
                        }
                        break;
                    case R.id.sub_exit:
                        MainActivity.this.finish();
                        break;
                    default:
                        break;
                }
                // Close the navigation drawer when an item is selected.
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mFabAdd = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    protected void bindListener() {
        mFabAdd.setOnClickListener(this);
    }

    @Override
    protected void unbindListener() {
        mFabAdd.setOnClickListener(null);
    }

    @Override
    protected BaseFragment getFirstFragment() {
        return SummyFragment.newInstance(SummyFragment.TYPE_ALL);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();
            } else {
                this.moveTaskToBack(true);
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                addSummary();
                break;
            default:
                break;
        }
    }


    /**
     * 根据标签获取fragment
     *
     * @param tag 标签
     * @return Object
     */
    private Object findFragByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    /**
     * 添加简介
     */
    private void addSummary() {
        String flag = "add_19";
        if (SPHelper.getBoolean(flag, false) == false) {
            WalkerDB db = new WalkerDB(WalkerApplication.getContext());
            ArrayList<Summary> data = new ArrayList<>();
            Summary login = new Summary();
            login.setSHOW_ID("1");
            login.setICON("ic_default");
            login.setSUMMARY("login");
            login.setDESCRIPTION("登录页面");
            login.setCLASS_TYPE(SQLiteConfig.CLASS_ACT);
            login.setCLASS_NAME("com.walker.activity.LoginActivity");
            login.setSHOW_TYPE(SQLiteConfig.SUMMARY_LOGIC);
            login.setACTIVE(SQLiteConfig.VAR_VALID);
            login.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(login);

            Summary cylinder = new Summary();
            cylinder.setSHOW_ID("2");
            cylinder.setICON("ic_default");
            cylinder.setSUMMARY("cylinderImageView");
            cylinder.setDESCRIPTION("循环显示一个长的图片");
            cylinder.setCLASS_TYPE(SQLiteConfig.CLASS_FRAG);
            cylinder.setCLASS_NAME("com.walker.fragment.CylinderImageFragment");
            cylinder.setSHOW_TYPE(SQLiteConfig.SUMMARY_UI);
            cylinder.setACTIVE(SQLiteConfig.VAR_VALID);
            cylinder.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(cylinder);

            Summary autoHbao = new Summary();
            autoHbao.setSHOW_ID("3");
            autoHbao.setICON("ic_default");
            autoHbao.setSUMMARY("自动抢红包");
            autoHbao.setDESCRIPTION("微信红包自动抢");
            autoHbao.setCLASS_TYPE(SQLiteConfig.CLASS_ACT);
            autoHbao.setCLASS_NAME("com.walker.activity.WxhbaoActivity");
            autoHbao.setSHOW_TYPE(SQLiteConfig.SUMMARY_OTHER);
            autoHbao.setACTIVE(SQLiteConfig.VAR_VALID);
            autoHbao.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(autoHbao);

            Summary cityList = new Summary();
            cityList.setSHOW_ID("4");
            cityList.setICON("ic_default");
            cityList.setSUMMARY("炫酷城市列表");
            cityList.setDESCRIPTION("炫酷的城市选择界面");
            cityList.setCLASS_TYPE(SQLiteConfig.CLASS_FRAG);
            cityList.setCLASS_NAME("com.walker.fragment.CityListFragment");
            cityList.setSHOW_TYPE(SQLiteConfig.SUMMARY_UI);
            cityList.setACTIVE(SQLiteConfig.VAR_VALID);
            cityList.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(cityList);

            Summary customTabs = new Summary();
            customTabs.setSHOW_ID("5");
            customTabs.setICON("ic_default");
            customTabs.setSUMMARY("Chrome Custom Tabs");
            customTabs.setDESCRIPTION("既能在App和网页之间流畅切换，又能有多种自定义选项。");
            customTabs.setCLASS_TYPE(SQLiteConfig.CLASS_FRAG);
            customTabs.setCLASS_NAME("com.walker.fragment.CustomTabsFragment");
            customTabs.setSHOW_TYPE(SQLiteConfig.SUMMARY_LOGIC);
            customTabs.setACTIVE(SQLiteConfig.VAR_VALID);
            customTabs.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(customTabs);

            Summary webView = new Summary();
            webView.setSHOW_ID("6");
            webView.setICON("ic_default");
            webView.setSUMMARY("WebView");
            webView.setDESCRIPTION("使用WebView实现本地与Web端的交互");
            webView.setCLASS_TYPE(SQLiteConfig.CLASS_ACT);
            webView.setCLASS_NAME("com.walker.activity.WebActivity");
            webView.setSHOW_TYPE(SQLiteConfig.SUMMARY_LOGIC);
            webView.setACTIVE(SQLiteConfig.VAR_VALID);
            webView.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(webView);

            Summary magicImage = new Summary();
            magicImage.setSHOW_ID("7");
            magicImage.setICON("ic_default");
            magicImage.setSUMMARY("MagicImage");
            magicImage.setDESCRIPTION("自定義圖片加載器");
            magicImage.setCLASS_TYPE(SQLiteConfig.CLASS_FRAG);
            magicImage.setCLASS_NAME("com.walker.fragment.MagicImageFragment");
            magicImage.setSHOW_TYPE(SQLiteConfig.SUMMARY_LOGIC);
            magicImage.setACTIVE(SQLiteConfig.VAR_VALID);
            magicImage.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(magicImage);

            Summary steepMode = new Summary();
            steepMode.setSHOW_ID("8");
            steepMode.setICON("ic_default");
            steepMode.setSUMMARY("SteepMode");
            steepMode.setDESCRIPTION("沉浸式模式");
            steepMode.setCLASS_TYPE(SQLiteConfig.CLASS_FRAG);
            steepMode.setCLASS_NAME("com.walker.fragment.SteepFragment");
            steepMode.setSHOW_TYPE(SQLiteConfig.SUMMARY_UI);
            steepMode.setACTIVE(SQLiteConfig.VAR_VALID);
            steepMode.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(steepMode);

            Summary bottomBar = new Summary();
            bottomBar.setSHOW_ID("9");
            bottomBar.setICON("ic_default");
            bottomBar.setSUMMARY("BottomBar");
            bottomBar.setDESCRIPTION("底部导航栏");
            bottomBar.setCLASS_TYPE(SQLiteConfig.CLASS_ACT);
            bottomBar.setCLASS_NAME("com.walker.activity.BottomBarActivity");
            bottomBar.setSHOW_TYPE(SQLiteConfig.SUMMARY_UI);
            bottomBar.setACTIVE(SQLiteConfig.VAR_VALID);
            bottomBar.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(bottomBar);

            Summary temperatureView = new Summary();
            temperatureView.setSHOW_ID("10");
            temperatureView.setICON("ic_default");
            temperatureView.setSUMMARY("TemperatureView");
            temperatureView.setDESCRIPTION("自定义温度仪表盘");
            temperatureView.setCLASS_TYPE(SQLiteConfig.CLASS_FRAG);
            temperatureView.setCLASS_NAME("com.walker.fragment.TemperatureViewFragment");
            temperatureView.setSHOW_TYPE(SQLiteConfig.SUMMARY_UI);
            temperatureView.setACTIVE(SQLiteConfig.VAR_VALID);
            temperatureView.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(temperatureView);

            Summary filePicker = new Summary();
            filePicker.setSHOW_ID("10");
            filePicker.setICON("ic_default");
            filePicker.setSUMMARY("ImagePicker");
            filePicker.setDESCRIPTION("图片选择器");
            filePicker.setCLASS_TYPE(SQLiteConfig.CLASS_ACT);
            filePicker.setCLASS_NAME("com.walker.activity.ImgPickerActivity");
            filePicker.setSHOW_TYPE(SQLiteConfig.SUMMARY_UI);
            filePicker.setACTIVE(SQLiteConfig.VAR_VALID);
            filePicker.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(filePicker);

            Summary securedSP = new Summary();
            securedSP.setSHOW_ID("11");
            securedSP.setICON("ic_default");
            securedSP.setSUMMARY("SecuredSPFragment");
            securedSP.setDESCRIPTION("加密处理偏好存储");
            securedSP.setCLASS_TYPE(SQLiteConfig.CLASS_FRAG);
            securedSP.setCLASS_NAME("com.walker.fragment.SecuredSPFragment");
            securedSP.setSHOW_TYPE(SQLiteConfig.SUMMARY_LOGIC);
            securedSP.setACTIVE(SQLiteConfig.VAR_VALID);
            securedSP.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(securedSP);

            Summary takePhoto = new Summary();
            takePhoto.setSHOW_ID("12");
            takePhoto.setICON("ic_default");
            takePhoto.setSUMMARY("TakePhotoFragment");
            takePhoto.setDESCRIPTION("基于RecyclerView实现拍照列");
            takePhoto.setCLASS_TYPE(SQLiteConfig.CLASS_FRAG);
            takePhoto.setCLASS_NAME("com.walker.fragment.TakePhotoFragment");
            takePhoto.setSHOW_TYPE(SQLiteConfig.SUMMARY_UI);
            takePhoto.setACTIVE(SQLiteConfig.VAR_VALID);
            takePhoto.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(takePhoto);

            Summary bottomSheet = new Summary();
            bottomSheet.setSHOW_ID("12");
            bottomSheet.setICON("ic_default");
            bottomSheet.setSUMMARY("BottomSheetFragment");
            bottomSheet.setDESCRIPTION("BottomSheet简单使用");
            bottomSheet.setCLASS_TYPE(SQLiteConfig.CLASS_FRAG);
            bottomSheet.setCLASS_NAME("com.walker.fragment.BottomSheetFragment");
            bottomSheet.setSHOW_TYPE(SQLiteConfig.SUMMARY_UI);
            bottomSheet.setACTIVE(SQLiteConfig.VAR_VALID);
            bottomSheet.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(bottomSheet);

            Summary touchImg = new Summary();
            touchImg.setSHOW_ID("13");
            touchImg.setICON("ic_default");
            touchImg.setSUMMARY("TouchImgFragment");
            touchImg.setDESCRIPTION("支持手势缩放的ImageView");
            touchImg.setCLASS_TYPE(SQLiteConfig.CLASS_FRAG);
            touchImg.setCLASS_NAME("com.walker.fragment.TouchImgFragment");
            touchImg.setSHOW_TYPE(SQLiteConfig.SUMMARY_UI);
            touchImg.setACTIVE(SQLiteConfig.VAR_VALID);
            touchImg.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(touchImg);

            Summary skils = new Summary();
            skils.setSHOW_ID("14");
            skils.setICON("ic_default");
            skils.setSUMMARY("SkillsActivity");
            skils.setDESCRIPTION("一些实用的小技巧");
            skils.setCLASS_TYPE(SQLiteConfig.CLASS_ACT);
            skils.setCLASS_NAME("com.walker.activity.SkillsActivity");
            skils.setSHOW_TYPE(SQLiteConfig.SUMMARY_OTHER);
            skils.setACTIVE(SQLiteConfig.VAR_VALID);
            skils.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(skils);

            Summary win10 = new Summary();
            win10.setSHOW_ID("15");
            win10.setICON("ic_default");
            win10.setSUMMARY("Win10UIFragment");
            win10.setDESCRIPTION("Win10风格的UI");
            win10.setCLASS_TYPE(SQLiteConfig.CLASS_FRAG);
            win10.setCLASS_NAME("com.walker.fragment.Win10UIFragment");
            win10.setSHOW_TYPE(SQLiteConfig.SUMMARY_OTHER);
            win10.setACTIVE(SQLiteConfig.VAR_VALID);
            win10.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(win10);

            Summary fliker = new Summary();
            fliker.setSHOW_ID("16");
            fliker.setICON("ic_default");
            fliker.setSUMMARY("LoadingFragment");
            fliker.setDESCRIPTION("loading专栏");
            fliker.setCLASS_TYPE(SQLiteConfig.CLASS_FRAG);
            fliker.setCLASS_NAME("com.walker.fragment.LoadingFragment");
            fliker.setSHOW_TYPE(SQLiteConfig.SUMMARY_UI);
            fliker.setACTIVE(SQLiteConfig.VAR_VALID);
            fliker.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(fliker);

            Summary pager = new Summary();
            pager.setSHOW_ID("17");
            pager.setICON("ic_default");
            pager.setSUMMARY("ScrollPagerFragment");
            pager.setDESCRIPTION("滑动翻页专栏");
            pager.setCLASS_TYPE(SQLiteConfig.CLASS_ACT);
            pager.setCLASS_NAME("com.walker.activity.ScrollPagerActivity");
            pager.setSHOW_TYPE(SQLiteConfig.SUMMARY_UI);
            pager.setACTIVE(SQLiteConfig.VAR_VALID);
            pager.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(pager);

            Summary uiAnim = new Summary();
            uiAnim.setSHOW_ID("18");
            uiAnim.setICON("ic_default");
            uiAnim.setSUMMARY("UIAnimationFragment");
            uiAnim.setDESCRIPTION("UI常见动画");
            uiAnim.setCLASS_TYPE(SQLiteConfig.CLASS_FRAG);
            uiAnim.setCLASS_NAME("com.walker.fragment.UIAnimationFragment");
            uiAnim.setSHOW_TYPE(SQLiteConfig.SUMMARY_UI);
            uiAnim.setACTIVE(SQLiteConfig.VAR_VALID);
            uiAnim.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(uiAnim);

            Summary recordView = new Summary();
            uiAnim.setSHOW_ID("19");
            uiAnim.setICON("ic_default");
            uiAnim.setSUMMARY("RecordView");
            uiAnim.setDESCRIPTION("自定义录音控件");
            uiAnim.setCLASS_TYPE(SQLiteConfig.CLASS_FRAG);
            uiAnim.setCLASS_NAME("com.walker.fragment.RecordViewFragment");
            uiAnim.setSHOW_TYPE(SQLiteConfig.SUMMARY_UI);
            uiAnim.setACTIVE(SQLiteConfig.VAR_VALID);
            uiAnim.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(uiAnim);

            Summary autoWrapText = new Summary();
            autoWrapText.setSHOW_ID("20");
            autoWrapText.setICON("ic_default");
            autoWrapText.setSUMMARY("AutoWrapText");
            autoWrapText.setDESCRIPTION("解决中英文排版问题的自动换行文本");
            autoWrapText.setCLASS_TYPE(SQLiteConfig.CLASS_FRAG);
            autoWrapText.setCLASS_NAME("com.walker.fragment.AutoWrapTextFragment");
            autoWrapText.setSHOW_TYPE(SQLiteConfig.SUMMARY_UI);
            autoWrapText.setACTIVE(SQLiteConfig.VAR_VALID);
            autoWrapText.setCREAT_DATE(DateTimeUtil.getNormalDate());
            data.add(autoWrapText);

            if (db.addSummary(data)) {
                SPHelper.put(flag, true);
            }
        } else {
            ToastUtil.showCenterShort("Nothing to add !");
        }
    }
}
