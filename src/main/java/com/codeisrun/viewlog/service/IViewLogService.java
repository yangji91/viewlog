package com.codeisrun.viewlog.service;

import com.codeisrun.viewlog.bean.FileInfo;
import com.codeisrun.viewlog.bean.Project;

import java.util.List;

/**
 * @author liubinqiang
 */
public interface IViewLogService {
    /**
     * 日志目录
     *
     * @param logPaths
     * @return
     */
    List<Project> getLogInfos();

    List<FileInfo> getFileInfos(String ip, String path);

}
