package com.black.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface WellhistoryL {
    @Select("select * from wopr_history")
    List<Map<String,Object>> getAll();
    @Select("select ${wellName} from wopr_history")
    List<Integer> getColByWellName(@Param("wellName") String wellName);
    @Select("select date from wopr_history")
    List<String> getDateCol();


}
