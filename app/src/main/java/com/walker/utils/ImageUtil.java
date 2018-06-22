package com.walker.utils;

import android.media.ExifInterface;

import java.io.IOException;

/**
 * summary :图片工具类
 * time    :2016/5/13 13:34
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class ImageUtil {
    /**
     * 获取图片的exif的旋转角度
     * @param path 文件路径
     * @return int
     */
    public static int onExifRotateAngle(String path) {
        int angle = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    break;
                default:break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return angle;
    }
}
