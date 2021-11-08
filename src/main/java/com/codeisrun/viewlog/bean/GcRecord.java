package com.codeisrun.viewlog.bean;

import lombok.Data;

import java.math.BigDecimal;

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

    //2021-11-08T12:11:31.648+0800: 337913.669: [GC (Allocation Failure) 2021-11-08T12:11:31.648+0800: 337913.669: [ParNew: 183669K->15209K(184320K), 0.0421268 secs] 316232K->148783K(503808K), 0.0422622 secs] [Times: user=0.15 sys=0.00, real=0.04 secs]
    public GcRecord(String str) {
        if (str != null) {
            String[] items = str.split(": ");
            if (items.length == 7) {
                dateTime = items[0];
                runTime = new BigDecimal(items[1]);
                gcType = GcType.ParNew;
                name = items[2];
            }
        }
    }

    private String dateTime;
    private BigDecimal runTime;
    private GcType gcType;
    private String name;
    private BigDecimal realTime;


}
