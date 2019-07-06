package com.atnanx.atcrowdfunding.order.vo.resp;

import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectReturnDetailVo;
import lombok.Data;

@Data
public class OrderRespVo {
    private String ordernum;

    private String createdate;

    private Integer money;

    private Integer rtncount;

    private String status;

    private String address;

    private String invoice;

    private String invoictitle;

    private String remark;

    private ProjectReturnDetailVo projectReturnDetailVo;
}
