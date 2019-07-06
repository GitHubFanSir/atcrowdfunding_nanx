package com.atnanx.atcrowdfunding.order.controller;

import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.req.order.OrderCreateVo;
import com.atnanx.atcrowdfunding.order.service.IOrderService;
import com.atnanx.atcrowdfunding.core.vo.req.pay.PayVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/order")
@Api
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @ResponseBody
    @ApiOperation("生成订单")
    @PostMapping("/generate_order")
    public ServerResponse generateOrder(OrderCreateVo orderCreateVo){
        return orderService.generateOrder(orderCreateVo);
    }

    @ApiOperation("订单支付")
    @RequestMapping(value = "/pay",method = {RequestMethod.GET,RequestMethod.POST})
    public void pay(PayVo payVo, HttpServletResponse response){
        ServerResponse payServerRespons = orderService.pay(payVo);
        if (payServerRespons.isSuccess()){
            response.setContentType("text/html;charset=utf-8");
            try {
                //直接将完整的表单html输出到页面
                response.getWriter().write((String) payServerRespons.getData());
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
