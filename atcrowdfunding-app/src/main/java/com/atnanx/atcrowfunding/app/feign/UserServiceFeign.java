package com.atnanx.atcrowfunding.app.feign;

import com.atnanx.atcrowdfunding.core.bean.TMemberAddress;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.req.member.MemberRegisterReqVo;
import com.atnanx.atcrowdfunding.core.vo.resp.member.MemberLoginRespVo;
import com.atnanx.atcrowfunding.app.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 这个UserServiceFeign使用configuration指定的FeignConfig；
 * 不指定就是SpringCloud默认配置，传输数据用json；
 */
@FeignClient(value = "ATCROWDFUNDING-USER",configuration = FeignConfig.class)
public interface UserServiceFeign {

    @PostMapping("/user/login_by_pwd")
    ServerResponse<MemberLoginRespVo> loginByPwd(@RequestParam(value = "loginacct", required = true) String loginacct,
                                            @RequestParam(value = "password", required = true) String password);


    /**
     * Http+Json；
     *
     *
     * 告诉SpringCloud远程服务，我们是x-www-form-urlencoded;
     * consumes：指定让SpringCloud编码成表单的k=v发出去，而不是json；
     * SpringCloud默认支持转k=v,但是没配置。
     * @param memberRegisterVo
     * @return
     */
    @PostMapping(value = "/user/register",consumes = "application/x-www-form-urlencoded")
    public ServerResponse<String> register(MemberRegisterReqVo memberRegisterVo);


    @GetMapping("/user/info/address")
    public ServerResponse<List<TMemberAddress>> getUserAddress(@RequestParam("accessToken") String accessToken);

    @PostMapping("/user/info/address")
    public ServerResponse<TMemberAddress> addUserAddress(@RequestParam("accessToken") String accessToken,
                                                      @RequestParam("address") String address);
}
