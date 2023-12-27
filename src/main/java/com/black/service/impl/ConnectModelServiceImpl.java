package com.black.service.impl;

import com.black.dao.file.FileDao;
import com.black.entity.WellInitParam;
import com.black.service.ConnectModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConnectModelServiceImpl implements ConnectModelService {
    private String userName="mike";//默认用户名为mike，这里应该调接口获得用户名称
    FileDao fileDao = new FileDao();//创建一个FileMaker实例对象
    Boolean initUserWorkSpace=false;
    @Autowired
    WellInitParamServiceImpl wellInitParamService;
    public List getNodeCoordByLayer(int layerNum) {
        List<WellInitParam> wellsByLayer = wellInitParamService.getWellsByLayer(layerNum);
        List<List<Float>> coordinateList = wellsByLayer.stream().map(t -> Arrays.asList(t.getX(), t.getY())).collect(Collectors.toList());
        return coordinateList;
    }

    @Override
    public List getWellNumByLayer(int layerNum) {
        List<WellInitParam> wellsByLayer = wellInitParamService.getWellsByLayer(layerNum);
        List<Integer> WellNum = wellsByLayer.stream().map(t -> t.getWellNum()).collect(Collectors.toList());
        return WellNum;
    }

    public void makeNodeFile(int layerNum) throws IOException {
        if (! initUserWorkSpace){fileDao.initUserWorkSpace(userName);}

        fileDao.makeNodeFile(layerNum,getNodeCoordByLayer(layerNum));
    }
    @Override
    public void callTriangle(int layerNum, double areaConstraint, double angleConstraint) throws Exception {
        fileDao.callTriangle(layerNum, areaConstraint, angleConstraint);//网格初始化，
    }

    @Override
    public void editPoly(int layerNum, String pyhhonEdit) {
        fileDao.editPoly(layerNum,pyhhonEdit);
    }

    @Override
    public HashMap<String, ArrayList<List<Double>>> getInitMeshData(int layerNum) throws IOException {
        fileDao.turnEleToPoly(layerNum);
        return fileDao.readPolyFile(layerNum);
    }

    @Override
    public HashMap<String, ArrayList<List<Double>>> getPolyData(int layerNum) throws IOException {
        return fileDao.readPolyFile(layerNum);
    }

}
