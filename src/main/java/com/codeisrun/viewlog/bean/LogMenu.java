package com.codeisrun.viewlog.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author liubinqiang
 * @date 2021-9-28
 */
@Data
public class LogMenu {
    public LogMenu() {
    }

    public LogMenu(String name, String url) {
        if (name != null) {
            this.name = name.trim();
        }
        if (url != null) {
            this.url = url.trim();
        }
    }

    private String name;
    private String url;
}
