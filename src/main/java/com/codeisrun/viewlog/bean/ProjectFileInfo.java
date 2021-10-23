package com.codeisrun.viewlog.bean;

import lombok.Data;

import java.util.List;

/**
 * 项目日志信息
 *
 * @author liubinqiang
 * @date 2021-10-17
 */
@Data
public class ProjectFileInfo {
    private String totalSize;
    /**
     * 文件信息
     */
    private List<FileInfo> fileInfoList;
    /**
     * 实例信息
     */
    private List<Project.ProjectNode> projectNodes;
}
