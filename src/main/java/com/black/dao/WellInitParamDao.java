package com.black.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.black.entity.WellInitParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface WellInitParamDao extends BaseMapper<WellInitParam> {


}
