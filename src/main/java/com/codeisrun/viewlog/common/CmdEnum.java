package com.codeisrun.viewlog.common;

/**
 * 命令类型，所有用到的命令，都定义在这里
 *
 * @author liubinqiang
 */
public enum CmdEnum {
    TAIL_F(1, "tail -f "),
    TAIL_N(2, "tail -"),
    GREP(3, "grep -C "),
    GZIP_DC(4, "gunzip -dc "),
    LS(5, "ls -lh --time-style '+%Y-%m-%d %H:%M:%S' ");

    private Integer code;
    private String cmdHeader;

    CmdEnum(int code, String cmdHeader) {
        this.code = code;
        this.cmdHeader = cmdHeader;
    }

    public int getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getCmdHeader() {
        return cmdHeader;
    }

    public void setCmdHeader(String cmdHeader) {
        this.cmdHeader = cmdHeader;
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
