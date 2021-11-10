package com.codeisrun.viewlog.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.regex.Matcher;

/**
 * 垃圾回收记录
 *
 * @author liubinqiang
 * @date 2021-11-8
 */
@Data
public class GcRecord {
    public GcRecord() {
    }

    public GcRecord(Matcher matcher) {
        this.dateTime = matcher.group(1);
        this.runTime = matcher.group(2);
        this.gcReason = matcher.group(3);
        this.youngUsedSize = matcher.group(4);
        this.youngAfterGcUsed = matcher.group(5);
        this.youngTotalSize = matcher.group(6);
        this.usedTime1 = matcher.group(7);
        this.heapUsedSize = matcher.group(8);
        this.heapAfterGcUsed = matcher.group(9);
        this.heapTotalSize = matcher.group(10);
        this.usedTime2 = matcher.group(11);
    }

    private Integer id;
    private GcType gcType;

    private String dateTime;
    private String runTime;
    private String gcReason;
    private String youngUsedSize;
    private String youngAfterGcUsed;
    private String youngTotalSize;
    private String usedTime1;
    private String heapUsedSize;
    private String heapAfterGcUsed;
    private String heapTotalSize;
    private String usedTime2;


    public static String getParNewReg() {
        //String s = "2021-11-08T12:11:31.648+0800: 337913.669: [GC (Allocation Failure) 2021-11-08T12:11:31.648+0800: 337913.669: [ParNew: 183669K->15209K(184320K), 0.0421268 secs] 316232K->148783K(503808K), 0.0422622 secs] [Times: user=0.15 sys=0.00, real=0.04 secs]";
        StringBuilder sb = new StringBuilder();
        //1：匹配-时间
        sb.append("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3})").append("[\\+\\-\\d:]{5}: ");
        //2：匹配-运行秒数
        sb.append("(\\d{1,}.\\d{1,})").append(": \\[GC \\(");
        //3：匹配-gc原因
        sb.append("([\\w\\s]{1,})").append("\\)").append("[\\s\\S]*").append("\\[ParNew: ");
        //4：匹配-年轻代-已用空间
        sb.append("(\\d{1,})").append("K->");
        //5：匹配-年轻代-gc后已用空间
        sb.append("(\\d{1,})").append("K\\(");
        //6：匹配-年轻代-总空间
        sb.append("(\\d{1,})").append("K\\), ");
        //7：匹配-耗时
        sb.append("([\\d.]{1,})").append(" secs\\] ");
        //8：匹配-堆-已用空间
        sb.append("(\\d{1,})").append("K->");
        //9：匹配-堆-gc后已用空间
        sb.append("(\\d{1,})").append("K\\(");
        //10：匹配-堆-总大小
        sb.append("(\\d{1,})").append("K\\), ");
        //11：匹配-耗时
        sb.append("([\\d.]{1,})").append("[\\s\\S]*");
        return sb.toString();
    }

}
