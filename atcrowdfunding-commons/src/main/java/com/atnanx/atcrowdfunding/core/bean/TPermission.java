package com.atnanx.atcrowdfunding.core.bean;

public class TPermission {
    private Integer id;

    private String name;

    private String title;

    private String icon;

    private Integer pid;

    public TPermission(Integer id, String name, String title, String icon, Integer pid) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.icon = icon;
        this.pid = pid;
    }

    public TPermission() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}