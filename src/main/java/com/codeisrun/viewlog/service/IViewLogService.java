package com.codeisrun.viewlog.service;

import com.codeisrun.viewlog.bean.FileInfo;
import com.codeisrun.viewlog.bean.Project;
import com.codeisrun.viewlog.bean.ProjectFileInfo;

import java.util.List;

/**
 * @author liubinqiang
 */
public interface IViewLogService {
    /**
     * 日志目录
     *
     * @return
     */
    List<Project> getProjectList();

    ProjectFileInfo getFileInfos(String ip, String path);

    boolean verifyPath(String ip, String path);
}
