package com.atnanx.atcrowdfunding.core.bean;

public class TPermissionResource {
    private Integer id;

    private Integer resourceid;

    private Integer permissionid;

    public TPermissionResource(Integer id, Integer resourceid, Integer permissionid) {
        this.id = id;
        this.resourceid = resourceid;
        this.permissionid = permissionid;
    }

    public TPermissionResource() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResourceid() {
        return resourceid;
    }

    public void setResourceid(Integer resourceid) {
        this.resourceid = resourceid;
    }

    public Integer getPermissionid() {
        return permissionid;
    }

    public void setPermissionid(Integer permissionid) {
        this.permissionid = permissionid;
    }
}