package com.atnanx.atcrowdfunding.core.bean;

public class TProjectInitiator {
    private Integer id;

    private Integer projectid;

    //自我介绍
    private String selfintroduction;
    //详细的自我介绍
    private String detailselfintroduction;
    //自己的电话
    private String telphone;
    //客服电话
    private String hotline;

    public TProjectInitiator(Integer id, Integer projectid, String selfintroduction, String detailselfintroduction, String telphone, String hotline) {
        this.id = id;
        this.projectid = projectid;
        this.selfintroduction = selfintroduction;
        this.detailselfintroduction = detailselfintroduction;
        this.telphone = telphone;
        this.hotline = hotline;
    }

    public TProjectInitiator() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public String getSelfintroduction() {
        return selfintroduction;
    }

    public void setSelfintroduction(String selfintroduction) {
        this.selfintroduction = selfintroduction == null ? null : selfintroduction.trim();
    }

    public String getDetailselfintroduction() {
        return detailselfintroduction;
    }

    public void setDetailselfintroduction(String detailselfintroduction) {
        this.detailselfintroduction = detailselfintroduction == null ? null : detailselfintroduction.trim();
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone == null ? null : telphone.trim();
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline == null ? null : hotline.trim();
    }
}