package com.atnanx.atcrowfunding.app.feign;

import com.atnanx.atcrowdfunding.core.bean.TOrder;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.req.order.OrderCreateVo;
import com.atnanx.atcrowdfunding.core.vo.req.order.OrderListVo;
import com.atnanx.atcrowdfunding.core.vo.req.pay.PayVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/order")
@FeignClient("ATCROWDFUNDING-ORDER")
public interface OrderFeignService {

    @PostMapping("/create")
     ServerResponse<TOrder> create(@RequestBody OrderCreateVo createVo);

    @PostMapping("/pay")
     String pay(PayVo vo) throws Exception;

    @PostMapping("/list")
     ServerResponse<List<OrderListVo>> orderList(@RequestParam("accessToken") String accessToken);

    @GetMapping("/memberOrder")
     ServerResponse<TOrder> getOrder(@RequestParam("accessToken") String accessToken, @RequestParam("orderNum") String orderNum);
}
