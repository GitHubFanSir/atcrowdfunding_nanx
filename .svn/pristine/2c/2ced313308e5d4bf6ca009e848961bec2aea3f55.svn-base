package com.atnanx.atcrowdfunding.core.constant.state;

import org.apache.commons.lang3.StringUtils;

public enum AuthStatusEnum {

    /*
    * 实名认证状态 0 - 未实名认证， 1 - 实名认证申请中， 2 - 已实名认证*/
    UNAUTH("0","未实名认证"),
    AUTHING("1","实名认证中"),
    AUTHED("2","已实名认证");

    //模板CODE
    private String code;
    //模板名称
    private String msg;


    AuthStatusEnum(String code, String msg) {
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
        for (AuthStatusEnum value : AuthStatusEnum.values()) {
            if (value.getCode().equals(code)){
                return value.getMsg();
            }
        }
        throw new RuntimeException("未找到匹配参数");
    }
}
