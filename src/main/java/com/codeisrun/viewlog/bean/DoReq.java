package com.codeisrun.viewlog.bean;

import com.codeisrun.viewlog.common.CmdEnum;
import lombok.Data;

import static com.codeisrun.viewlog.common.CmdEnum.*;

/**
 * 执行命令请求
 *
 * @author liubinqiang
 */
@Data
public class DoReq {
    private String ip;
    private int code;
    private String path;
    private int length;
    private String key;

    public String getCmd() {
        String result = "";
        CmdEnum cmdEnum = CmdEnum.getByCode(this.code);
        if (cmdEnum != null) {
            switch (cmdEnum) {
                case TAIL_F:
                    result = TAIL_F.getCmdHeader() + this.path;
                    break;
                case TAIL_N:
                    result = TAIL_N.getCmdHeader() + this.length + " " + this.path;
                    break;
                case GREP:
                    result = GREP.getCmdHeader() + this.length + " " + this.key + " " + this.path;
                    break;
                case GZIP_DC:
                    result = GZIP_DC.getCmdHeader() + this.path + " | grep -aC " + this.length + " " + this.key;
                    break;
            }
        }
        return result;
    }
}
