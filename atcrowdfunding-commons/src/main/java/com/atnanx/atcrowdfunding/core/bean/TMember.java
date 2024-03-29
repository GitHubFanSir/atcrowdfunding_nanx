package com.atnanx.atcrowdfunding.core.bean;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *  @ApiModel:描述返回的对象
 *
 *  每一个功能页面只用部分数据，为他单独抽取vo；
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TMember {

    @ApiModelProperty("会员的id")
    private Integer id;

    @ApiModelProperty(value = "会员的账号",required = true)
    private String loginacct;

    @ApiModelProperty(value = "密码",required = true)
    private String userpswd;

    @ApiModelProperty("会员的昵称")
    private String username;

    @ApiModelProperty(value="邮箱地址",required = true)
    private String email;

    @ApiModelProperty(value="实名认证状态",required = true)
    private String authstatus;

    @ApiModelProperty(value="用户类型",required = true)
    private String usertype;

    @ApiModelProperty(value="真实名称",required = true)
    private String realname;

    @ApiModelProperty(value="身份证号码",required = true)
    private String cardnum;

    @ApiModelProperty(value="账户类型",required = true)
    private String accttype;


}