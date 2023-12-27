package com.black.controller;

import com.black.response.ResponseData;
import com.black.service.ConnectModelService;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ConnectModel")
public class ConnectModelController {
    private static final Logger log = LoggerFactory.getLogger(ConnectModelController.class);
    @Autowired
    ConnectModelService connectModelService;
    @PostMapping("/mesnInit")
    ResponseData mensInit(@RequestBody Map<String,String> reqMap) throws IOException {
        try {
            int layerNum=Integer.parseInt(reqMap.get("layerNum"));
            double areaConstraint=Double.parseDouble(reqMap.get("areaConstraint"));
            double angleConstraint=Double.parseDouble(reqMap.get("angleConstraint"));
            connectModelService.makeNodeFile(layerNum);
            connectModelService.callTriangle(layerNum,areaConstraint,angleConstraint);
            return ResponseData.ok("ok");
        }catch (Exception e){
            return ResponseData.fail(e.getMessage());
        }

    }
    @PostMapping("/turnEleToPoly")
    ResponseData turnEleToPoly(@RequestBody Map<String,String> reqMap ){
        int layerNum=Integer.valueOf(reqMap.get("layerNum"));
        try {
            return ResponseData.ok(connectModelService.getInitMeshData(layerNum));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.fail(e.toString());
        }
    }
    @PostMapping("loadPoly")
    ResponseData loadPoly (@RequestBody Map<String,String> reqMap){
        log.warn("loadPoly 运行了");
        int layerNum=Integer.valueOf(reqMap.get("layerNum"));
        try {
            return ResponseData.ok(connectModelService.getPolyData(layerNum));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.fail(e.toString());
        }

    }
    @PostMapping("/editPoly")
    ResponseData editPoly(@RequestBody Map<String,String> reqMap ){
        int layerNum=Integer.valueOf(reqMap.get("layerNum"));
        String edit = reqMap.get("edit");
        try{
            connectModelService.editPoly(layerNum,edit);
            return ResponseData.ok(null);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseData.fail(e.toString());
        }
    }
}
