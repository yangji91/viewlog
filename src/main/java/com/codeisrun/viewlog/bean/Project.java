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

    public static Project getFromLogPath(String logPath, int projectId) {
        if (logPath != null && logPath.length() > 0) {
            String[] ls = logPath.split("\\|");
            if (ls.length == 3) {
                Project project = new Project();
                project.setProjectId(projectId);
                project.setGroupName(ls[0].trim());
                project.setName(ls[1].trim());
                project.setNodeList(ProjectNode.genList(ls[2], projectId));
                return project;
            }
        }
        return null;
    }

    /**
     * 项目id
     */
    private int projectId;
    /**
     * 分组名称
     */
    private String groupName;
    /**
     * 项目名称
     */
    private String name;
    private List<ProjectNode> nodeList;

    public String getGroupName() {
        return groupName == null ? "" : groupName;
    }

    public String getName() {
        return name == null ? "" : name;
    }


    /**
     * 项目实例
     */
    @Data
    public static class ProjectNode {
        public static List<ProjectNode> genList(String config, int projectId) {
            List<ProjectNode> list = new ArrayList<>();
            if (config != null) {
                String[] nodes = config.split("\\^");
                int currentNodeId = 1;
                for (String node : nodes) {
                    if (node != null) {
                        String[] items = node.split("@");
                        if (items.length == 2) {
                            ProjectNode info = new ProjectNode();
                            info.setNodeId(currentNodeId);
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
                            info.viewFileInfoUrl = String.format("/viewlog/info?projectId=%s&nodeId=%s&ip=%s&path=%s",
                                    projectId, currentNodeId, info.getIp(), LogUtil.urlEncode(info.getLogPath()));
                            list.add(info);
                            currentNodeId = currentNodeId + 1;
                        }
                    }
                }
            }
            return list;
        }

        /**
         * 节点id
         */
        private int nodeId;
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

