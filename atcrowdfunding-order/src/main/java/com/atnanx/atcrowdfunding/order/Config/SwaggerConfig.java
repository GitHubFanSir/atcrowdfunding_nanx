package com.atnanx.atcrowdfunding.order.Config;

import com.google.common.base.Predicates;
import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "crowdfunding-order", name = "swagger2-enable", havingValue = "true")
public class SwaggerConfig {
    //${key:defaultValue}
   /* @Value("${swagger2.enable:false}")
    private boolean enable = false;*/

    @Bean("订单模块")
    public Docket projectApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("订单模块")
                .pathMapping("/")
                .select()// 选择那些路径和api会生成document
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))// 错误路径不监控
                .paths(PathSelectors.regex("/.*"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("尚筹网-系统平台接口文档")
                .description("提供用户模块/审核模块/项目模块/支付模块的文档")
                .termsOfServiceUrl("http://www.atguigu.com/")
                .version("1.0")
                .build();
    }

}
