package com.codeisrun.viewlog.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;

/**
 * @author liubinqiang
 * @date 2021-11-8
 */
@Data
public class GcResult {
    /**
     * gc记录
     */
    private List<GcRecord> gcRecordList;
    /**
     * 开始统计时间
     */
    private float beginRunTime = 0;
    /**
     * 结束统计时间
     */
    private float endRunTime = 0;
    /**
     * 统计时间长度
     */
    private float runTime = 0;
    /**
     * 年轻代gc次数
     */
    private int youngGcCount = 0;
    /**
     * 年轻代gc频率
     */
    private float youngGcFrequency;
    /**
     * 年轻代gc总停顿时间
     */
    private float youngGcTotalStopWorldTime = 0;
    /**
     * 年轻代gc最大一次停顿时间
     */
    private float youngGcMaxStopWorldTime = 0;
    /**
     * 老年代gc次数
     */
    private int oldGcCount = 0;
    /**
     * 老年代gc频率
     */
    private float oldGcFrequency;
    /**
     * 老年代gc总停顿时间
     */
    private float oldGcTotalStopWorldTime = 0;
    /**
     * 老年代gc最大一次停顿时间
     */
    private float oldGcMaxStopWorldTime = 0;
    /**
     * 停顿总时间
     */
    private float totalStopWorldTime = 0;
    /**
     * 吞吐量
     */
    private String throughput;


    public float getBeginRunTime() {
        return gcRecordList.stream().map(GcRecord::getRunTime).min(Comparator.comparing(Float::floatValue)).orElse(0F);
    }

    public float getEndRunTime() {
        return gcRecordList.stream().map(GcRecord::getRunTime).max(Comparator.comparing(Float::floatValue)).orElse(0F);
    }

    public float getRunTime() {
        return getEndRunTime() - getBeginRunTime();
    }

    public int getYoungGcCount() {
        return (int) gcRecordList.stream().filter(g -> GcLogType.ParNew.equals(g.getGcType())).count();
    }

    public int getOldGcCount() {
        return (int) gcRecordList.stream().filter(g -> GcLogType.CMSInitialMark.equals(g.getGcType())).count();
    }

    public float getYoungGcFrequency() {
        return getRunTime() / getYoungGcCount();
    }

    public float getYoungGcMaxStopWorldTime() {
        return getGcRecordList().stream().filter(g -> GcLogType.ParNew.equals(g.getGcType()))
                .map(GcRecord::getStopWorldTime)
                .max(Comparator.comparing(Float::floatValue)).orElse(0f);
    }

    public float getOldGcMaxStopWorldTime() {
        return getGcRecordList().stream().filter(g -> GcLogType.CMSInitialMark.equals(g.getGcType()) || GcLogType.CMSFinalRemark.equals(g.getGcType()))
                .map(GcRecord::getStopWorldTime)
                .max(Comparator.comparing(Float::floatValue)).orElse(0f);
    }

    public float getOldGcFrequency() {
        return getRunTime() / getOldGcCount();
    }


    public float getTotalStopWorldTime() {
        if (gcRecordList != null && !gcRecordList.isEmpty()) {
            for (GcRecord gcRecord : gcRecordList) {
                totalStopWorldTime = totalStopWorldTime + gcRecord.getStopWorldTime();
            }
        }
        return totalStopWorldTime;
    }

    public float getYoungGcTotalStopWorldTime() {
        for (GcRecord gcRecord : gcRecordList) {
            if (GcLogType.ParNew.equals(gcRecord.getGcType())) {
                youngGcTotalStopWorldTime = youngGcTotalStopWorldTime + gcRecord.getStopWorldTime();
            }
        }
        return youngGcTotalStopWorldTime;
    }

    public float getOldGcTotalStopWorldTime() {
        for (GcRecord gcRecord : gcRecordList) {
            if ((GcLogType.CMSInitialMark.equals(gcRecord.getGcType()) || GcLogType.CMSFinalRemark.equals(gcRecord.getGcType()))) {
                oldGcTotalStopWorldTime = oldGcTotalStopWorldTime + gcRecord.getStopWorldTime();
            }
        }
        return oldGcTotalStopWorldTime;
    }


    public String getThroughput() {
        BigDecimal a = new BigDecimal(getTotalStopWorldTime());
        BigDecimal b = new BigDecimal(getRunTime());
        BigDecimal c = b.subtract(a);
        BigDecimal d = c.divide(b, 12, BigDecimal.ROUND_HALF_UP);
        d = d.multiply(new BigDecimal(100));
        DecimalFormat format = new DecimalFormat("0.0000000000");
        return format.format(d) + "%";
    }

}
