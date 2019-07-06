package com.atnanx.atcrowdfunding.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableHystrixDashboard
@EnableHystrix

@EnableCircuitBreaker
@EnableFeignClients
@EnableDiscoveryClient
/**
 * 加事务；
 * 1）、@EnableTransactionManagement ；开启事务管理功能
 * 2）、给方法上加@Transaction；
 */
@EnableTransactionManagement //开启基于注解的事务管理功能；
@MapperScan("com.atnanx.atcrowdfunding.project.mapper")
//@EnableSwagger2
@SpringBootApplication
public class AtcrowdfundingProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtcrowdfundingProjectApplication.class, args);
    }

}
