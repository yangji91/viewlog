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
    TAIL_F("TAIL_F", "tail -f "),

    /**
     * 查看日志尾n行日志
     * 完整命令：tail -200 /home/logs/gc.log
     */
    TAIL_N("TAIL_N", "tail -"),

    /**
     * 搜索日志
     * 完整命令：grep -C 5 key /home/logs/gc.log
     */
    GREP_C("GREP_C", "grep -C "),

    /**
     * 搜索压缩文件日志
     * 完整命令：gunzip -dc /home/logs/catalina.out.tar.gz | grep -aC 5 key
     */
    GZIP_DC("GZIP_DC", "gunzip -dc "),

    /**
     * 列出日志文件信息
     * 完整命令：ls -lht --time-style '+%Y-%m-%d %H:%M:%S' /home/logs
     */
    LS("LS", "ls -lht --time-style '+%Y-%m-%d %H:%M:%S' "),

    /**
     * 查看日志头n行日志
     * 完整命令：tail -200 /home/logs/gc.log
     */
    HEAD_N("HEAD_N", "head -"),

    /**
     * 压缩文件日志-查看头
     * 完整命令：gunzip -dc /home/logs/catalina.out.tar.gz | head -10
     */
    GZIP_DC_HEAD("GZIP_DC_HEAD", "gunzip -dc "),

    /**
     * 压缩文件日志-查看尾
     * 完整命令：gunzip -dc /home/logs/catalina.out.tar.gz | tail -10
     */
    GZIP_DC_TAIL("GZIP_DC_TAIL", "gunzip -dc "),

    /**
     * 搜索日志，只返回相关行
     * 完整命令：grep ParNew gc.log |tail -n 10
     */
    GREP("GREP", "grep ");

    private String code;
    private String cmdHeader;

    CmdEnum(String code, String cmdHeader) {
        this.code = code;
        this.cmdHeader = cmdHeader;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCmdHeader() {
        return cmdHeader;
    }

    public void setCmdHeader(String cmdHeader) {
        this.cmdHeader = cmdHeader;
    }

    public static CmdEnum getByCode(String code) {
        for (CmdEnum t : CmdEnum.values()) {
            if (t.code.equals(code)) {
                return t;
            }
        }
        return null;
    }

    public static boolean isRealTimeCmd(String cmd) {
        return cmd != null && cmd.startsWith(TAIL_F.getCmdHeader());
    }


    public static String getCmd(String code, String path, String key, String length) {
        String result = "";
        CmdEnum cmdEnum = CmdEnum.getByCode(code);
        if (cmdEnum != null) {
            switch (cmdEnum) {
                case TAIL_F:
                    result = TAIL_F.getCmdHeader() + path;
                    break;
                case TAIL_N:
                    result = TAIL_N.getCmdHeader() + length + " " + path;
                    break;
                case HEAD_N:
                    result = HEAD_N.getCmdHeader() + length + " " + path;
                    break;
                case GREP_C:
                    result = GREP_C.getCmdHeader() + length + " " + getQuotationMark(key) + key + getQuotationMark(key) + " " + path;
                    break;
                case GZIP_DC:
                    result = GZIP_DC.getCmdHeader() + path + " | grep -aC " + length + " " + getQuotationMark(key) + key + getQuotationMark(key);
                    break;
                case GZIP_DC_HEAD:
                    result = GZIP_DC.getCmdHeader() + path + " | head -" + length;
                    break;
                case GZIP_DC_TAIL:
                    result = GZIP_DC.getCmdHeader() + path + " | tail -" + length;
                    break;
                default:
            }
        }
        return result;
    }

    private static final String QUOTATION_SINGLE = "'";
    private static final String QUOTATION_DOUBLE = "\"";

    public static String getQuotationMark(String key) {
        if (key != null && key.contains("'")) {
            return QUOTATION_DOUBLE;
        } else {
            return QUOTATION_SINGLE;
        }
    }

}
