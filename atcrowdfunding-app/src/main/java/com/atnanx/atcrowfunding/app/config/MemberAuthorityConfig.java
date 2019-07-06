package com.atnanx.atcrowfunding.app.config;

import com.atnanx.atcrowdfunding.interceptor.AuthorityInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(prefix = "crowdfunding-app", name = "auth-intercept-enable", havingValue = "true")
@Slf4j
public class MemberAuthorityConfig implements WebMvcConfigurer {
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

        registry.addInterceptor(new AuthorityInterceptor("front",stringRedisTemplate)).addPathPatterns("/**")
                .excludePathPatterns("/bootstrap/**","/css/**","/fonts/**",
                        "/img/**","/jquery/**","/layer/**","/script/**",
                        "/login.html","/reg.html","/index.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

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
