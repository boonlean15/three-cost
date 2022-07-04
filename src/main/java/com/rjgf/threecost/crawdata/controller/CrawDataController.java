package com.rjgf.threecost.crawdata.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rjgf.threecost.crawdata.entity.CrawDataResultExcel;
import com.rjgf.threecost.crawdata.listener.ResultExcelListener;
import com.rjgf.threecost.crawdata.mapper.CrawDataResultExcelMapper;
import com.rjgf.threecost.crawdata.mapper.SbDataMapper;
import com.rjgf.threecost.crawdata.service.CrawDataResultExcelService;
import com.rjgf.threecost.crawdata.util.BaiduCoordinateConvertUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;


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
    CrawDataResultExcelMapper crawDataResultExcelMapper;

    @Resource
    SqlSessionFactory sqlSessionFactory;

    @Resource
    SbDataMapper sbDataMapper;

    @RequestMapping("/trans")
    public void transBD(){
        String fileName = "D:\\润建\\设计院\\三费\\三费\\三费表和数据\\广东省小区平方米价格和平均租金20220207.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取3000条数据 然后返回过来 直接调用使用数据就行
        //输出设计大表 和pmis表
        EasyExcel.read(fileName, CrawDataResultExcel.class, new ResultExcelListener(baiduCoordinateConvertUtils, crawDataResultExcelService)).sheet().doRead();
    }

    @RequestMapping("sqlTime")
    public void testSql(){
        long start = System.currentTimeMillis();
        List<CrawDataResultExcel> crawDataResultExcels = crawDataResultExcelMapper.selectList(new LambdaQueryWrapper<>());
        long end = System.currentTimeMillis();
        long total = end - start;
        System.out.println("sql ---------- 查询耗时 -------- " + total);
    }

    @RequestMapping("sqlCursor")
    public void testSqlCursor(HttpServletResponse response) throws Exception {
        long start = System.currentTimeMillis();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        sqlSession.getConnection().setAutoCommit(false);
        Cursor<CrawDataResultExcel> cursor = sqlSession.getMapper(CrawDataResultExcelMapper.class).HandlerResult();
        long end = System.currentTimeMillis();
        long total = end - start;
        System.out.println("sql cursor ---------- 查询耗时 -------- " + total);

        long write1 = System.currentTimeMillis();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), CrawDataResultExcel.class).build();
        WriteSheet sheet = EasyExcel.writerSheet(0,"baidu").build();

        Iterator<CrawDataResultExcel> iterator = cursor.iterator();
        List<CrawDataResultExcel> datas = new ArrayList<>();
        while (iterator.hasNext()) {
            CrawDataResultExcel next = iterator.next();
            datas.add(next);
            if(datas.size()==500){
                excelWriter.write(datas, sheet);
                datas.clear();
            }
        }
        excelWriter.write(datas, sheet);
        excelWriter.finish();
        long write2 = System.currentTimeMillis();
        long write = write2 - write1;
        System.out.println("cursor 写出excel耗时 -------- " + write);

    }

    @RequestMapping("writeTime")
    public void testWrite(HttpServletResponse response) throws IOException {
        List<CrawDataResultExcel> crawDataResultExcels = crawDataResultExcelMapper.selectList(new LambdaQueryWrapper<>());


        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        long start = System.currentTimeMillis();
        EasyExcel.write(response.getOutputStream(), CrawDataResultExcel.class).sheet("baidu").doWrite(crawDataResultExcels);
        long end = System.currentTimeMillis();
        long total = end - start;
        System.out.println("write ---------- excel耗时 -------- " + total);

    }

    @RequestMapping("writeTimeSplit")
    public void writeTimeSplit(HttpServletResponse response) throws IOException {
        List<CrawDataResultExcel> crawDataResultExcels = crawDataResultExcelMapper.selectList(new LambdaQueryWrapper<>());


        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        long start = System.currentTimeMillis();
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), CrawDataResultExcel.class).build();
        WriteSheet sheet = EasyExcel.writerSheet(0,"baidu").build();
        Iterator<CrawDataResultExcel> iterator = crawDataResultExcels.iterator();
        List<CrawDataResultExcel> datas = new ArrayList<>();
        while (iterator.hasNext()){
            CrawDataResultExcel next = iterator.next();
            datas.add(next);
            if(datas.size()==50){
                excelWriter.write(datas, sheet);
                datas.clear();
            }
        }
        excelWriter.write(datas, sheet);
        excelWriter.finish();
        long end = System.currentTimeMillis();
        long total = end - start;
        System.out.println("write split ---------- excel耗时 -------- " + total);

    }

    @RequestMapping("/downExcel")
    public void downExcel(HttpServletResponse response ) throws IOException {

        long start = System.currentTimeMillis();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<CrawDataResultExcel> crawDataResultExcels = crawDataResultExcelMapper.selectList(new LambdaQueryWrapper<>());
        long query = System.currentTimeMillis();
        long sqlTime = query - start;
        System.out.println("sql 查询时间是 ------------ " + sqlTime);
        long write1 = System.currentTimeMillis();
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), CrawDataResultExcel.class).build();
        WriteSheet sheet = EasyExcel.writerSheet(0,"baidu").build();
        Iterator<CrawDataResultExcel> iterator = crawDataResultExcels.iterator();
        List<CrawDataResultExcel> datas = new ArrayList<>();
        while (iterator.hasNext()){
            CrawDataResultExcel next = iterator.next();
            datas.add(next);
            if(datas.size()==500){
                excelWriter.write(datas, sheet);
                datas.clear();
            }
        }
        excelWriter.write(datas, sheet);
        excelWriter.finish();
        long write2 = System.currentTimeMillis();
        long write = write2 - write1;
        System.out.println("写共耗时 ----------- " + write);

        long end = System.currentTimeMillis();
        long total = end - start;
        System.out.println("下载一共耗时多少时间 ------------ " + total);
       /* long write1 = System.currentTimeMillis();
        EasyExcel.write(response.getOutputStream(), CrawDataResultExcel.class).sheet("baidu").doWrite(crawDataResultExcels);
        long write2 = System.currentTimeMillis();
        long write = write2 - write1;
        System.out.println("写共耗时 ----------- " + write);

        long end = System.currentTimeMillis();
        long total = end - start;
        System.out.println("下载一共耗时多少时间 ------------ " + total);*/

//        EasyExcel.write("excelFileName", CrawDataResultExcel.class).sheet("baidu").doWrite(crawDataResultExcels);

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


    public void testAgain(){
        
    }


}
