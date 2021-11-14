package com.codeisrun.viewlog.bean;

import com.codeisrun.viewlog.util.DataUtil;
import lombok.Data;

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

    public static GcRecord genRecord(Matcher matcher, String gcLogType) {
        GcRecord gcRecord = null;
        switch (gcLogType) {
            case GcLogType.ParNew:
                gcRecord = genParNewRecord(matcher);
                break;
            case GcLogType.CMS1:
                gcRecord = genCms1Record(matcher);
                break;
            case GcLogType.CMS2:
                gcRecord = genCms2Record(matcher);
                break;
            default:
        }
        return gcRecord;
    }

    public static GcRecord genParNewRecord(Matcher matcher) {
        GcRecord gcRecord = new GcRecord();
        gcRecord.setGcType(GcLogType.ParNew);
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

    public static GcRecord genCms1Record(Matcher matcher) {
        GcRecord gcRecord = new GcRecord();
        gcRecord.setGcType(GcLogType.CMS1);
        gcRecord.setDateTime(matcher.group(1));
/*        gcRecord.setRunTime(matcher.group(2));
        gcRecord.setGcReason(matcher.group(3));
        gcRecord.setYoungUsedSize(matcher.group(4));
        gcRecord.setYoungAfterGcUsed(matcher.group(5));
        gcRecord.setYoungTotalSize(matcher.group(6));
        gcRecord.setUsedTime1(matcher.group(7));*/
        return gcRecord;
    }

    public static GcRecord genCms2Record(Matcher matcher) {
        GcRecord gcRecord = new GcRecord();
        gcRecord.setGcType(GcLogType.CMS2);
        gcRecord.setDateTime(matcher.group(1));
//        gcRecord.setRunTime(matcher.group(2));
        return gcRecord;
    }

    private Integer id;
    private String gcType;


    private String dateTime;
    private float runTime;
    private String gcReason;
    private String youngUsedSize;
    private String youngAfterGcUsed;
    private String youngTotalSize;
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

    public static String getGcReg(String gcLogType) {
        String reg = null;
        switch (gcLogType) {
            case GcLogType.ParNew:
                reg = getParNewReg();
                break;
            case GcLogType.CMS1:
                reg = getCms1Reg();
                break;
            case GcLogType.CMS2:
                reg = getCms2Reg();
                break;
            default:
        }
        return reg;
    }

    private static String getCms1Reg() {
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

    private static String getCms2Reg() {
        StringBuilder sb = new StringBuilder();
        sb.append("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3})").append("[\\+\\-\\d:]{5}: ");
        sb.append("(\\d{1,}.\\d{1,})").append(": \\[GC \\(CMS Final Remark\\) ");
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
