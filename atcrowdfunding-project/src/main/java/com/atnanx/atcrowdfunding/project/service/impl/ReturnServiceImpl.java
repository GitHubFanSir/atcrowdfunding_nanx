package com.atnanx.atcrowdfunding.project.service.impl;

import com.atnanx.atcrowdfunding.core.bean.TProject;
import com.atnanx.atcrowdfunding.core.bean.TReturn;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectReturnDetailVo;
import com.atnanx.atcrowdfunding.project.mapper.TProjectMapper;
import com.atnanx.atcrowdfunding.project.mapper.TReturnMapper;
import com.atnanx.atcrowdfunding.project.service.IReturnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReturnServiceImpl implements IReturnService {

    @Autowired
    private TReturnMapper returnMapper;
    @Autowired
    private TProjectMapper projectMapper;

    @Override
    public ServerResponse getDetail(Integer returnId) {
        TReturn tReturn = returnMapper.selectByPrimaryKey(returnId);

        ProjectReturnDetailVo projectReturnDetailVo = assemReturnDetail(tReturn);

        return ServerResponse.createBySuccess("查找对应回报信息成功",projectReturnDetailVo);
    }

    @Override
    public ServerResponse reduceProjectReturnStock(Integer rtnCount, Integer returnId) {
        //?造成死锁问题
        int count = returnMapper.selectCountById(returnId);
        int stock = count - rtnCount;
        if (stock<0){
            return ServerResponse.createByErrorMessage(returnId+"回报的库存到临界值，无法扣减"+rtnCount+"数量");
        }
        TReturn tReturn = new TReturn();
        tReturn.setId(returnId);
        tReturn.setCount(stock);
        int updateResult = returnMapper.updateByPrimaryKeySelective(tReturn);
//        int updateResult = returnMapper.reduceProjectReturnStock(rtnCount,returnId);
        if (updateResult<=0){
            return ServerResponse.createByErrorMessage("更新回报库存失败");
        }
        return ServerResponse.createBySuccessMessage("更新回报库存成功");
    }

    public ProjectReturnDetailVo assemReturnDetail(TReturn tReturn){
        ProjectReturnDetailVo projectReturnDetailVo = new ProjectReturnDetailVo();
        BeanUtils.copyProperties(tReturn,projectReturnDetailVo);
        TProject project = projectMapper.selectByPrimaryKey(tReturn.getProjectid());
        projectReturnDetailVo.setProjectName(project.getName());
        projectReturnDetailVo.setProjectRemark(project.getRemark());
        return projectReturnDetailVo;

    }

}
