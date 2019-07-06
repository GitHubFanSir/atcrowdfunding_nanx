package com.atnanx.atcrowdfunding.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableFeignClients

@MapperScan("com.atnanx.atcrowdfunding.order.mapper")
@SpringBootApplication
public class AtcrowdfundingOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtcrowdfundingOrderApplication.class, args);
    }

}
