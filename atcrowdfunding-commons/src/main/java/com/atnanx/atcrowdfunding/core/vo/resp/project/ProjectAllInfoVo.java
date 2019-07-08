package com.atnanx.atcrowdfunding.core.vo.resp.project;


import com.atnanx.atcrowdfunding.core.bean.TProject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAllInfoVo extends TProject {
    private String imgurl;

    private String typeName;

    private String typeRemark;
}
