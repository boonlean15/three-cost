package com.rjgf.threecost.crawdata.controller;

import com.alibaba.excel.EasyExcel;
import com.rjgf.threecost.crawdata.entity.CrawDataResultExcel;
import com.rjgf.threecost.crawdata.listener.ResultExcelListener;
import com.rjgf.threecost.crawdata.mapper.SbDataMapper;
import com.rjgf.threecost.crawdata.service.CrawDataResultExcelService;
import com.rjgf.threecost.crawdata.util.BaiduCoordinateConvertUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * @author linch
 * @create 2022/2/28 17:03
 */
@RestController
@RequestMapping("/craw")
public class CrawDataController {

    @Resource
    BaiduCoordinateConvertUtils baiduCoordinateConvertUtils;

    @Resource
    CrawDataResultExcelService crawDataResultExcelService;

    @Resource
    SbDataMapper sbDataMapper;

    @RequestMapping("/trans")
    public void transBD(){
        String fileName = "H:\\润建\\三费\\三费表和数据\\广东省小区平方米价格和平均租金20220207.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取3000条数据 然后返回过来 直接调用使用数据就行
        //输出设计大表 和pmis表
        EasyExcel.read(fileName, CrawDataResultExcel.class, new ResultExcelListener(baiduCoordinateConvertUtils, crawDataResultExcelService)).sheet().doRead();
    }

    @RequestMapping("/hello")
    public String getHello(){
        return "Hello, Lin Chuang Wei";
    }

    @RequestMapping("/insertMap")
    public String insertMap(){
        Map<String, String> sbData = new HashMap<>(4);
        sbData.put("1","v1");
        sbData.put("2","v2");
        sbData.put("3","v3");
        sbData.put("4","v4");
        sbDataMapper.insertMapData(sbData);
        return "添加成功";
    }

    @RequestMapping("testMappp")
    public String hotfixMyProblem(){
        return "ful";
    }

    @RequestMapping("/justSoS")
    public String justSoS(){

        return "just f y";
    }



}
