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

    //序列化成json 字符串传给后端服务
    /*@RequestBody */
    @PostMapping("/generate_order")
    ServerResponse<TOrder> create(@RequestBody OrderCreateVo createVo,@RequestParam("accessToken") String accessToken);

    @PostMapping("/pay")
    ServerResponse pay(@RequestBody PayVo vo,@RequestParam("accessToken") String accessToken) ;

    @PostMapping("/list")
    ServerResponse<List<OrderListVo>> orderList(@RequestParam("accessToken") String accessToken);

    @GetMapping("/memberOrder")
    ServerResponse<TOrder> getOrder(@RequestParam("accessToken") String accessToken, @RequestParam("orderNum") String orderNum);
}
