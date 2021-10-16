package com.codeisrun.viewlog.service;

import com.codeisrun.viewlog.bean.FileInfo;
import com.codeisrun.viewlog.bean.Project;
import com.codeisrun.viewlog.common.CmdEnum;
import com.codeisrun.viewlog.config.SystemConfig;
import com.codeisrun.viewlog.util.LinuxUtil;
import com.codeisrun.viewlog.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 执行ssh命令方式，支持读取远程服务器文件
 *
 * @author liubinqiang
 */
@Service("sshViewLogService")
public class SshViewLogServiceImpl implements IViewLogService {

    @Autowired
    private SystemConfig systemConfig;


    @Override
    public List<Project> getLogInfos() {
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
    public List<FileInfo> getFileInfos(String ip, String path) {
        String ret = LinuxUtil.doCmdAndGetStr(ip,
                systemConfig.getServerUsername(ip), systemConfig.getServerPassword(ip), CmdEnum.LS.getCmdHeader() + path, path);
        List<FileInfo> fileInfos = FileInfo.getListByStr(ret);
        for (FileInfo f : fileInfos) {
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
        return fileInfos;
    }

}
