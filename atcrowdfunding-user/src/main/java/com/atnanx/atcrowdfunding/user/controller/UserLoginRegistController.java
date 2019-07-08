package com.atnanx.atcrowdfunding.user.controller;


import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.req.member.MemberRegisterReqVo;
import com.atnanx.atcrowdfunding.user.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;



@Api(tags = "用户登录注册模块")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserLoginRegistController {

    @Autowired
    private IMemberService memberService;

    @ApiOperation("检查用户是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginacct", value = "账号（手机号）", required = true),
    })
    @GetMapping("check_member_exist")
    public ServerResponse<Map> checkMemberExist(String loginacct){
        ServerResponse serverResponse = memberService.checkMemberExist(loginacct);
        serverResponse.setData(null);
        return serverResponse;
    }

    @ApiOperation("用户使用密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginacct", value = "账号（手机号）", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)
    })
    @PostMapping("/login_by_pwd")
    public ServerResponse loginByPwd(@RequestParam("loginacct") String loginacct,
                                @RequestParam("password") String password) {
        ServerResponse serverResponse = memberService.login(loginacct, password);


        return serverResponse;
    }

    @ApiOperation("用户使用手机验证码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginacct", value = "手机号", required = true),
            @ApiImplicitParam(name = "verificationCode ", value = "手机验证码", required = true)
    })
    @PostMapping("/login_by_code")
    public ServerResponse loginByCode(@RequestParam("loginacct") String loginacct,
                                @RequestParam("verificationCode") String verificationCode) {
        ServerResponse serverResponse = memberService.loginByCode(loginacct, verificationCode);



        return serverResponse;
    }

    /**
     * @param memberRegisterVo
     * @return
     *
     * SpringCloud：Http+Json;
     *      @RequestBody:将请求体中的json数据转为指定的这对象
     *
     * 以后的post请去都代表接受json数据
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ServerResponse<String> register(MemberRegisterReqVo memberRegisterVo) {

        log.debug("{} 用户正在注册：", memberRegisterVo.getMobile());
        ServerResponse serverResponse = memberService.register(memberRegisterVo);

        return serverResponse;
    }

    @ApiOperation("获取短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile",value = "手机号",required = true),
            @ApiImplicitParam(name = "type",value = "(采用不同模板)验证码类型 1000-注册,1001-登录",required = true)
    })
    @PostMapping("/sendsms")
    public ServerResponse<String> sendSms(@RequestParam("mobile") String mobile,String type) {
//        smsTemplate.sendCodeSms(mobile,code);
        //cookie/session；浏览器
        /**
         * 浏览器：
         *      同一个页面共享：pageContext
         *      同一次请求：request
         *      同一次会话：session：Map
         *      同一个应用：application；
         *
         * 多端了；
         *      同一个页面共享：各端使用自己的方式
         *      同一次请求共享数据：将数据以json写出去；
         *      同一次会话：把数据一个公共的地方【redis】，
         *                ：把数据一个公共的地方【redis】，
         *
         *                success("").msg("短信发送完成")
         */

        ServerResponse serverResponse = memberService.sendSms(mobile,type);
        return serverResponse;
    }

    @ApiOperation("重置密码")
    @PostMapping("/reset")
    public ServerResponse resetPassword(){
        return null;
    }
}
