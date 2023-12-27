package com.black.service;


import com.black.entity.Layer;
import com.black.entity.WellInitParam;

import java.util.List;

/**
 * @program: initVueProject
 * @description: 业务层
 * @author: Va1m
 * @create: 2022-03-17 15:40
 */
public interface WellInitParamService {
    /**
     * 根据层数获取该层油井信息
     * @return
     * @param layerNum
     */
    List<WellInitParam> getWellsByLayer(Integer layerNum);
    List<WellInitParam> getWellsByPage(Integer currentPage);
    int deleteWellById(Integer wellId);
    int deleteWellByWellName(String wellName);
    Integer getTotalPageNum();
    int addWell(WellInitParam wellInitParam);
    List<String> getAllWellNames();
}
