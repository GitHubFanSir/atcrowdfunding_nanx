package com.atnanx.atcrowdfunding.core.bean;

public class TResource {
    private Integer id;

    private String url;

    private String createat;

    private String updateat;

    public TResource(Integer id, String url, String createat, String updateat) {
        this.id = id;
        this.url = url;
        this.createat = createat;
        this.updateat = updateat;
    }

    public TResource() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getCreateat() {
        return createat;
    }

    public void setCreateat(String createat) {
        this.createat = createat == null ? null : createat.trim();
    }

    public String getUpdateat() {
        return updateat;
    }

    public void setUpdateat(String updateat) {
        this.updateat = updateat == null ? null : updateat.trim();
    }
}