package com.codeisrun.viewlog.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
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

    public static String doCmdAndGetStr(String serverIp, String user, String pwd, String cmd, String path) {
        InputStream inputStream = doCmd(serverIp, user, pwd, cmd, path);
        if (inputStream == null) {
            return null;
        }
        String result = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        log.info("\n执行命令：{}\n响应：{}", cmd, result);
        return result;
    }

    public static InputStream doCmd(String serverIp, String user, String pwd, String cmd, String path) {
        if (serverIp == null) {
            return doCmd(cmd, path);
        } else {
            //执行远程命令
            //sshpass -p root ssh root@192.168.0.8 "ll"
            return doCmd(String.format("sshpass -p %s ssh -o StrictHostKeyChecking=no %s@%s \"%s\"", user, pwd, serverIp, cmd), path);
        }
    }

    private static InputStream doCmd(String cmd, String path) {
        //log.info("执行命令：{}", cmd);
        if (verifyCmd(cmd, path)) {
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
            }
        }
        return null;
    }

    public static boolean verifyCmd(String cmd, String path) {
        if (cmd != null && cmd.length() > 0) {

        }
        return true;
    }
}
