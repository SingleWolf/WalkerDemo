package com.walker.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.bsx.baolib.log.BaoLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * summary :相机相关辅助类
 * time    :2016/5/20 14:38
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class CameraUtil {
    /**
     * 调用相机的请求码
     */

    public static final int CAMERA_REQUEST_CODE = 1;

    /**
     * 调用相册的请求码
     */

    public static final int GALLERY_REQUEST_CODE = 2;

    /**
     * 启动相机拍照
     *
     * @param context 上下文
     * @param path    文件路径
     */
    public static void openCamera(Context context, String path) {
        try {
            File f = new File(path);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //系统大于N的统一用FileProvider处理
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                //将文件转换成content://Uri形式
                Uri photoUri= FileProvider.getUriForFile(context,"com.walker.provider",f);
                //申请临时读写文件权限
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            }else{
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            }

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                ((Activity) context).startActivityForResult(intent,
                        CAMERA_REQUEST_CODE);
            }
        } catch (OutOfMemoryError e) {
            System.gc();
            BaoLog.e("openCamera", e);
            openCamera(context, path);
        } catch (Exception e) {
            BaoLog.e("openCamera", e);
        }
    }

    /**
     * 启动相册
     *
     * @param context 上下文
     */
    public static void openGallery(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                ((Activity) context).startActivityForResult(intent,
                        GALLERY_REQUEST_CODE);
            }
        } catch (OutOfMemoryError e) {
            System.gc();
            BaoLog.e("openGallery", e);
            openGallery(context);
        } catch (Exception e) {
            BaoLog.e("openGallery", e);
        }
    }

    /**
     * 图片添加到图库
     *
     * @param context 上下文
     * @param bitmap  图片资源
     * @return boolean
     */
    public static boolean galleryAddPic(Context context, Bitmap bitmap) {
        boolean result = false;
        try {
            String imageFileName = "Walker_" + System.currentTimeMillis() + ".jpg";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            File output = new File(storageDir, imageFileName);
            result = galleryAddPic(context, output.getAbsolutePath(), bitmap);
        } catch (Exception e) {
            BaoLog.e("galleryAddPic", e);
            result = false;
        } finally {
            return result;
        }
    }

    /**
     * 图片添加到图库
     *
     * @param context  上下文
     * @param savePath 保存路径
     * @param bitmap   图片资源
     * @return boolean
     */
    public static boolean galleryAddPic(Context context, String savePath, Bitmap bitmap) {
        FileOutputStream fos = null;
        boolean result = false;
        try {
            File output = new File(savePath);
            fos = new FileOutputStream(output);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            insertMedia(context, output, "image/jpeg");
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
            return result;
        }
    }

    /**
     * 加入到系统的图库中
     */
    private static void insertMedia(Context context, File output, String mime) {
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.DATA, output.getAbsolutePath());
            values.put(MediaStore.Video.Media.MIME_TYPE, mime);
            //记录到系统媒体数据库，通过系统的gallery可以即时查看
            context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            //通知系统去扫描
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(output)));
        } catch (Exception e) {
        }
    }
}
