package com.walker.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;

import com.bsx.baolib.log.BaoLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * summary :Bitmap工具类
 * time    :2016/5/13 11:02
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class BitmapUtil {
    /**
     * 根据reqWidth, reqHeight计算最合适的inSampleSize
     *
     * @param options   BitmapFactory.Options
     * @param maxWidth  最大宽
     * @param maxHeight 最大高
     * @return int
     */
    public static int onSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        // raw height and width of image
        int rawWidth = options.outWidth;
        int rawHeight = options.outHeight;

        // calculate best sample size
        int inSampleSize = 0;
        if (rawHeight > maxHeight || rawWidth > maxWidth) {
            float ratioWidth = (float) rawWidth / maxWidth;
            float ratioHeight = (float) rawHeight / maxHeight;
            inSampleSize = (int) Math.min(ratioHeight, ratioWidth);
        }
        inSampleSize = Math.max(1, inSampleSize);

        return inSampleSize;
    }

    /**
     * 更节省内存的读取raw资源成Bitmap
     *
     * @param context 上下文
     * @param rawId   资源id
     * @return Bitmap
     */
    public static Bitmap getRaw(Context context, int rawId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(rawId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 旋转Bitmap(默认回收传进来的原始Bitmap)
     *
     * @param originBitmap 原始bitmap
     * @param angle        旋转角度
     * @return Bitmap
     */
    public static Bitmap onRotate(Bitmap originBitmap, int angle) {
        return onRotate(originBitmap, angle, true);
    }

    /**
     * 旋转Bitmap
     *
     * @param originBitmap 原始bitmap
     * @param angle        旋转角度
     * @param recycle      是否回收传进来的原始Bitmap
     * @return Bitmap
     */
    public static Bitmap onRotate(Bitmap originBitmap, int angle, boolean recycle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotatedBitmap = Bitmap.createBitmap(originBitmap, 0, 0, originBitmap.getWidth(), originBitmap.getHeight(), matrix, true);
        if (recycle && originBitmap != null && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }
        return rotatedBitmap;
    }

    /**
     * 缩放Bitmap(默认回收传进来的原始Bitmap)
     *
     * @param originBitmap 原始bitmap
     * @param scaleX       横向缩放粒度
     * @param scaleY       纵向缩放粒度
     * @return Bitmap
     */
    public static Bitmap onScale(Bitmap originBitmap, float scaleX, float scaleY) {
        return onScale(originBitmap, scaleX, scaleY, true);
    }

    /**
     * 缩放Bitmap - 按缩放倍数
     *
     * @param originBitmap 原始bitmap
     * @param scaleX       横向缩放粒度
     * @param scaleY       纵向缩放粒度
     * @param recycle      是否回收传进来的原始Bitmap
     * @return Bitmap
     */
    public static Bitmap onScale(Bitmap originBitmap, float scaleX, float scaleY, boolean recycle) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        Bitmap scaledBitmap = Bitmap.createBitmap(originBitmap,
                0, 0, originBitmap.getWidth(), originBitmap.getHeight(), matrix, true);
        if (recycle && originBitmap != null && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }
        return scaledBitmap;
    }

    /**
     * 缩放Bitmap - 缩放到目标大小
     *
     * @param originBitmap 原始bitmap
     * @param dstWidth     目标宽度
     * @param dstHeight    目标高度
     * @param recycle      是否回收传进来的原始Bitmap
     * @return Bitmap
     */
    public static Bitmap onScale(Bitmap originBitmap, int dstWidth, int dstHeight, boolean recycle) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originBitmap, dstWidth, dstHeight, true);
        if (recycle && originBitmap != null && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }
        return scaledBitmap;
    }

    /**
     * 获取缩略图（默认关闭自动旋转）
     *
     * @param path      文件路径
     * @param maxWidth  最大宽
     * @param maxHeight 最大高
     * @return Bitmap
     */
    public static Bitmap onThumbnail(String path, int maxWidth, int maxHeight) {
        return onThumbnail(path, maxWidth, maxHeight, false);
    }

    /**
     * 获取缩略图（支持自动旋转）
     * 已优化处理
     *
     * @param path       文件路径
     * @param maxWidth   最大宽
     * @param maxHeight  最大高
     * @param autoRotate 是否自动旋转
     * @return Bitmap
     */
    public static Bitmap onThumbnail(String path, int maxWidth, int maxHeight, boolean autoRotate) {
        int angle = 0;
        if (autoRotate) {
            angle = ImageUtil.onExifRotateAngle(path);
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高, 此时返回bm为空
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        if (options.outHeight == -1 || options.outWidth == -1) {
            try {
                ExifInterface exifInterface = new ExifInterface(path);
                int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                options.outWidth = width;
                options.outHeight = height;
            } catch (IOException e) {
                BaoLog.e("onThumbnail", e);
            }
        }
        // 计算缩放比
        int sampleSize = onSampleSize(options, maxWidth, maxHeight);
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        bitmap = BitmapFactory.decodeFile(path, options);

        if (autoRotate && angle != 0) {
            bitmap = onRotate(bitmap, angle);
        }

        return bitmap;
    }


    /**
     * 保存到本地destFile
     *
     * @param bitmap   待保存bitmap
     * @param quality  质量参数
     * @param destFile 目标文件
     * @param recycle  是否释放原始资源
     * @return String
     */
    public static String onSave(Bitmap bitmap, int quality, File destFile, boolean recycle) {
        try {
            FileOutputStream out = new FileOutputStream(destFile);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, quality, out)) {
                out.flush();
                out.close();
            }

            if (recycle && bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }

            return destFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            BaoLog.e("onSave", e);
        } catch (IOException e) {
            BaoLog.e("onSave", e);
        }
        return null;
    }

    /**
     * 圆角Bitmap
     *
     * @param originBitmap 原始bitmap
     * @param radius       弧度
     * @param recycle      是否回收原始资源
     * @return Bitmap
     */
    public static Bitmap onRound(Bitmap originBitmap, int radius, boolean recycle) {
        // 准备画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // 准备裁剪的矩阵
        Rect rect = new Rect(0, 0, originBitmap.getWidth(), originBitmap.getHeight());
        RectF rectF = new RectF(new Rect(0, 0, originBitmap.getWidth(), originBitmap.getHeight()));

        Bitmap roundBitmap = Bitmap.createBitmap(originBitmap.getWidth(), originBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundBitmap);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        // 这一句是核心，关于Xfermode和SRC_IN请自行查阅
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(originBitmap, rect, rect, paint);

        // 是否回收原始Bitmap
        if (recycle && originBitmap != null && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }

        return roundBitmap;
    }

    /**
     * 圆形Bitmap：居中裁剪
     *
     * @param originBitmap 原始bitmap
     * @param recycle      是否回收原始资源
     * @return Bitmap
     */
    public static Bitmap onCircle(Bitmap originBitmap, boolean recycle) {
        int min = originBitmap.getWidth() > originBitmap.getHeight() ? originBitmap.getHeight() : originBitmap.getWidth();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap circleBitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 居中显示
        int left = -(originBitmap.getWidth() - min) / 2;
        int top = -(originBitmap.getHeight() - min) / 2;
        canvas.drawBitmap(originBitmap, left, top, paint);

        // 是否回收原始Bitmap
        if (recycle && originBitmap != null && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }

        return circleBitmap;
    }

    /**
     * 灰阶效果
     *
     * @param originBitmap 原始bitmap
     * @param recycle      是否回收原始资源
     * @return Bitmap
     */
    public static Bitmap onGray(Bitmap originBitmap, boolean recycle) {
        Bitmap grayBitmap = Bitmap.createBitmap(originBitmap.getWidth(),
                originBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(grayBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixColorFilter =
                new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(originBitmap, 0, 0, paint);

        // 是否回收原始Bitmap
        if (recycle && originBitmap != null && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }

        return grayBitmap;
    }

    /**
     * 将uri资源转为bitmap
     *
     * @param context 上下文
     * @param uri     照片资源
     * @return Bitmap
     */
    public static Bitmap onConvert(Context context, Uri uri) {
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            BaoLog.e("onConvert", e);
        } catch (OutOfMemoryError e) {
            System.gc();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            BaoLog.e("onConvert", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                BaoLog.e("onConvert", e);
            }
        }
        return bitmap;
    }

    /**
     * 将uri资源转为bitmap
     *
     * @param context   上下文
     * @param uri       照片的uri
     * @param dstWidth  目标宽度
     * @param dstHeight 目标高度
     * @return Bitmap
     */
    public static Bitmap onConvert(Context context, Uri uri, int dstWidth, int dstHeight) {
        InputStream is = null;
        Bitmap originBitmap = null;
        Bitmap goalBitmap = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            originBitmap = BitmapFactory.decodeStream(is);
            goalBitmap = onScale(originBitmap, dstWidth, dstHeight, true);
        } catch (FileNotFoundException e) {
            BaoLog.e("onConvert", e);
        } catch (OutOfMemoryError e) {
            System.gc();
            originBitmap = BitmapFactory.decodeStream(is);
            goalBitmap = onScale(originBitmap, dstWidth, dstHeight, true);
        } catch (Exception e) {
            BaoLog.e("onConvert", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                BaoLog.e("onConvert", e);
            }
        }
        return goalBitmap;
    }
}
