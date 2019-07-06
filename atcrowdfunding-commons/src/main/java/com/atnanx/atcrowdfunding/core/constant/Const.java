/**
 * 业务上的common
 */
package com.atnanx.atcrowdfunding.core.constant;

public class Const {
    public static final String FRONT_SESSION_LOGIN_MEMBER="loginUser";

    public interface memberInfo{
        String REDIS_LOGIN_MEMBER_PREFIX="member:login:info:";

        String ACCESS_TOKEN ="accessToken";

        long MEMBER_REDIS_SESSION_EXTIME=30;


        String REDIS_REGISTER_CODE_PREFIX="member:register:code:";
        String REDIS_LOGIN_CODE_PREFIX="member:login:code:";

        Integer CODE_SESSION_EXTIME=2;
    }

    public interface projectInfo{
        String REDIS_TEMP_PROJECT_PREFIX="project:temp:info:";

        Integer PROJECT_TEMP_SAVE_EXTIME =30;
    }



    public interface AlipayCallbackStatus{
        String  TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }
}
