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

    ProjectFileInfo getFileInfos(int projectId, int nodeId, String ip, String path);

    Project.ProjectNode getNode(int projectId, int nodeId);

    Project getProject(int projectId);

    boolean verifyPath(String ip, String path);
}
