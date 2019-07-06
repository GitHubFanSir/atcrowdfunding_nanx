/**
 * 这个common代表会对controller层产生影响
 */
package com.atnanx.atcrowdfunding.user.filter;

import com.atnanx.atcrowdfunding.core.bean.TMember;
import com.atnanx.atcrowdfunding.core.constant.Const;
import com.atnanx.atcrowdfunding.core.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Slf4j
@WebFilter(urlPatterns = "/*")
public class SessionExpireFilter implements Filter {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String accessTokenStr = httpServletRequest.getParameter(Const.memberInfo.ACCESS_TOKEN);
//        String loginToken = CookieUtil.readLoginLoken(httpServletRequest);
        if (StringUtils.isNotBlank(accessTokenStr)){
            /*String userStr = ShardedRedisPoolUtil.get(loginToken);
            MemberLoginRespVo member = JsonUtil.Json2Obj(userStr, MemberLoginRespVo.class);
            if(member!=null){
                ShardedRedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }*/
            String memberStr = stringRedisTemplate.opsForValue().get(Const.memberInfo.REDIS_LOGIN_MEMBER_PREFIX + accessTokenStr);
            TMember member = JsonUtil.Json2Obj(memberStr, TMember.class);
            if (member!=null){
                stringRedisTemplate.expire(Const.memberInfo.REDIS_LOGIN_MEMBER_PREFIX+accessTokenStr,Const.memberInfo.MEMBER_REDIS_SESSION_EXTIME, TimeUnit.MINUTES);
            }

        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
