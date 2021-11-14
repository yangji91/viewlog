package com.codeisrun.viewlog.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liubinqiang
 * @date 2021-11-8
 */
@Data
public class GcResult {
    private String beginTime;
    private String endTime;
    private float beginRunTime;
    private float endRunTime;
    private String runTime;
    private BigDecimal totalRealTime = new BigDecimal(0);
    private List<GcRecord> gcRecordList;

    public BigDecimal getTotalRealTime() {
        if (gcRecordList != null && !gcRecordList.isEmpty()) {
            for (GcRecord gcRecord : gcRecordList) {
                if (gcRecord.getUsedTime1() != null) {
                    totalRealTime = totalRealTime.add(new BigDecimal(gcRecord.getUsedTime1()));
                }
            }
        }
        return totalRealTime;
    }
}
