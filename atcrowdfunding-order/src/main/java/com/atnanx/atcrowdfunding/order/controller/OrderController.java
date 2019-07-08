package com.atnanx.atcrowdfunding.order.controller;

import com.atnanx.atcrowdfunding.core.bean.TMember;
import com.atnanx.atcrowdfunding.core.bean.TOrder;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.constant.Const;
import com.atnanx.atcrowdfunding.core.constant.state.OrderStatusEnum;
import com.atnanx.atcrowdfunding.core.util.JsonUtil;
import com.atnanx.atcrowdfunding.core.vo.req.order.OrderCreateVo;
import com.atnanx.atcrowdfunding.core.vo.req.order.OrderListVo;
import com.atnanx.atcrowdfunding.core.vo.req.pay.PayAsyncVo;
import com.atnanx.atcrowdfunding.core.vo.req.pay.PayVo;
import com.atnanx.atcrowdfunding.order.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/order")
@Api
public class OrderController {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ResponseBody
    @ApiOperation("生成订单")
    @PostMapping("/generate_order")
    public ServerResponse<TOrder> generateOrder(@RequestBody OrderCreateVo orderCreateVo){  /*@RequestBody */

        return orderService.generateOrder(orderCreateVo);
    }

    @ApiOperation("订单支付")
    @RequestMapping(value = "/pay",method = {RequestMethod.GET,RequestMethod.POST})
    public ServerResponse pay(@RequestBody PayVo payVo, HttpServletResponse response){
        return orderService.pay(payVo);
    }
    /**
     * 按照订单号，查出当前用户的订单
     * @param accessToken
     * @param orderNum
     * @return
     */
    @ApiOperation("按照订单号查询订单详情")
    @GetMapping("/memberOrder")
    public ServerResponse<TOrder> getOrder(@RequestParam("accessToken") String accessToken,@RequestParam("orderNum") String orderNum){
        String s = stringRedisTemplate.opsForValue().get(Const.memberInfo.REDIS_LOGIN_MEMBER_PREFIX + accessToken);
        TMember member = JsonUtil.Json2Obj(s,TMember.class);
        TOrder order = orderService.getOrder(member.getId(),orderNum);
        return ServerResponse.createBySuccess(order);
    }

    @ApiOperation("查询用户的最新5个订单")
    @PostMapping("/list")
    public ServerResponse<List<OrderListVo>> orderList(@RequestParam("accessToken") String accessToken){
        return orderService.orderList(accessToken);
    }

    @ApiOperation("取消订单")
    @PostMapping("/cancle")
    public ServerResponse<String> cancle(){
        return null;
    }

    @ApiOperation("支付宝支付成功")
    @PostMapping("/alipay/success")
    public String alipay(PayAsyncVo vo){
        //
        String orderNo = vo.getOut_trade_no();
        orderService.updateOrder(orderNo, OrderStatusEnum.PAYED);
        return "success";
    }
}
