package com.codeisrun.viewlog.service;

import com.codeisrun.viewlog.bean.FileInfo;
import com.codeisrun.viewlog.bean.LogFileFilter;
import com.codeisrun.viewlog.bean.LogInfo;
import com.codeisrun.viewlog.bean.LogMenu;
import com.codeisrun.viewlog.common.CmdEnum;
import com.codeisrun.viewlog.config.SystemConfig;
import com.codeisrun.viewlog.util.LinuxUtil;
import com.codeisrun.viewlog.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 执行ssh命令方式，支持读取远程服务器文件
 *
 * @author liubinqiang
 */
@Service("sshViewLogService")
public class SshViewLogServiceImpl implements IViewLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SshViewLogServiceImpl.class);

    @Autowired
    private SystemConfig systemConfig;


    @Override
    public List<LogInfo> getLogInfos(String logPaths) {
        List<LogInfo> logInfos = new ArrayList<>();
        if (logPaths != null) {
            String[] ps = logPaths.split(",");
            for (String p : ps) {
                LogInfo info = LogInfo.getFromLogPath(p, systemConfig.httpPath);
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
            f.setDownloadUrl((systemConfig.httpPath + "/download?ip=" + ip + "&path=" + LogUtil.urlEncode(subPath)));
            f.setOpenUrl(systemConfig.httpPath + "/open?ip=" + ip + "&path=" + LogUtil.urlEncode(subPath));
            f.setRealTimeLogUrl(systemConfig.httpPath + "/do?ip=" + ip + "&code=1&path=" + LogUtil.urlEncode(subPath));
            f.setLatestNumLogUrl(systemConfig.httpPath + "/do?ip=" + ip + "&code=2&path=" + LogUtil.urlEncode(subPath));
            f.setSearchLogUrl(systemConfig.httpPath + "/do?ip=" + ip + "&code=3&path=" + LogUtil.urlEncode(subPath));
            f.setSearchGzipLogUrl(systemConfig.httpPath + "/do?ip=" + ip + "&code=4&path=" + LogUtil.urlEncode(subPath));
            f.setFileIcon(LogUtil.getIcon(f));
            f.setLogFile(LogUtil.isLogFile(f));
        }
        return fileInfos;
    }

    @Override
    public String readFile(String path) {
        String log = "";
        try {
            if (path != null && path.length() > 0) {
                File file = new File(path);
                if (file.exists() && file.isFile() && file.length() > 0) {
                    if (LogUtil.isInScope(file.lastModified())) {
                        byte[] bs = Files.readAllBytes(Paths.get(path));
                        log = new String(bs);
                    } else {
                        log = "该文件超过访问范围，不允许访问";
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("", ex);
        }
        return log;
    }

    @Override
    public boolean verifyCmd(String cmd) {
        if (cmd != null && cmd.length() > 0) {
            if (cmd.startsWith("gunzip -dc")) {
                return true;
            }
            if (cmd.startsWith("tail") || cmd.startsWith("grep")) {
                String[] cs = cmd.split(" ");
                String filePath = cs[cs.length - 1];
                List<LogInfo> logInfos = getLogInfos(systemConfig.logPaths);
                if (logInfos.stream().anyMatch(f -> filePath.startsWith(f.getLogPath()))) {
                    File file = new File(filePath);
                    if (file.exists() && file.isFile() && LogUtil.isInScope(file.lastModified())) {
                        LOGGER.info("将要用命令访问文件：{}", filePath);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public LinkedHashMap<String, List<LogMenu>> getMenuItemList() {
        LinkedHashMap<String, List<LogMenu>> menuMap = new LinkedHashMap<>();
        if (systemConfig.logMenu != null) {
            String[] logs = systemConfig.logMenu.split(",");
            for (String log : logs) {
                String[] items = log.split("\\|");
                if (items.length == 3) {
                    items[0] = items[0].trim();
                    if (menuMap.containsKey(items[0])) {
                        menuMap.get(items[0]).add(new LogMenu(items[1], items[2]));
                    } else {
                        List<LogMenu> temp = new ArrayList<>();
                        temp.add(new LogMenu(items[1], items[2]));
                        menuMap.put(items[0], temp);
                    }
                }
            }
        }
        return menuMap;
    }
}
