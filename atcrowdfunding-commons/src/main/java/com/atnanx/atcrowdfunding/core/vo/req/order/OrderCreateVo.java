package com.atnanx.atcrowdfunding.core.vo.req.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class OrderCreateVo {
    @ApiModelProperty(value = "访问令牌" ,required = true)
    private String accessToken;//用户登录以后的访问令牌
    @ApiModelProperty(value = "支持的项目的id" ,required = true)
    private Integer projectid;
    @ApiModelProperty(value = "具体回报id" ,required = true)
    private Integer returnid;
    @ApiModelProperty(value = "订单总额；支持金额" ,required = true)
    private Integer money;//
    @ApiModelProperty(value = "回报数量" ,required = true)
    private Integer rtncount;//
    @ApiModelProperty(value = "收货地址;是详细地址，不是id" ,required = true)
    private String address;
    @ApiModelProperty(value = "0 - 不开发票， 1 - 开发票" ,required = true)
    private String invoice;//
    @ApiModelProperty(value = "发票抬头" ,required = true)
    private String invoictitle;//
    @ApiModelProperty(value = "备注" )
    private String remark;//
}
