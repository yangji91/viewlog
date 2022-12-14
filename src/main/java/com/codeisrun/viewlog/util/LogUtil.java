package com.codeisrun.viewlog.util;

import com.codeisrun.viewlog.bean.FileInfo;
import com.codeisrun.viewlog.common.ConstStr;
import com.codeisrun.viewlog.config.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public static boolean isCompressFile(String fileName) {
        if (fileName.endsWith(".tar.gz") || fileName.endsWith(".zip")) {
            return true;
        }
        return false;
    }

    public static boolean isLogFile(File log) {
        if (log.isDirectory()) {
            return false;
        }
        if (isCompressFile(log.getName())) {
            return false;
        }
        return true;
    }

    public static boolean isLogFile(FileInfo fileInfo) {
        if (fileInfo.isDirectory()) {
            return false;
        } else {
            if (isCompressFile(fileInfo.getName())) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static boolean isCompressFile(FileInfo fileInfo) {
        if (fileInfo.isDirectory()) {
            return false;
        } else {
            if (isCompressFile(fileInfo.getName())) {
                return true;
            }
        }
        return false;
    }


    public static String getIcon(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                return ConstStr.iconFolder;
            } else {
                if (isCompressFile(file.getName())) {
                    return ConstStr.iconZip;
                } else {
                    return ConstStr.iconLog;
                }
            }
        }
        return null;
    }

    public static String getIcon(FileInfo fileInfo) {
        if (fileInfo.isDirectory()) {
            return ConstStr.iconFolder;
        } else {
            if (isCompressFile(fileInfo.getName())) {
                return ConstStr.iconZip;
            } else {
                return ConstStr.iconLog;
            }
        }
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
}
