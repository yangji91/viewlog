package com.ronfton.viewlog.controller;

import com.ronfton.viewlog.bean.FileInfo;
import com.ronfton.viewlog.bean.LogInfo;
import com.ronfton.viewlog.service.IViewLogService;
import com.ronfton.viewlog.util.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liubinqiang
 */
@Controller
@RequestMapping("${http-path}")
public class HomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Value("${log-path}")
    private String logPaths;
    @Value("${netty-websocket.port}")
    private String wsPort;
    @Value("${netty-websocket.path}")
    private String wsPath;
    @Value("${ws-url}")
    private String wsUrl;

    @Autowired
    private IViewLogService viewLogService;


    /**
     * 主页
     *
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("")
    public String index(HttpServletRequest request, ModelMap modelMap) {
        List<LogInfo> logs = viewLogService.getLogInfos(logPaths);
        modelMap.put("logs", logs);
        return "index";
    }


    /**
     * 日志目录
     *
     * @param request
     * @param modelMap
     * @param path
     * @return
     */
    @RequestMapping("/info")
    public String logInfo(HttpServletRequest request, ModelMap modelMap, String path) {
        List<FileInfo> fs = viewLogService.getFileInfos(path);
        modelMap.put("fs", fs);
        modelMap.put("path", path);
        return "fileInfo";
    }

    /**
     * 浏览器打开日志
     *
     * @param request
     * @param modelMap
     * @param path
     * @return
     */
    @RequestMapping(value = "/open", produces = MediaType.TEXT_HTML_VALUE)
    public String open(HttpServletRequest request, ModelMap modelMap, String path) {
        String log = viewLogService.readFile(path);
        modelMap.put("log", log);
        modelMap.put("path", path);
        return "viewLog";
    }

    /**
     * 下载日志文件
     *
     * @param request
     * @param path
     * @return
     */
    @RequestMapping("/download")
    private ResponseEntity<InputStreamResource> downloadFile(HttpServletRequest request, String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                String zipPath = path + ".zip";
                FileOutputStream fos1 = new FileOutputStream(new File(zipPath));
                List<File> fs = new ArrayList<>();
                fs.add(file);
                ZipUtil.toZip(fs, fos1);
                FileSystemResource fileSystemResource = new FileSystemResource(zipPath);
                String userAgent = request.getHeader("User-Agent");
                String oraFileName = fileSystemResource.getFilename();
                String formFileName = oraFileName;

                // 针对IE或者以IE为内核的浏览器：
                if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                    formFileName = java.net.URLEncoder.encode(formFileName, "UTF-8");
                } else {
                    // 非IE浏览器的处理：
                    formFileName = new String(formFileName.getBytes("UTF-8"), "ISO-8859-1");
                }

                HttpHeaders headers = new HttpHeaders();
                headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", formFileName));
                headers.add("Pragma", "no-cache");
                headers.add("Expires", "0");
                return ResponseEntity
                        .ok()
                        .headers(headers)
                        .contentLength(fileSystemResource.contentLength())
                        .contentType(MediaType.parseMediaType("application/octet-stream"))
                        .body(new InputStreamResource(fileSystemResource.getInputStream()));
            } else {
                return null;
            }
        } catch (Exception ex) {
            LOGGER.error("", ex);
            return null;
        }
    }


    /**
     * 执行命令
     *
     * @param request
     * @param modelMap
     * @param cmd
     * @return
     */
    @RequestMapping("/do")
    public String viewDo(HttpServletRequest request, ModelMap modelMap, String cmd) {
        StringBuilder sb = new StringBuilder();
        sb.append(wsUrl).append("?cmd=");
        if (cmd != null) {
            sb.append(cmd);
        }
        String wsUrl = sb.toString();
        LOGGER.info("ws地址：{}", wsUrl);
        modelMap.put("wsUrl", wsUrl);
        return "viewDo";
    }
}
