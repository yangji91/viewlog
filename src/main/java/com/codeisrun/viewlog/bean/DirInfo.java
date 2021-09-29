package com.codeisrun.viewlog.bean;

import lombok.Data;

/**
 * @author liubinqiang
 */
@Data
public class DirInfo {
    public DirInfo() {
    }

    public DirInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }

    private String name;
    private String url;
}
