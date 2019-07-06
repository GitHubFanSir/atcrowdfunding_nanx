package com.atnanx.atcrowdfunding.core.constant.state;

import org.apache.commons.lang3.StringUtils;

/**
 * 项目状态
 * @author lfy
 *
 */
public enum ProjectStatusEnum {
	
	DRAFT("0","草稿"),
	SUBMIT_AUTH("1","提交审核申请"),
	AUTHING("2","后台正在审核"),
	AUTHED("3","后台审核通过"),
	AUTHFAIL("4","审核失败");
	
	private String code;
	private String status;


	ProjectStatusEnum(String code, String status) {
		this.code = code;
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public String getStatus() {
		return status;
	}

	public static String getProjectStatus(String code){
		if (StringUtils.isBlank(code))
			throw new RuntimeException("您的参数有误");
		for (ProjectStatusEnum projectStatusEnum : ProjectStatusEnum.values()) {
			if (projectStatusEnum.getCode().equals(code)){
				return projectStatusEnum.getStatus();
			}
		}
		throw new RuntimeException("未找到匹配参数");
	}

}
