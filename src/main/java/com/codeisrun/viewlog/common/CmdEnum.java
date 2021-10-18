package com.codeisrun.viewlog.common;

/**
 * 命令类型，所有用到的命令，都定义在这里
 *
 * @author liubinqiang
 */
public enum CmdEnum {

    TAIL_F(1, "tail -f ", "实时日志"),
    TAIL_N(2, "tail -", "最新n行日志"),
    GREP(3, "grep -C ", "搜索日志"),
    GZIP_DC(4, "gunzip -dc ", "搜索压缩文件日志"),
    LS(5, "ls -lht --time-style '+%Y-%m-%d %H:%M:%S' ", "列出日志文件信息");

    private Integer code;
    private String cmdHeader;
    private String info;

    CmdEnum(int code, String cmdHeader, String info) {
        this.code = code;
        this.cmdHeader = cmdHeader;
        this.info = info;
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
