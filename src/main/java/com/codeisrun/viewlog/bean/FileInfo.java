package com.codeisrun.viewlog.bean;

import lombok.Builder;
import lombok.Data;

/**
 * @author liubinqiang
 */
@Data
@Builder
public class FileInfo {
    private String path;
    private String name;
    private String modifyTime;
    private String size;
    private boolean isDirectory;
    private boolean isLogFile;
    private String dirUrl;
    private String openUrl;
    private String downloadUrl;
    /**
     * 实时日志地址
     */
    private String realTimeLogUrl;

    /**
     * 最近多少条日志
     */
    private String latestNumLogUrl;
    private String fileIcon;
    private String searchLogUrl;
    private String searchGzipLogUrl;

    public String genDirUrl(String path) {
        if (this.isDirectory) {

        }
        return null;
    }
}