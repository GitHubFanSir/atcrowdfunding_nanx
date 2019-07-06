package com.atnanx.atcrowdfunding.core.vo.resp.project;

import com.atnanx.atcrowdfunding.core.bean.TProject;
import com.atnanx.atcrowdfunding.core.bean.TReturn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAllAllInfoVo extends TProject {

    private String headerImage;//头图
    private List<String> detailImages;//详情图
    private String memberName;//发布者的名字
    private List<TReturn> returns;//所有的档位信息
    private Integer timeLife;//剩余时间
}
