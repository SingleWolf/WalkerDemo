package com.walker.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bsx.baolib.log.BaoLog;
import com.walker.WalkerApplication;
import com.walker.utils.APPUtil;
import com.walker.utils.MD5Util;
import com.walker.utils.StringUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * summary :图片加载（三级存储模式）
 * time    :2016/7/27 08:13
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class MagicImage {
    /**
     * 仅加载模式
     */
    public static final int MODE_LOAD_ONLY = 1;
    /**
     * 加载且显示模式
     */
    public static final int MODE_LOAD_SHOW = 2;
    /**
     * 加载完毕的回馈指令
     */
    public static final int CODE_LOAD_OVER = 0x110;
    /**
     * 记录所有正在下载或等待下载的任务（仅加载模式）。
     */
    private Set<LoadWorkerTask> mLoadTasks;
    /**
     * 记录所有正在下载或等待下载的任务（加载且显示模式）。
     */
    private Set<ShowWorkerTask> mShowTasks;
    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private LruCache<String, Bitmap> mMemoryCache;

    /**
     * 图片硬盘缓存核心类。
     */
    private DiskLruCache mDiskLruCache;
    /**
     * 待下载的资源url
     */
    private List<String> mLoadUrl;
    /**
     * 异步消息处理
     */
    private Handler mHandler;

    /*获取实例
     * @param mode 模式
     * @return MagicImage
     */
    public static MagicImage newInstance(int mode) {
        return new MagicImage(mode);
    }

    private MagicImage(int mode) {
        if (mode == MODE_LOAD_ONLY) {
            mLoadTasks = new HashSet<>();
        } else if (mode == MODE_LOAD_SHOW) {
            mShowTasks = new HashSet<>();
        }
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        // 设置图片缓存大小为程序最大可用内存的1/8
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
        try {
            // 获取图片缓存路径
            File cacheDir = getDiskCacheDir(WalkerApplication.getContext(), "thumb");
            // 创建DiskLruCache实例，初始化缓存数据
            mDiskLruCache = DiskLruCache
                    .open(cacheDir, APPUtil.getVersionCode(), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            BaoLog.e("MagicImage", e);
        } catch (Exception e) {
            BaoLog.e("MagicImage", e);
        }
    }


    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key    LruCache的键，这里传入图片的URL地址。
     * @param bitmap LruCache的键，这里传入从网络上下载的Bitmap对象。
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的Bitmap对象，或者null。
     */
    public Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }


    /**
     * 取消所有正在下载或等待下载的任务。
     */
    public void cancelAllTasks() {
        if (mLoadTasks != null) {
            for (LoadWorkerTask task : mLoadTasks) {
                task.cancel(false);
            }
        }
        if (mShowTasks != null) {
            for (ShowWorkerTask task : mShowTasks) {
                task.cancel(false);
            }
        }
    }

    /**
     * 根据传入的uniqueName获取硬盘缓存的路径地址。
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        File cacheDir = new File(StringUtil.pliceStr(cachePath, File.separator, uniqueName));
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    /**
     * 将缓存记录同步到journal文件中。
     */
    public void flushCache() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 仅加载模式
     *
     * @param loadUrl 加载资源集
     * @param handler 异步消息处理
     */
    public void onlyLoad(List<String> loadUrl, Handler handler) {
        mLoadUrl = loadUrl;
        mHandler = handler;
        for (String url : loadUrl) {
            Bitmap bitmap = getBitmapFromMemoryCache(url);
            if (bitmap == null) {
                LoadWorkerTask task = new LoadWorkerTask();
                mLoadTasks.add(task);
                task.execute(url);
            } else {
                bitmap.recycle();
                mLoadUrl.remove(url);
            }
        }
        isLoadOver();
    }

    /**
     * 加载Bitmap对象。此方法会在LruCache中检查所有屏幕中可见的ImageView的Bitmap对象，
     * 如果发现任何一个ImageView的Bitmap对象不在缓存中，就会开启异步线程去下载图片。
     *
     * @param imageView ImageView
     * @param imageUrl  图片的url
     */
    public void loadShow(ImageView imageView, String imageUrl) {
        try {
            Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
            if (bitmap == null) {
                ShowWorkerTask task = new ShowWorkerTask();
                mShowTasks.add(task);
                task.execute(Pair.create(imageUrl, imageView));
            } else {
                if (imageView != null && bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        } catch (Exception e) {
            BaoLog.e("loadShow", e);
        }
    }

    /**
     * 查看加载是否完毕
     */
    private void isLoadOver() {
        if (mLoadUrl.isEmpty()) {
            if (mHandler != null) {
                mHandler.sendEmptyMessage(CODE_LOAD_OVER);
            }
        }
    }

    /**
     * 异步下载图片的任务
     */
    class LoadWorkerTask extends AsyncTask<String, Void, Bitmap> {

        /**
         * 图片的URL地址
         */
        private String imageUrl;

        @Override
        protected Bitmap doInBackground(String... params) {
            if (!isCancelled()) {
                imageUrl = params[0];
                FileDescriptor fileDescriptor = null;
                FileInputStream fileInputStream = null;
                DiskLruCache.Snapshot snapShot = null;
                try {
                    // 生成图片URL对应的key
                    final String key = MD5Util.getMD5(imageUrl);
                    // 查找key对应的缓存
                    snapShot = mDiskLruCache.get(key);
                    if (snapShot == null) {
                        // 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
                        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                        if (editor != null) {
                            OutputStream outputStream = editor.newOutputStream(0);
                            if (downloadUrlToStream(imageUrl, outputStream)) {
                                editor.commit();
                            } else {
                                editor.abort();
                            }
                        }
                        // 缓存被写入后，再次查找key对应的缓存
                        snapShot = mDiskLruCache.get(key);
                    }
                    if (snapShot != null) {
                        fileInputStream = (FileInputStream) snapShot.getInputStream(0);
                        fileDescriptor = fileInputStream.getFD();
                    }
                    // 将缓存数据解析成Bitmap对象
                    Bitmap bitmap = null;
                    if (fileDescriptor != null) {
                        bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    }
                    if (bitmap != null) {
                        // 将Bitmap对象添加到内存缓存当中
                        addBitmapToMemoryCache(params[0], bitmap);
                    }
                    return bitmap;
                } catch (IOException e) {
                    BaoLog.e("MagicImage", e);
                } finally {
                    if (fileDescriptor == null && fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            BaoLog.e("MagicImage", e);
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                bitmap.recycle();
            }
            mLoadTasks.remove(this);
            mLoadUrl.remove(imageUrl);
            isLoadOver();
        }

    }

    /**
     * 异步下载、展示图片的任务
     */
    class ShowWorkerTask extends AsyncTask<Pair, Void, Bitmap> {

        /**
         * 图片的URL地址
         */
        private String imageUrl;
        /**
         * imageview
         */
        private ImageView imageView;

        @Override
        protected Bitmap doInBackground(Pair... params) {
            if (!isCancelled()) {
                imageUrl = (String) params[0].first;
                imageView = (ImageView) params[0].second;
                FileDescriptor fileDescriptor = null;
                FileInputStream fileInputStream = null;
                DiskLruCache.Snapshot snapShot = null;
                try {
                    // 生成图片URL对应的key
                    final String key = MD5Util.getMD5(imageUrl);
                    // 查找key对应的缓存
                    snapShot = mDiskLruCache.get(key);
                    if (snapShot == null) {
                        // 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
                        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                        if (editor != null) {
                            OutputStream outputStream = editor.newOutputStream(0);
                            if (downloadUrlToStream(imageUrl, outputStream)) {
                                editor.commit();
                            } else {
                                editor.abort();
                            }
                        }
                        // 缓存被写入后，再次查找key对应的缓存
                        snapShot = mDiskLruCache.get(key);
                    }
                    if (snapShot != null) {
                        fileInputStream = (FileInputStream) snapShot.getInputStream(0);
                        fileDescriptor = fileInputStream.getFD();
                    }
                    // 将缓存数据解析成Bitmap对象
                    Bitmap bitmap = null;
                    if (fileDescriptor != null) {
                        bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    }
                    if (bitmap != null) {
                        // 将Bitmap对象添加到内存缓存当中
                        addBitmapToMemoryCache((String) params[0].first, bitmap);
                    }
                    return bitmap;
                } catch (IOException e) {
                    BaoLog.e("ShowWorkerTask", e);
                } finally {
                    if (fileDescriptor == null && fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            BaoLog.e("ShowWorkerTask", e);
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            // 根据Tag找到相应的ImageView控件，将下载好的图片显示出来。
            if (bitmap != null && TextUtils.equals(imageView.getTag().toString(), imageUrl)) {
                imageView.setImageBitmap(bitmap);
            }
            mShowTasks.remove(this);
        }
    }

    /**
     * 建立HTTP请求，并获取Bitmap对象。
     *
     * @param urlString 图片的URL地址
     * @return 解析后的Bitmap对象
     */
    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}