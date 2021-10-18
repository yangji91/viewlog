package com.codeisrun.viewlog.util;

import com.codeisrun.viewlog.common.CmdEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * 执行命令都在这里
 *
 * @author liubinqiang
 * @date 2021-10-12
 */
@Slf4j
public class LinuxUtil {

    public static String doCmdAndGetStr(String serverIp, String user, String pwd, String cmd, String path) throws Exception {
        InputStream inputStream = doCmd(serverIp, user, pwd, cmd, path);
        if (inputStream == null) {
            return null;
        }
        String result = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        log.info("\n执行命令：{}\n响应：{}", cmd, result);
        return result;
    }

    public static InputStream doCmd(String serverIp, String user, String pwd, String cmd, String path) throws Exception {
        if (!verifyCmd(cmd)) {
            log.error("不支持该命令：ip={},cmd={}", serverIp, cmd);
            throw new Exception("不支持该命令");
        }
        if (serverIp == null) {
            return doCmd(cmd, path);
        } else {
            //执行远程命令
            //sshpass -p root ssh root@192.168.0.8 "ll"
            return doCmd(String.format("sshpass -p %s ssh -o StrictHostKeyChecking=no %s@%s \"%s\"", pwd, user, serverIp, cmd), path);
        }
    }

    private static InputStream doCmd(String cmd, String path) throws IOException {
        //log.info("执行命令：{}", cmd);
        String[] commands = {"/bin/sh", "-c", cmd};
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(commands);
            //log.info("process.waitFor={}", process.waitFor());
            //在执行实时日志的时候不能打印
            //String errorMsg = new BufferedReader(new InputStreamReader(process.getErrorStream())).lines().collect(Collectors.joining("\n"));
            //log.info("执行命令是否有错误信息：{}", errorMsg);
            InputStream inputStream = process.getInputStream();
            return inputStream;
        } catch (Exception e) {
            log.error("执行命令报错：", e);
            throw e;
        }
    }

    public static boolean verifyCmd(String cmd) {
        if (cmd != null && cmd.length() > 0) {
            for (CmdEnum cmdEnum : CmdEnum.values()) {
                if (cmd.startsWith(cmdEnum.getCmdHeader())) {
                    return true;
                }
            }
        }
        return false;
    }
}
