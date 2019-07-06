package com.atnanx.atcrowdfunding.core.bean;

import com.google.common.collect.Lists;

import java.util.List;

public class TTag {
    private Integer id;

    private Integer pid;

    private String name;

    private List<TTag> childTagList= Lists.newArrayList();

    public TTag(Integer id, Integer pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    public List<TTag> getChildTagList() {
        return childTagList;
    }

    public void setChildTagList(List<TTag> childTagList) {
        this.childTagList = childTagList;
    }

    public TTag() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}