package com.black.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Value;

@Data
@TableName("well_init_param")
//这个对象只会在初始时设置一次，所有的计算共用，没有大量的读写
public class WellInitParam {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String layerNum;
    private String wellIndexInLayer;
    private String wellName;
    private Integer wellNum;
    private Float x;
    private Float y;
    private Float h;
    private Float poro;
    private Float initPressure;
    private Float initSw;
    private Float k;
    private Integer wellType;
}
