package com.atnanx.atcrowfunding.app.controller;

import com.atnanx.atcrowdfunding.core.bean.TReturn;
import com.atnanx.atcrowdfunding.core.bean.TType;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.req.order.OrderListVo;
import com.atnanx.atcrowdfunding.core.vo.resp.member.MemberLoginRespVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectAllAllInfoVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectAllInfoVo;
import com.atnanx.atcrowfunding.app.feign.OrderFeignService;
import com.atnanx.atcrowfunding.app.feign.ProjectFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PageController {

    @Autowired
    ProjectFeignService projectFeignService;

    @Autowired
    OrderFeignService orderFeignService;

    /**
     * 1）、只要以.html结尾的都是直接去页面
     * 2）、只要动态请求都不以html结尾
     * @return
     */
    @GetMapping("/member.html")
    public String memberPage(HttpSession session, RedirectAttributes redirectAttributes){
       /* MemberLoginRespVo loginUser = (MemberLoginRespVo) session.getAttribute("loginUser");
        if(loginUser == null){
            //没登录
            redirectAttributes.addFlashAttribute("msg","请先登录");
            return "redirect:/login.html";
        }*/

        return "member";
    }

    @GetMapping("/minecrowdfunding.html")
    public String minecrowdfunding(HttpSession session, RedirectAttributes redirectAttributes,Model model){
        /*MemberLoginRespVo loginUser = (MemberLoginRespVo) session.getAttribute("loginUser");
        if(loginUser == null){
            //没登录
            redirectAttributes.addFlashAttribute("msg","请先登录");
            return "redirect:/login.html";
        }*/

        MemberLoginRespVo loginUser = (MemberLoginRespVo) session.getAttribute("loginUser");
        //1、远程查询当前用户的最新的五个订单信息；
        ServerResponse<List<OrderListVo>> orderList = orderFeignService.orderList(loginUser.getAccessToken());

        List<OrderListVo> data = orderList.getData();
        for (OrderListVo orderListVo : data) {
            //2、每一个订单查询自己的项目
            ServerResponse<ProjectAllAllInfoVo> detail = projectFeignService.getDetail(orderListVo.getOrder().getProjectid());
            ProjectAllAllInfoVo data1 = detail.getData();
            orderListVo.setProject(data1);
            //3、查询我们这个订单的档位
            List<TReturn> returns = data1.getReturns();
            for (TReturn aReturn : returns) {
                if(aReturn.getId() == orderListVo.getOrder().getReturnid()){
                    orderListVo.setTReturn(aReturn);
                }
            }

        }
        model.addAttribute("ordersList",data);

        return "member/minecrowdfunding";
    }

    @GetMapping(value = {"/","/index.html"})
    public String indexPage(Model model){

        //调用远程服务查出所有项目
       //AppResponse<List<TProject>> index = projectFeignService.getAllIndex();
       // projectFeignService
        //这边查出所有的压力好大啊
        ServerResponse<List<ProjectAllInfoVo>> allIndex = projectFeignService.getAllIndex();
        List<ProjectAllInfoVo> projectAllInfoVoList = allIndex.getData();

        ServerResponse<List<TType>> typeResponse = projectFeignService.sysType();
        List<TType> typeList = typeResponse.getData();
        //hashmap
        model.addAttribute("projects",projectAllInfoVoList);
        model.addAttribute("types",typeList);
        return "index";
    }
}
