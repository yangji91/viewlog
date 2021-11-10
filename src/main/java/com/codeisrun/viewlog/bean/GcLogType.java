package com.codeisrun.viewlog.bean;

/**
 * 垃圾收集器类型
 *
 * @author liubinqiang
 * @date 2021-11-8
 */
public interface GcLogType {
    String ParNew = "ParNew";
    String CMS1 = "CMS Initial Mark";
    String CMS2 = "CMS Final Remark";
}
