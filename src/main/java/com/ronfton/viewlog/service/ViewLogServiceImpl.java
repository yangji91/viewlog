package com.ronfton.viewlog.service;

import com.ronfton.viewlog.bean.FileInfo;
import com.ronfton.viewlog.bean.LogFileFilter;
import com.ronfton.viewlog.bean.LogInfo;
import com.ronfton.viewlog.config.SystemConfig;
import com.ronfton.viewlog.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liubinqiang
 */
@Service
public class ViewLogServiceImpl implements IViewLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewLogServiceImpl.class);

    @Autowired
    private SystemConfig systemConfig;

    @Override
    public List<LogInfo> getLogInfos(String logPaths) {
        List<LogInfo> logInfos = new ArrayList<>();
        if (logPaths != null) {
            String[] ps = logPaths.split(",");
            for (String p : ps) {
                if (p != null && p.length() > 0) {
                    String[] ls = p.split("\\|");
                    if (ls.length == 2) {
                        LogInfo info = new LogInfo(ls[0], ls[1], systemConfig.httpPath);
                        logInfos.add(info);
                    }
                }
            }
        }
        return logInfos;
    }

    @Override
    public List<FileInfo> getFileInfos(String path) {
        List<FileInfo> fs = new ArrayList<>();
        try {
            if (path != null && path.length() > 0) {
                File file = new File(path);
                if (file.isDirectory() && file.listFiles() != null && file.listFiles().length > 0) {
                    File[] logs = file.listFiles(new LogFileFilter());
                    if (logs.length > 0) {
                        for (File log : logs) {
                            FileInfo info = FileInfo.builder()
                                    .name(log.getName())
                                    .path(log.getCanonicalPath())
                                    .size(LogUtil.getPathSizeAndFileCount(log.getCanonicalPath()))
                                    .isDirectory(log.isDirectory())
                                    .dirUrl(log.isDirectory() ? (LogUtil.getInfoUrl(log.getCanonicalPath())) : null)
                                    .modifyTime(LogUtil.timespanToDateStr(log.lastModified()))
                                    .downloadUrl(systemConfig.httpPath + "/download?path=" + LogUtil.urlEncode(log.getCanonicalPath()))
                                    .openUrl(systemConfig.httpPath + "/open?path=" + LogUtil.urlEncode(log.getCanonicalPath()))
                                    .realTimeLogUrl(systemConfig.httpPath + "/do?code=1&path=" + LogUtil.urlEncode(log.getCanonicalPath()))
                                    .latestNumLogUrl(systemConfig.httpPath + "/do?code=2&path=" + LogUtil.urlEncode(log.getCanonicalPath()))
                                    .searchLogUrl(systemConfig.httpPath + "/do?code=3&path=" + LogUtil.urlEncode(log.getCanonicalPath()))
                                    .fileIcon(LogUtil.getIcon(log))
                                    .build();
                            fs.add(info);
                        }
                        fs = fs.stream().sorted(Comparator.comparing(FileInfo::getModifyTime)).collect(Collectors.toList());
                        Collections.reverse(fs);
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("", ex);
        }
        return fs;
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
}
