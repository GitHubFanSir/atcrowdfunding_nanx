package com.atnanx.atcrowdfunding.project.service;

import com.atnanx.atcrowdfunding.core.common.ServerResponse;

public interface IReturnService {

    ServerResponse getDetail(Integer returnId);

    ServerResponse reduceProjectReturnStock(Integer rtnCount, Integer returnId);
}
