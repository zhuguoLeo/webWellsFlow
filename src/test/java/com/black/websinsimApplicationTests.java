package com.black;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.dao.DataBaseInitDao;
import com.black.dao.WellInitParamDao;
import com.black.dao.WellhistoryL;
import com.black.dao.file.FileDao;
import com.black.entity.WellInitParam;
import com.black.service.ConnectModelService;
import com.black.service.WellInitParamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class websinsimApplicationTests {
    @Autowired
    WellInitParamService wellInitParamService;
    @Autowired
    private WellInitParamDao wellInitParamDao;
    @Autowired
    DataBaseInitDao dataBaseInit;
    @Autowired
    ConnectModelService connectModelService;
    @Autowired
    WellhistoryL wellhistoryL;

    @Test
    void TestPage() {
        Page page = wellInitParamDao.selectPage(new Page(1, 10), null);
        System.out.println("wellInitParamDao.selectCount(null) = " + wellInitParamDao.selectCount(null));
//        System.out.println(page.getRecords());
    }

    @Test
        //这个方法根据WellInitParam表的井名生成对应的【层内单井】历史数据表
    void createLayerQTable(){
        QueryWrapper<WellInitParam> qw = new QueryWrapper<WellInitParam>().select("layer_num");
        List<WellInitParam> wellInitParams = wellInitParamDao.selectList(qw);
        List<String> layerNumList = wellInitParams.stream().distinct().map((e) -> e.getLayerNum()).collect(Collectors.toList());//判断有几层的数据
        Map<String, List<String>> layerToWellNamesMap = new HashMap<>();
        Map<String, String> layerTowellNameFieldSQL = new HashMap<>();
        for (String s : layerNumList) {
            QueryWrapper<WellInitParam> qw1 = new QueryWrapper<WellInitParam>().eq("layer_num", s);
            List<WellInitParam> wellInitParams1 = wellInitParamDao.selectList(qw1);
            List<String> wellNameSInLayer = wellInitParams1.stream().map((e) -> e.getWellName()).collect(Collectors.toList());
            layerToWellNamesMap.put(s,wellNameSInLayer);
        }//把层和井对应上
        for (String k : layerToWellNamesMap.keySet()) {
            StringBuilder wellNameFieldSQL = new StringBuilder();
            for (String s1 : layerToWellNamesMap.get(k).stream().map((e) -> e + " float DEFAULT NULL").collect(Collectors.toList())) {
                wellNameFieldSQL.append(s1);
                wellNameFieldSQL.append(",");
            }
            wellNameFieldSQL.delete(wellNameFieldSQL.length()-1,wellNameFieldSQL.length());//去掉最后一个逗号
            layerTowellNameFieldSQL.put(k,wellNameFieldSQL.toString());
        }//
        for (String s : layerTowellNameFieldSQL.keySet()) {
            String tableName="q_"+s;
            System.out.println(layerTowellNameFieldSQL.get(s));
            dataBaseInit.createProdPerformanceTable(tableName,layerTowellNameFieldSQL.get(s));
        }
    }
    @Test
    //这个方法根据WellInitParam表的井名生成对应的单井历史数据表
    void createPerformanceTable(){

        List<String> wellNameList = wellInitParamService.getAllWellNames();
        List<String> collect = wellNameList.stream().map((e) -> e + " float DEFAULT NULL").collect(Collectors.toList());
        StringBuilder wellNameFieldSQL = new StringBuilder();
        for (String s : collect) {
            wellNameFieldSQL.append(s);
            wellNameFieldSQL.append(",");
        }
        wellNameFieldSQL.delete(wellNameFieldSQL.length()-1,wellNameFieldSQL.length());
        dataBaseInit.createProdPerformanceTable("wopr_history",wellNameFieldSQL.toString());
        dataBaseInit.createProdPerformanceTable("wopr_history",wellNameFieldSQL.toString());
        dataBaseInit.createProdPerformanceTable("wwct_history",wellNameFieldSQL.toString());
        dataBaseInit.createProdPerformanceTable("wopr_insim",wellNameFieldSQL.toString());
        dataBaseInit.createProdPerformanceTable("wbhp_insim",wellNameFieldSQL.toString());
        dataBaseInit.createProdPerformanceTable("wwct_insim",wellNameFieldSQL.toString());
        System.out.println(wellNameFieldSQL);
    }
    @Test
    //用map来接收数据库内容
    void getTableToMap(){
        List<Map<String, Object>> all = wellhistoryL.getAll();
        System.out.println(all);
    }
    @Test
        //        用来测试建表语句;
    void tableCreateAnnotaion(){
        String SQL="W401 float DEFAULT NULL";
        dataBaseInit.createProdPerformanceTable("test_table3",SQL);

    }
    @Test
    void initTest(){
        dataBaseInit.changeAllWellNames();
        dataBaseInit.addPrefix("w");
    }
    //给井名加前缀W 替换 - 为 _
    @Test
    void getPageTest(){
        Integer currentPage = 1;
        Page page = wellInitParamDao.selectPage(new Page(currentPage, 10), null);
        List<WellInitParam> records = page.getRecords();
        System.out.println(records);
    }
    @Test
    void deleteTable(){
        dataBaseInit.deleteTable("test_table3");
    }
    @Test
    void makeFile() throws Exception {
        FileDao fileDao = new FileDao();
        fileDao.initUserWorkSpace("mike");
        fileDao.callTriangle(1,18000,20);
    }
    @Test
    void turnEleToPoly() throws IOException {
        FileDao fileDao = new FileDao();
        fileDao.initUserWorkSpace("mike");
        fileDao.turnEleToPoly(1);
    }
    @Test
    void editPoly() throws IOException {
        FileDao fileDao = new FileDao();
        fileDao.initUserWorkSpace("mike");
        fileDao.editPoly(1,"p.deletePoint(11)");
    }
    @Test
    void readPolyFile() throws IOException {
        FileDao fileDao = new FileDao();
        fileDao.initUserWorkSpace("mike");
        fileDao.readPolyFile(1);
    }

    @Test
    void listTest(){
        List<String> allWellNames = wellInitParamService.getAllWellNames();
        int size = wellhistoryL.getAll().size();
        List all = new ArrayList<List<Integer>>();
        allWellNames.forEach((name)->{
            List<Integer> colByWellName = wellhistoryL.getColByWellName(name);
            all.add(colByWellName);
        });

//        System.out.println(all);
        List Rlist = new ArrayList<>();

        for (int i=0;i<size;i++){
            int e=0;
            for (int i1 = 0; i1 < all.size(); i1++) {
                List<Integer> o1 = (List<Integer>) all.get(i1);
                e=e+o1.get(i);
            }
            Rlist.add(e);
        }
        System.out.println(Rlist);


    }


}



