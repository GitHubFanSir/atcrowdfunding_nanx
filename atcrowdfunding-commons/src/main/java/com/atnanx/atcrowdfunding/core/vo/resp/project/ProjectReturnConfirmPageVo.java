package com.atnanx.atcrowdfunding.core.vo.resp.project;

import com.atnanx.atcrowdfunding.core.bean.TReturn;
import lombok.Getter;
import lombok.Setter;


public class ProjectReturnConfirmPageVo {
    @Setter @Getter
    private ProjectAllAllInfoVo project;
    @Setter @Getter
    private TReturn currentReturn;

    @Getter
    @Setter
    private String name;
    @Setter @Getter
    private String memberName;
    @Setter @Getter
    private String content;//回报的内容
    @Setter @Getter
    private Integer num;//购买了几个回报
    @Setter @Getter
    private Integer supportmoney; //单个指定回报需要的金额
    @Setter @Getter
    private Integer freight;

    private Integer totalPrice;

    public Integer getTotalPrice() {
        if(num!=null){
            totalPrice = num*supportmoney+freight;
        }else {
            totalPrice = 0;
        }
        return totalPrice;
    }
}
