package com.ronfton.viewlog.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
}
