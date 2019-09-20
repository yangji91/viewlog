package com.ronfton.viewlog.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liubinqiang
 */
public class Util {

    public static String dateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String byteToM(long length) {
        float m = 1024 * 1024;
        float ret = length / m;
        DecimalFormat format = new DecimalFormat(",##0.00M");
        return format.format(ret);
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
