package com.atnanx.atcrowdfunding.order.Config;

import com.atnanx.atcrowdfunding.interceptor.AuthorityInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@ConditionalOnProperty(prefix = "crowdfunding-order", name = "auth-intercept-enable", havingValue = "true")
@Slf4j
public class MemberAuthorityConfig extends WebMvcConfigurationSupport {
    //WebMvcConfigurationSupport
// 因为 WebMvcConfigurationSupport 是配置类，项目启动便会立即加载

   /* @Bean
    AuthorityInterceptor localInterceptor() {
        return new AuthorityInterceptor();
    }*/
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("添加拦截器"); 
        //拦截器先只拦截project的,后面写拦截用户和订单的
        //addPathPatterns是添加自己的规则，这里是指拦截所有请求
        registry.addInterceptor(new AuthorityInterceptor("back",stringRedisTemplate)).addPathPatterns("/**")
                .excludePathPatterns( "/swagger-ui.html/**","/swagger-resources/**", "/webjars/**", "/v2/**");
//                .excludePathPatterns("/static/**","/templates/**"

    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
      /*  registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");*/
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }

     /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// 设置允许跨域的路径
                .allowedOrigins("*") //设置允许 跨域请求的域名
                .allowCredentials(true) //是否允许证书 不在默认开启
                .allowedMethods("GET","POST","PUT","DELETE") //设置允许的方法
                .maxAge(3600); //允许跨域的时间

    }*/
}
