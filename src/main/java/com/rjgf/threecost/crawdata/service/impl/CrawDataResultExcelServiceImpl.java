package com.rjgf.threecost.crawdata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjgf.threecost.crawdata.async.PriorityVo;
import com.rjgf.threecost.crawdata.entity.CrawDataResultExcel;
import com.rjgf.threecost.crawdata.mapper.CrawDataResultExcelMapper;
import com.rjgf.threecost.crawdata.service.CrawDataResultExcelService;
import org.springframework.stereotype.Service;

/**
 * @author linch
 * @create 2022/3/1 11:27
 */
@Service
public class CrawDataResultExcelServiceImpl extends ServiceImpl<CrawDataResultExcelMapper, CrawDataResultExcel> implements CrawDataResultExcelService {

    @Override
    public String testMethod() {
        String result = null;
        try{
            result = "testMethod";
            int i = 1/0;
            return result;
        }catch (Exception e){
            SbDataServiceImpl.priorityVo = new PriorityVo();
            e.printStackTrace();
            return result;
        }
    }
}
