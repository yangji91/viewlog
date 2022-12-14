package com.codeisrun.viewlog.controller;

import com.alibaba.fastjson.JSON;
import com.codeisrun.viewlog.bean.*;
import com.codeisrun.viewlog.config.SystemConfig;
import com.codeisrun.viewlog.service.IViewLogService;
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
import java.util.Set;

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
    private IViewLogService viewLogService;

    /**
     * 项目目录页面
     *
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("index")
    public String indexSearch(HttpServletRequest request, ModelMap modelMap) {
        log.info("访问日志搜索首页收到请求");
        Set<ProjectGroup> groupList = viewLogService.getProjectGroupList();
        modelMap.put("groupList", groupList);
        return "index";
    }

    /**
     * 项目目录页面
     *
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping(path = {"", "/menu"})
    public String indexMenu(HttpServletRequest request, ModelMap modelMap) {
        long a = System.currentTimeMillis();
        log.info("访问日志首页收到请求");
        List<Project> logs = viewLogService.getProjectList();
        log.info(JSON.toJSONString(logs));
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
    public String logInfo(HttpServletRequest request, ModelMap modelMap, int projectId, int nodeId, String ip, String path) {
        ProjectFileInfo fs = viewLogService.getFileInfos(projectId, nodeId, ip, path);
        modelMap.put("fs", fs);
        Project project = viewLogService.getProject(projectId);
        modelMap.put("name", project == null ? "" : project.getProjectName());
        modelMap.put("ip", ip);
        modelMap.put("nodeId", nodeId);
        modelMap.put("logSearchViewLines", systemConfig.logSearchViewLines);
        return "fileInfo";
    }

    /**
     * gc日志分析
     *
     * @param modelMap
     * @param projectId
     * @param ip
     * @param path
     * @return
     */
    @RequestMapping("/gcInfo")
    public String gcInfo(ModelMap modelMap, int projectId, String ip, String path) {
        GcResult gcResult = viewLogService.analyseGcLog(ip, path);
        modelMap.put("gc", gcResult);
        Project project = viewLogService.getProject(projectId);
        modelMap.put("name", project == null ? "" : project.getProjectName());
        modelMap.put("ip", ip);
        return "gcInfo";
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
                .append("&cmd=").append(req.getCode())
                .append("&path=").append(req.getPath())
                .append("&key=").append(req.getKey())
                .append("&length=").append(req.getLength());
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
