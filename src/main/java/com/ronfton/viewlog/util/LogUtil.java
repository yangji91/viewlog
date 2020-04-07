package com.ronfton.viewlog.util;

import com.ronfton.viewlog.bean.DirInfo;
import com.ronfton.viewlog.bean.LogInfo;
import com.ronfton.viewlog.common.ConstStr;
import com.ronfton.viewlog.config.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.print.PSPrinterJob;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author liubinqiang
 */
@Component
public class LogUtil {

    private static SystemConfig systemConfig;

    @Autowired
    private void setSystemConfig(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }

    private static float KB = 1024;
    private static float MB = 1024 * 1024;

    /**
     * 查看日志文件范围
     *
     * @param fileEditTime
     * @return
     */
    public static boolean isInScope(long fileEditTime) {
        long a = 3600;
        long startTime = System.currentTimeMillis() - (24 * a * 1000 * systemConfig.logScope);
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

    /**
     * 计算文件或者文件夹大小
     *
     * @param path
     * @return
     */
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

    /**
     * 计算目录文件个数和文件夹个数
     *
     * @param path
     * @return
     */
    public static int[] getFileNumber(String path) {
        int[] num = {0, 0};
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


    private static int[] getDirNumber(File file, int[] num) {
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

    public static String getIcon(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                return ConstStr.iconFolder;
            } else {
                if (file.getName().endsWith(".log.gz") || file.getName().endsWith(".zip")) {
                    return ConstStr.iconZip;
                } else {
                    return ConstStr.iconLog;
                }
            }
        }
        return null;
    }

    public static String getIcon(String path) {
        File file = new File(path);
        return getIcon(file);
    }

    /**
     * 获取日志目录
     *
     * @return
     */
    public static Map<String, String> getLogInfos() {
        Map<String, String> logs = new HashMap<>();
        if (systemConfig.logPaths != null) {
            String[] ps = systemConfig.logPaths.split(",");
            for (String p : ps) {
                if (p != null && p.length() > 0) {
                    String[] ls = p.split("\\|");
                    if (ls.length == 2) {
                        logs.put(ls[0], ls[1]);
                    }
                }
            }
        }
        return logs;
    }

    public static boolean isLegal(String path) {
        if (path != null) {
            Map<String, String> logs = getLogInfos();
            for (String s : logs.values()) {
                if (path.startsWith(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 日志目录分层
     *
     * @param path
     * @return
     */
    public static List<DirInfo> getPathHierarchy(String path) {
        List<DirInfo> ds = new ArrayList<>();
        if (path != null && path.length() > 0) {
            Map<String, String> logs = getLogInfos();
            for (String s : logs.values()) {
                if (path.startsWith(s)) {
                    ds.add(new DirInfo(s, getInfoUrl(s)));
                    if (path.length() > s.length()) {
                        path = path.substring(s.length());
                        String sp = System.getProperty("os.name").contains("Windows") ? (File.separator + File.separator) : File.separator;
                        String[] ps = path.split(sp);
                        String currentPath = s;
                        for (String p : ps) {
                            if (p != null && p.length() > 0) {
                                currentPath = currentPath + File.separator + p;
                                ds.add(new DirInfo(p, getInfoUrl(currentPath)));
                            }
                        }
                    }
                    return ds;
                }
            }
        }
        return ds;
    }

    public static String getInfoUrl(String path) {
        return systemConfig.httpPath + "/info?path=" + LogUtil.urlEncode(path);
    }

    public static String getPathSizeAndFileCount(String path) {
        String result = "";
        File file = new File(path);
        if (file.exists()) {
            result = bytesToView(getFileLength(path));
            if (file.isDirectory()) {
                int[] numbers = getFileNumber(path);
                result = result + " (" + numbers[0] + "个文件)";
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));
        String a1 = "D:\\var\\log\\rft-boss\\uaps-app-order-polling.2019-11-29.log";
        String a = "/home/roncoo/pay/app/order-polling/logs/backup/2019-12-15-08-04";
        String[] bs = a.split("/");
        System.out.println(bs.length);
    }
}
