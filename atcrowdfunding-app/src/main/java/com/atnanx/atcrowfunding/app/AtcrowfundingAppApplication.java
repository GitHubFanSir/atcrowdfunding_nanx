package com.atnanx.atcrowfunding.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;



/**
 * 静态资源；
 * Springboot觉得：
 *      "classpath:/META-INF/resources/", "classpath:/resources/",
 * 	    "classpath:/static/", "classpath:/public/" ;
 * 	    这四个地方的静态资源可以直接访问
 *
 */
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class AtcrowfundingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtcrowfundingAppApplication.class, args);
    }

}
