package com.black.controller;

import com.black.dao.WellhistoryL;
import com.black.response.ResponseData;
import com.black.service.WellInitParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {
    private static final Logger log = LoggerFactory.getLogger(HistoryController.class);
    @Autowired
    WellInitParamService wellInitParamService;
    @Autowired
    WellhistoryL wellhistoryL;
    @GetMapping("/L")
    ResponseData getHistoryL(){
        try{
            return ResponseData.ok(wellhistoryL.getAll());
        }catch (Exception e){
            return ResponseData.fail(e.getMessage());
        }
    }
    @GetMapping("/getLcol")
    ResponseData getHistoryLByWellName(String wellName){
        try{
            List<Integer> colByWellName = wellhistoryL.getColByWellName(wellName);
            List<String> dateCol = wellhistoryL.getDateCol();
            List<? extends List<? extends Serializable>> R = Arrays.asList(dateCol,colByWellName );

            return ResponseData.ok(R);
        }catch (Exception e){
            return ResponseData.fail(e.getMessage());
        }
    }
    @GetMapping("getSumL")
    ResponseData getSumL(){
        try{
            List<String> allWellNames = wellInitParamService.getAllWellNames();
            int size = wellhistoryL.getAll().size();
            List all = new ArrayList<List<Integer>>();
            allWellNames.forEach((name)->{
                List<Integer> colByWellName = wellhistoryL.getColByWellName(name);
                all.add(colByWellName);
            });

            List Rlist = new ArrayList<>();

            for (int i=0;i<size;i++){
                int e=0;
                for (int i1 = 0; i1 < all.size(); i1++) {
                    List<Integer> o1 = (List<Integer>) all.get(i1);
                    e=e+o1.get(i);
                }
                Rlist.add(e);
            }
            List<String> dateCol = wellhistoryL.getDateCol();
            List<List> R = Arrays.asList(dateCol, Rlist);
            return ResponseData.ok(R);
        }catch (Exception e){
            return ResponseData.fail(e.getMessage());
        }

    }


}
