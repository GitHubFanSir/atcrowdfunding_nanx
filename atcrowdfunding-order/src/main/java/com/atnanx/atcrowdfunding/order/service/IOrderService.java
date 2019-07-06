package com.atnanx.atcrowdfunding.order.service;

import com.atnanx.atcrowdfunding.core.bean.TOrder;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.req.order.OrderCreateVo;
import com.atnanx.atcrowdfunding.core.vo.req.pay.PayVo;

public interface IOrderService {
    ServerResponse generateOrder(OrderCreateVo orderCreateVo);

    ServerResponse pay(PayVo payVo);

    TOrder getOrder(Integer memberId, String orderNum);
}
