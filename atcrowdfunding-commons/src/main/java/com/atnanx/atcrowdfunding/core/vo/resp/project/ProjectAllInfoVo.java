package com.atnanx.atcrowdfunding.core.vo.resp.project;


import com.atnanx.atcrowdfunding.core.bean.TProject;

public class ProjectAllInfoVo extends TProject {
    private String imgurl;

    private String typeName;
    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
