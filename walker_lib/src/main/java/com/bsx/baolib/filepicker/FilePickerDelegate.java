package com.bsx.baolib.filepicker;

import android.app.Application;

import com.bsx.baolib.filepicker.utils.image.FrescoManager;


/**
 * Created by droidNinja on 14/06/16.
 */
public class FilePickerDelegate extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FrescoManager.init(this);
    }
}
