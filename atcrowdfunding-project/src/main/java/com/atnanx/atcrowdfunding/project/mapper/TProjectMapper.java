package com.atnanx.atcrowdfunding.project.mapper;

import com.atnanx.atcrowdfunding.core.bean.TProject;
import com.atnanx.atcrowdfunding.core.bean.TProjectExample;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectAllInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TProjectMapper {
    long countByExample(TProjectExample example);

    int deleteByExample(TProjectExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TProject record);

    int insertSelective(TProject record);

    List<TProject> selectByExample(TProjectExample example);

    TProject selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TProject record, @Param("example") TProjectExample example);

    int updateByExample(@Param("record") TProject record, @Param("example") TProjectExample example);

    int updateByPrimaryKeySelective(TProject record);

    int updateByPrimaryKey(TProject record);

    List<ProjectAllInfoVo> getAllProjectsInfos();
}