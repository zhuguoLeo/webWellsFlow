package com.black.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
/**
 * 油井相关数据
 */
public class Well {
    private String wellName;
    private Float x;
    private Float y;
    private Float h;
    private Float poro;
    private Float initPressure;
    private Float Sw;
    private Float k;

    public ArrayList<Float> toCoordinate(){
        return new ArrayList<Float>() {{
            add(x);
            add(y);
        }};
    }
}
