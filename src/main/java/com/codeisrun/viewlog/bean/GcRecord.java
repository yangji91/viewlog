package com.codeisrun.viewlog.bean;

import com.codeisrun.viewlog.util.DataUtil;
import lombok.Data;

import java.util.regex.Matcher;

import static com.codeisrun.viewlog.bean.GcLogType.*;

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

    public static GcRecord genRecord(Matcher matcher, GcLogType gcLogType) {
        GcRecord gcRecord = null;
        switch (gcLogType) {
            case ParNew:
                gcRecord = genParNewRecord(matcher);
                break;
            case CMSInitialMark:
                gcRecord = genCMSInitialMarkRecord(matcher);
                break;
            case CMSFinalRemark:
                gcRecord = genCMSFinalRemarkRecord(matcher);
                break;
            default:
        }
        return gcRecord;
    }

    public static GcRecord genParNewRecord(Matcher matcher) {
        GcRecord gcRecord = new GcRecord();
        gcRecord.setGcType(ParNew);
        gcRecord.setDateTime(matcher.group(1));
        gcRecord.setRunTime(Float.parseFloat(matcher.group(2)));
        gcRecord.setGcReason(matcher.group(3));
        gcRecord.setYoungUsedSize(matcher.group(4));
        gcRecord.setYoungAfterGcUsed(matcher.group(5));
        gcRecord.setYoungTotalSize(matcher.group(6));
        gcRecord.setUsedTime1(matcher.group(7));
        gcRecord.setHeapUsedSize(matcher.group(8));
        gcRecord.setHeapAfterGcUsed(matcher.group(9));
        gcRecord.setHeapTotalSize(matcher.group(10));
        gcRecord.setUsedTime2(matcher.group(11));
        return gcRecord;
    }

    public static GcRecord genCMSInitialMarkRecord(Matcher matcher) {
        GcRecord gcRecord = new GcRecord();
        gcRecord.setGcType(CMSInitialMark);
        gcRecord.setDateTime(matcher.group(1));
        gcRecord.setRunTime(Float.parseFloat(matcher.group(2)));
        gcRecord.setGcReason(matcher.group(3));
        gcRecord.setYoungUsedSize(matcher.group(4));
        gcRecord.setYoungAfterGcUsed(matcher.group(5));
        gcRecord.setYoungTotalSize(matcher.group(6));
        gcRecord.setUsedTime1(matcher.group(7));
        return gcRecord;
    }

    public static GcRecord genCMSFinalRemarkRecord(Matcher matcher) {
        GcRecord gcRecord = new GcRecord();
        gcRecord.setGcType(CMSFinalRemark);
        gcRecord.setDateTime(matcher.group(1));
        gcRecord.setRunTime(Float.parseFloat(matcher.group(2)));
        gcRecord.setOldAfterGcUsed(matcher.group(3));
        gcRecord.setOldTotalSize(matcher.group(4));
        gcRecord.setHeapAfterGcUsed(matcher.group(5));
        gcRecord.setHeapTotalSize(matcher.group(6));
        gcRecord.setUsedTime1(matcher.group(7));
        return gcRecord;
    }

    private Integer id;
    private GcLogType gcType;


    private String dateTime;
    private float runTime;
    private String gcReason;
    private String youngUsedSize;
    private String youngAfterGcUsed;
    private String youngTotalSize;
    private String oldUsedSize;
    private String oldAfterGcUsed;
    private String oldTotalSize;
    private String usedTime1;
    private String heapUsedSize;
    private String heapAfterGcUsed;
    private String heapTotalSize;
    private String usedTime2;

    private float intervalTime;


    public String getYoungUsedSize() {
        return DataUtil.kbToMb(youngUsedSize);
    }

    public String getYoungUsedSizeRate() {
        return DataUtil.rate(youngUsedSize, youngTotalSize);
    }

    public String getYoungAfterGcUsed() {
        return DataUtil.kbToMb(youngAfterGcUsed);
    }

    public String getYoungAfterGcUsedRate() {
        return DataUtil.rate(youngAfterGcUsed, youngTotalSize);
    }

    public String getYoungAfterGcUsedUpRate() {
        return DataUtil.upRate(youngUsedSize, youngAfterGcUsed, youngTotalSize);
    }

    public String getYoungTotalSize() {
        return DataUtil.kbToMb(youngTotalSize);
    }

    public String getHeapUsedSize() {
        return DataUtil.kbToMb(heapUsedSize);
    }

    public String getHeapUsedSizeRate() {
        return DataUtil.rate(heapUsedSize, youngAfterGcUsed);
    }

    public String getHeapAfterGcUsed() {
        return DataUtil.kbToMb(heapAfterGcUsed);
    }

    public String getHeapTotalSize() {
        return DataUtil.kbToMb(heapTotalSize);
    }


    public void setDateTime(String dateTime) {
        if (dateTime != null) {
            dateTime = dateTime.replace("T", " ");
        }
        this.dateTime = dateTime;
    }

    public static String getGcReg(GcLogType gcLogType) {
        String reg = null;
        switch (gcLogType) {
            case ParNew:
                reg = getParNewReg();
                break;
            case CMSInitialMark:
                reg = getCMSInitialMarkReg();
                break;
            case CMSFinalRemark:
                reg = getCMSFinalRemarkReg();
                break;
            default:
        }
        return reg;
    }

    private static String getCMSInitialMarkReg() {
        //2021-11-10T14:10:06.695+0800: 517828.715: [GC (CMS Initial Mark) [1 CMS-initial-mark: 216631K(319488K)] 312340K(503808K), 0.0318128 secs] [Times: user=0.12 sys=0.00, real=0.03 secs]
        StringBuilder sb = new StringBuilder();
        sb.append("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3})").append("[\\+\\-\\d:]{5}: ");
        sb.append("(\\d{1,}.\\d{1,})").append(": \\[GC \\(CMS Initial Mark\\) \\[1 CMS-initial-mark: ");
        sb.append("(\\d{1,})").append("K\\(");
        sb.append("(\\d{1,})").append("K\\)\\] ");
        sb.append("(\\d{1,})").append("K\\(");
        sb.append("(\\d{1,})").append("K\\), ");
        sb.append("([\\d.]{1,})").append(" secs\\]");
        return sb.toString();
    }

    private static String getCMSFinalRemarkReg() {
        StringBuilder sb = new StringBuilder();
        sb.append("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3})").append("[\\+\\-\\d:]{5}: ");
        sb.append("(\\d{1,}.\\d{1,})").append(": \\[GC \\(CMS Final Remark\\) ").append("[\\s\\S]*");
        sb.append("\\[1 CMS-remark: ").append("(\\d{1,})").append("K\\(");
        sb.append("(\\d{1,})").append("K\\)\\] ");
        sb.append("(\\d{1,})").append("K\\(");
        sb.append("(\\d{1,})").append("K\\), ");
        sb.append("([\\d.]{1,})").append(" secs\\]");
        return sb.toString();
    }

    private static String getParNewReg() {
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
