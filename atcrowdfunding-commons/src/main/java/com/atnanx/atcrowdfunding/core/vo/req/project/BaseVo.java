package com.atnanx.atcrowdfunding.core.vo.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class BaseVo {

    //临时项目令牌，用于获取前端追加的是哪个工程的内容， projectid容易被他人知晓接口信息，从而追加错误信息
    @ApiModelProperty(value = "项目临时令牌", required = true)
    protected String projectToken;

    //用户访问令牌
    @ApiModelProperty(value = "访问令牌", required = true)
    protected String accessToken;
}
