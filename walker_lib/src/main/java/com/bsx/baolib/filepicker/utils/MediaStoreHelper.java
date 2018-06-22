package com.bsx.baolib.filepicker.utils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.bsx.baolib.filepicker.cursors.loadercallbacks.FileResultCallback;
import com.bsx.baolib.filepicker.cursors.loadercallbacks.PhotoDirLoaderCallbacks;
import com.bsx.baolib.filepicker.models.PhotoDirectory;


public class MediaStoreHelper {
  public static void getPhotoDirs(FragmentActivity activity, Bundle args, FileResultCallback<PhotoDirectory> resultCallback) {
    activity.getSupportLoaderManager()
            .initLoader(0, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
  }
}