package com.codeisrun.viewlog.bean;

/**
 * 垃圾收集器类型
 *
 * @author liubinqiang
 * @date 2021-11-8
 */
public enum GcLogType {

    ParNew("ParNew", "ParNew: "),

    CMSInitialMark("ParNew", "CMS Initial Mark"),

    CMSFinalRemark("ParNew", "CMS Final Remark");

    private GcLogType(String code, String logKey) {
        this.code = code;
        this.logKey = logKey;
    }

    /**
     * gc日志类型
     */
    public String code;
    /**
     * 搜索该日志时候的key
     */
    public String logKey;
}
