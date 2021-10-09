package com.codeisrun.viewlog.service;

import com.codeisrun.viewlog.bean.FileInfo;
import com.codeisrun.viewlog.bean.LogInfo;
import com.codeisrun.viewlog.bean.LogMenu;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author liubinqiang
 */
public interface IViewLogService {
    List<LogInfo> getLogInfos(String logPaths);

    List<FileInfo> getFileInfos(String path);

    String readFile(String path);

    boolean verifyCmd(String cmd);

    LinkedHashMap<String, List<LogMenu>> getMenuItemList();
}