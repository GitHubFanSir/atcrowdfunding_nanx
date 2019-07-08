package com.atnanx.atcrowdfunding.project.service.feign;

import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/user")
@FeignClient("ATCROWDFUNDING-MEMBER")
public interface MemberFeignService {


    @PostMapping("/info/get_name")
    ServerResponse<String> getMemberName(@RequestParam("memberId") Integer memberId);




}
