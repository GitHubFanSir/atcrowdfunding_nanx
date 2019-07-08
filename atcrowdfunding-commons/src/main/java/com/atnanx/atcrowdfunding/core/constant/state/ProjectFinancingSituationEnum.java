package com.atnanx.atcrowdfunding.core.constant.state;

import org.apache.commons.lang3.StringUtils;

/**
 * 项目状态
 * @author lfy
 *
 */
public enum ProjectFinancingSituationEnum {
	/*
	* 0 - 即将开始， 1 - 众筹中， 2 - 众筹成功， 3 - 众筹失败(因为筹资天数和发布日期可以计算出结束时间)*/
	ABOUTTOBEGIN("0","即将开始"),
	RAISEOF("1","众筹中"),
	RAISESUCCESS("2","众筹成功"),
	RAISEFAILED("3","众筹失败");

	private String code;
	private String status;


	ProjectFinancingSituationEnum(String code, String status) {
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
		for (ProjectFinancingSituationEnum projectStatusEnum : ProjectFinancingSituationEnum.values()) {
			if (projectStatusEnum.getCode().equals(code)){
				return projectStatusEnum.getStatus();
			}
		}
		throw new RuntimeException("未找到匹配参数");
	}

}
