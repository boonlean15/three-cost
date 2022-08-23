package com.rjgf.threecost.crawdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjgf.threecost.crawdata.entity.SbData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

@Mapper
public interface SbDataMapper extends BaseMapper<SbData> {

    void insertMapData(@Param("param") Map<String, String> param);

    void clearSource();

    Date getDateStr();

    String getTestTime();
}
