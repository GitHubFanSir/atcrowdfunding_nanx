package com.atnanx.atcrowdfunding.order.service;

import com.atnanx.atcrowdfunding.core.bean.TOrder;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.constant.state.OrderStatusEnum;
import com.atnanx.atcrowdfunding.core.vo.req.order.OrderCreateVo;
import com.atnanx.atcrowdfunding.core.vo.req.order.OrderListVo;
import com.atnanx.atcrowdfunding.core.vo.req.pay.PayVo;

import java.util.List;

public interface IOrderService {
    ServerResponse<TOrder> generateOrder(OrderCreateVo orderCreateVo);

    ServerResponse pay(PayVo payVo);

    TOrder getOrder(Integer memberId, String orderNum);

    ServerResponse<List<OrderListVo>> orderList(String accessToken);

    void updateOrder(String orderNo, OrderStatusEnum payed);
}
