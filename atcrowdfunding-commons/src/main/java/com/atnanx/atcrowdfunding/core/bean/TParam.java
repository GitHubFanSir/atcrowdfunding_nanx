package com.atnanx.atcrowdfunding.core.bean;

public class TParam {
    private Integer id;

    private String name;

    private String code;

    private String val;

    public TParam(Integer id, String name, String code, String val) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.val = val;
    }

    public TParam() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val == null ? null : val.trim();
    }
}