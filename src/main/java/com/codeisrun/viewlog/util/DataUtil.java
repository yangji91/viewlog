package com.codeisrun.viewlog.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author liubinqiang
 * @date 2021-11-11
 */
public class DataUtil {

    public static String kbToMb(String kb) {
        if (kb == null) {
            return null;
        }
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(new BigDecimal(kb).divide(new BigDecimal(1024))) + "MB";
    }

    public static String rate(String a, String b) {
        if (a == null || b == null) {
            return null;
        }
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format((new BigDecimal(a).divide(new BigDecimal(b), 4, BigDecimal.ROUND_HALF_DOWN)).multiply(new BigDecimal(100))) + "%";
    }

    public static String upRate(String before, String after, String total) {
        if (before == null || after == null || total == null) {
            return null;
        }
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(new BigDecimal(before).subtract(new BigDecimal(after)).divide(new BigDecimal(total), 4, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100))) + "%";
    }

    public static void main(String[] args) {
        String s = rate("80", "60");
        System.out.println(s);
    }
}
