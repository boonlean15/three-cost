package com.rjgf.threecost.crawdata.dao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class SbDataDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String testQueryTemplate(){
        String queryForObject = jdbcTemplate.queryForObject("select name from sb_data where name = ?", String.class);

        log.info("queryForObject -------- " + queryForObject);

        return queryForObject;
    }


}
