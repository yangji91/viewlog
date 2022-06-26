package com.codeisrun.viewlog.service;

import com.codeisrun.viewlog.bean.*;

import java.util.List;
import java.util.Set;

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
     * 查询项目分组
     *
     * @return
     */
    Set<ProjectGroup> getProjectGroupList();

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
