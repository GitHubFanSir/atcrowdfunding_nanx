package com.atnanx.atcrowdfunding.order.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayConfig {
    private String appId;
    //异步通知
    private String notifyUrl;
    //同步通知
    private String returnUrl;
    //签名类型 RSA2
    private String signType;
    private String charset;
    //支付宝沙箱网关
    private String gatewayUrl;

    //RSA2商户私钥、公钥和支付宝公钥
    private String privateKey;
    private String publicKey;
    private String alipayPublicKey;

}
