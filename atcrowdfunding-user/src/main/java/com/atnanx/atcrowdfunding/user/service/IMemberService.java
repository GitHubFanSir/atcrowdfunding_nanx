package com.atnanx.atcrowdfunding.user.service;

import com.atnanx.atcrowdfunding.core.bean.TMemberAddress;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.req.member.MemberRegisterReqVo;

public interface IMemberService {
    /**
     * 检查用户是否存在
     * @param username
     * @return
     */
    ServerResponse checkMemberExist(String username);

    /**
     * 门户用户根据密码登录
     * @param username
     * @param password
     * @return
     */
    ServerResponse login(String username, String password);

    /**
     * 门户用户注册
     * @param memberRegisterVo
     * @return
     */
    ServerResponse register(MemberRegisterReqVo memberRegisterVo);

    /**
     * 获取用户信息
     * @param accessToken
     * @return
     */
    ServerResponse getMemberInfo(String accessToken);

    /**
     * 门户用户 根据验证码登录
     * @param username
     * @param verificationCode
     * @return
     */
    ServerResponse loginByCode(String username, String verificationCode);

    /**
     * 发送短信
     * @param mobile 手机号
     * @param type 发送验证码类型
     * @return
     */
    ServerResponse sendSms(String mobile, String type);

    ServerResponse getMemberAddress(Integer id);

    ServerResponse<TMemberAddress> addAddress(String accessToken, String address);
}
