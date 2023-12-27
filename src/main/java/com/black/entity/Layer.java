package com.black.entity;

import lombok.Data;

import java.awt.geom.FlatteningPathIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * 层相关数据
 */
@Data
public class Layer {
    /**
     * 层名
     */
    private Integer layerNum;
    private ArrayList<Well> wellsInLayer;
}
