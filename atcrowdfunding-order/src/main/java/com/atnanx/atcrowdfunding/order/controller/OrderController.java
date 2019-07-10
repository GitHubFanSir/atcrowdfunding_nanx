package com.atnanx.atcrowdfunding.order.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
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
import com.atnanx.atcrowdfunding.order.component.AlipayConfig;
import com.atnanx.atcrowdfunding.order.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Api
@Slf4j
public class OrderController {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AlipayConfig alipayConfig;

    @ApiOperation("生成订单")
    @PostMapping("/generate_order")
    public ServerResponse<TOrder> generateOrder(@RequestBody OrderCreateVo orderCreateVo){  /*@RequestBody */

        return orderService.generateOrder(orderCreateVo);
    }

    @ApiOperation("订单支付")
    @RequestMapping(value = "/pay",method = {RequestMethod.GET,RequestMethod.POST})
    public ServerResponse pay(@RequestBody PayVo payVo){
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
    public ServerResponse alipay(PayAsyncVo vo, HttpServletRequest request){
        //
        String orderNo = vo.getOut_trade_no();
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        Boolean alipayRSACheckedV2;
        try {
            alipayRSACheckedV2 = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey()
                    , alipayConfig.getCharset(), alipayConfig.getSignType());
            if (!alipayRSACheckedV2){
                log.error("验签失败，不是支付宝的回调");
                return ServerResponse.createByErrorMessage("验签不通过，不是支付宝传过来的数据");
            }
            log.info("验签正确，准备根据支付宝回调来的参数做交易状态等的处理");
        } catch (AlipayApiException e) {
            log.error("支付宝验签回调参数异常",e);
        }


        orderService.updateOrder(orderNo, OrderStatusEnum.PAYED);
        return ServerResponse.createBySuccessMessage("success");
    }
}
