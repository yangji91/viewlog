package com.ronfton.viewlog.bean;

import com.ronfton.viewlog.util.LogUtil;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author liubinqiang
 */
public class LogFileFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        //判断文件名称
/*        boolean isLog = name.endsWith(".log") || name.endsWith(".out") || name.endsWith(".txt");
        if (isLog) {*/
        //判断文件修改时间
        File file = new File(dir + File.separator + name);
        if (file.exists()) {
            return LogUtil.isInScope(file.lastModified());
        }
        //}
        return false;

    }
}
