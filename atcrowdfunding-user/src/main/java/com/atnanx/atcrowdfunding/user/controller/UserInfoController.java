package com.atnanx.atcrowdfunding.user.controller;

import com.atnanx.atcrowdfunding.core.bean.TMember;
import com.atnanx.atcrowdfunding.core.bean.TMemberAddress;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.constant.Const;
import com.atnanx.atcrowdfunding.core.util.JsonUtil;
import com.atnanx.atcrowdfunding.user.mapper.TMemberMapper;
import com.atnanx.atcrowdfunding.user.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户个人信息模块")
@RestController
@RequestMapping("/user/info")
public class UserInfoController {

    @Autowired
    private IMemberService memberService;
    @Autowired
    private TMemberMapper memberMapper;

    @Autowired
    StringRedisTemplate redisTemplate;
    @ApiOperation("获取个人信息")
    @PostMapping("/get")
    public ServerResponse<TMember> getUserInfo(@RequestParam("accessToken") String accessToken){

        //续期？每一个操作过来，重新设置login:member的超时

        ServerResponse serverResponse = memberService.getMemberInfo(accessToken);

        //redisTemplate.expire(AppConstant.MEMBER_LOGIN_CACHE_PREFIX+accessToken,30, TimeUnit.MINUTES);

        return serverResponse;
    }

    @ApiOperation("获取发布人名")
    @PostMapping("/get_name")
    public ServerResponse<String> getMemberName(@RequestParam("memberId") Integer memberId){

        //续期？每一个操作过来，重新设置login:member的超时

        TMember member = memberMapper.selectByPrimaryKey(memberId);
        if (member==null){
            return ServerResponse.createByErrorMessage("查询发布人名失败");
        }

        //redisTemplate.expire(AppConstant.MEMBER_LOGIN_CACHE_PREFIX+accessToken,30, TimeUnit.MINUTES);

        return ServerResponse.createBySuccess(member.getUsername());
    }

    @ApiOperation("更新个人信息")
    @PostMapping("/")
    public ServerResponse<String> updateUserInfo(){
        return null;
    }

    @ApiOperation("获取用户收货地址")
    @GetMapping("/address")
    public ServerResponse<List<TMemberAddress>> getUserAddress(@RequestParam("accessToken") String accessToken){
        String memberStr = redisTemplate.opsForValue().get(Const.memberInfo.REDIS_LOGIN_MEMBER_PREFIX + accessToken);
        TMember member = JsonUtil.Json2Obj(memberStr, TMember.class);

        return memberService.getMemberAddress(member.getId());
    }

    @ApiOperation("新增用户收货地址")
    @PostMapping("/address")
    public ServerResponse<TMemberAddress> addUserAddress(@RequestParam("accessToken") String accessToken,
                                                      @RequestParam("address") String address){
        return memberService.addAddress(accessToken,address);
    }

    @ApiOperation("修改用户收货地址")
    @PutMapping("/address")
    public ServerResponse<String> updateUserAddress(){
        return null;
    }

    @ApiOperation("删除用户收货地址")
    @DeleteMapping("/address")
    public ServerResponse<String> deleteUserAddress(){
        return null;
    }

    @ApiOperation("获取我发起的项目")
    @GetMapping("/create/project")
    public ServerResponse<String> getCreateProject(){
        return null;
    }

    @ApiOperation("获取我的系统消息")
    @GetMapping("/message")
    public ServerResponse<String> getMyMessage(){
        return null;
    }

    @ApiOperation("查看我的订单")
    @GetMapping("/order")
    public ServerResponse<String> getMyOrder(){
        return null;
    }

    @ApiOperation("删除我的订单")
    @DeleteMapping("/order")
    public ServerResponse<String> deleteMyOrder(){
        return null;
    }

    @ApiOperation("获取我关注的项目")
    @GetMapping("/star/project")
    public ServerResponse<String> getStarProject(){
        return null;
    }

    @ApiOperation("获取我支持的项目")
    @GetMapping("/support/project")
    public ServerResponse<String> getSupportProject(){
        return null;
    }
}
