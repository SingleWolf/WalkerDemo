package com.walker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


/**
 * 基于UIAutomator2.0的自动化测试demo
 *
 * Created by Walker on 2017/5/17.
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class CalculatorTest {
    private UiDevice mDevice;
    private static final int LAUNCH_TIMEOUT = 3000;
    private final String BASIC_SAMPLE_PACKAGE = "com.htc.calculator";

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
    public void calculatorTest() {
        mDevice.findObject(By.desc("全部应用程序")).click();
        mDevice.wait(Until.hasObject(By.text("计算器")), LAUNCH_TIMEOUT);
        mDevice.findObject(By.text("计算器")).click();

        UiObject2 buttonDel = mDevice.wait(Until.findObject(By.res(BASIC_SAMPLE_PACKAGE, "del")), 500);
        UiObject2 button8 = mDevice.wait(Until.findObject(By.res(BASIC_SAMPLE_PACKAGE, "digit8")), 500);
        UiObject2 buttonX = mDevice.wait(Until.findObject(By.res(BASIC_SAMPLE_PACKAGE, "mul")), 500);
        UiObject2 button9 = mDevice.wait(Until.findObject(By.res(BASIC_SAMPLE_PACKAGE, "digit9")), 500);
        UiObject2 buttonEqual = mDevice.wait(Until.findObject(By.res(BASIC_SAMPLE_PACKAGE, "equal")), 500);
        UiObject2 output = mDevice.wait(Until.findObject(By.clazz("android.widget.EditText")), 500);

        buttonDel.click();
        button8.click();
        buttonX.click();
        button9.click();
        buttonEqual.click();
        assertEquals(output.getText(), "72");

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
}
