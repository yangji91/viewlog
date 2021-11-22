package com.codeisrun.viewlog.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * @author liubinqiang
 * @date 2021-11-11
 */
public class DataUtil {
    public static final BigDecimal SECOND_DAY = new BigDecimal(24 * 60 * 60);
    public static final BigDecimal SECOND_HOUR = new BigDecimal(60 * 60);
    public static final BigDecimal SECOND_MINUTE = new BigDecimal(60);

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

    public static String secondFormat(float second) {
        if (second == 0) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        BigDecimal a = new BigDecimal(second);
        BigDecimal[] temp;
        DecimalFormat format = new DecimalFormat("0");
        if (a.compareTo(SECOND_DAY) >= 0) {
            temp = a.divideAndRemainder(SECOND_DAY);
            sb.append(format.format(temp[0])).append("天");
            a = temp[1];
        }
        if (a.compareTo(SECOND_HOUR) >= 0) {
            temp = a.divideAndRemainder(SECOND_HOUR);
            sb.append(format.format(temp[0])).append("时");
            a = temp[1];
        }
        if (a.compareTo(SECOND_MINUTE) >= 0) {
            temp = a.divideAndRemainder(SECOND_MINUTE);
            sb.append(format.format(temp[0])).append("分");
            a = temp[1];
        }
        DecimalFormat format2 = new DecimalFormat("0.000");
        sb.append(format2.format(a)).append("秒");
        return sb.toString();
    }


    public static void main(String[] args) {
        System.out.println(secondFormat(1534590.875f));
    }
}
