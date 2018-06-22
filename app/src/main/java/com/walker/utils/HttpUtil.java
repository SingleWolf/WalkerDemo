package com.walker.utils;

import com.bsx.baolib.log.BaoLog;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * summary :网络请求工具类
 * time    :2016/7/22 09:11
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class HttpUtil {
    /**
     * 获取请求状态
     *
     * @param strUrl url
     * @return int
     */
    public static int getRespStatus(String strUrl) {
        int status = -1;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            // 添加了UTF-8字符信息
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setRequestProperty("connection", "Keep-Alive");
            status = conn.getResponseCode();
        } catch (MalformedURLException e) {
            BaoLog.e("getRespStatus", e);
        } catch (IOException e) {
            BaoLog.e("getRespStatus", e);
        }
        return status;
    }

}
