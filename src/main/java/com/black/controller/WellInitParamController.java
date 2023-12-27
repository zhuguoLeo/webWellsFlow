package com.black.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.dao.WellInitParamDao;
import com.black.entity.WellInitParam;
import com.black.response.ResponseData;
import com.black.service.WellInitParamService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/BaseData/WellInitParam")
public class WellInitParamController {

    @Autowired
    WellInitParamService wellInitParamService;
    @GetMapping("")
    public ResponseData loadPage( Integer currentPage){
        try{
            System.out.println("currentPage = " + currentPage);
            return ResponseData.ok(wellInitParamService.getWellsByPage(currentPage));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseData.fail(e.toString());}
    }
    @GetMapping("/getWellNames")
    ResponseData getWellNames(){
        try{
            return ResponseData.ok(wellInitParamService.getAllWellNames());
        }catch (Exception e){
            return ResponseData.fail(e.getMessage());
        }
    }
    @GetMapping("getTotal")
    public Integer getTotal(){
        return wellInitParamService.getTotalPageNum();
    }

    @GetMapping("/getWellsByLayer/{layerNum}")
    public ResponseData getWellsByLayer(@PathVariable(name = "layerNum") Integer layerNum){
        try{
            return ResponseData.ok(wellInitParamService.getWellsByLayer(layerNum));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseData.fail(e.toString());
        }
    }
    @PostMapping("addWell")
    public ResponseData addWell(@RequestBody WellInitParam wellInitParam){
        try{
            System.out.println(wellInitParam.toString());
            int i = wellInitParamService.addWell(wellInitParam);
            return ResponseData.ok(i);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseData.fail(e.toString());
        }
    }
    @PostMapping("deleteWell")
    public ResponseData deleteWell(@RequestBody Map<String,String> reqMap){
        try{
            assert reqMap.size()==1;
            //暂时只考虑一个参数
            if(reqMap.keySet().contains("wellId")){
                int wellId = Integer.parseInt(reqMap.get("wellId"));
                int i = wellInitParamService.deleteWellById(wellId);
                return ResponseData.ok(i);
            }
            else if(reqMap.keySet().contains("wellName")){
                int i = wellInitParamService.deleteWellByWellName(reqMap.get("wellName"));
                return ResponseData.ok(i);
            }
            else return ResponseData.fail("输入key有误，请检查");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseData.fail(e.toString());
        }

    }

}
