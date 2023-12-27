package com.black.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.dao.WellInitParamDao;
import com.black.entity.Layer;
import com.black.entity.WellInitParam;
import com.black.response.ResponseData;
import com.black.service.WellInitParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: initVueProject
 * @description:
 * @author: Va1m
 * @create: 2022-03-17 15:44
 */
@Service
public class WellInitParamServiceImpl implements WellInitParamService {
    @Autowired
    WellInitParamDao wellInitParamDao;
    @Override
    public List<WellInitParam> getWellsByLayer(Integer layerNum) {
        //校验层数合法性
        if(layerNum == null || layerNum < 1){
            throw new RuntimeException("layerNum illegal!");
        }
        LambdaQueryWrapper<WellInitParam> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WellInitParam::getLayerNum, layerNum);
        List<WellInitParam> wellInitParams = wellInitParamDao.selectList(lambdaQueryWrapper.orderByAsc(WellInitParam::getWellIndexInLayer));
//        查询结果按照层内序号升序
        return wellInitParams;
    }

    @Override
    public List<WellInitParam> getWellsByPage(Integer currentPage) {
        Page page = wellInitParamDao.selectPage(new Page(currentPage, 10), null);
        List<WellInitParam> records = page.getRecords();
        return records;
    }

    @Override
    public int deleteWellById(Integer wellId) {
        int i = wellInitParamDao.deleteById(wellId);
        return i;
    }

    @Override
    public int deleteWellByWellName(String wellName) {
        LambdaQueryWrapper<WellInitParam> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WellInitParam::getWellName, wellName);
        int delete = wellInitParamDao.delete(lambdaQueryWrapper);
        return delete;
    }

    @Override
    public Integer getTotalPageNum() {
        return wellInitParamDao.selectCount(null);
    }

    @Override
    public int addWell(WellInitParam wellInitParam) {
        int insert = wellInitParamDao.insert(wellInitParam);
        return insert;
    }

    @Override
    public List<String> getAllWellNames() {
        QueryWrapper<WellInitParam> qw = new QueryWrapper<WellInitParam>().select("well_name");
        List<WellInitParam> wellInitParams = wellInitParamDao.selectList(qw);
        List<String> wellNameList = wellInitParams.stream().distinct().map((e) -> e.getWellName()).collect(Collectors.toList());
        return wellNameList;
    }
}
