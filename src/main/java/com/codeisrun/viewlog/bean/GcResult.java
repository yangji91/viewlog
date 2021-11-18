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
    private List<GcRecord> gcRecordList;

    private float beginRunTime = 0;
    private float endRunTime = 0;
    private float runTime = 0;

    private int youngGcCount = 0;
    private float youngGcFrequency;
    private float youngGcStopWorldTime = 0;

    private int oldGcCount = 0;
    private float oldGcFrequency;
    private float oldGcStopWorldTime = 0;
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

    public float getOldGcFrequency() {
        return getRunTime() / getOldGcCount();
    }


    public float getTotalStopWorldTime() {
        if (gcRecordList != null && !gcRecordList.isEmpty()) {
            for (GcRecord gcRecord : gcRecordList) {
                if (gcRecord.getUsedTime1() != null) {
                    totalStopWorldTime = totalStopWorldTime + Float.parseFloat(gcRecord.getUsedTime1());
                }
            }
        }
        return totalStopWorldTime;
    }

    public float getYoungGcStopWorldTime() {
        for (GcRecord gcRecord : gcRecordList) {
            if (GcLogType.ParNew.equals(gcRecord.getGcType()) && gcRecord.getUsedTime1() != null) {
                youngGcStopWorldTime = youngGcStopWorldTime + Float.parseFloat(gcRecord.getUsedTime1());
            }
        }
        return youngGcStopWorldTime;
    }

    public float getOldGcStopWorldTime() {
        for (GcRecord gcRecord : gcRecordList) {
            if ((GcLogType.CMSInitialMark.equals(gcRecord.getGcType()) || GcLogType.CMSFinalRemark.equals(gcRecord.getGcType())) && gcRecord.getUsedTime1() != null) {
                oldGcStopWorldTime = oldGcStopWorldTime + Float.parseFloat(gcRecord.getUsedTime1());
            }
        }
        return oldGcStopWorldTime;
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
