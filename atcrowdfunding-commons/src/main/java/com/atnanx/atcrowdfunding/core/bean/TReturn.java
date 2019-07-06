package com.atnanx.atcrowdfunding.core.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TReturn {

    /*
     */
    private Integer id;
    //项目ID
    private Integer projectid;
    //0 - 实物回报， 1 虚拟物品回报
    private String type;
    //支持金额
    private Integer supportmoney;
    //回报内容
    private String content;
    //0 为不限回报数量
    private Integer count;
    //单笔限购
    private Integer signalpurchase;
    //限购数量
    private Integer purchase;
    //运费
    private Integer freight;
    //0 - 不开发票， 1 - 开发票
    private String invoice;
    //回报时间,众筹成功后多少天进行回报
    private Integer rtndate;

}