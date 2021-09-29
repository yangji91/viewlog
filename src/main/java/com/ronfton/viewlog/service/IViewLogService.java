package com.ronfton.viewlog.service;

import com.ronfton.viewlog.bean.FileInfo;
import com.ronfton.viewlog.bean.LogInfo;
import com.ronfton.viewlog.bean.LogMenu;

import java.util.HashMap;
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
