package com.codeisrun.viewlog.common;

/**
 * 命令类型，所有用到的命令，都定义在这里
 *
 * @author liubinqiang
 */
public enum CmdEnum {


    /**
     * 实时日志
     * 完整命令：tail -f /home/logs/gc.log
     */
    TAIL_F(1, "tail -f "),

    /**
     * 查看日志尾n行日志
     * 完整命令：tail -200 /home/logs/gc.log
     */
    TAIL_N(2, "tail -"),

    /**
     * 搜索日志
     * 完整命令：grep -C 5 key /home/logs/gc.log
     */
    GREP(3, "grep -C "),

    /**
     * 搜索压缩文件日志
     * 完整命令：gunzip -dc /home/logs/catalina.out.tar.gz | grep -aC 5 key
     */
    GZIP_DC(4, "gunzip -dc "),

    /**
     * 列出日志文件信息
     * 完整命令：ls -lht --time-style '+%Y-%m-%d %H:%M:%S' /home/logs
     */
    LS(5, "ls -lht --time-style '+%Y-%m-%d %H:%M:%S' "),

    /**
     * 查看日志头n行日志
     * 完整命令：tail -200 /home/logs/gc.log
     */
    HEAD_N(6, "head -"),

    /**
     * 压缩文件日志-查看头
     * 完整命令：gunzip -dc /home/logs/catalina.out.tar.gz | head -10
     */
    GZIP_DC_HEAD(7, "gunzip -dc "),

    /**
     * 压缩文件日志-查看尾
     * 完整命令：gunzip -dc /home/logs/catalina.out.tar.gz | tail -10
     */
    GZIP_DC_TAIL(8, "gunzip -dc ");

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

    public static boolean isRealTimeCmd(String cmd) {
        return cmd != null && cmd.startsWith(TAIL_F.getCmdHeader());
    }

}
