package com.ronfton.viewlog.service;

import com.ronfton.viewlog.bean.FileInfo;
import com.ronfton.viewlog.bean.LogInfo;

import java.util.List;

/**
 * @author liubinqiang
 */
public interface IViewLogService {
    List<LogInfo> getLogInfos(String logPaths);

    List<FileInfo> getFileInfos(String path);

    String readFile(String path);

    boolean verifyCmd(String cmd);
}
