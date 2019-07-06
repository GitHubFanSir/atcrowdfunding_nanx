package com.atnanx.atcrowdfunding.project.vo;

import com.atnanx.atcrowdfunding.core.bean.TReturn;
import com.atnanx.atcrowdfunding.core.vo.req.project.BaseVo;
import lombok.Data;

import java.util.List;


@Data
public class ProjectRedisStorageVo extends BaseVo {


    Integer memberid;

    /**
     *   第一屏的信息
     */
    //项目的分类id
    private List<Integer> typeids;
    //项目的标签id
    private List<Integer> tagids;
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
    //支持者数量
    private Integer supporter;
    //完成度
    private Integer completion;
    //创建日期
    private String createdate;
    //关注者数量
    private Integer follower;


    //项目头部图片
    private String headerImage;
    //项目详情图片
    private List<String> detailsImage;
    //自我介绍
    private String selfintroduction;
    //详细的自我介绍
    private String detailselfintroduction;
    //自己的电话
    private String telphone;
    //客服电话
    private String hotline;

    /**
     * 第二屏的信息
     */
    private List<TReturn> projectReturns;//项目回报

    //发起人信息：自我介绍，详细自我介绍，联系电话，客服电话

}
