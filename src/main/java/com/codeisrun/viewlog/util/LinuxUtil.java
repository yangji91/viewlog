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
        if (!verifyCmd(cmd)) {
            log.error("不支持该命令：ip={},cmd={}", serverIp, cmd);
            throw new Exception("不支持该命令");
        }
        if (serverIp != null && serverIp.length() > 0) {
            //执行远程命令
            //sshpass -p root ssh root@192.168.0.8 "ll"
            cmd = String.format("sshpass -p %s ssh -o StrictHostKeyChecking=no %s@%s \"%s\"", pwd, user, serverIp, cmd);
        }
        //log.info("执行命令：{}", cmd);
        String[] commands = {"/bin/sh", "-c", cmd};
        Process process = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            process = Runtime.getRuntime().exec(commands);
            //log.info("process.waitFor={}", process.waitFor());
            //在执行实时日志的时候不能打印
            //String errorMsg = new BufferedReader(new InputStreamReader(process.getErrorStream())).lines().collect(Collectors.joining("\n"));
            //log.info("执行命令是否有错误信息：{}", errorMsg);
            inputStream = process.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String result = bufferedReader.lines().collect(Collectors.joining("\n"));
            log.info("\n执行命令：{}\n响应：{}", cmd, result);
            return result;
        } catch (Exception e) {
            log.error("执行命令报错：", e);
            throw e;
        } finally {
            if (inputStreamReader != null) {
                log.info("释放资源inputStreamReader.hashCode={}", inputStreamReader.hashCode());
                inputStreamReader.close();
            }
            if (bufferedReader != null) {
                log.info("释放资源bufferedReader.hashCode={}", bufferedReader.hashCode());
                bufferedReader.close();
            }
            if (inputStream != null) {
                log.info("释放资源inputStream.hashCode={}", inputStream.hashCode());
                inputStream.close();
            }
            if (process != null) {
                log.info("释放资源process.hashCode={}", process.hashCode());
                process.destroy();
            }
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
