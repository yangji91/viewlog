package com.codeisrun.viewlog.bean;

import com.codeisrun.viewlog.util.LogUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目信息
 *
 * @author liubinqiang
 */
@Data
public class Project {
    public Project() {
    }

    public static Project getFromLogPath(String logPath) {
        if (logPath != null && logPath.length() > 0) {
            String[] ls = logPath.split("\\|");
            if (ls.length == 3) {
                Project info = new Project();
                info.setGroupName(ls[0]);
                info.setName(ls[1]);
                info.setNodeList(ProjectNode.genList(ls[2], info.getName()));
                return info;
            }
        }
        return null;
    }

    /**
     * 分组名称
     */
    private String groupName;
    /**
     * 项目名称
     */
    private String name;
    private List<ProjectNode> nodeList;


    @Data
    public static class ProjectNode {
        public static List<ProjectNode> genList(String config, String name) {
            List<ProjectNode> list = new ArrayList<>();
            if (config != null) {
                String[] nodes = config.split("\\^");
                for (String node : nodes) {
                    if (node != null) {
                        String[] items = node.split("@");
                        if (items.length == 2) {
                            ProjectNode info = new ProjectNode();
                            if (items[0] != null) {
                                String[] envIp = items[0].split("-");
                                if (envIp.length > 0) {
                                    info.setEnv(envIp[0]);
                                }
                                if (envIp.length > 1) {
                                    info.setIp(envIp[1]);
                                }
                            }
                            info.setLogPath(items[1].trim());
                            info.viewFileInfoUrl = String.format("/viewlog/info?ip=%s&path=%s&name=%s", info.getIp(), LogUtil.urlEncode(info.getLogPath()), name);
                            list.add(info);
                        }
                    }
                }
            }
            return list;
        }

        /**
         * 哪个环境
         */
        private String env;

        /**
         * 服务器地址
         */
        private String ip;
        /**
         * 项目日志路径
         */
        private String logPath;
        /**
         * 查看所有日志文件地址
         */
        private String viewFileInfoUrl;

        public String getIp() {
            return ip == null ? "" : ip;
        }

        public String getViewIp() {
            if (ip != null && ip.length() > 0) {
                return ip;
            } else {
                return "本机";
            }
        }
    }

}

