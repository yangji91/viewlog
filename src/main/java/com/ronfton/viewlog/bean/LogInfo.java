package com.ronfton.viewlog.bean;

import com.ronfton.viewlog.util.LogUtil;
import lombok.Data;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 日志信息
 *
 * @author liubinqiang
 */
@Data
public class LogInfo {
    public LogInfo() {
    }

    public LogInfo(String name, String logPath, String httpPath) {
        this.name = name;
        this.logPath = logPath;
        this.fileTotalLength = LogUtil.bytesToView(LogUtil.getFileLength(logPath));
        long[] num = LogUtil.getFileNumber(logPath);
        this.fileNum = num[0];
        this.dirNum = num[1];
        this.fileIcon = LogUtil.getIcon(logPath);
        this.latestLogFile = getLatestFile();
        this.viewFileInfoUrl = httpPath + "/info?path=" + LogUtil.urlEncode(this.logPath);
//        if (this.latestLogFile != null) {
//            this.realTimeLogUrl = httpPath + "/do?cmd=tail -f " + this.logPath + File.separator + this.latestLogFile;
//            this.latestNumLogUrl = httpPath + "/do?cmd=tail -200 " + this.logPath + File.separator + this.latestLogFile;
//
//        }
    }

    /**
     * 项目名称
     */
    private String name;
    /**
     * 项目日志路径
     */
    private String logPath;
    /**
     * 日志目录总大小
     */
    private String fileTotalLength;
    /**
     * 包含文件个数
     */
    private long fileNum;
    /**
     * 包含文件夹个数
     */
    private long dirNum;
    /**
     * 最新日志文件
     */
    private String latestLogFile;
    /**
     * 日志最后修改时间
     */
    private String latestModifyTime;
    /**
     * 实时日志地址
     */
    private String realTimeLogUrl;

    /**
     * 最近多少条日志
     */
    private String latestNumLogUrl;

    /**
     * 查看所有日志文件地址
     */
    private String viewFileInfoUrl;

    private String fileIcon;


    /**
     * 从日志路径中找到最新日志并且是当天最大的
     *
     * @return
     */
    public String getLatestFile() {
        String fileName = null;
        File file = new File(this.logPath);
        if (file != null && file.exists()) {
            file.length();
            if (file.isFile()) {
                fileName = file.getName();
            } else {
                if (file.listFiles() != null && file.listFiles().length > 0) {
                    //过滤掉其它文件
                    File[] logs = file.listFiles(new LogFileFilter());
                    if (logs != null && logs.length > 0) {
                        //按修改日期排序
                        List<File> files = Arrays.stream(logs).sorted(Comparator.comparing(File::lastModified)).collect(Collectors.toList());
                        Collections.reverse(files);
                        //得到最新文件日期
                        long lastModifyTime = files.get(0).lastModified();
                        Date lastModifyTimeDate = new Date(lastModifyTime);
                        this.latestModifyTime = LogUtil.dateFormat(lastModifyTimeDate);
/*                        LocalDate localDate = lastModifyTimeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        long timestamp = localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
                        //只查询最新日期的所有文件
                        files = files.stream().filter(f -> f.lastModified() >= timestamp).collect(Collectors.toList());
                        //按大小排序
                        files = files.stream().sorted(Comparator.comparing(File::length)).collect(Collectors.toList());
                        Collections.reverse(files);
                        fileName = files.get(0).getName();*/
                    }
                }
            }
        }
        return fileName;
    }
}

