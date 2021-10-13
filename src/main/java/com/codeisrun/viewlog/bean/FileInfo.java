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

    public String genDirUrl(String path) {
        if (this.isDirectory) {

        }
        return null;
    }

    public static List<FileInfo> getListByStr(String str) {
        List<FileInfo> list = new ArrayList<>();
        if (str != null) {
            String[] fs = str.split("\n");
            if (fs.length > 1) {
                for (int i = 1; i < fs.length; i++) {
                    String f = fs[i];
                    String[] items = f.split(" ");
                    if (items.length == 8) {
                        FileInfo info = FileInfo.builder()
                                .name(items[7])
                                .size(items[4])
                                .modifyTime(items[5] + " " + items[6])
                                .build();
                        list.add(info);
                    }
                }
            }
        }
        return list;
    }
}
