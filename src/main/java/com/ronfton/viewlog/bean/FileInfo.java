package com.ronfton.viewlog.bean;

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
    private boolean isFile;
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
}
