package com.codeisrun.viewlog.bean;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志文件信息
 *
 * @author liubinqiang
 */
@Data
@Builder
public class FileInfo {
    private String path;
    private String name;
    private String modifyTime;
    private String size;
    private boolean isDirectory;
    private boolean isLogFile;
    private String dirUrl;
    private String openUrl;
    private String downloadUrl;
    /**
     * 实时日志地址
     */
    private String realTimeLogUrl;

    /**
     * 最近多少条日志
     */
    private String latestNumLogUrl;
    private String fileIcon;
    private String searchLogUrl;
    private String searchGzipLogUrl;


    public static ProjectFileInfo getListByStr(ProjectFileInfo projectFileInfo, String str) {
        List<FileInfo> list = new ArrayList<>();
        if (str != null) {
            String[] fs = str.split("\n");
            projectFileInfo.setTotalSize(fs[0]);
            for (int i = 1; i < fs.length; i++) {
                String f = fs[i];
                //多个空格替换为一个空格
                f = f.replaceAll(" +", " ");
                String[] items = f.split(" ");
                if (items.length == 8) {
                    //-普通文件 d目录
                    boolean isDir = items[0].startsWith("d");
                    FileInfo info = FileInfo.builder()
                            .isDirectory(isDir)
                            .name(items[7])
                            .size(items[4])
                            .modifyTime(items[5] + " " + items[6])
                            .build();
                    list.add(info);
                }
            }
        }
        projectFileInfo.setFileInfoList(list);
        return projectFileInfo;
    }
}
