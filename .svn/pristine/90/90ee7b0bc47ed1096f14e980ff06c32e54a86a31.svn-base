package com.atnanx.atcrowdfunding.user.common;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import com.atnanx.atcrowdfunding.core.common.ResponseCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class ExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //没有这行记录异常日志信息，那错误信息将会丢失
        Logger logger = LoggerFactory.getLogger(ExceptionResolver.class);

        log.error("{} exception",request.getRequestURI(),ex);
        //有普通的view和jstlview等等
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject("status",ResponseCode.Error.getCode());
        modelAndView.addObject("msg","接口异常,详情请查看服务端日志的异常信息");
        modelAndView.addObject("data",ex.toString());
        modelAndView.addObject("data2",ex.getMessage());
        return modelAndView;
    }
}
