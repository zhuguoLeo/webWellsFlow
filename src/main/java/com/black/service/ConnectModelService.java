package com.black.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface ConnectModelService {
    List getNodeCoordByLayer(int layerNum);
    List getWellNumByLayer(int layerNum);
    void makeNodeFile(int layerNum) throws IOException;
    void callTriangle(int layerNum, double areaConstraint, double angleConstraint) throws Exception;
    void editPoly(int layerNum,String pyhhonEdit);
    HashMap<String, ArrayList<List<Double>>> getInitMeshData(int layerNum) throws IOException;
    HashMap<String, ArrayList<List<Double>>> getPolyData(int layerNum) throws IOException;


}
