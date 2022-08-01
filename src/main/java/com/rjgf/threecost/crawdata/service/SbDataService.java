package com.rjgf.threecost.crawdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjgf.threecost.crawdata.entity.SbData;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;

@Transactional(rollbackFor = Exception.class)
public interface SbDataService extends IService<SbData> {

    void testAsync() throws FileNotFoundException;

}
