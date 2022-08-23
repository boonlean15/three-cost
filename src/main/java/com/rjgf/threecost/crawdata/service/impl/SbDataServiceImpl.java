package com.rjgf.threecost.crawdata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjgf.threecost.crawdata.async.PriorityVo;
import com.rjgf.threecost.crawdata.entity.SbData;
import com.rjgf.threecost.crawdata.mapper.SbDataMapper;
import com.rjgf.threecost.crawdata.service.SbDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SbDataServiceImpl extends ServiceImpl<SbDataMapper, SbData> implements SbDataService {

    @Resource
    CrawDataResultExcelServiceImpl crawDataResultExcelService;

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Resource
    SbDataMapper sbDataMapper;

    public static PriorityVo priorityVo;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void testAsync() throws FileNotFoundException {
        String sql = "CREATE TABLE sb_data_bak as SELECT * FROM sb_data;";

        this.namedParameterJdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                return ps.execute();
            }
        });

        sbDataMapper.clearSource();

        Map<String, String> sbData = new HashMap<>(4);
        sbData.put("1-now-1","v-1");
        sbData.put("2-now-2","v-2");
        sbData.put("3-now-3","v-3");
        sbData.put("4-now-4","v-4");
        sbDataMapper.insertMapData(sbData);

        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\linch\\Desktop\\1.txt");

        StringBuilder rollBackSql = new StringBuilder();
        rollBackSql.append("INSERT INTO ");
        rollBackSql.append("sb_data");
        rollBackSql.append(" SELECT * FROM sb_data_bak");
        this.namedParameterJdbcTemplate.execute(rollBackSql.toString(), new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                return ps.execute();
            }
        });

        Date dateStr = sbDataMapper.getDateStr();
        log.info("date --------- " + dateStr);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void testRollback() {
//        try{
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        Map<String, String> sbData = new HashMap<>(4);
        sbData.put("11-now-1","v-1");
        sbData.put("12-now-2","v-2");
        sbData.put("13-now-3","v-3");
        sbData.put("14-now-4","v-4");
        sbDataMapper.insertMapData(sbData);
        int i = 1/0;
    }

    @Async("secondExecutor")
    @Override
    public void testAsyncError() {
        log.error("测试异步异常报错");
        try{
            SbData sbData = null;
            String id = sbData.getId();
            log.info("id ---- " + id);
        }finally {
            log.error("try finally  ");
        }
    }
    @Async
    @Override
    public void testAsyncDefault() {
        log.error("testAsyncDefault 测试默认异步Executor");
        try{
            SbData sbData = null;
            String id = sbData.getId();
            log.info("id ---- " + id);
        }finally {
            log.error("try finally  ");
        }
    }

    @Override
    public void testMethodException() {
        try{
            crawDataResultExcelService.testMethod();
            log.info("this.priorityVo ---- " + this.priorityVo);
            int j = 1/0;
            int i = j;
            log.info("iiiii" + i);
        }catch(Exception e){
            log.info("this.priorityVo ------- " + this.priorityVo);
            e.printStackTrace();
        }
    }
}
