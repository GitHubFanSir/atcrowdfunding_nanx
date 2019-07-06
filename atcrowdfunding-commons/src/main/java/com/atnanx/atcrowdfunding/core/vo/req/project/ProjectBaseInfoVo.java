package com.atnanx.atcrowdfunding.core.vo.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class ProjectBaseInfoVo extends BaseVo {

    /**
     *   第一屏的信息
     */

    /**
     * type和tag 关联表
     */
    //项目的分类id
    @ApiModelProperty(value = "项目的分类id", required = true)
    private List<Integer> typeids;
    //项目的标签id
    @ApiModelProperty(value = "项目的标签id", required = true)
    private List<Integer> tagids;

    /**
     * project表相关信息
     */
    //项目名称
    @ApiModelProperty(value = "项目名称", required = true)
    private String name;
    //项目简介
    @ApiModelProperty(value = "项目简介", required = true)
    private String remark;
    //筹资金额
    @ApiModelProperty(value = "筹资金额", required = true)
    private Long money;
    //筹资天数
    @ApiModelProperty(value = "筹资天数", required = true,example = "1")
    private Integer day;

    /**
     * t_project_images图片表相关信息
     */
    //项目头部图片
    @ApiModelProperty(value = "项目头部图片", required = true)
    private String headerImage;
    //项目详情图片
    @ApiModelProperty(value = "项目详情图片", required = true)
    private List<String> detailsImage;

    /**
     * t_project_initiator表内容
     */
    @ApiModelProperty(value = "自我介绍")
    private String selfintroduction;
    @ApiModelProperty(value = "详细自我介绍")
    private String detailselfintroduction;
    @ApiModelProperty(value = "联系电话", required = true)
    private String telphone;
    @ApiModelProperty(value = "客服电话", required = true)
    private String hotline;
 /*   //发布日期
    @ApiModelProperty(value = "发布日期", required = true)
    private String deploydate;
      //创建日期
    @ApiModelProperty(value = "创建日期", required = true)
    String createdate;
      //支持金额
    @ApiModelProperty(value = "支持金额", required = true)
    private Long supportmoney;
    //支持者数量
    @ApiModelProperty(value = "支持者数量", required = true)
    private Integer supporter;
     //完成度
    @ApiModelProperty(value = "完成度", required = true)
    private Short completion;
     //关注者数量
    @ApiModelProperty(value = "关注者数量", required = true)
    private Integer follower;
    */

}
