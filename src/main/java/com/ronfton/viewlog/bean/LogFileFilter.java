package com.ronfton.viewlog.bean;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author liubinqiang
 */
public class LogFileFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        boolean isLog = name.endsWith(".log") || name.endsWith(".out") || name.endsWith(".txt");
        return isLog;
    }
}
