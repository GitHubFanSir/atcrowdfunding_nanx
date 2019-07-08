package com.atnanx.atcrowdfunding.project.mapper;

import com.atnanx.atcrowdfunding.core.bean.TMemberPhoneCode;
import com.atnanx.atcrowdfunding.core.bean.TMemberPhoneCodeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TMemberPhoneCodeMapper {
    long countByExample(TMemberPhoneCodeExample example);

    int deleteByExample(TMemberPhoneCodeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TMemberPhoneCode record);

    int insertSelective(TMemberPhoneCode record);

    List<TMemberPhoneCode> selectByExample(TMemberPhoneCodeExample example);

    TMemberPhoneCode selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TMemberPhoneCode record, @Param("example") TMemberPhoneCodeExample example);

    int updateByExample(@Param("record") TMemberPhoneCode record, @Param("example") TMemberPhoneCodeExample example);

    int updateByPrimaryKeySelective(TMemberPhoneCode record);

    int updateByPrimaryKey(TMemberPhoneCode record);
}