package com.atnanx.atcrowfunding.app.controller;

import com.alipay.api.AlipayApiException;
import com.atnanx.atcrowdfunding.core.bean.TMemberAddress;
import com.atnanx.atcrowdfunding.core.bean.TOrder;
import com.atnanx.atcrowdfunding.core.bean.TReturn;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.req.order.OrderCreatePageVo;
import com.atnanx.atcrowdfunding.core.vo.req.order.OrderCreateVo;
import com.atnanx.atcrowdfunding.core.vo.req.pay.PayVo;
import com.atnanx.atcrowdfunding.core.vo.resp.member.MemberLoginRespVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectAllAllInfoVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectReturnConfirmPageVo;
import com.atnanx.atcrowfunding.app.feign.OrderFeignService;
import com.atnanx.atcrowfunding.app.feign.ProjectFeignService;
import com.atnanx.atcrowfunding.app.feign.UserServiceFeign;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RequestMapping("/pay")
@Controller
public class PayController {

    @Autowired
    ProjectFeignService projectFeignService;

    @Autowired
    UserServiceFeign userServiceFeign;

    @Autowired
    OrderFeignService orderFeignService;

    /*@Autowired
    AlipayTemplate alipayTemplate;*/

    /**
     * 支付第一步，点击支持，跳到回报确认页
     *
     * @Param retId 档位id
     * @Param projectId 项目id
     */
    @GetMapping("/step-1.html")
    public String payStep1ToReturnConfirmPage(@RequestParam("retId") Integer returnId,
                                              @RequestParam("projectId") Integer projectId,
                                              Model model,
                                              HttpSession session) {
        ProjectReturnConfirmPageVo pageVo = new ProjectReturnConfirmPageVo();
        //1、查出这档位/项目的详细信息，封装成vo给页面
        ServerResponse<ProjectAllAllInfoVo> response = projectFeignService.getDetail(projectId);
        //提取远程查到的项目详情
        ProjectAllAllInfoVo projectAllAllInfoVo = response.getData();
        TReturn currentReturn = null;
        List<TReturn> returns = projectAllAllInfoVo.getReturns(); //提取回报是哪个
        for (TReturn aReturn : returns) {
            if (aReturn.getId() == returnId) {
                currentReturn = aReturn;
            }
        }


        //封装值
        pageVo.setName(projectAllAllInfoVo.getName());
        pageVo.setContent(currentReturn.getContent());
        pageVo.setFreight(currentReturn.getFreight());
        pageVo.setMemberName(projectAllAllInfoVo.getMemberName() + "宇宙公司");
        pageVo.setNum(1);
        pageVo.setSupportmoney(currentReturn.getSupportmoney());
        pageVo.setProject(projectAllAllInfoVo);
        pageVo.setCurrentReturn(currentReturn);


        model.addAttribute("projectVo", pageVo);
        session.setAttribute("project", pageVo);
        //显示支持的是哪个档位的信息
        return "order/pay-step-1";
    }

    /**
     *
     * @param num
     * @param session
     * @param redirectAttributes  重定向保存数据
     * @param model   转发
     * @return
     */
    @GetMapping("/confirm-order.html")
    public String payStep2ToOrderConfirmPage(
            @RequestParam("num") Integer num,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {
        MemberLoginRespVo loginUser = (MemberLoginRespVo) session.getAttribute("loginUser");
       /* if (loginUser == null) {
            redirectAttributes.addFlashAttribute("msg", "当前操作需要登录，请先登录");
            return "redirect:/login.html";
        }*/

        //1、取出session刚才保存的项目信息
        ProjectReturnConfirmPageVo project = (ProjectReturnConfirmPageVo) session.getAttribute("project");
        //更新session之前保存的数量信息
        project.setNum(num);

        //2、调用用户服务查询出用户的收货地址
        ServerResponse<List<TMemberAddress>> userAddress = userServiceFeign.getUserAddress(loginUser.getAccessToken());

        model.addAttribute("userAddress", userAddress.getData());

        return "order/pay-step-2";
    }

    /**
     * 点击立即付款
     * @param vo
     * @param session
     * @return
     *
     * pay.html；响应编码为html
     * pay.json；编码为json；415；
     */
    @ResponseBody
    @PostMapping(value = "/pay")
    public void pay(OrderCreatePageVo vo, HttpSession session,
                    RedirectAttributes attributes, HttpServletResponse response) throws Exception {
        //获取当前登录的用户
        MemberLoginRespVo loginUser = (MemberLoginRespVo) session.getAttribute("loginUser");
        /*if (loginUser == null) {
            attributes.addFlashAttribute("msg", "当前操作需要登录，请先登录");
            return "redirect:/login.html";
        }*/
        ProjectReturnConfirmPageVo project = (ProjectReturnConfirmPageVo) session.getAttribute("project");

        //1、调用远程创建订单接口
        OrderCreateVo orderCreateVo = new OrderCreateVo();
        BeanUtils.copyProperties(vo,orderCreateVo);
        /**
         *     private String accessToken;//用户登录以后的访问令牌
         *     private Integer projectid;//支持的项目的id
         *     private Integer returnid;//支持的档位的id
         *     private Integer money;//订单总额；支持金额
         *     private Integer rtncount;//回报数量
         */
        orderCreateVo.setAccessToken(loginUser.getAccessToken());
        orderCreateVo.setProjectid(project.getProject().getId());
        orderCreateVo.setReturnid(project.getCurrentReturn().getId());
        orderCreateVo.setMoney(project.getTotalPrice());
        orderCreateVo.setRtncount(project.getNum());

        //创建完订单
        ServerResponse<TOrder> orderResponse = orderFeignService.create(orderCreateVo,orderCreateVo.getAccessToken());
        TOrder order = orderResponse.getData();
        //2、调用远程的支付服务，准备订单支付用的数据
        PayVo payVo = new PayVo();
        payVo.setAccessToken(loginUser.getAccessToken());
        payVo.setOrderNo(order.getOrdernum());
        payVo.setProjectName(project.getProject().getName());
        payVo.setReturnId(String.valueOf(project.getCurrentReturn().getId()));
        //3、调用远程的支付服务，返回支付的页面
        ServerResponse payServerResponse = orderFeignService.pay(payVo, loginUser.getAccessToken());
       /* payVo.setBody(new String(project.getProject().getName().getBytes("UTF-8"),"UTF-8"));
        payVo.setOut_trade_no(new String(order.getOrdernum().getBytes("UTF-8"),"UTF-8"));
        payVo.setSubject(new String(project.getProject().getName().getBytes("UTF-8"),"UTF-8"));
        payVo.setTotal_amount(new String(order.getMoney().toString().getBytes("UTF-8"),"UTF-8"));*/

        if (payServerResponse.isSuccess()){
            response.setContentType("text/html;charset=utf-8");
            try {
                //直接将完整的表单html输出到页面
                response.getWriter().write((String) payServerResponse.getData());
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @GetMapping("/success.html")
    public String paySucessPage(){

        return "redirect:/minecrowdfunding.html";
    }


    /**
     * 将未支付的订单，再次跳到支付页面进行支付
     */
    @ResponseBody
    @GetMapping("/topay")
    public void toPay(@RequestParam("orderNum") String orderNum,HttpSession session) throws UnsupportedEncodingException, AlipayApiException {

        MemberLoginRespVo loginUser = (MemberLoginRespVo) session.getAttribute("loginUser");
        ProjectReturnConfirmPageVo project = (ProjectReturnConfirmPageVo) session.getAttribute("project");
        //1、远程按照订单号，查出订单的信息，继续支付
        ServerResponse<TOrder> response = orderFeignService.
                getOrder(loginUser.getAccessToken(), orderNum);
        TOrder order = response.getData();
        //2、调用支付方法
        //2、调用远程的支付服务，准备订单支付用的数据
        PayVo payVo = new PayVo();
        payVo.setAccessToken(loginUser.getAccessToken());
        payVo.setOrderNo(order.getOrdernum());
        payVo.setProjectName(project.getProject().getName());
        payVo.setReturnId(String.valueOf(order.getReturnid()));
        //3、调用远程的支付服务，返回支付的页面
        orderFeignService.pay(payVo,loginUser.getAccessToken());

    }

}
