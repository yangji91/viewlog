package com.codeisrun.viewlog.bean;

import com.codeisrun.viewlog.common.CmdEnum;
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
                case GZIP_DC:
                    //gzip -dc catalina.out.2021-10-02-01-40.tar.gz | grep -a -C 1 'Exception'
                    result = "gunzip -dc " + this.path + "| grep -a -C " + this.length + " " + this.key;
                    break;
            }
        }
        return result;
    }
}
