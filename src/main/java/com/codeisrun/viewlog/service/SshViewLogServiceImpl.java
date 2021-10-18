package com.codeisrun.viewlog.service;

import com.codeisrun.viewlog.bean.FileInfo;
import com.codeisrun.viewlog.bean.Project;
import com.codeisrun.viewlog.bean.ProjectFileInfo;
import com.codeisrun.viewlog.common.CmdEnum;
import com.codeisrun.viewlog.config.SystemConfig;
import com.codeisrun.viewlog.util.LinuxUtil;
import com.codeisrun.viewlog.util.LogUtil;
import com.sun.org.apache.xalan.internal.lib.NodeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.codeisrun.viewlog.util.LinuxUtil.*;

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


    @Override
    public List<Project> getProjectList() {
        List<Project> logInfos = new ArrayList<>();
        if (systemConfig.logPaths != null) {
            String[] ps = systemConfig.logPaths.split(",");
            for (String p : ps) {
                Project info = Project.getFromLogPath(p);
                if (info != null) {
                    logInfos.add(info);
                }
            }
        }
        return logInfos;
    }


    @Override
    public ProjectFileInfo getFileInfos(String ip, String path) {
        ProjectFileInfo projectFileInfo = new ProjectFileInfo();
        if (!verifyPath(ip, path)) {
            projectFileInfo.setTotalSize("文件路径不支持");
            return projectFileInfo;
        }
        String ret = null;
        try {
            ret = doCmdAndGetStr(ip,
                    systemConfig.getServerUsername(ip), systemConfig.getServerPassword(ip), CmdEnum.LS.getCmdHeader() + path, path);
        } catch (Exception e) {
            log.error("查询项目信息报错：", e);
            projectFileInfo.setTotalSize(e.getMessage());
            return projectFileInfo;
        }
        projectFileInfo = FileInfo.getListByStr(projectFileInfo, ret);
        for (FileInfo f : projectFileInfo.getFileInfoList()) {
            String subPath = path + File.separator + f.getName();
            f.setPath(subPath);
            f.setDownloadUrl(("/viewlog/download?ip=" + ip + "&path=" + LogUtil.urlEncode(subPath)));
            f.setOpenUrl("/viewlog/open?ip=" + ip + "&path=" + LogUtil.urlEncode(subPath));
            f.setRealTimeLogUrl("/viewlog/do?ip=" + ip + "&code=1&path=" + LogUtil.urlEncode(subPath));
            f.setLatestNumLogUrl("/viewlog/do?ip=" + ip + "&code=2&path=" + LogUtil.urlEncode(subPath));
            f.setSearchLogUrl("/viewlog/do?ip=" + ip + "&code=3&path=" + LogUtil.urlEncode(subPath));
            f.setSearchGzipLogUrl("/viewlog/do?ip=" + ip + "&code=4&path=" + LogUtil.urlEncode(subPath));
            f.setFileIcon(LogUtil.getIcon(f));
            f.setLogFile(LogUtil.isLogFile(f));
        }
        return projectFileInfo;
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

}
