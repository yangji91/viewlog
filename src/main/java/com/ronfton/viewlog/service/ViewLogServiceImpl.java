package com.ronfton.viewlog.service;

import com.ronfton.viewlog.bean.FileInfo;
import com.ronfton.viewlog.bean.LogFileFilter;
import com.ronfton.viewlog.bean.LogInfo;
import com.ronfton.viewlog.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Value("${log-path}")
    private String logPaths;

    @Value("${http-path}")
    private String httpPath;


    @Override
    public List<LogInfo> getLogInfos(String logPaths) {
        List<LogInfo> logInfos = new ArrayList<>();
        if (logPaths != null) {
            String[] ps = logPaths.split(",");
            for (String p : ps) {
                if (p != null && p.length() > 0) {
                    String[] ls = p.split("\\|");
                    if (ls.length == 2) {
                        LogInfo info = new LogInfo(ls[0], ls[1], httpPath);
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
                                    .size(Util.bytesToView(log.length()))
                                    .isFile(log.isFile())
                                    .modifyTime(Util.timespanToDateStr(log.lastModified()))
                                    .downloadUrl(httpPath + "/download?path=" + Util.urlEncode(log.getCanonicalPath()))
                                    .openUrl(httpPath + "/open?path=" + Util.urlEncode(log.getCanonicalPath()))
                                    .realTimeLogUrl(httpPath + "/do?cmd=tail -f " + Util.urlEncode(log.getCanonicalPath()))
                                    .latestNumLogUrl(httpPath + "/do?cmd=tail -200 " + Util.urlEncode(log.getCanonicalPath()))
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
                    if (Util.isInScope(file.lastModified())) {
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
            if (cmd.startsWith("tail")) {
                String[] cs = cmd.split(" ");
                String filePath = cs[cs.length - 1];
                List<LogInfo> logInfos = getLogInfos(logPaths);
                if (logInfos.stream().anyMatch(f -> filePath.startsWith(f.getLogPath()))) {
                    File file = new File(filePath);
                    if (file.exists() && file.isFile() && Util.isInScope(file.lastModified())) {
                        LOGGER.info("将要用命令访问文件：{}", filePath);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
