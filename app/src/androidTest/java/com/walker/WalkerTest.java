package com.walker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.widget.Toast;

import com.walker.utils.ToastUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * WalkerDemo的自动化UI测试
 *
 * Created by Walker on 2017/5/18.
 */

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class WalkerTest {
    private UiDevice mDevice;
    private static final int LAUNCH_TIMEOUT = 3000;
    private final String BASIC_SAMPLE_PACKAGE = "com.walker";

    @Before
    public void setUp() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkPreconditions() {
        assertThat(mDevice, notNullValue());
    }

    @Test
    public void walkerTest(){
        mDevice.findObject(By.desc("全部应用程序")).click();
        mDevice.wait(Until.hasObject(By.text("沃 逮")), LAUNCH_TIMEOUT);
        mDevice.findObject(By.text("沃 逮")).click();

        UiObject2 fabCreate = mDevice.wait(Until.findObject(By.res(BASIC_SAMPLE_PACKAGE, "fab")), 500);
        fabCreate.click();
        sleepForTime(1000);
        swipeDown();
        UiObject2 recyclerView = mDevice.wait(Until.findObject(By.res(BASIC_SAMPLE_PACKAGE, "recyclerView")), 500);
        recyclerView.getChildren().get(0).click();
        sleepForTime(1000);
        UiObject email = new UiObject(new UiSelector().resourceId("com.walker:id/edt_email"));
        UiObject passWord = new UiObject(new UiSelector().resourceId("com.walker:id/edt_password"));
        UiObject2 login = mDevice.wait(Until.findObject(By.res(BASIC_SAMPLE_PACKAGE, "login_form")), 500);
        try {
            email.setText("SingleWolf@gmail.com");
            mDevice.pressEnter();
            passWord.setText("123456");
            login.click();
            assertEquals(email.getText().toString().trim(), "SingleWolf@gmail.com");
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void sleepForTime(int time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

    public void swipeLeft() {//左滑
        int y = mDevice.getDisplayHeight();
        int x = mDevice.getDisplayWidth();
        mDevice.swipe(x-100, y/2, 100, y/2, 8);
    }
    public void swipeRight() {//右滑
        int y = mDevice.getDisplayHeight();
        int x = mDevice.getDisplayWidth();
        mDevice.swipe(100, y/2, x-100, y/2, 8);
    }
    public void swipeDown() {//下滑
        int y = mDevice.getDisplayHeight();
        int x = mDevice.getDisplayWidth();
        mDevice.swipe(x/2, 200, x/2, y-200, 8);
        sleepForTime(5000);
    }
    public void swipeUp() {//上滑
        int y = mDevice.getDisplayHeight();
        int x = mDevice.getDisplayWidth();
        mDevice.swipe(x/2, y-200, x/2, 200, 8);
    }
}
