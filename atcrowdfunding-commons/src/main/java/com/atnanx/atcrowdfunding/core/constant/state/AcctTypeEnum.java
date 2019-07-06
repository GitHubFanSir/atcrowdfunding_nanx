package com.atnanx.atcrowdfunding.core.constant.state;

import org.apache.commons.lang3.StringUtils;

public enum AcctTypeEnum {
    ////账户类型: 0 - 企业， 1 - 个体， 2 - 个人， 3 - 政府
    COMPANTY("0","企业"),
    PERSON_COMPANY("1","个体"),
    PERSON("2","个人"),
    GOV("3","政府");

    //模板CODE
    private String code;
    //模板名称
    private String msg;


    AcctTypeEnum(String code, String msg) {
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
        for (AcctTypeEnum value : AcctTypeEnum.values()) {
            if (value.getCode().equals(code)){
                return value.getMsg();
            }
        }
        throw new RuntimeException("未找到匹配参数");
    }
}
