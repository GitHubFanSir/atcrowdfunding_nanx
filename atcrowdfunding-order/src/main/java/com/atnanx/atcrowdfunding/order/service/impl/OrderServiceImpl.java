package com.atnanx.atcrowdfunding.order.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.atnanx.atcrowdfunding.core.bean.TMember;
import com.atnanx.atcrowdfunding.core.bean.TOrder;
import com.atnanx.atcrowdfunding.core.bean.TOrderExample;
import com.atnanx.atcrowdfunding.core.common.ResponseCode;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.constant.Const;
import com.atnanx.atcrowdfunding.core.constant.state.OrderStatusEnum;
import com.atnanx.atcrowdfunding.core.util.DateTimeUtil;
import com.atnanx.atcrowdfunding.core.util.JsonUtil;
import com.atnanx.atcrowdfunding.core.vo.req.order.OrderCreateVo;
import com.atnanx.atcrowdfunding.core.vo.req.order.OrderListVo;
import com.atnanx.atcrowdfunding.core.vo.req.pay.PayVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectReturnDetailVo;
import com.atnanx.atcrowdfunding.order.component.AlipayConfig;
import com.atnanx.atcrowdfunding.order.mapper.TOrderMapper;
import com.atnanx.atcrowdfunding.order.service.IOrderService;
import com.atnanx.atcrowdfunding.order.service.feign.ProjectFeignService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private TOrderMapper orderMapper;
    @Autowired
    private ProjectFeignService projectFeignService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
//    @Transactional  事务会因为，网络远程调用超时会拉长本地数据库事务时间，导致事务一直锁住数据库的字段，
    //需要分布式事务解决
    public ServerResponse<TOrder> generateOrder(OrderCreateVo orderCreateVo) {
        String memberStr = stringRedisTemplate.opsForValue().get(Const.memberInfo.REDIS_LOGIN_MEMBER_PREFIX + orderCreateVo.getAccessToken());
        TMember member = JsonUtil.Json2Obj(memberStr, TMember.class);

        /* private Integer projectid;//支持的项目的id
    private Integer returnid;//支持的档位的id
    private Integer money;//订单总额；支持金额
    private Integer rtncount;//回报数量
    private String address;//收货地址;是详细地址，不是id*/
        TOrder order = new TOrder();
        BeanUtils.copyProperties(orderCreateVo,order);
        order.setMemberid(member.getId());
        order.setOrdernum(generateOrderNo(member.getId()));
        order.setCreatedate(DateTimeUtil.dateToStr(new Date()));
        order.setStatus(String.valueOf(OrderStatusEnum.UNPAY.getCode()));

        int insertCount = orderMapper.insertSelective(order);
        if (insertCount<=0){
            log.error("插入订单失败");
            return ServerResponse.createByErrorMessage("插入订单失败");
        }

        //這個远程调用怎么保证事务的一致性啊
        reduceProjectReturnStock(order.getRtncount(),order.getReturnid(),orderCreateVo.getAccessToken());

        return ServerResponse.createBySuccess(order);
    }


    private void reduceProjectReturnStock(Integer rtnCount,Integer returnId,String accessToken){
        ServerResponse response = projectFeignService.reduceProjectReturnStock(rtnCount,returnId,accessToken);
        log.info(response.getMsg());
        if (!response.isSuccess()){
            log.error(response.getData().toString());
        }
    }

    @Override
    public void updateOrder(String out_trade_no, OrderStatusEnum payed) {
        TOrder order = new TOrder();
        order.setStatus(String.valueOf(payed.getCode()));
        TOrderExample example = new TOrderExample();
        example.createCriteria().andOrdernumEqualTo(out_trade_no);
        orderMapper.updateByExampleSelective(order,example);
    }

    @Override
    public ServerResponse<List<OrderListVo>> orderList(String accessToken) {
        String memberStr = stringRedisTemplate.opsForValue().get(Const.memberInfo.REDIS_LOGIN_MEMBER_PREFIX + accessToken);
        TMember member = JsonUtil.Json2Obj(memberStr,TMember.class);

        //1、查出最新的五个订单信息；
        List<TOrder> orders = orderMapper.selectLimit5Order(member.getId());
        if (CollectionUtils.isEmpty(orders)){
            ServerResponse.createByErrorMessage("查询订单失败");
        }
        //2、查出每个订单的项目信息
        List<OrderListVo> orderListVos = new ArrayList<>();


        for (TOrder order : orders) {
            OrderListVo vo = new OrderListVo();
            //vo封装订单和项目对象
            vo.setOrder(order);
            //在查出每个订单对应的项目
            orderListVos.add(vo);
        }

        return ServerResponse.createBySuccess(orderListVos);
    }

    @Override
    public TOrder getOrder(Integer memberId, String orderNum) {
        TOrderExample example = new TOrderExample();
        example.createCriteria().andOrdernumEqualTo(orderNum).andMemberidEqualTo(memberId);
        List<TOrder> orders = orderMapper.selectByExample(example);
        return orders!=null&orders.size()==1?orders.get(0):null;
    }

    public ServerResponse pay(PayVo payVo){
        String memberStr = stringRedisTemplate.opsForValue().get(Const.memberInfo.REDIS_LOGIN_MEMBER_PREFIX + payVo.getAccessToken());
        TMember member = JsonUtil.Json2Obj(memberStr, TMember.class);

        TOrder order = orderMapper.selectByUserIdAndOrderNo(payVo.getOrderNo(),member.getId());
        if (order ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGAL_ARGUMENT.getCode(),ResponseCode.ILLEAGAL_ARGUMENT.getDesc());
        }


        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getGatewayUrl(),alipayConfig.getAppId(),
                alipayConfig.getPrivateKey(),"json",alipayConfig.getCharset(),alipayConfig.getAlipayPublicKey(),alipayConfig.getSignType());
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(alipayConfig.getReturnUrl());
        request.setNotifyUrl(alipayConfig.getNotifyUrl());

        ServerResponse<ProjectReturnDetailVo> returnInfoResponse = projectFeignService.getReturnInfo(Integer.valueOf(payVo.getReturnId()));
        ProjectReturnDetailVo productReturnDetail =null ;
        if (returnInfoResponse.isSuccess()){
            productReturnDetail = returnInfoResponse.getData();
        }
        if (productReturnDetail==null){
            return ServerResponse.createByErrorMessage("没有指定对应的回报信息");
        }
        //商户自己的订单号
        String outTradeNoStr = order.getOrdernum();
        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String total_amount = String.valueOf(order.getMoney());
        //订单名称，订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费” new StringBuilder().append(projectName).append(outTradeNo).toString()
        String subject = payVo.getProjectName()+payVo.getOrderNo();
        //商品描述，订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append(productReturnDetail.getContent()).append("售卖").append(order.getRtncount()).append("份,共").append(total_amount).append("元").toString();
        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";
        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID uid
        String sellerId = alipayConfig.getSellerId();


        List<GoodsDetail> goodsDetailList = Lists.newArrayList();
        GoodsDetail goodsDetail = new GoodsDetail();
        goodsDetail.setGoodsId(productReturnDetail.getId());
        goodsDetail.setGoodsName(productReturnDetail.getProjectName());
        //需要修改冗余的数据库设计字段
        goodsDetail.setPrice(productReturnDetail.getSupportmoney().toString());
        goodsDetail.setQuantity(Long.valueOf(order.getRtncount()));
        goodsDetailList.add(goodsDetail);
        /*
        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";
        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID uid
        String sellerId = "";
        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";
        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";
        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500"); //pid 2088102175911193*/

        AlipayTradePagePayModel model = new AlipayTradePagePayModel();

        model.setOutTradeNo(outTradeNoStr);
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setTotalAmount(total_amount);
        model.setSubject(subject);
        model.setBody(body);
        model.setTimeoutExpress(timeoutExpress);
        model.setGoodsDetail(goodsDetailList);
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节
        //公用回传参数，如果请求时传递了该参数，则返回给商户时会回传该参数。支付宝只会在同步返回（包括跳转回商户网站）和异步通知时将该参数原样返回。本参数必须进行UrlEncode之后才可以发送给支付宝。
//        model.setPassbackParams(URLEncoder.encode(String.valueOf(loginid),"utf-8"));

        //请求
        request.setBizModel(model);
        AlipayTradePagePayResponse response = null;
        try {
            response = alipayClient.pageExecute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            log.info("调用成功!");
            //返回form 的string信息
            return ServerResponse.createBySuccess("调用成功",response.getBody());
        } else {
            log.error("支付宝预下单失败!!!");
            return ServerResponse.createByErrorMessage("调用失败");
        }

    }

    private String generateOrderNo(Integer userId){
        //时间+用户id+随机数
        long currentTimeMillis = System.currentTimeMillis();

        return currentTimeMillis + ""+ userId+ new Random().nextInt(100);
    }
}
