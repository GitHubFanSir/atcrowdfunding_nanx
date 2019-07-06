package com.atnanx.atcrowdfunding.core.vo.resp.project;

import com.atnanx.atcrowdfunding.core.vo.req.project.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ProjectReturnDetailVo extends BaseVo {

    @ApiModelProperty(value = "回报id", required = true)
    private String id;
    @ApiModelProperty(value = "项目ID", required = true)
    private String projectid;
    @ApiModelProperty(value = "项目 名称", required = true)
    private String projectName;
    @ApiModelProperty(value = "项目简介", required = true)
    private String projectRemark;


    @ApiModelProperty(value = "0 - 实物回报， 1 虚拟物品回报", required = true)
    private String type;

    @ApiModelProperty(value = "支持金额", required = true)
    private Integer supportmoney;

    @ApiModelProperty(value = "回报内容", required = true)
    private String content;

    @ApiModelProperty(value = "0 为不限回报数量", required = true)
    private Integer count;

    @ApiModelProperty(value = "单笔限购", required = true)
    private Integer signalpurchase;

    @ApiModelProperty(value = "限购数量", required = true)
    private Integer purchase;

    @ApiModelProperty(value = "运费", required = true)
    private Integer freight;

    @ApiModelProperty(value = "0 - 不开发票， 1 - 开发票", required = true)
    private String invoice;

    @ApiModelProperty(value = "回报时间,众筹成功后多少天进行回报", required = true)
    private Integer rtndate;
}
