package com.codeisrun.viewlog.service;

import com.codeisrun.viewlog.bean.FileInfo;
import com.codeisrun.viewlog.bean.GcResult;
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

    /**
     * 获取该目录下所有文件信息
     *
     * @param projectId
     * @param nodeId
     * @param ip
     * @param path
     * @return
     */
    ProjectFileInfo getFileInfos(int projectId, int nodeId, String ip, String path);

    Project.ProjectNode getNode(int projectId, int nodeId);

    Project getProject(int projectId);

    boolean verifyPath(String ip, String path);

    GcResult analyseGcLog(String ip, String path);
}
