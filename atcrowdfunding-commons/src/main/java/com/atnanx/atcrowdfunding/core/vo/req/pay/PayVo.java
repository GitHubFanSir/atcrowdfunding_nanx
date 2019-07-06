package com.atnanx.atcrowdfunding.core.vo.req.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PayVo {
    @ApiModelProperty(value = "订单号", required = true)
    String orderNo;
    @ApiModelProperty(value = "用户令牌", required = true)
    String accessToken;
    @ApiModelProperty(value = "项目名称", required = true)
    String projectName;
    @ApiModelProperty(value = "回报id", required = true)
    String returnId;
}
