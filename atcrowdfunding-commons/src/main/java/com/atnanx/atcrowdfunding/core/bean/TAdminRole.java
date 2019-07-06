package com.atnanx.atcrowdfunding.core.bean;

public class TAdminRole {
    private Integer id;

    private Integer adminid;

    private Integer roleid;

    public TAdminRole(Integer id, Integer adminid, Integer roleid) {
        this.id = id;
        this.adminid = adminid;
        this.roleid = roleid;
    }

    public TAdminRole() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdminid() {
        return adminid;
    }

    public void setAdminid(Integer adminid) {
        this.adminid = adminid;
    }

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }
}