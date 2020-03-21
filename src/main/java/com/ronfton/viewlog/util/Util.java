package com.ronfton.viewlog.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liubinqiang
 */
@Component
public class Util {

    private static int logScope;

    @Value("${log-scope}")
    private void setLogScope(int ls) {
        logScope = ls;
    }

    private static float KB = 1024;
    private static float MB = 1024 * 1024;

    public static boolean isInScope(long fileEditTime) {
        long a = 3600;
        long startTime = System.currentTimeMillis() - (24 * a * 1000 * logScope);
        return fileEditTime > startTime;
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String bytesToView(long length) {
        if (length < KB) {
            return length + "Bytes";
        } else if (length < MB) {
            DecimalFormat format = new DecimalFormat(",##0.00KB");
            return format.format(length / KB);
        } else {
            DecimalFormat format = new DecimalFormat(",##0.00MB");
            return format.format(length / MB);
        }
    }

    public static String timespanToDateStr(long timespan) {
        Date date = new Date(timespan);
        return dateFormat(date);
    }

    public static String urlDecoder(String str) {
        try {
            if (str != null && str.length() > 0) {
                str = URLDecoder.decode(str, "utf-8");
            }
        } catch (Exception ex) {

        }
        return str;
    }

    public static String urlEncode(String str) {
        try {
            if (str != null && str.length() > 0) {
                str = URLEncoder.encode(str, "utf-8");
            }
        } catch (Exception ex) {

        }
        return str;
    }

    public static long getFileLength(String path) {
        long length = 0;
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                length = getDirSize(file, length);
            }
        }
        return length;
    }


    private static long getDirSize(File file, long length) {
        if (file.isFile()) {
            //如果是文件，获取文件大小累加
            length += file.length();
        } else if (file.isDirectory()) {
            //获取目录中的文件及子目录信息
            File[] f1 = file.listFiles();
            for (int i = 0; i < f1.length; i++) {
                //调用递归遍历f1数组中的每一个对象
                length = getDirSize(f1[i], length);
            }
        }
        return length;
    }

    public static long[] getFileNumber(String path) {
        long[] num = {0, 0};
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                num = getDirNumber(file, num);
                if (num[1] > 0) {
                    num[1] -= 1;
                }
            }
        }
        return num;
    }


    private static long[] getDirNumber(File file, long[] num) {
        if (file.isFile()) {
            //如果是文件，获取文件大小累加
            num[0] += 1;
        } else if (file.isDirectory()) {
            num[1] += 1;
            //获取目录中的文件及子目录信息
            File[] f1 = file.listFiles();
            for (int i = 0; i < f1.length; i++) {
                //调用递归遍历f1数组中的每一个对象
                num = getDirNumber(f1[i], num);
            }
        }
        return num;
    }

    public static void main(String[] args) {
        long[] l = getFileNumber("D:\\var\\log\\rft-boss");
        System.out.println(l);
    }
}
