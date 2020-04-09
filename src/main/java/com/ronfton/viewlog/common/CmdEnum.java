package com.ronfton.viewlog.common;

/**
 * @author liubinqiang
 */
public enum CmdEnum {
    TAIL_F(1),
    TAIL_N(2),
    GREP(3);

    private int code;


    CmdEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static CmdEnum getByCode(int code) {
        for (CmdEnum t : CmdEnum.values()) {
            if (t.code == code) {
                return t;
            }
        }
        return null;
    }

}
