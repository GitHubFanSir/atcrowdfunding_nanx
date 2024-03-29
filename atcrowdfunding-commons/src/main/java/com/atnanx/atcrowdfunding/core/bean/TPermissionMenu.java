package com.atnanx.atcrowdfunding.core.bean;

public class TPermissionMenu {
    private Integer id;

    private Integer menuid;

    private Integer permissionid;

    public TPermissionMenu(Integer id, Integer menuid, Integer permissionid) {
        this.id = id;
        this.menuid = menuid;
        this.permissionid = permissionid;
    }

    public TPermissionMenu() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMenuid() {
        return menuid;
    }

    public void setMenuid(Integer menuid) {
        this.menuid = menuid;
    }

    public Integer getPermissionid() {
        return permissionid;
    }

    public void setPermissionid(Integer permissionid) {
        this.permissionid = permissionid;
    }
}