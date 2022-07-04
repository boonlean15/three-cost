package com.rjgf.threecost.crawdata.listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.rjgf.threecost.crawdata.entity.CrawDataResultExcel;
import com.rjgf.threecost.crawdata.entity.vo.BaiduReCoordinate;
import com.rjgf.threecost.crawdata.service.CrawDataResultExcelService;
import com.rjgf.threecost.crawdata.util.BaiduCoordinateConvertUtils;
import com.rjgf.threecost.crawdata.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author linch
 * @create 2022/2/28 14:31
 */
@Slf4j
public class ResultExcelListener extends AnalysisEventListener<CrawDataResultExcel> {

    /**
     * 输出临时文件夹
     */
    private final String OUT_PATH = "H:\\润建\\三费\\三费表和数据\\";

    private String excelFileName = OUT_PATH + "广东省小区平方米价格和平均租金-百度api请求转换.xlsx";

    private BaiduCoordinateConvertUtils baiduCoordinateConvertUtils;

    private CrawDataResultExcelService crawDataResultExcelService;

    private List<CrawDataResultExcel> transList = new ArrayList<>();

    public ResultExcelListener(BaiduCoordinateConvertUtils baiduCoordinateConvertUtils, CrawDataResultExcelService crawDataResultExcelService) {
        this.baiduCoordinateConvertUtils = baiduCoordinateConvertUtils;
        this.crawDataResultExcelService = crawDataResultExcelService;
    }

    @Override
    public void invoke(CrawDataResultExcel data, AnalysisContext context) {

        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        data.setId(UUIDUtils.getShortUUID("tc"));
//        cachedDataList.add(data);
        if(transList.size() < 2000){
            transList.add(data);
        }

        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
//        if (cachedDataList.size() >= BATCH_COUNT) {
//            saveData();
//            // 存储完成清理 list
//            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
//        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {

//        List<CrawDataResultExcel> crawDataResultExcels = transList.subList(0, 200);
//        crawDataResultExcels.forEach(item -> {
//            if(StringUtils.isBlank(item.getLongitude()) || StringUtils.isBlank(item.getLatitude())){
//                return;
//            }
//            BaiduReCoordinate bdReCoordinate = getBDReCoordinate(Double.parseDouble(item.getLongitude()), Double.parseDouble(item.getLatitude()));
//            if(bdReCoordinate != null){
//                String district = bdReCoordinate.getDistrict();
//                String city = bdReCoordinate.getCity();
//                Optional.ofNullable(district).ifPresent(v -> item.setCounty(v));
//                Optional.ofNullable(city).ifPresent(v -> item.setCity(v));
//                item.setId(UUIDUtils.getShortUUID("tc"));
//            }
//        });
//        log.info("{}条数据，开始存储数据库！", crawDataResultExcels.size());

//        EasyExcel.write(excelFileName, CrawDataResultExcel.class).sheet("baidu").doWrite(crawDataResultExcels);
//        List<List<CrawDataResultExcel>> split = com.rjgf.threecost.crawdata.util.ListUtils.split(crawDataResultExcels, 1000);
//        split.forEach(item -> {
//            crawDataResultExcelService.saveBatch(crawDataResultExcels);
//        });
//        log.info("存储数据库成功！");
//        EasyExcel.write(excelFileName, CrawDataResultExcel.class).sheet("baidu").doWrite(crawDataResultExcels);
//        log.info("写数据------到excel");
        crawDataResultExcelService.saveBatch(transList);

    }

    private BaiduReCoordinate getBDReCoordinate(double longitude, double latitude){
        BaiduReCoordinate wgs84ll = baiduCoordinateConvertUtils.getReCoordinate("wgs84ll", longitude, latitude);
        return wgs84ll;
    }
}
