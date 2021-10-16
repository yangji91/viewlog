package com.codeisrun.viewlog.controller;

import com.codeisrun.viewlog.bean.DoReq;
import com.codeisrun.viewlog.config.SystemConfig;
import com.codeisrun.viewlog.service.IViewLogService;
import com.codeisrun.viewlog.bean.FileInfo;
import com.codeisrun.viewlog.bean.Project;
import com.codeisrun.viewlog.util.LogUtil;
import com.codeisrun.viewlog.util.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
@Controller
@RequestMapping("/viewlog")
public class LogController {

    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private IViewLogService sshViewLogService;

    /**
     * 主页
     *
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping(path = {"", "/menu"})
    public String index(HttpServletRequest request, ModelMap modelMap) {
        long a = System.currentTimeMillis();
        log.info("访问日志首页收到请求");
        List<Project> logs = sshViewLogService.getLogInfos();
        modelMap.put("logs", logs);
        log.info("访问日志首页返回，耗时：{}", System.currentTimeMillis() - a);
        return "menu";
    }

    /**
     * 项目日志文件信息
     *
     * @param request
     * @param modelMap
     * @param path
     * @return
     */
    @RequestMapping("/info")
    public String logInfo(HttpServletRequest request, ModelMap modelMap, String ip, String path) {
        List<FileInfo> fs = sshViewLogService.getFileInfos(ip, path);
        modelMap.put("fs", fs);
        modelMap.put("path", LogUtil.getPathHierarchy(path));
        return "fileInfo";
    }

    /**
     * 执行命令，前端页面发送websocket连接，把参数传给后端，后端执行命令，前端通过websocket接收日志
     *
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("/do")
    public String viewDo(HttpServletRequest request, ModelMap modelMap, DoReq req) {
        StringBuilder sb = new StringBuilder();
        sb.append(systemConfig.wsPath)
                .append("?ip=").append(req.getIp())
                .append("&cmd=").append(req.getCmd())
                .append("&path=").append(req.getPath());
        String wsUrl = sb.toString();
        log.info("ws地址：{}", wsUrl);
        modelMap.put("wsUrl", wsUrl);
        modelMap.put("key", req.getKey());
        modelMap.put("wsPort", systemConfig.wsPort);
        return "viewDo";
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
                if (!LogUtil.isCompressFile(file.getName())) {
                    path = path + ".zip";
                    FileOutputStream fos1 = new FileOutputStream(new File(path));
                    List<File> fs = new ArrayList<>();
                    fs.add(file);
                    ZipUtil.toZip(fs, fos1);
                }
                //下载
                FileSystemResource fileSystemResource = new FileSystemResource(path);
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
            log.error("", ex);
            return null;
        }
    }


}
