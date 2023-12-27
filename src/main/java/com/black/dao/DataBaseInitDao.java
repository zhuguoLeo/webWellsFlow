package com.black.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.entity.WellInitParam;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
@Mapper
public interface DataBaseInitDao {
    @Update("UPDATE well_init_param SET well_name=replace(well_name, '-', '_');")
    void changeAllWellNames();
    @Update("UPDATE well_init_param SET well_name=concat(#{Prefix},well_name);")
    void addPrefix(@Param("Prefix") String Prefix);
    @Update("create table ${tableName}( id int PRIMARY KEY NOT NULL AUTO_INCREMENT," +  " date date(6) DEFAULT NULL," +
            " time_step int DEFAULT NULL,"+
            "${wellNameFieldSQL}"
            +")"
            )
    void createProdPerformanceTable(@Param("tableName") String tableName, @Param("wellNameFieldSQL") String wellNameFieldSQL);
    @Update("DROP TABLE ${tableName};")
    void deleteTable(@Param("tableName") String tableName);
}
