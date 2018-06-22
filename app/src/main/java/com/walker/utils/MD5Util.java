package com.walker.utils;


import com.bsx.baolib.log.BaoLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * summary :MD5加解密工具类
 * time    :2016/5/13 16:18
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class MD5Util {
    /**
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
     */
    protected static char[] HEXDIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * MessageDigest
     */
    protected static MessageDigest MESSAGEDIGEST = null;

    static {
        try {
            MESSAGEDIGEST = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            BaoLog.e("MD5Util初始化失败", e);
        }
    }

    /**
     * 生成字符串的md5校验值
     *
     * @param s 待加密字符串
     * @return String
     */
    public static String getMD5(String s) {
        return getMD5(s.getBytes());
    }


    /**
     * 生成文件的md5校验值
     *
     * @param file 待加密文件
     * @return String
     */
    public static String getMD5(File file) {
        String md5 = "";
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int numRead = 0;
            while ((numRead = fis.read(buffer)) > 0) {
                MESSAGEDIGEST.update(buffer, 0, numRead);
            }
            fis.close();
            md5 = bufferToHex(MESSAGEDIGEST.digest());
        } catch (FileNotFoundException e) {
            BaoLog.e("getFileMD5", e);
        } catch (IOException e) {
            BaoLog.e("getFileMD5", e);
        }
        return md5;
    }


    /**
     * @param bytes 待加密的字码
     * @return String
     */
    public static String getMD5(byte[] bytes) {
        MESSAGEDIGEST.update(bytes);
        return bufferToHex(MESSAGEDIGEST.digest());
    }

    /**
     * @param bytes 字节
     * @return String
     */
    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    /**
     * @param bytes 字节
     * @param m     初始位置
     * @param n     长度
     * @return String
     */
    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    /**
     * @param bt           字节
     * @param stringbuffer 拼接字符集
     */
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = HEXDIGITS[(bt & 0xf0) >> 4]; // 取字节中高 4 位的数字转换, >>>
        // 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        char c1 = HEXDIGITS[bt & 0xf]; // 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
