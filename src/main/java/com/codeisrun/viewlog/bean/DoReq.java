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
    private String code;
    private String path;
    private int length;
    private String key;

}
