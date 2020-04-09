package com.ronfton.viewlog.bean;

import com.ronfton.viewlog.common.CmdEnum;
import lombok.Data;

/**
 * @author liubinqiang
 */
@Data
public class DoReq {
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
                    result = "tail -f " + this.path;
                    break;
                case TAIL_N:
                    result = "tail -" + this.length + " " + this.path;
                    break;
                case GREP:
                    result = "grep -C " + this.length + " " + this.key + " " + this.path;
                    break;
            }
        }
        return result;
    }
}
