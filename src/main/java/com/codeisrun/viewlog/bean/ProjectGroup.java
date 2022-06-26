package com.codeisrun.viewlog.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目分组
 *
 * @author liubinqiang
 * @date 2022-4-21
 */
@Data
public class ProjectGroup {
	public ProjectGroup() {
		this.setProjectList(new ArrayList<>());
	}

	private String groupName;
	private List<Project> projectList;
}
