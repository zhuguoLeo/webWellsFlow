package com.black.dao.file;
import com.black.controller.ConnectModelController;

import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


//应该在电脑上放c文件，部署在服务器上编译好，这里省事直接部署上了编译后的exe文件
public class FileDao
{
    private static final Logger log = LoggerFactory.getLogger(ConnectModelController.class);
    private String userName="mike";//默认的测试用户名为mike
    boolean isinitUserSpace=false;
    String rootSpaceName="TriangleWorkSpace";
    private String  PYTHONPath="C:\\Users\\Administrator\\AppData\\Local\\Programs\\Python\\Python310\\python.exe";

    private String getTargetPath(){//这个方法返回部署目录的地址
        String realClassPath = FileDao.class.getClass().getResource("/").getPath();
        String[] split = realClassPath.split("/");
        String[] targetPathArray = Arrays.copyOfRange(split, 0, split.length - 1);
        StringBuilder targetPath = new StringBuilder();
        for (String s : targetPathArray) {
            targetPath.append(s);
            targetPath.append("/");
        }
        return String.valueOf(targetPath);
    }
    public void initUserWorkSpace(String userName){
        this.userName=userName;
        boolean mkrootWorkSpacedir = new File(getTargetPath() + rootSpaceName).mkdir();//如果没文件夹就创建Triangle文件夹
        boolean mkUserWorkSpacedir = new File(getTargetPath() +rootSpaceName+ "/" + userName).mkdir();
        boolean mkINSIMrWorkSpacedir = new File(getTargetPath() +rootSpaceName+ "/" + userName+'/'+ "meshFileForINSIM").mkdir();
        isinitUserSpace=true;
    }//初始化“用户工作空间”的时候会把用户名传入到FileDao实例中
    public void makeNodeFile(int layerNum,List<List<Float>> coordinateList) throws IOException {
        assert isinitUserSpace;
        String userSpacePath=getTargetPath()+rootSpaceName+"/"+userName+"/";
        File nodeFile = new File(userSpacePath+"/"+"layer"+layerNum+".node");//文件名以层名为
         FileWriter fileWriter = new FileWriter(nodeFile);
         String[] nodeFileHead={coordinateList.size()+"","2","0","0"};//node文件的文件头
         Arrays.stream(nodeFileHead).forEach((i)->{
             try {
                 fileWriter.write(i+" ");
             } catch (IOException e) {
                 e.printStackTrace();
             }
         });
         fileWriter.write("\n");
         for (int i = 0; i < coordinateList.size(); i++) {
             fileWriter.write(((i+1)+"")+"    "+(coordinateList.get(i).get(0)+"")+"  "+(coordinateList.get(i).get(1)+"")+"\n");
         }
         fileWriter.flush();
        fileWriter.close();
     }
    public void callTriangle(int layerNum, double areaConstraint, double angleConstraint) throws Exception {
        String nodeFilePath=(getTargetPath() +rootSpaceName+ "/"+userName+"/"+"layer"+layerNum+".node").replace("/","\\").substring(1);
        File nodeFile = new File(nodeFilePath);
        //=======================根据参数执行Triangle
        if (!(nodeFile.exists())){
            log.error("can't find"+nodeFilePath+"\n"+"PleaseCheck");
            throw new Exception("can't find"+nodeFilePath+"\n"+"PleaseCheck");

        }
//当Runtime.getRuntime().exec参数为为数组时候，为正常运行Triangle.exe
// 元素应该分别为1.执行的程序 2.参数 3.被程序处理的文件（如果有）
//                String[] arg={"G:\\LZGsProject\\webinsim\\target\\TriangleWorkSpace\\"+"triangle.exe","v",""};
        if (areaConstraint==-1&&angleConstraint==-1)
        {
            try {
                String[] arg={getTargetPath() +rootSpaceName+ "/"+"triangle.exe","",nodeFilePath};
                //dos命令中的地址必须是反斜杠，这里做处理
                Process pr=Runtime.getRuntime().exec(arg);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        pr.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) { log.warn("TriangleWorking:"+line); }
                in.close();
                pr.waitFor();
                }
            catch (Exception e)
                {
                    e.printStackTrace();
                }
        }
        else if(areaConstraint>0&&angleConstraint==-1)
        {
            try
            {
                String[] arg={getTargetPath() +rootSpaceName+ "/"+"triangle.exe","-a"+areaConstraint,nodeFilePath};
                //dos命令中的地址必须是反斜杠，这里做处理
                Process pr=Runtime.getRuntime().exec(arg);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        pr.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) { log.warn("TriangleWorking:"+line); }
                in.close();
                pr.waitFor();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(areaConstraint==-1&&angleConstraint>0)
        {
            try
            {
                String[] arg={getTargetPath() +rootSpaceName+ "/"+"triangle.exe","-q"+angleConstraint,nodeFilePath};
                //dos命令中的地址必须是反斜杠，这里做处理
                Process pr=Runtime.getRuntime().exec(arg);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        pr.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) { log.warn("TriangleWorking:"+line); }
                in.close();
                pr.waitFor();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(areaConstraint>0&&angleConstraint>0)
        {
            try
            {
                String[] arg={getTargetPath() +rootSpaceName+ "/"+"triangle.exe","-q"+angleConstraint+"a"+areaConstraint,nodeFilePath};
                //dos命令中的地址必须是反斜杠，这里做处理
                Process pr=Runtime.getRuntime().exec(arg);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        pr.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) { log.warn("TriangleWorking:"+line); }
                in.close();
                pr.waitFor();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else {
            log.error("wrrong Triangle param,check it!");
            throw new Exception("wrrong Triangle param,check it!");
        }
    }
    public void turnEleToPoly(int layerNum){//这个功能是靠python程序实现的，将node和ele文件转换为poly文件能进行网格的编辑，
        String pythonProgramPath="C:\\Users\\Administrator\\Desktop\\网格操作包\\caseForCall\\makePoly.py";
        String eleFilePath=(getTargetPath() +rootSpaceName+ "/"+userName+"/"+"layer"+layerNum+".1.ele").replace("/","\\").substring(1);
        String nodeFilePath=(getTargetPath() +rootSpaceName+ "/"+userName+"/"+"layer"+layerNum+".1.node").replace("/","\\").substring(1);

        File pythonProgram = new File(pythonProgramPath);
        File eleFile = new File(eleFilePath);
        File nodeFile = new File(nodeFilePath);
        File PYTHON = new File(PYTHONPath);
        if ((!pythonProgram.exists())){
            log.error("FileDao.turnEleToPoly can't find "+pythonProgramPath);
            return;}
        if ((!eleFile.exists())){
            log.error("FileDao.turnEleToPoly can't find "+eleFilePath);
            return;}
        if ((!nodeFile.exists())){
            log.error("FileDao.turnEleToPoly can't find "+nodeFilePath);
            return;}
        if ((!PYTHON.exists())){
            log.error("FileDao.turnEleToPoly can't find "+PYTHONPath);
            return;}

//上面都是在做文件存在判断
        try {
            String userSpacePath=getTargetPath()+rootSpaceName+"/"+userName+"/";
            String polyFilePath=userSpacePath+"layer"+layerNum+".1.poly";
            String targetFile=polyFilePath.replace("/","\\").substring(1);//python程序已经
//            String polyEdit="";//这里是注入的python命令，可以对poly文件进行编辑
            String[] arg={PYTHONPath,pythonProgramPath,"-eleFile",eleFilePath,"-nodeFile",nodeFilePath,"-targetFile",targetFile};
            //dos命令中的地址必须是反斜杠，这里做处理
            Process pr=Runtime.getRuntime().exec(arg);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                log.warn(("PythonWorking:" + line));//python程序的运行情况在这里打印
            }
            in.close();
            pr.waitFor();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void editPoly(int layerNum ,String pythonEdit){
        String pythonProgramPath="C:\\Users\\Administrator\\Desktop\\网格操作包\\caseForCall\\editPoly.py";

        String polyFilePath=(getTargetPath() +rootSpaceName+ "/"+userName+"/"+"layer"+layerNum+".1.poly").replace("/","\\").substring(1);
        File pythonProgram = new File(pythonProgramPath);
        File PYTHON = new File(PYTHONPath);
        File polyFil = new File(polyFilePath);
        if ((!PYTHON.exists())){
            log.error("FileDao.turnEleToPoly can't find "+PYTHONPath);
            return;}
        if ((!polyFil.exists())){
            log.error("FileDao.turnEleToPoly can't find "+polyFilePath);
            return;}
        if ((!pythonProgram.exists())){
            log.error("FileDao.turnEleToPoly can't find "+pythonProgramPath);
            return;}
        try {
            String[] arg={PYTHONPath,pythonProgramPath,"-polyFile",polyFilePath,"-polyEdit",pythonEdit,"-targetFile",polyFilePath};
//            String[] arg={PYTHONPath,pythonProgramPath};
            //dos命令中的地址必须是反斜杠，这里做处理
            Process pr=Runtime.getRuntime().exec(arg);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                log.warn(("PythonWorking:" + line));//python程序的运行情况在这里打印
            }
            in.close();
            pr.waitFor();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public HashMap<String, ArrayList<List<Double>>> readPolyFile(int layerNum) throws IOException {
        String userSpacePath=getTargetPath()+rootSpaceName+"/"+userName+"/";
        String polyFilePath=userSpacePath+"layer"+layerNum+".1.poly";
        File polyFile = new File(polyFilePath);
        if (polyFile.exists()){
            BufferedReader bf = new BufferedReader(new FileReader(polyFilePath));
            String nodeHead=bf.readLine();//poly文件的第一行是nodeHead
            int nodeLinesCount = Integer.parseInt(nodeHead.split("\\s+")[0]);
            ArrayList<List<Double>> nodeCoordList = new ArrayList<>();//读取初始化Poly的点
            for (int i = 0; i < nodeLinesCount; i++) {
                List<Double> nodeCoord = Lists.newArrayList(bf.readLine().trim().split("\\s+"))//每一行的数据放在列表中
                        .stream().map((item)->Double.valueOf(item)).collect(Collectors.toList());//字符串转Double

//                nodeCoord.remove(0);
                //是否保留序号
                nodeCoordList.add(nodeCoord);
            }
            String segHead=bf.readLine();//segHead
            int segLinesCount = Integer.parseInt(segHead.split("\\s+")[0]);
            ArrayList<List<Double>> segList = new ArrayList<>();//线段初始化Poly的点
            for (int i = 0; i < segLinesCount; i++) {
                List<Double> seg = Lists.newArrayList(bf.readLine().trim().split("\\s+"))//每一行的数据放在列表中
                        .stream().map((item)->Double.valueOf(item)).collect(Collectors.toList());//字符串转Double
//                seg.remove(0);
                //是否保留序号
                segList.add(seg);
            }
            bf.close();
            //这个Map返回Poly文件内的信息，现在包含点和线，未来可能包含其他
            HashMap<String, ArrayList<List<Double>>> polyContent = new HashMap<>();
            polyContent.put("segList",segList);
            polyContent.put("nodeCoordList",nodeCoordList);
            return polyContent;
        }
        else {
            log.error("FileDap readPolyFile can't find "+polyFilePath);
            return null;
        }
    }
}
