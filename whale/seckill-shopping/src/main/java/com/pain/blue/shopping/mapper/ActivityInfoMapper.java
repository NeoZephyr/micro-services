package com.pain.blue.shopping.mapper;

import com.pain.blue.shopping.domain.pojo.ActivityInfo;
import com.pain.blue.shopping.domain.pojo.ActivityInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActivityInfoMapper {
    long countByExample(ActivityInfoExample example);

    int deleteByExample(ActivityInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ActivityInfo record);

    int insertSelective(ActivityInfo record);

    List<ActivityInfo> selectByExample(ActivityInfoExample example);

    ActivityInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ActivityInfo record, @Param("example") ActivityInfoExample example);

    int updateByExample(@Param("record") ActivityInfo record, @Param("example") ActivityInfoExample example);

    int updateByPrimaryKeySelective(ActivityInfo record);

    int updateByPrimaryKey(ActivityInfo record);

    ActivityInfo selectOpenedByProductId(@Param("productId") String productId);

    int updateStatus(@Param("id") Long id, @Param("status") Byte status);
}