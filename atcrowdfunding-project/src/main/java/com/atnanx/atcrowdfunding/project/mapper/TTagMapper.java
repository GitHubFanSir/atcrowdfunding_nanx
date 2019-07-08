package com.atnanx.atcrowdfunding.project.mapper;

import com.atnanx.atcrowdfunding.core.bean.TTag;
import com.atnanx.atcrowdfunding.core.bean.TTagExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TTagMapper {
    long countByExample(TTagExample example);

    int deleteByExample(TTagExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TTag record);

    int insertSelective(TTag record);

    List<TTag> selectByExample(TTagExample example);

    TTag selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TTag record, @Param("example") TTagExample example);

    int updateByExample(@Param("record") TTag record, @Param("example") TTagExample example);

    int updateByPrimaryKeySelective(TTag record);

    int updateByPrimaryKey(TTag record);
}