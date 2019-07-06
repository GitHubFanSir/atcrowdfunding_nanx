package com.atnanx.atcrowdfunding.core.constant.state;

import org.apache.commons.lang3.StringUtils;

public enum UserTypeEnum {
    PERSONAL("0","个人"),
    COMPANY("1","企业");

    //模板CODE
    private String code;
    //模板名称
    private String msg;


    UserTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static String getMsg(String code){
        if (StringUtils.isBlank(code))
            throw new RuntimeException("您的参数有误");
        for (UserTypeEnum value : UserTypeEnum.values()) {
            if (value.getCode().equals(code)){
                return value.getMsg();
            }
        }
        throw new RuntimeException("未找到匹配的TemplateCode");
    }
}
