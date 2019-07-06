package com.atnanx.atcrowdfunding.core.bean;

import java.util.Date;

public class TMemberPhoneCode {
    private Integer id;

    private String phone;

    private String code;

    private String type;

    private Date gmtCreate;

    private Date gmtUpdate;

    public TMemberPhoneCode(Integer id, String phone, String code, String type, Date gmtCreate, Date gmtUpdate) {
        this.id = id;
        this.phone = phone;
        this.code = code;
        this.type = type;
        this.gmtCreate = gmtCreate;
        this.gmtUpdate = gmtUpdate;
    }

    public TMemberPhoneCode() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtUpdate() {
        return gmtUpdate;
    }

    public void setGmtUpdate(Date gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
    }
}