package com.atnanx.atcrowdfunding.order.service.feign;

import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectReturnDetailVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/project")
@FeignClient("ATCROWDFUNDING-PROJECT")
public interface ProjectFeignService {


    @GetMapping("/return/info")
    ServerResponse<ProjectReturnDetailVo> getReturnInfo(@RequestParam("returnId") Integer returnId);

    @PostMapping("/operation/update_return")
    ServerResponse reduceProjectReturnStock(@RequestParam("rtnCount") Integer rtnCount,
                                            @RequestParam("returnId") Integer returnId,
                                            @RequestParam("accessToken") String accessToken);


}
