package com.atnanx.atcrowdfunding.order.mapper;

import com.atnanx.atcrowdfunding.core.bean.TTransaction;
import com.atnanx.atcrowdfunding.core.bean.TTransactionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TTransactionMapper {
    long countByExample(TTransactionExample example);

    int deleteByExample(TTransactionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TTransaction record);

    int insertSelective(TTransaction record);

    List<TTransaction> selectByExample(TTransactionExample example);

    TTransaction selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TTransaction record, @Param("example") TTransactionExample example);

    int updateByExample(@Param("record") TTransaction record, @Param("example") TTransactionExample example);

    int updateByPrimaryKeySelective(TTransaction record);

    int updateByPrimaryKey(TTransaction record);
}