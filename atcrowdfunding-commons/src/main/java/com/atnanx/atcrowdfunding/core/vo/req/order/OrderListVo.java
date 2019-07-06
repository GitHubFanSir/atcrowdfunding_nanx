package com.atnanx.atcrowdfunding.core.vo.req.order;

import com.atnanx.atcrowdfunding.core.bean.TOrder;
import com.atnanx.atcrowdfunding.core.bean.TReturn;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectAllAllInfoVo;
import lombok.Data;

@Data
public class OrderListVo {

    private TOrder order;
    private ProjectAllAllInfoVo project;
    private TReturn tReturn;


}
