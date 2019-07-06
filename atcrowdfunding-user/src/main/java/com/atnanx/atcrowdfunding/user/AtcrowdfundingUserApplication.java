package com.atnanx.atcrowdfunding.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


//1）、开启从注册中心中注册发现服务的功能  @EnableDiscoveryClien
//2）、开启基于Feign的声明式远程调用  @EnableFeignClients
//3）、开启断路器功能  @EnableCircuitBreaker

/**
 * 1、引入feign依赖
 * 2、开启服务发现功能  @EnableDiscoveryClient
 * 3、开启feign声明式客户端调用功能；
 *
 * @EnableFeignClients （发现了其他的服务，那之间用什么联调调用API呢，feign可以）
 * 4、
 */
@ServletComponentScan("com.atnanx.atcrowdfunding.member.filter")

@EnableCircuitBreaker
@EnableFeignClients
@EnableDiscoveryClient

@EnableSwagger2
@MapperScan("com.atnanx.atcrowdfunding.user.mapper")
//exclude = DataSourceAutoConfiguration.class
@SpringBootApplication
public class AtcrowdfundingUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtcrowdfundingUserApplication.class, args);
    }

}
