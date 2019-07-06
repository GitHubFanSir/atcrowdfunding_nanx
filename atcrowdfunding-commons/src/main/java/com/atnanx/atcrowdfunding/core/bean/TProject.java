package com.atnanx.atcrowdfunding.core.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TProject {

    private Integer id;

    //项目名称
    private String name;
    //项目简介
    private String remark;
    //筹资金额
    private Long money;
    //筹资天数
    private Integer day;
    //0 - 即将开始， 1 - 众筹中， 2 - 众筹成功， 3 - 众筹失败
    private String status;
    //发布日期
    private String deploydate;


    //支持金额
    private Long supportmoney;
    //支持者数量
    private Integer supporter;
    //完成度
    private Integer completion;
    //创建日期
    private String createdate;
    //关注者数量
    private Integer follower;

    private Integer memberid;



}