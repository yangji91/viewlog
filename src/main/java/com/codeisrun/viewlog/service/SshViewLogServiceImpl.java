package com.codeisrun.viewlog.service;

import com.codeisrun.viewlog.bean.*;
import com.codeisrun.viewlog.common.CmdEnum;
import com.codeisrun.viewlog.config.SystemConfig;
import com.codeisrun.viewlog.util.LinuxUtil;
import com.codeisrun.viewlog.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * 执行ssh命令方式，支持读取远程服务器文件
 *
 * @author liubinqiang
 */
@Slf4j
@Service
public class SshViewLogServiceImpl implements IViewLogService {

    @Autowired
    private SystemConfig systemConfig;
    private static List<Project> cacheProjectList = new ArrayList<>();


    @Override
    public List<Project> getProjectList() {
        if (!cacheProjectList.isEmpty()) {
            return cacheProjectList;
        }
        if (systemConfig.logPaths != null) {
            String[] ps = systemConfig.logPaths.split(",");
            int currentProjectId = 1;
            for (String p : ps) {
                Project info = Project.getFromLogPath(p, currentProjectId);
                if (info != null) {
                    cacheProjectList.add(info);
                    currentProjectId = currentProjectId + 1;
                }
            }
        }
        return cacheProjectList;
    }

    private List<Project.ProjectNode> getById(int projectId, int nodeId) {
        Optional<Project> optionalProject = getProjectList().stream().filter(p -> p.getProjectId() == projectId).findFirst();
        if (optionalProject.isPresent()) {
            if (!optionalProject.get().getNodeList().isEmpty()) {
                Optional<Project.ProjectNode> nodeOptional = optionalProject.get().getNodeList().stream().filter(n -> n.getNodeId() == nodeId).findFirst();
                if (nodeOptional.isPresent()) {
                    return optionalProject.get().getNodeList().stream().filter(n -> n.getEnv().equals(nodeOptional.get().getEnv())).collect(Collectors.toList());
                }
            }
        }
        return null;
    }


    @Override
    public ProjectFileInfo getFileInfos(int projectId, int nodeId, String ip, String path) {
        ProjectFileInfo projectFileInfo = new ProjectFileInfo();
        if (!verifyPath(ip, path)) {
            projectFileInfo.setTotalSize("文件路径不支持");
            return projectFileInfo;
        }
        String ret = null;
        try {
            ret = LinuxUtil.doCmdAndGetStr(ip,
                    systemConfig.getServerUsername(ip), systemConfig.getServerPassword(ip), CmdEnum.LS.getCmdHeader() + path, path);
        } catch (Exception e) {
            log.error("查询项目信息报错：", e);
            projectFileInfo.setTotalSize(e.getMessage());
            return projectFileInfo;
        }
        projectFileInfo = FileInfo.getListByStr(projectFileInfo, ret);
        for (FileInfo f : projectFileInfo.getFileInfoList()) {
            f.setProjectId(projectId);
            f.setNodeId(nodeId);
            f.setIp(ip);
            String subPath = path + File.separator + f.getName();
            f.setPath(subPath);
            f.setDownloadUrl(("/viewlog/download?ip=" + ip + "&path=" + LogUtil.urlEncode(subPath)));
            f.setOpenUrl("/viewlog/open?ip=" + ip + "&path=" + LogUtil.urlEncode(subPath));
            f.setRealTimeLogUrl(joinUrl(ip, CmdEnum.TAIL_F, subPath));
            f.setTailNumLogUrl(joinUrl(ip, CmdEnum.TAIL_N, subPath));
            f.setHeadNumLogUrl(joinUrl(ip, CmdEnum.HEAD_N, subPath));
            f.setSearchLogUrl(joinUrl(ip, CmdEnum.GREP_C, subPath));
            f.setSearchGzipLogUrl(joinUrl(ip, CmdEnum.GZIP_DC_TAIL, subPath));
            f.setHeadGzipLogUrl(joinUrl(ip, CmdEnum.GZIP_DC_HEAD, subPath));
            f.setTailGzipLogUrl(joinUrl(ip, CmdEnum.GZIP_DC_TAIL, subPath));
            f.setFileIcon(LogUtil.getIcon(f));
            f.setLogFile(LogUtil.isLogFile(f));
            f.setCompressFile(LogUtil.isCompressFile(f));
        }
        projectFileInfo.setProjectNodes(getById(projectId, nodeId));
        return projectFileInfo;
    }

    private String joinUrl(String ip, CmdEnum cmdEnum, String subPath) {
        return String.format("/viewlog/do?ip=%s&code=%s&path=%s", ip, cmdEnum.getCode(), LogUtil.urlEncode(subPath));
    }

    @Override
    public Project.ProjectNode getNode(int projectId, int nodeId) {
        Optional<Project> optionalProject = getProjectList().stream().filter(p -> p.getProjectId() == projectId).findFirst();
        if (optionalProject.isPresent()) {
            if (!optionalProject.get().getNodeList().isEmpty()) {
                Optional<Project.ProjectNode> nodeOptional = optionalProject.get().getNodeList().stream().filter(n -> n.getNodeId() == nodeId).findFirst();
                if (nodeOptional.isPresent()) {
                    return nodeOptional.get();
                }
            }
        }
        return null;
    }

    @Override
    public Project getProject(int projectId) {
        Optional<Project> optionalProject = getProjectList().stream().filter(p -> p.getProjectId() == projectId).findFirst();
        if (optionalProject.isPresent()) {
            return optionalProject.get();
        }
        return null;
    }

    @Override
    public boolean verifyPath(String ip, String path) {
        if (path != null) {
            List<Project> projectList = getProjectList();
            for (Project p : projectList) {
                for (Project.ProjectNode n : p.getNodeList()) {
                    if (Objects.equals(n.getIp(), ip) && path.contains(n.getLogPath())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public GcResult analyseGcLog(String ip, String path) {
        GcResult gcResult = new GcResult();
        List<GcRecord> recordList = new ArrayList<>();
        gcResult.setGcRecordList(recordList);
        getGcResult(ip, path, gcResult, "ParNew: ", GcLogType.ParNew);
        getGcResult(ip, path, gcResult, "CMS Initial Mark", GcLogType.CMSInitialMark);
        getGcResult(ip, path, gcResult, "CMS Final Remark", GcLogType.CMSFinalRemark);
        gcResult.initData();
        return gcResult;
    }

    private void getGcResult(String ip, String path, GcResult gcResult, String key, GcLogType gcLogType) {
        String retStr = null;
        try {
            retStr = LinuxUtil.doCmdAndGetStr(ip,
                    systemConfig.getServerUsername(ip), systemConfig.getServerPassword(ip), CmdEnum.GREP.getCmdHeader() + " '" + key + "' " + path + " |tail -n 1000", path);
            List<GcRecord> recordList = new ArrayList<>();
            String[] strArray = retStr.split("\n");
            Pattern pattern = Pattern.compile(GcRecord.getGcReg(gcLogType));
            float lastGcTime = 0;
            for (int i = 0; i < strArray.length; i++) {
                Matcher matcher = pattern.matcher(strArray[i]);
                if (matcher.find()) {
                    GcRecord gcRecord = GcRecord.genRecord(matcher, gcLogType);
                    gcRecord.setId(i + 1);
                    gcRecord.setIntervalTime(gcRecord.getRunTime() - lastGcTime);
                    lastGcTime = gcRecord.getRunTime();
                    recordList.add(gcRecord);
                }
            }
            log.info("解析gc日志：strArray.length={},recordList.size={}", strArray.length, recordList.size());
            gcResult.getGcRecordList().addAll(recordList);
        } catch (Exception e) {
            log.error("查询日志信息报错：", e);
        }
    }

}
